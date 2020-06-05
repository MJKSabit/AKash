package github.mjksabit.akash.app.Network;

import github.mjksabit.akash.app.Controller.Application;
import github.mjksabit.akash.app.Controller.Authenticate;
import github.mjksabit.akash.app.Main;
import github.mjksabit.akash.app.Model.Request;
import javafx.application.Platform;
import org.json.JSONException;
import org.json.JSONObject;

public class AuthenticateRequest extends Request<Authenticate> {

    public AuthenticateRequest(Authenticate requester) {
        super(requester);
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

        ServerConnect.getInstance().waitForResponse(REQUEST_SIGNUP, (json) -> {
            boolean success = json.getBoolean(RESPONSE_SUCCESS);
            if(success) {
                Main.showSuccess("Sign Up Success" , 2000);

                Platform.runLater(() -> {
                    requester.backToLogIn(null);
                    requester.setLogInCredentials( mobileNo, password);
                });
            }
            else {
                Main.showSuccess("Sign Up Failed" , 2000);
            }

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

        ServerConnect.getInstance().waitForResponse(REQUEST_LOGIN, (json) ->  {
            boolean success = json.getBoolean(RESPONSE_SUCCESS);

            if (success){
                Main.showSuccess("Log In Success" , 2000);
                Platform.runLater(() -> {
                    Application controller = (Application) Main.replaceSceneContent("application");
                    try {
                        controller.setUser(mobileNo, password, json.getString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                Main.showError("Invalid Username/Password" , 2000);
            }
        });
    }
}
