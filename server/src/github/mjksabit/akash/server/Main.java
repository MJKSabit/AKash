package github.mjksabit.akash.server;

import github.mjksabit.akash.server.Controller.Client;
import github.mjksabit.akash.server.Model.DBModel;
import github.mjksabit.akash.server.Model.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    static int PORT = 26979;

    public static void main(String[] args) throws IOException {
        System.out.println("Server Running...");

        Scanner adminCommand = new Scanner(System.in);

        if(args.length > 0) {
            DBModel.DATABASE_LOCATION = "jdbc:sqlite:" + args[0];
            System.out.println("Setting DatabaseLocation: "+args[0]);
        }

        if (args.length>1 && Pattern.matches("\\d+", args[1])) {
            PORT = Integer.parseInt(args[1]);
            System.out.println("Setting PORT: "+PORT);
        }

        Runnable target;
        Thread adminThread = new Thread(() -> {
            String serverCommand;

            System.err.println("============= ADMIN MODE RUNNING ==============");
            System.err.println("TYPE 'HELP' TO SEE AVAILABLE COMMANDS...");

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
                            System.out.println("Agent can login using these credential:\n" +
                                    "Mobile:   agent"+commands[1]+"\n" +
                                    "Password: "+commands[3]);
                            break;
                        case "EXIT":
                            System.exit(0);
                        default:
                            System.err.println("================= COMMAND NOT RECOGNIZED ================");
                            System.out.println("Available Commands (Don't use extra space):\n" +
                                    "NOTIFY::<notification-text>\n" +
                                    "\t\tBroadcast Notifications\n" +
                                    "NEW::<agent-code>::<agent-name>::<agent-password>\n" +
                                    "\t\tAdd New Agent\n" +
                                    "TRANSFER::<agent-code>::<amount-to-transfer>\n" +
                                    "\t\tTransfer money to Agent\n" +
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

        try {
            serverSocket = new ServerSocket(PORT);
        }
        catch (BindException e) {
            System.err.println("========== PLEASE CHANGE THE PORT AND TRY AGAIN ==========");
            adminCommand.nextLine();
            System.exit(-1);
        }

        while (true){
            clientSocket = serverSocket.accept();
            System.out.println("New Client Connected...");
            new Thread(new Client(clientSocket)).start();
        }


    }
}
