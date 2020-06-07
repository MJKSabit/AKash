package github.mjksabit.akash.server.Controller;

import github.mjksabit.akash.server.Model.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

/**
 * Client class is created every time a client is connected to the Server
 * Implements {@link Runnable} to start as a new {@link Thread}
 *
 * {@param socket} Client Socket for I/O
 *
 */
public class Client implements Runnable {

    private final Socket socket;

    private User user;
    private RequestHandler requestHandler = null;

    public Client(Socket socket) {
        this.socket = socket;
        requestHandler = new RequestHandler();
    }

    @Override
    public void run() {

        // Open I/O Connection between Server and Client
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream();
             BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream));) {

            // Uses Request-Response Architecture
            String request, response;

            while (socket.isConnected()) {

                // Wait for Client Request
                request = in.readLine();

                // Get JSON from request
                JSONObject jsonRequest = new JSONObject(request);

                // Explicit Client Disconnect
                if (jsonRequest.getString(RequestHandler.REQUEST_TYPE).equals("exit"))
                    break;

                // Send response in JSON to handleRequest
                response = requestHandler.handle(jsonRequest);

                // MUST INCLUDE '\n', ELSE CLIENT CAN NOT READ LINE
                out.write(response + "\n");

                // Send Response to Client
                out.flush();
            }

        } catch (IOException | JSONException e) {
            System.err.println(">>> Invalid Request Type (Unsupported API)");
        } catch (NullPointerException e) {
            // Normal Exit...
        }

        System.out.println(">>> EXIT Client");
    }
}
