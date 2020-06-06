package github.mjksabit.akash.server.Controller;

import github.mjksabit.akash.server.Model.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

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

        try(InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream));){

            String request, response;

            while (socket.isConnected()) {

                // Wait for Client Request
                request = in.readLine();

                JSONObject jsonRequest = new JSONObject(request);

                // Explicit Client Disconnect
                if(jsonRequest.getString(RequestHandler.REQUEST_TYPE).equals("exit")) break;


                response = requestHandler.handle(jsonRequest);
//                System.out.println("RESPONSE: "+response);
                // MUST INCLUDE '\n', else client can not read LINE
                out.write(response+"\n");
                out.flush();
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            // Normal Exit...
        }

        System.out.println("Client Exit...");
    }
}
