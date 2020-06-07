package github.mjksabit.akash.app.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import github.mjksabit.akash.app.Main;
import github.mjksabit.akash.app.Model.Controller;
import github.mjksabit.akash.app.Model.Password;
import github.mjksabit.akash.app.Network.AuthenticateRequest;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.regex.Pattern;

public class Authenticate extends Controller {

    private static final String AGENT_PREFIX = "agent"; // Prefix added before the Agent Code

    // RequestHandler :: Tightly Coupled with this Controller
    private AuthenticateRequest request = null;

    @FXML
    private AnchorPane rootPane;

    // JavaFX Controller Initializer
    @FXML
    public void initialize() {
        backToLogIn(null);
        setRootNode(rootPane);

        // Setting Up RequestService Specialized for this Controller
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

    @FXML //// DRY
    public void backToLogIn(ActionEvent event) {

        // Slide Animation
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(ANIMATION_TIME), pane_signup);

        pane_login.setVisible(true);
        pane_login.setDisable(false);

        translateTransition.setFromX(0);
        translateTransition.setToX(pane_signup.getWidth());

        translateTransition.play();

        // Thread to Completely Hide signup_pane after ANIMATION_TIME
        new Thread(() -> {
            try {
                // Wait for Animation to end
                Thread.sleep(ANIMATION_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Disable other_pane
            Platform.runLater(() -> {
                pane_signup.setVisible(false);
                pane_signup.setDisable(true);
            });
        }).start();
    }

    @FXML
    public void logInRequest(ActionEvent event) {
        // Extract Data from TextFields
        String mobile = textLoginMobileNo.getText();
        String password = textLogInPassword.getText();

        if (mobile.isEmpty() || password.isEmpty()) { /// Replace with Regex?
            Main.showError("Empty Fields", 2000);
            return;
        }

        // Send Data to RequestHandlers
        request.logInRequest(mobile, password);
    }

    /**
     * Updates Log In page with given Data
     *
     * @param mobile   Mobile Number
     * @param password Password
     */
    public void setLogInCredentials(String mobile, String password) {
        textLoginMobileNo.setText(mobile);
        textLogInPassword.setText(password);
    }

    private final int ANIMATION_TIME = 500;

    @FXML
    public void showSignUpPane(ActionEvent event) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(ANIMATION_TIME), pane_signup);

        // Make Visible First, The Animate
        pane_signup.setVisible(true);
        pane_signup.setDisable(false);

        translateTransition.setFromX(pane_signup.getWidth());
        translateTransition.setToX(0);

        translateTransition.play();

        // Thread to Completely Hide login_pane after ANIMATION_TIME
        new Thread(() -> {
            // Wait for Animation to Complete
            try {
                Thread.sleep(ANIMATION_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Hide login_pane
            Platform.runLater(() -> {
                pane_login.setVisible(false);
                pane_login.setDisable(true);
            });
        }).start();
    }

    @FXML
    void signUpRequest(ActionEvent event) {
        // Extract Data from Required TextFields
        String mobile = textSignUpMobileNo.getText();
        String name = textSignUpName.getText();
        String password = textSignUpPassword.getText();

        // Check if Empty
        if (mobile.isEmpty() || password.isEmpty() || name.isEmpty()) {
            Main.showError("ERROR: Empty Text Fields", 2000);
            return;
        }

        // Check if a Valid Mobile Number is Provided, Or Agent <id>
        if (!(Pattern.matches("^01\\d{9}", mobile) || Pattern.matches("^"+AGENT_PREFIX+"\\d+$", mobile))) {
            textSignUpMobileNo.requestFocus();
            textSignUpMobileNo.setFocusColor(Color.RED);
            return;
        }

        // Delegate data to RequestHandler
        request.signUpRequest(mobile, password, name);

        // Clear TextFields
        textSignUpName.setText("");
        textSignUpPassword.setText("");
        textSignUpMobileNo.setText("");
    }

}
