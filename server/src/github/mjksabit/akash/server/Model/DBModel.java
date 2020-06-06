package github.mjksabit.akash.server.Model;

import java.sql.*;
import java.util.ArrayList;

public class DBModel {
    private static final String DATABASE_LOCATION = "jdbc:sqlite:/media/sabit/Data/@CODE/Java/AKash/database/akash.db";

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

    private static DBModel instance = null;

    private Connection dbConnect = null;

    private Connection makeConnection() {
        System.out.println("Connecting Database...");
        try {
            return DriverManager.getConnection(DATABASE_LOCATION);
        }
        catch (SQLException e) {
            System.out.println("Can not open database");
            e.printStackTrace();
            return null;
        }
    }

    private DBModel() {
        dbConnect = makeConnection();
    }

    public static DBModel getInstance() {
        if(instance == null)
            instance = new DBModel();

        return instance;
    }

    public User getUser(String mobile, String password) {
        String sql = "SELECT " + USER_MOBILE_NO + ", " +
                USER_NAME + ", " + USER_PASSWORD + " FROM " +
                USER_TABLE + " WHERE " +
                USER_MOBILE_NO + " = '" + mobile + "' "+ "AND "+
                USER_PASSWORD + " = '" + password + "'";

        User user = null;

        try (Statement statement = dbConnect.createStatement();
            ResultSet result = statement.executeQuery(sql)) {

            if (result.next()) {
                user = new User(result.getString(USER_MOBILE_NO), result.getString(USER_PASSWORD), result.getString(USER_NAME));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return user;
    }

    public boolean userExists(String mobile) {
        String sql = "SELECT " + USER_MOBILE_NO + " FROM " + USER_TABLE +
                " WHERE " + USER_MOBILE_NO + " = '" + mobile + "'";

        boolean status = false;
        try (Statement statement = dbConnect.createStatement();
             ResultSet result = statement.executeQuery(sql)) {

            if (result.next()) {
                status = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return status;
    }

    public double getBalance(String mobile) {
        String sql = "SELECT " + USER_BALANCE + ", " + USER_MOBILE_NO + " FROM " + USER_TABLE + " WHERE " + USER_MOBILE_NO + " = ? ";

        System.out.println(sql);

        double balance = -1;
        try (PreparedStatement pst = dbConnect.prepareStatement(sql)) {
            pst.setString(1, mobile);

            ResultSet resultSet = pst.executeQuery();
            balance = resultSet.getDouble(1);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return balance;
    }

    public boolean createUser(String mobile, String password, String name) {
        String sql = "INSERT INTO " + USER_TABLE + " ( " +
                USER_MOBILE_NO + ", " + USER_PASSWORD + ", " + USER_NAME + " ) " +
                "VALUES ( ?, ?, ? )";

        try (PreparedStatement statement = dbConnect.prepareStatement(sql)) {
            statement.setString(1, mobile);
            statement.setString(2, password);
            statement.setString(3, name);

            statement.execute();
        } catch (SQLException throwables) {
            // user exists
            throwables.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean makeTransaction(String sender, String receiver, double amount, String reference, String type) {
        String sql = "INSERT INTO " + TRANSACTION_TABLE + " ( " +
                TRANSACTION_SENDER + ", " + TRANSACTION_RECEIVER + ", " + TRANSACTION_AMOUNT + ", " + TRANSACTION_REFERENCE + ", " + TRANSACTION_TYPE + ") " +
                "VALUES ( ?, ?, ?, ?, ?)";

        String balanceUpdate = "UPDATE " + USER_TABLE + " SET " +
                USER_BALANCE + " = " + USER_BALANCE + " + ? " +
                "WHERE " + USER_MOBILE_NO + " = ?";

        boolean success = true;

        try {
            dbConnect.setAutoCommit(false);

            try (PreparedStatement transaction = dbConnect.prepareStatement(sql)) {
                transaction.setString(1, sender);
                transaction.setString(2, receiver);
                transaction.setDouble(3, amount);
                transaction.setString(4, reference);
                transaction.setString(5, type);

                transaction.executeUpdate();
            }

            if(!receiver.isEmpty()) try (PreparedStatement receiverBalance = dbConnect.prepareStatement(balanceUpdate, Statement.RETURN_GENERATED_KEYS)){
                receiverBalance.setDouble(1, amount);
                receiverBalance.setString(2, receiver);

                int affectedRow = receiverBalance.executeUpdate();
                if(affectedRow != 1) {
                    success = false;
                }
            }

            System.out.println(balanceUpdate);

            try (PreparedStatement senderBalance = dbConnect.prepareStatement(balanceUpdate, Statement.RETURN_GENERATED_KEYS)){
                senderBalance.setDouble(1, -amount);
                senderBalance.setString(2, sender);

                int affectedRow = senderBalance.executeUpdate();
                if(affectedRow != 1) {
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
                dbConnect.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        return success;
    }

    public ArrayList<Transaction> getTransactions(String mobileNumber, int index, int limit, int filter) {
        ArrayList<Transaction> transactions = new ArrayList<>();

        String firstCol, secondCol;
        if(filter == 0) {
            firstCol = TRANSACTION_SENDER;
            secondCol = TRANSACTION_RECEIVER;
        } else if (filter == -1) {
            firstCol = TRANSACTION_SENDER;
            secondCol = TRANSACTION_SENDER;
        } else {
            firstCol = TRANSACTION_RECEIVER;
            secondCol = TRANSACTION_RECEIVER;
        }

        String sql = "SELECT " +
                TRANSACTION_ID + ", " +
                TRANSACTION_SENDER + ", " +
                TRANSACTION_RECEIVER + ", " +
                TRANSACTION_TYPE + ", " +
                TRANSACTION_REFERENCE + ", " +
                TRANSACTION_AMOUNT +
                " FROM " + TRANSACTION_TABLE +
                " WHERE " +
                firstCol +
                " like '" + mobileNumber + "' OR " +
                secondCol+
                " like '" + mobileNumber + "' "+
                "ORDER BY " + TRANSACTION_ID + " DESC " +
                " LIMIT " + limit +
                " OFFSET " + index;
        System.out.println(sql);

        try (PreparedStatement select = dbConnect.prepareStatement(sql)){
            try (ResultSet resultSet = select.executeQuery()) {
                while (resultSet.next()) {
                    boolean isCashOut = resultSet.getString(TRANSACTION_SENDER).equals(mobileNumber);
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
            }

        } catch (SQLException throwables) {
           throwables.printStackTrace();
            return null;
        }

        return transactions;
    }

    public ArrayList<String> getNotifications() {
        String sql = "SELECT * from notification";
        ArrayList<String> list = new ArrayList<>();

        try (Statement statement = dbConnect.createStatement();
             ResultSet resultSet =  statement.executeQuery(sql)){
            while (resultSet.next()) {
                list.add(resultSet.getString(1));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return list;

    }
}
