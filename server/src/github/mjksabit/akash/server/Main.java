package github.mjksabit.akash.server;

import github.mjksabit.akash.server.Model.DBModel;
import github.mjksabit.akash.server.Model.User;

public class Main {

    public static void main(String[] args) {
        System.out.println("Server Running...");

        DBModel db = DBModel.getInstance();

        System.out.println(db.createUser(new User("01303060524", "password", "Jehadul")));
        System.out.println(db.getBalance("1000"));

    }
}
