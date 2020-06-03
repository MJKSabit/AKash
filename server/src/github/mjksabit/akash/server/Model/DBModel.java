package github.mjksabit.akash.server.Model;

import java.sql.*;

public class DBModel {
    private static final String DATABASE_LOCATION = "jdbc:sqlite:/media/sabit/Data/@CODE/Java/AKash/database/akash.db";

    private static final String USER_TABLE = "user";
    private static final String USER_NAME = "name";
    private static final String USER_PASSWORD = "password";
    private static final String USER_MOBILE_NO = "mobileno";
    private static final String USER_BALANCE = "balance";

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

    public boolean createUser(User user) {
        String sql = "INSERT INTO " + USER_TABLE + " ( " +
                USER_MOBILE_NO + ", " + USER_PASSWORD + ", " + USER_NAME + " ) " +
                "VALUES ( ?, ?, ? )";

        try (PreparedStatement statement = dbConnect.prepareStatement(sql)) {
            statement.execute(sql);
        } catch (SQLException throwables) {
//            throwables.printStackTrace();
            return false;
        }

        return true;
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
}
