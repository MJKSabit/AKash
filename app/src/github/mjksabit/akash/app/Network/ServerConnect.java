package github.mjksabit.akash.app.Network;

import github.mjksabit.akash.app.Main;
import github.mjksabit.akash.app.Model.RequestAction;
import org.json.JSONObject;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

public class ServerConnect implements Closeable{
    // Port used to connect to server
    public static int PORT = 26979;

    // Server host
    private static final String LOCALHOST = "127.0.0.1";

    private static final int TIMEOUT = 10000;

    // Singleton instance
    private static ServerConnect instance = null;
    private Socket socket = null;

    InputStream inputStream = null;
    OutputStream outputStream = null;
    BufferedReader in = null;
    BufferedWriter out = null;

    // Response Listener in a Parallel Thread
    ResponseListener responseListener = null;

    private ServerConnect() {
        try {
            // Create Connection
            socket = new Socket(LOCALHOST, PORT);

            socket.setSoTimeout(TIMEOUT);

            // Input Stream from Server
            inputStream = socket.getInputStream();
            // Output Stream to Server
            outputStream = socket.getOutputStream();

            in = new BufferedReader(new InputStreamReader(inputStream));
            out = new BufferedWriter(new OutputStreamWriter(outputStream));

            // ResponseListen from InputStream
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

    // Singleton Instance Getter
    public static ServerConnect getInstance() {
        if (instance == null)
            instance = new ServerConnect();

        return instance;
    }

    /**
     * Send Request to Server
     * @param object    Request JSONObject, Must have a REQUEST_TYPE
     */
    public void sendRequest(JSONObject object) {
        try {
            // '\n' is a must need for Server to readLine
            out.write(object.toString()+"\n");

            // Send to Server
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Actions to be taken if we get this Response of responseType
     * @param responseType  Type of Response to Listen for
     * @param action        Action to be taken for getting the Response, use Lambda
     */
    public void waitForResponse(String responseType, RequestAction action) {
        responseListener.addResponseKeyword(responseType, action);
        if(socket.isConnected()) {
            responseListener.startExplicitListening();
        } else {
            Main.showError("Not Connected to the Server", 2000);
            responseListener.stop();
            // Stop Listening
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
