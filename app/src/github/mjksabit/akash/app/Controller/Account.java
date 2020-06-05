package github.mjksabit.akash.app.Controller;

import com.jfoenix.controls.JFXPasswordField;
import github.mjksabit.akash.app.Main;
import github.mjksabit.akash.app.Model.Controller;
import github.mjksabit.akash.app.Model.User;
import github.mjksabit.akash.app.Network.AccountRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Account extends Controller {

    User user = null;
    AccountRequest request = null;

    public void setUser(User user) {
        this.user = user;
        textMobileNumber.setText(user.getMobile());
        textUserName.setText(user.getName());
    }

    @FXML
    public void initialize() {
        super.setRootNode(root);
        request = new AccountRequest(this);
    }

    @FXML
    private AnchorPane root;

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
        getStage().close();
    }



    @FXML
    void updateUserInfo(ActionEvent event) {
        String oldPassword = textOldPassword.getText();

        if (user.getPassword().equals(oldPassword)) {
            String newPassword = textNewPassword.getText();
            if(newPassword.isEmpty()) {
                textNewPassword.requestFocus();
                textNewPassword.setFocusColor(Color.RED);
            }
            else {
                request.changePassword(user, newPassword);
                dialogClose(null);
            }
        } else {
            Main.showError((Pane)getStage().getScene().getRoot(), "Password Mismatch!", 2000);
        }
    }

}
