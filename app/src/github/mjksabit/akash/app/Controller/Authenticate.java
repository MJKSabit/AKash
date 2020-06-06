package github.mjksabit.akash.app.Controller;

import com.jfoenix.controls.*;
import github.mjksabit.akash.app.Main;
import github.mjksabit.akash.app.Model.Controller;
import github.mjksabit.akash.app.Network.AuthenticateRequest;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.regex.Pattern;

public class Authenticate extends Controller {

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
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(ANIMATION_TIME), pane_signup);
        pane_login.setVisible(true);
        pane_login.setDisable(false);

        translateTransition.setFromX(0);
        translateTransition.setToX(369);
        translateTransition.play();

        new Thread(() -> {
            try {
                Thread.sleep(ANIMATION_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Platform.runLater( () -> {
                pane_signup.setVisible(false);
                pane_signup.setDisable(true);
            });
        }).start();
    }

    @FXML
    public void logInRequest(ActionEvent event) {
        String mobile, password;

        mobile = textLoginMobileNo.getText();
        password = textLogInPassword.getText();

        if (mobile.isEmpty() || password.isEmpty()) {
            Stage stage = (Stage) rootPane.getScene().getWindow();

//            JFXDialog dialog = new JFXDialog();
            Main.showError("Empty Fields...", 2000);
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

    private final int ANIMATION_TIME = 500;
    @FXML
    public void showSignUpPane(ActionEvent event) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(ANIMATION_TIME), pane_signup);
        pane_signup.setVisible(true);
        pane_signup.setDisable(false);
        translateTransition.setFromX(379);
        translateTransition.setToX(0);
        translateTransition.play();

        new Thread(() -> {
            try {
                Thread.sleep(ANIMATION_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Platform.runLater( () -> {
                pane_login.setVisible(false);
                pane_login.setDisable(true);
            });
        }).start();
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

        if (!Pattern.matches("^01\\d{9}", mobile)) {
            textSignUpMobileNo.requestFocus();
            textSignUpMobileNo.setFocusColor(Color.RED);
            return;
        }

        request.signUpRequest(mobile, password, name);

        textSignUpName.setText("");
        textSignUpPassword.setText("");
        textSignUpMobileNo.setText("");
    }

}
