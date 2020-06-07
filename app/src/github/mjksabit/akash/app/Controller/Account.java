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

    // RequestHandler :: Tightly Coupled with this Controller
    AccountRequest request = null;

    // Initialized Before Other Tasks
    public void setUser(User user) {
        this.user = user;
        textMobileNumber.setText(user.getMobile());
        textUserName.setText(user.getName());
    }

    @FXML
    public void initialize() {
        super.setRootNode(root);

        // Set Up Specialized RequestHandler for this Controller
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
        // Close Window
        getStage().close();
    }


    @FXML
    void updateUserInfo(ActionEvent event) {
        String oldPassword = textOldPassword.getText();

        // Check if old Password Matches
        if (user.verifyPassword(oldPassword)) {
            String newPassword = textNewPassword.getText();

            if (newPassword.isEmpty()) {
                // Focus to Add New Password
                textNewPassword.requestFocus();
                textNewPassword.setFocusColor(Color.RED);
            } else {
                // Request Handled by Request Handler
                request.changePassword(user, newPassword);

                // Close Current Window
                dialogClose(null);
            }
        } else {
            // Password doesn't Match
            Pane rootPane = (Pane) getStage().getScene().getRoot();
            Main.showError(rootPane, "Password Mismatch!", 2000);
        }
    }

}
