package github.mjksabit.akash.server;

import github.mjksabit.akash.server.Controller.Client;
import github.mjksabit.akash.server.Model.DBModel;
import github.mjksabit.akash.server.Model.User;
import org.apache.commons.codec.digest.DigestUtils;

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

            System.err.println("ADMIN MODE RUNNING; PLEASE ENTER 'DB:<database-absolute-path>' to connect to database");

            while (!(serverCommand = adminCommand.nextLine()).isEmpty()) {
                String[] commands = serverCommand.split("::");

                try {
                    switch (commands[0]) {
                        case "DB":
                            DBModel.DATABASE_LOCATION = "jdbc:sqlite:" + commands[1];
                            break;
                        case "NOTIFY":
                            DBModel.getInstance().adminAddNotification(commands[1]);
                            break;
                        case "TRANSFER":
                            String agentCode = commands[1];
                            double ammount = Double.parseDouble(commands[2]);
                            DBModel.getInstance().adminAddToAgent(agentCode, ammount);
                            break;
                        case "NEW":
                            DBModel.getInstance().createUser("agent" + commands[1],
                                    DigestUtils.sha256Hex(commands[3]), commands[2]);
                            break;
                        case "EXIT":
                            System.exit(0);
                        default:
                            System.err.println("================= COMMAND NOT RECOGNIZED ================");
                            System.out.println("Available Commands (Don't use extra space):\n" +
                                    "NOTIFY::<notification-text>\n" +
                                    "NEW::<agent-code>::<agent-name>::<agent-password>\n" +
                                    "TRANSFER::<agent-code>::<amount-to-transfer>\n" +
                                    "EXIT\n\n");
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("==================== INVALID ARGUMENT(s) ==================");
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
