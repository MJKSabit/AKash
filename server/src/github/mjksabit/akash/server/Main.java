package github.mjksabit.akash.server;

import github.mjksabit.akash.server.Controller.Client;
import github.mjksabit.akash.server.Model.DBModel;
import github.mjksabit.akash.server.Model.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    final static int PORT = 26979;

    public static void main(String[] args) throws IOException {
        System.out.println("Server Running...");

        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        serverSocket = new ServerSocket(PORT);

        while (true){
            clientSocket = serverSocket.accept();
            System.out.println("New Client Connected...");
            new Thread(new Client(clientSocket)).start();
        }


    }
}
