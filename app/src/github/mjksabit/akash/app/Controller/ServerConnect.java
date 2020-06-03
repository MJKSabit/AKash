package github.mjksabit.akash.app.Controller;

import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

public class ServerConnect implements Closeable{
    private static final int PORT = 26979;
    private static final String LOCALHOST = "127.0.0.1";
    private static final int TIMEOUT = 10000;


    private static ServerConnect instance = null;
    private Socket socket = null;

    InputStream inputStream = null;
    OutputStream outputStream = null;
    BufferedReader in = null;
    BufferedWriter out = null;

    ResponseListener responseListener = null;

    public void handleResponse(String responseText) {
        System.out.println(responseText);
    }

    private ServerConnect() {
        try {
            socket = new Socket(LOCALHOST, PORT);
            socket.setSoTimeout(TIMEOUT);

            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            in = new BufferedReader(new InputStreamReader(inputStream));
            out = new BufferedWriter(new OutputStreamWriter(outputStream));

            responseListener = new ResponseListener(this, in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ServerConnect getInstance() {
        if (instance == null)
            instance = new ServerConnect();

        if (!instance.socket.isConnected()) {
            instance.close();
            instance = null;
        }

        return instance;
    }

    public void sendRequest(JSONObject object) {
        try {
            out.write(object.toString()+"\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Request Sent...");
        responseListener.start();
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
