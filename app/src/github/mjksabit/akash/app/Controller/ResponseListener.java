package github.mjksabit.akash.app.Controller;

import github.mjksabit.akash.app.Model.RequestAction;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

public class ResponseListener implements Runnable {

    public static final String RESPONSE_TYPE = "responseType";

    private BufferedReader in = null;
    ServerConnect server = null;
    private boolean continueListening;

    private Map<String, RequestAction> responseMap;

    public ResponseListener(BufferedReader in) {
        this.in = in;
        responseMap = new HashMap<>();
        continueListening = true;
        new Thread(this).start();
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
        while (continueListening) {
            try {
                String response = in.readLine();
                System.out.println(response);

                JSONObject object = new JSONObject(response);
                String responseType = object.getString(RESPONSE_TYPE);

                if(responseMap.containsKey(responseType)) {
                    responseMap.get(responseType).handle(object);
                }
                else {
                    System.out.println("Unknown Response...");
                }
            } catch (SocketTimeoutException e) {
                System.out.println("Server timeout...");
            }
            catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
