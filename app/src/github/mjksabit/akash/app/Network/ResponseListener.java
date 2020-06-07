package github.mjksabit.akash.app.Network;

import github.mjksabit.akash.app.Main;
import github.mjksabit.akash.app.Model.RequestAction;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

public class ResponseListener implements Runnable {

    public static final String RESPONSE_TYPE = "responseType";

    private BufferedReader in = null;
    ServerConnect server = null;
    private boolean continueListening;
    private Thread listenerThread;

    private Map<String, RequestAction> responseMap;

    public ResponseListener(BufferedReader in) {
        this.in = in;
        responseMap = new HashMap<>();
        listenerThread = null;
    }

    // Start Listening for Response
    public void startExplicitListening() {

        // Lazy Initialization
        if(listenerThread==null)
            listenerThread = new Thread(this);

        // If thread Already Done, start again
        if(!listenerThread.isAlive()) listenerThread.start();
    }

    // Look For Response
    public void addResponseKeyword(String response, RequestAction action) {
        responseMap.put(response, action);
    }

    // After Response Has been Found, you can remove it...
    public void removeResponseKeyword (String response) {
        if(responseMap.containsKey(response))
            responseMap.remove(response);
    }

    // Stop Listening
    public void stop() {
        continueListening = false;
    }



    @Override
    public void run() {
        continueListening = true;

        // Continue Listening if Some Response to Handle in responseMap
        while (continueListening && !responseMap.isEmpty()) {
            try {
                String response = in.readLine();

                JSONObject object = new JSONObject(response);
                String responseType = object.getString(RESPONSE_TYPE);

                if(responseMap.containsKey(responseType)) {
                    responseMap.get(responseType).handle(object);
                    responseMap.remove(responseType);
                }
                else {
                    Main.showError("Unknown Response...", 1000);
                }
            } catch (SocketTimeoutException e) {
                System.out.println("Server timeout...");
                System.out.println(responseMap.size());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        listenerThread = null;
    }
}
