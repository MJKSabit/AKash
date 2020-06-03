package github.mjksabit.akash.server.Controller;

import github.mjksabit.akash.server.Model.User;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;

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
                if(jsonRequest.getString(RequestHandler.REQUEST_TYPE).equals("exit")) break;


                response = requestHandler.handle(jsonRequest);
                out.write(response+"\n");
                out.flush();
            }

        } catch (IOException | JSONException | NullPointerException e) {
            e.printStackTrace();
        }

        System.out.println("Client Exit...");
    }
}
