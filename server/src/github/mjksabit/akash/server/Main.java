package github.mjksabit.akash.server;

import github.mjksabit.akash.server.Controller.Client;
import github.mjksabit.akash.server.Model.DBModel;
import github.mjksabit.akash.server.Model.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    final static int PORT = 26979;

    public static void main(String[] args) throws IOException {
        System.out.println("Server Running...");

        Scanner adminCommand = new Scanner(System.in);

        Runnable target;
        Thread adminThread = new Thread(() -> {
            String serverCommand;
            while (!(serverCommand = adminCommand.nextLine()).isEmpty()) {
                String[] commands = serverCommand.split("::");

                switch (commands[0]) {
                    case "NOTIFY":
                        DBModel.getInstance().adminAddNotification(commands[1]);
                        break;
                    case "AGENT":
                        String agentCode = commands[1];
                        double ammount = Double.parseDouble(commands[2]);
                        DBModel.getInstance().adminAddToAgent(agentCode, ammount);
                        break;
                    case "EXIT":
                        System.exit(0);
                    default:
                        System.err.println("Command Not Recognized...");
                }
            }
        });

        adminThread.start();

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
