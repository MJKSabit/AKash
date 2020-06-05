package github.mjksabit.akash.app.Network;

import github.mjksabit.akash.app.Controller.Account;
import github.mjksabit.akash.app.Main;
import github.mjksabit.akash.app.Model.Request;
import github.mjksabit.akash.app.Model.User;
import javafx.application.Platform;
import org.json.JSONException;
import org.json.JSONObject;

public class AccountRequest extends Request<Account> {

    public AccountRequest(Account requester) {
        super(requester);
    }

    public void changePassword(User user, String newPassword) {
        JSONObject request = new JSONObject();

        try {
            request.put(REQUEST_TYPE, REQUEST_CHANGE_PASSWORD);

            request.put("mobile", user.getMobile());
            request.put("oldpassword", user.getPassword());
            request.put("newpassword", newPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerConnect.getInstance().sendRequest(request);

        ServerConnect.getInstance().waitForResponse(REQUEST_CHANGE_PASSWORD, (response) -> {
            if (response.getBoolean(RESPONSE_SUCCESS)) {
                Platform.runLater( () -> Main.showSuccess("Password Changed Successfully", 2000));
            }
            else {
                Platform.runLater(() -> Main.showError("Can't Change Password :(", 2000));
            }
        });
    }
}
