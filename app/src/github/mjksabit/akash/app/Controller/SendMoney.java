package github.mjksabit.akash.app.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXProgressBar;
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
import javafx.scene.input.DragEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import static java.lang.Double.*;

public class SendMoney extends Controller {

    User user = null;
    SendMoneyRequest request = null;

    public void setUser(User user) {
        this.user = user;
        textSenderNumber.setText(user.getMobile());
    }

    @FXML
    public void initialize() {
        sendProgress.setVisible(false);
        request = new SendMoneyRequest(this);
        setRootNode(root);
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

    final int totalTime = 1000;
    final int perStep = 50;
    boolean tap;

    boolean validateInput() {
        if(textSendTo.getText().isEmpty()) {
            textSendTo.requestFocus();
            textSendTo.setFocusColor(Color.RED);
            return false;
        }

        if(textAmount.getText().isEmpty()) {
            textAmount.requestFocus();
            textAmount.setFocusColor(Color.RED);
            return false;
        }

        try{Double.parseDouble(textAmount.getText());}
        catch (NumberFormatException e) {
            textAmount.requestFocus();
            textAmount.setFocusColor(Color.RED);
            return false;
        }

        if(!textPassword.getText().equals(user.getPassword())) {
            Main.showError((Pane) getRoot(), "Password Mismatch!", 2000);
            return false;
        }

        return true;
    }

    @FXML
    void startSendMoneyProgress(Event event) {
        if(!validateInput()) return;

        tap = true;
        new Thread(() -> {
            int time;
            sendProgress.setVisible(true);
            for (time=perStep; time<=totalTime && tap; time += perStep) {
                double progress = ((double)time)/totalTime;
                try {
                    Thread.sleep(perStep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(() -> sendProgress.setProgress(progress));
            }
            sendProgress.setVisible(false);
            if (time>totalTime) sendMoneyRequest();
        }).start();
    }

    @FXML
    void stopSendMoneyProgress(Event event) {
        tap = false;
    }

    public void sendMoneyRequest() {
        request.sendMoney(user, textSendTo.getText(), parseDouble(textAmount.getText()), textReference.getText());
    }
}
