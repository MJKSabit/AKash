package github.mjksabit.akash.server.Model;

import java.sql.*;
import java.util.ArrayList;

public class DBModel {
    public static String DATABASE_LOCATION = "jdbc:sqlite:/media/sabit/Data/@CODE/Java/AKash/database/akash.db";

    private static final String ADMIN = "agent0";

    private static final String USER_TABLE = "user";
    private static final String USER_NAME = "name";
    private static final String USER_PASSWORD = "password";
    private static final String USER_MOBILE_NO = "mobileno";
    private static final String USER_BALANCE = "balance";

    private static final String TRANSACTION_TABLE = "transaction_data";
    private static final String TRANSACTION_SENDER = "sender";
    private static final String TRANSACTION_RECEIVER = "receiver";
    private static final String TRANSACTION_AMOUNT = "amount";
    private static final String TRANSACTION_REFERENCE = "reference";
    private static final String TRANSACTION_TYPE = "type";
    private static final String TRANSACTION_ID = "TrxId";

    private static final String NOTIFICATION_TABLE = "notification";

    private static DBModel instance = null;

    private Connection dbConnect = null;

    private Connection makeConnection() {
        System.out.println("Connecting Database...");
        try {
            return DriverManager.getConnection(DATABASE_LOCATION);
        } catch (SQLException e) {
            System.out.println("Can not open database");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Singleton DBModel Class
     */
    private DBModel() {
        dbConnect = makeConnection();
    }

    /**
     * @return Singleton {@link DBModel} Object
     */
    public static DBModel getInstance() {
        if (instance == null)
            instance = new DBModel();

        return instance;
    }

    public void adminAddNotification(String text) {
        String sql = "INSERT INTO " + NOTIFICATION_TABLE + " VALUES ( '" +
                text + "' )";
        try (Statement statement = dbConnect.createStatement()) {
            statement.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("================ ERROR ADDING Notification ===============");
            return;
        }
        System.out.println("============== NOTIFICATION ADDED ==================");
    }

    public User getUser(String mobile, String password) {
        String sql =
                "SELECT " +
                    USER_MOBILE_NO + ", " +
                    USER_NAME + ", " +
                    USER_PASSWORD +
                " FROM " + USER_TABLE +
                    " WHERE " +
                        USER_MOBILE_NO + " = '" + mobile + "' " +
                        "AND " +
                        USER_PASSWORD + " = '" + password + "'";

        User user = null;

        try (Statement statement = dbConnect.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            // If a user is found in Database ( a record )
            if (result.next()) {
                user = new User(
                        result.getString(USER_MOBILE_NO),
                        result.getString(USER_PASSWORD),
                        result.getString(USER_NAME)
                );
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("================ ERROR GETTING USER from DATABASE ============");
        }

        return user;
    }

    public boolean userExists(String mobile) {
        String sql =
                "SELECT " +
                     USER_MOBILE_NO +
                " FROM " + USER_TABLE +
                    " WHERE " +
                        USER_MOBILE_NO + " = '" + mobile + "'";

        boolean status = false;
        try (Statement statement = dbConnect.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            // A record is found ?
            if (result.next()) {
                status = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("================== ERROR CHECKING for EXISTING USER ================");
        }

        return status;
    }

    public double getBalance(String mobile) {
        String sql =
                "SELECT " +
                        USER_BALANCE + ", " +
                        USER_MOBILE_NO +
                " FROM " + USER_TABLE +
                    " WHERE " +
                        USER_MOBILE_NO + " = ? ";

        double balance = 0;
        try (PreparedStatement pst = dbConnect.prepareStatement(sql)) {
            // SQL Index starts from 1, Replace ? with mobile as String
            pst.setString(1, mobile);

            ResultSet resultSet = pst.executeQuery();
            balance = resultSet.getDouble(1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("================== ERROR Getting Balance ================");
        }
        return balance;
    }

    public boolean createUser(String mobile, String password, String name) {
        String sql =
                "INSERT INTO " +
                        USER_TABLE +
                        " ( " +
                            USER_MOBILE_NO + ", " +
                            USER_PASSWORD + ", " +
                            USER_NAME +
                        " ) " +
                "VALUES ( ?, ?, ? )";

        try (PreparedStatement statement = dbConnect.prepareStatement(sql)) {
            statement.setString(1, mobile);
            statement.setString(2, password);
            statement.setString(3, name);

            statement.execute();
        } catch (SQLException throwables) {
            // User exists, Can't Create new User as Mobile_No is the Primary Key
            throwables.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean makeTransaction(String sender, String receiver, double amount, String reference, String type) {
        String sql =
                "INSERT INTO " +
                        TRANSACTION_TABLE +
                        " ( " +
                            TRANSACTION_SENDER + ", " +
                            TRANSACTION_RECEIVER + ", " +
                            TRANSACTION_AMOUNT + ", " +
                            TRANSACTION_REFERENCE + ", " +
                            TRANSACTION_TYPE +
                        ") " +
                "VALUES ( ?, ?, ?, ?, ?)";

        String balanceUpdate =
                "UPDATE " +
                        USER_TABLE +
                 " SET " +
                        USER_BALANCE + " = " + USER_BALANCE + " + ? " +
                "WHERE " +
                        USER_MOBILE_NO + " = ?";

        boolean success = true;

        try {
            // Do fully or Do none : Transaction
            dbConnect.setAutoCommit(false);

            try (PreparedStatement transaction = dbConnect.prepareStatement(sql)) {
                transaction.setString(1, sender);
                transaction.setString(2, receiver);
                transaction.setDouble(3, amount);
                transaction.setString(4, reference);
                transaction.setString(5, type);

                transaction.executeUpdate();
            }

            // Not Outer Transfer Protocol
            if (!receiver.isEmpty())
                try (PreparedStatement receiverBalance = dbConnect.prepareStatement(balanceUpdate, Statement.RETURN_GENERATED_KEYS)) {
                    receiverBalance.setDouble(1, amount);
                    receiverBalance.setString(2, receiver);

                    int affectedRow = receiverBalance.executeUpdate();

                    if (affectedRow != 1) {
                        // No update means transfer Failed
                        success = false;
                    }
                }


            try (PreparedStatement senderBalance = dbConnect.prepareStatement(balanceUpdate, Statement.RETURN_GENERATED_KEYS)) {
                senderBalance.setDouble(1, -amount);
                senderBalance.setString(2, sender);

                int affectedRow = senderBalance.executeUpdate();

                if (affectedRow != 1) {
                    // No update means transfer Failed
                    success = false;
                }
            }

            if (success) {
                dbConnect.commit();
            } else {
                dbConnect.rollback();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            success = false;
        } finally {
            try {
                // Back to Auto Commit
                dbConnect.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return success;
    }

    private static final int FILTER_SHOW_ALL = 0;
    private static final int FILTER_SHOW_SENT = -1;
    private static final int FILTER_SHOW_RECEIVED = +1;

    public ArrayList<Transaction> getTransactions(String mobileNumber, int index, int limit, int filter) {
        ArrayList<Transaction> transactions = new ArrayList<>();

        String firstColumn, secondColumn;

        if (filter == FILTER_SHOW_ALL) {
            // User in Either SENDER or RECEIVER
            firstColumn = TRANSACTION_SENDER;
            secondColumn = TRANSACTION_RECEIVER;
        } else if (filter == FILTER_SHOW_SENT) {
            // User is SENDER only
            firstColumn = TRANSACTION_SENDER;
            secondColumn = TRANSACTION_SENDER;
        } else {
            // User is RECEIVER only
            firstColumn = TRANSACTION_RECEIVER;
            secondColumn = TRANSACTION_RECEIVER;
        }

        String sql =
                "SELECT " +
                        TRANSACTION_ID + ", " +
                        TRANSACTION_SENDER + ", " +
                        TRANSACTION_RECEIVER + ", " +
                        TRANSACTION_TYPE + ", " +
                        TRANSACTION_REFERENCE + ", " +
                        TRANSACTION_AMOUNT +
                " FROM " +
                        TRANSACTION_TABLE +
                " WHERE " +
                        firstColumn + " = '" + mobileNumber +
                    "' OR " +
                        secondColumn + " = '" + mobileNumber + "' " +
                "ORDER BY " +
                        TRANSACTION_ID + " DESC " +
                " LIMIT " +
                        limit +
                " OFFSET " +
                        index;

        try (PreparedStatement select = dbConnect.prepareStatement(sql);
             ResultSet resultSet = select.executeQuery()) {

            while (resultSet.next()) {
                boolean isCashOut = resultSet.getString(TRANSACTION_SENDER).equals(mobileNumber);

                // Other User without Me
                String otherUser = (isCashOut) ? resultSet.getString(TRANSACTION_RECEIVER) : resultSet.getString(TRANSACTION_SENDER);

                transactions.add(new Transaction(
                        resultSet.getString(TRANSACTION_ID),
                        otherUser,
                        resultSet.getString(TRANSACTION_TYPE),
                        resultSet.getString(TRANSACTION_REFERENCE),
                        isCashOut,
                        resultSet.getDouble(TRANSACTION_AMOUNT)
                ));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("================== ERROR Getting Transaction ===============");
            return null;
        }

        return transactions;
    }

    public ArrayList<String> getNotifications() {
        String sql =
                "SELECT " +
                        "* " +
                "FROM " +
                        NOTIFICATION_TABLE;

        ArrayList<String> list = new ArrayList<>();

        try (Statement statement = dbConnect.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                list.add(resultSet.getString(1));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("================== ERROR Getting Notification ===============");
        }

        return list;
    }

    public boolean changePassword(String mobile, String oldpassword, String newpassword) {
        String sql =
                "UPDATE " +
                        USER_TABLE + " " +
                " SET " +
                        USER_PASSWORD + " = '" + newpassword + "' " +
                " WHERE " +
                        USER_MOBILE_NO + " = '" + mobile + "' " +
                    " AND " +
                        USER_PASSWORD + " = '" + oldpassword + "'";

        int numberOfUpdatedRows;
        try (Statement statement = dbConnect.createStatement()) {
            numberOfUpdatedRows = statement.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("================== ERROR Changing Password ===============");
            numberOfUpdatedRows = 0;
        }

        return numberOfUpdatedRows != 0;
    }

    public void adminAddToAgent(String agentNumber, double ammount) {
        boolean status = makeTransaction(
                ADMIN,
                agentNumber,
                ammount,
                "Cash in from ADMIN",
                "Send Money");
        String NOT = status ? "" : "NOT ";

        System.out.println("================ AGENT MONEY " + NOT + "SENT ========================");
    }
}
