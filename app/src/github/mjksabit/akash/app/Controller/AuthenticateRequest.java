package github.mjksabit.akash.app.Controller;

import github.mjksabit.akash.app.Main;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import org.json.JSONException;
import org.json.JSONObject;

public class AuthenticateRequest {
    private static final String REQUEST_TYPE = "requestType";

    private static final String REQUEST_LOGIN = "login";
    private static final String RESPONSE_LOGIN_SUCCESS = "loginsuccess";
    private static final String RESPONSE_LOGIN_FAILED = "loginfailed";

    private static final String REQUEST_SIGNUP = "signup";
    private static final String RESPONSE_SIGNUP_SUCCESS = "signupsuccess";
    private static final String RESPONSE_SIGNUP_FAILED = "signupfailed";

    AuthenticateC requester;
    public AuthenticateRequest(AuthenticateC requester) {
        this.requester = requester;
    }

    public void signUpRequest(String mobileNo, String password, String name) {
        JSONObject request = new JSONObject();

        try {
            request.put(REQUEST_TYPE, REQUEST_SIGNUP);

            request.put("mobile", mobileNo);
            request.put("password", password);
            request.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerConnect.getInstance().sendRequest(request);

        ServerConnect.getInstance().waitForResponse(RESPONSE_SIGNUP_SUCCESS, (json) -> {
            Main.showSuccess("Sign Up Success" , 2000);

            Platform.runLater(() -> {
                requester.backToLogIn(null);
                requester.setLogInCredentials( mobileNo, password);
            });
        });

        ServerConnect.getInstance().waitForResponse(RESPONSE_SIGNUP_FAILED, (json) -> {
            Main.showSuccess("Sign Up Failed" , 2000);
        });
    }

    public void logInRequest(String mobileNo, String password) {
        JSONObject request = new JSONObject();

        try {
            request.put(REQUEST_TYPE, REQUEST_LOGIN);

            request.put("mobile", mobileNo);
            request.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerConnect.getInstance().sendRequest(request);

        ServerConnect.getInstance().waitForResponse(RESPONSE_LOGIN_SUCCESS, (json) ->  {
            Main.showSuccess("Log In Success" , 2000);
        });

        ServerConnect.getInstance().waitForResponse(RESPONSE_LOGIN_FAILED, (json) ->  {
            Main.showError("Another account exists with this number" , 2000);
        });
    }
}
