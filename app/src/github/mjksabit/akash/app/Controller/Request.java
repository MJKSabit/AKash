package github.mjksabit.akash.app.Controller;

import javafx.application.Platform;
import org.json.JSONException;
import org.json.JSONObject;

public class Request {
    private static final String REQUEST_TYPE = "requestType";

    private static final String TYPE_LOGIN = "login";
    private static final String RESPONSE_LOGIN_SUCCESS = "loginsuccess";
    private static final String RESPONSE_LOGIN_FAILED = "loginfailed";

    private static final String TYPE_SIGNUP = "signup";


    ServerConnect serverConnect = null;

    public Request() {
        serverConnect = ServerConnect.getInstance();
    }

    public void logInRequest(String mobileNo, String password) {
        JSONObject request = new JSONObject();

        try {
            request.put(REQUEST_TYPE, TYPE_LOGIN);

            request.put("mobile", mobileNo);
            request.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        serverConnect.sendRequest(request);

        serverConnect.waitForResponse(RESPONSE_LOGIN_SUCCESS, (json) ->  {
            System.out.println("Sign In Success...");
            serverConnect.stopListeningResponse(RESPONSE_LOGIN_SUCCESS);
            serverConnect.stopListeningResponse(RESPONSE_LOGIN_FAILED);
        });

        serverConnect.waitForResponse(RESPONSE_LOGIN_FAILED, (json) ->  {
            System.out.println("Sign In Failed...");
            serverConnect.stopListeningResponse(RESPONSE_LOGIN_SUCCESS);
            serverConnect.stopListeningResponse(RESPONSE_LOGIN_FAILED);
        });
    }
}
