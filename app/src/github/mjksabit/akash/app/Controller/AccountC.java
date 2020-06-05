package github.mjksabit.akash.app.Controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import github.mjksabit.akash.app.Main;
import github.mjksabit.akash.app.Model.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Stack;

public class AccountC {

    User user = null;
    Stage stage = null;

    public void setUser(User user) {
        this.user = user;
        textMobileNumber.setText(user.getMobile());
        textUserName.setText(user.getName());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private Label textMobileNumber;

    @FXML
    private Label textUserName;

    @FXML
    private JFXPasswordField textNewPassword;

    @FXML
    private JFXPasswordField textOldPassword;

    @FXML
    void dialogClose(ActionEvent event) {
        stage.close();
    }

    private static final String REQUEST_TYPE = "requestType";
    private static final String RESPONSE_SUCCESS = "success";
    private static final String REQUEST_CHANGE_PASSWORD = "changepassword";

    @FXML
    void updateUserInfo(ActionEvent event) {
        String oldPassword = textOldPassword.getText();

        if (user.getPassword().equals(oldPassword)) {
            String newPassword = textNewPassword.getText();
            if(newPassword.isEmpty()) {
                textNewPassword.requestFocus();
            }
            else {
                JSONObject request = new JSONObject();
                try {
                    request.put(REQUEST_TYPE, REQUEST_CHANGE_PASSWORD);

                    request.put("oldpassword", oldPassword);
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

                dialogClose(null);
            }
        } else {
            Main.showError((Pane)stage.getScene().getRoot(), "Password Mismatch!", 2000);
        }
    }

}
