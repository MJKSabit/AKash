package github.mjksabit.akash.app.Controller;

import com.jfoenix.controls.*;
import github.mjksabit.akash.app.Main;
import github.mjksabit.akash.app.Model.Controller;
import github.mjksabit.akash.app.Netword.AuthenticateRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AuthenticateC extends Controller {

    private AuthenticateRequest request = null;

    @FXML
    private AnchorPane rootPane;

    @FXML
    public void initialize() {
        backToLogIn(null);
        setRootNode(rootPane);

        request = new AuthenticateRequest(this);
    }

    @FXML
    private Pane pane_login;

    @FXML
    private JFXTextField textLoginMobileNo;

    @FXML
    private JFXPasswordField textLogInPassword;

    @FXML
    private JFXButton buttonLogIn;

    @FXML
    private Pane pane_signup;

    @FXML
    private JFXTextField textSignUpMobileNo;

    @FXML
    private JFXPasswordField textSignUpPassword;

    @FXML
    private JFXTextField textSignUpName;

    @FXML
    public void backToLogIn(ActionEvent event) {
        pane_login.setVisible(true);
        pane_login.setDisable(false);

        pane_signup.setVisible(false);
        pane_signup.setDisable(true);
    }

    @FXML
    public void logInRequest(ActionEvent event) {
        String mobile, password;

        mobile = textLoginMobileNo.getText();
        password = textLogInPassword.getText();

        if (mobile.isEmpty() || password.isEmpty()) {
            Stage stage = (Stage) rootPane.getScene().getWindow();

//            JFXDialog dialog = new JFXDialog();
            Main.showError("Empty Text Fields...", 2000);
            return;
        }

        request.logInRequest(mobile, password);

//        textLoginMobileNo.setText("");
//        textLogInPassword.setText("");
    }

    public void setLogInCredentials(String mobile, String password) {
        textLoginMobileNo.setText(mobile);
        textLogInPassword.setText(password);
    }

    @FXML
    public void showSignUpPane(ActionEvent event) {
        pane_signup.setVisible(true);
        pane_signup.setDisable(false);

        pane_login.setVisible(false);
        pane_login.setDisable(true);
    }

    @FXML
    void signUpRequest(ActionEvent event) {
        String mobile = textSignUpMobileNo.getText();
        String name = textSignUpName.getText();
        String password = textSignUpPassword.getText();

        if (mobile.isEmpty() || password.isEmpty() || name.isEmpty()) {
//            Stage stage = (Stage) rootPane.getScene().getWindow();
            Main.showError("Empty Text Fields...", 2000);
            return;
        }

        request.signUpRequest(mobile, password, name);

        textSignUpName.setText("");
        textSignUpPassword.setText("");
        textSignUpMobileNo.setText("");
    }

}
