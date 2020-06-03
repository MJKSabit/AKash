package github.mjksabit.akash.server;

import github.mjksabit.akash.server.Model.DBModel;

public class Main {

    public static void main(String[] args) {
        System.out.println("Server...");

        DBModel db = DBModel.getInstance();
    }
}
