package github.mjksabit.akash.server;

import github.mjksabit.akash.server.Controller.Client;
import github.mjksabit.akash.server.Model.DBModel;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

    static int PORT = 26979; // PORT Number to Open Socket into
    private static final String SEPARATOR = "::"; // Separator for ADMIN Commands
    private static final String AGENT_PREFIX = "agent"; // Prefix added before the Agent Code

    /**
     * Server for BKash App
     *
     * Parameters
     *
     * {@param args} While running provide "Database Absolute Path" as args[0] and "PORT" optional as args[1]
     *
     * @author Md. Jehadul Karim Sabit
     */
    public static void main(String[] args) throws IOException {

        System.out.println("Server Running...");

        // ADMIN input Scanner
        Scanner adminCommand = new Scanner(System.in);

        // Use DatabaseLocation if Provided
        if (args.length > 0) {
            DBModel.DATABASE_LOCATION = "jdbc:sqlite:" + args[0];
            System.out.println("Setting DatabaseLocation: " + args[0]);
        }

        // Set new PORT if provided after matching that It's an Integer
        if (args.length > 1 && Pattern.matches("\\d+", args[1])) {
            PORT = Integer.parseInt(args[1]);
            System.out.println("Setting PORT: " + PORT);
        }

        // Admin Commands Input Thread
        Thread adminThread = new Thread(() -> {
            String serverCommand;

            // Shows Message to Console to help user understand the Server is running
            System.err.println("============= ADMIN MODE RUNNING ==============");
            System.err.println("TYPE 'HELP' TO SEE AVAILABLE COMMANDS...");

            // Runs the server until "EXIT" Command is Entered
            while (!(serverCommand = adminCommand.nextLine()).isEmpty()) {
                String[] commands = serverCommand.split(SEPARATOR);

                try {
                    switch (commands[0]) {
                        // Custom Database initializer
                        case "DB":
                            DBModel.DATABASE_LOCATION = "jdbc:sqlite:" + commands[1];
                            break;
                        // Send Notification to all user
                        case "NOTIFY":
                            DBModel.getInstance().adminAddNotification(commands[1]);
                            break;
                        // Transfer Money to Agent
                        case "TRANSFER":
                            String agentNumber = AGENT_PREFIX+commands[1];
                            double ammount = Double.parseDouble(commands[2]);
                            DBModel.getInstance().adminAddToAgent(agentNumber, ammount);
                            break;
                        // CREATE New Agent
                        case "NEW":
                            boolean status;
                            String _agentNumber       = AGENT_PREFIX + commands[1];
                            String _agentName       = commands[2];
                            String _agentPassword   = commands[3];
                            status = DBModel.getInstance().createUser(_agentNumber,
                                    DigestUtils.sha256Hex(_agentPassword),  // Password Hashing before inserting into Database
                                    _agentName);

                            if (status) // If new Agent is Created, Show Confirmation Message
                                System.out.println(
                                    "=============== AGENT CREATED ==============\n" +
                                    "Mobile:   " + _agentNumber + "\n" +
                                    "Password: " + _agentPassword);
                            break;
                        // Exit the Server
                        case "EXIT":
                            System.exit(0);
                        default:
                            System.err.println("================= COMMAND NOT RECOGNIZED ================");
                            System.out.println("Available Commands (Don't use extra space):\n" +
                                    "NOTIFY"+SEPARATOR+"<notification-text>\n" +
                                    "\t\t"+     "Broadcast Notifications\n" +

                                    "NEW"+SEPARATOR+"<agent-code>"+SEPARATOR+"<agent-name>"+SEPARATOR+"<agent-password>\n" +
                                    "\t\t"+     "Add New Agent\n" +

                                    "TRANSFER"+SEPARATOR+"<agent-code>"+SEPARATOR+"<amount-to-transfer>\n" +
                                    "\t\t"+     "Transfer money to Agent\n" +

                                    "EXIT");
                    }
                } catch (ArrayIndexOutOfBoundsException | InputMismatchException e ) { // Not Enough Arguments | Can't Parse Integer/Double
                    System.err.println("==================== INVALID ARGUMENT(s) ==================");
                }
            }
        });

        adminThread.start();

        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(PORT);
        } catch (BindException e) {
            System.err.println("========== PLEASE CHANGE THE PORT AND TRY AGAIN ==========");

            // Display Error Message and Wait...
            adminCommand.nextLine();
            System.exit(-1);
        }

        while (true) {
            // Allow client to connect to Server Every time
            clientSocket = serverSocket.accept();

            System.out.println(" >>> NEW Client Connected");

            // Start New Thread for New Client
            new Thread(new Client(clientSocket)).start();
        }


    }
}
