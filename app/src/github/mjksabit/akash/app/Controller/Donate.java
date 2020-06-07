package github.mjksabit.akash.app.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import github.mjksabit.akash.app.Main;
import github.mjksabit.akash.app.Model.Controller;
import github.mjksabit.akash.app.Model.User;
import github.mjksabit.akash.app.Network.SendMoneyRequest;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.regex.Pattern;

import static java.lang.Double.parseDouble;

public class Donate extends Controller {

    User user = null;

    // RequestHandler :: Tightly Coupled with this Controller
    SendMoneyRequest request = null;

    // Set User Data
    public void setUser(User user) {
        this.user = user;
        textSenderNumber.setText(user.getMobile());
    }

    @FXML
    public void initialize() {
        super.setRootNode(root);

        // Send Progress only Visible when Send is Tapped
        sendProgress.setVisible(false);

        // Sets Up RequestHandler
        request = new SendMoneyRequest(this);
    }

    @FXML
    AnchorPane root;

    @FXML
    private Label textSenderNumber;

    @FXML
    private JFXTextField textSendTo;

    @FXML
    private JFXTextField textReference;

    @FXML
    private JFXPasswordField textPassword;

    @FXML
    private JFXButton buttonSend;

    @FXML
    private ProgressBar sendProgress;

    @FXML
    private JFXTextField textAmount;

    // TextField Empty Validator, if Empty, focus
    boolean notEmpty(JFXTextField textField) {
        if (textField.getText().isEmpty()) {
            textField.requestFocus();
            textField.setFocusColor(Color.RED);
            return false;
        }
        return true;
    }

    // TextField Number Validator, if Not a Number, focus
    boolean isMobileNumber(JFXTextField textField) {
        if (Pattern.matches("^01\\d{9}", textField.getText()))
            return true;
        textField.requestFocus();
        textField.setFocusColor(Color.RED);
        return false;
    }

    // Number Validator, if not a number, focus
    boolean isNumber(JFXTextField textField) {
        if (Pattern.matches("^\\d*\\.?\\d*$", textField.getText()))
            return true;
        textField.requestFocus();
        textField.setFocusColor(Color.RED);
        return false;
    }

    // Password Validator, if doesn't match, focus
    boolean matchPassword(JFXPasswordField passwordField) {
        if (passwordField.getText().isEmpty()) {
            passwordField.requestFocus();
            passwordField.setFocusColor(Color.RED);
            return false;
        }

        if (!user.verifyPassword(passwordField.getText())) {
            Main.showError((Pane) getRoot(), "Password Mismatch!", 2000);
            return false;
        }
        return true;
    }

    boolean validateInput() {
        return notEmpty(textSendTo) && notEmpty(textAmount) &&
                isMobileNumber(textSendTo) && isNumber(textAmount) &&
                matchPassword(textPassword);
    }

    final int totalTime = 1000;
    final int perStep = 50;
    boolean tap;

    @FXML
    void startSendMoneyProgress(Event event) {
        // Don't start Send Money Progress if InputValidation Fails
        if (!validateInput()) return;

        tap = true;

        // ProgressBar Progress Update Thread
        new Thread(() -> {
            int time;

            sendProgress.setProgress(0);
            sendProgress.setVisible(true);

            // Loop Until totalTime or not Tapped to Send
            for (time = perStep; time <= totalTime && tap; time += perStep) {
                double progress = ((double) time) / totalTime;

                // WaitFor perStep milliSeconds then Update Progress Bar
                try {
                    Thread.sleep(perStep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Platform.runLater(() -> sendProgress.setProgress(progress));
            }

            // Hide Progress Bar
            sendProgress.setVisible(false);

            // Loop was Ended due to Time being Finished
            if (time > totalTime) sendMoneyRequest();

        }).start();
    }

    @FXML
    void stopSendMoneyProgress(Event event) {
        // Currently not tapped to Send Button
        tap = false;
    }

    public void sendMoneyRequest() {
        request.sendMoney(user, textSendTo.getText(), parseDouble(textAmount.getText()), textReference.getText(), "Donation");
    }
}
