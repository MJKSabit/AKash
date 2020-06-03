package github.mjksabit.akash.server;

import github.mjksabit.akash.server.Model.DBModel;

public class Main {

    public static void main(String[] args) {
        System.out.println("Server Running...");

        DBModel db = DBModel.getInstance();
        System.out.println(db.getUser("01986283829", "password"));
        System.out.println(db.userExists("01986283829"));
    }
}
