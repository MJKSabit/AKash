package github.mjksabit.akash.app.Network;

import github.mjksabit.akash.app.Main;
import github.mjksabit.akash.app.Model.RequestAction;
import org.json.JSONObject;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

public class ServerConnect implements Closeable{
    public static int PORT = 26979;
    private static final String LOCALHOST = "127.0.0.1";
    private static final int TIMEOUT = 10000;


    private static ServerConnect instance = null;
    private Socket socket = null;

    InputStream inputStream = null;
    OutputStream outputStream = null;
    BufferedReader in = null;
    BufferedWriter out = null;

    ResponseListener responseListener = null;

    private ServerConnect() {
        try {
            socket = new Socket(LOCALHOST, PORT);
            socket.setSoTimeout(TIMEOUT);

            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            in = new BufferedReader(new InputStreamReader(inputStream));
            out = new BufferedWriter(new OutputStreamWriter(outputStream));

            responseListener = new ResponseListener(in);
        }
        catch (ConnectException e) {
            // Connection Error
            Main.showError("Can not connect to Server", 2000);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ServerConnect getInstance() {
        if (instance == null)
            instance = new ServerConnect();

        return instance;
    }

    public void sendRequest(JSONObject object) {
        try {
            out.write(object.toString()+"\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println("Request Sent...");
    }

    public void waitForResponse(String responseType, RequestAction action) {
        responseListener.addResponseKeyword(responseType, action);
        if(socket.isConnected()) {
            responseListener.startExplicitListening();
        } else {
            Main.showError("Not Connected to the Server", 2000);
        }
    }

    public void stopListeningResponse (String responseType) {
        responseListener.removeResponseKeyword(responseType);
    }

    @Override
    public void close() {
        try {
            responseListener.stop();

            out.close();
            in.close();

            outputStream.close();
            inputStream.close();

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
