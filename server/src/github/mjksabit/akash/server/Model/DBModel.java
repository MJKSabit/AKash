package github.mjksabit.akash.server.Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBModel {
    private static final String DATABASE_LOCATION = "jdbc:sqlite:/media/sabit/Data/@CODE/Java/AKash/database/akash.db";

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

}
