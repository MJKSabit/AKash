package github.mjksabit.akash.app.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import github.mjksabit.akash.app.Model.Controller;
import github.mjksabit.akash.app.Model.User;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.DragEvent;

public class SendMoney extends Controller {

    User user = null;

    public void setUser(User user) {
        this.user = user;
        textSenderNumber.setText(user.getMobile());
    }

    @FXML
    public void initialize() {
        sendProgress.setVisible(false);
    }

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

    @FXML
    void startSendMoneyProgress(Event event) {
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
        System.out.println("SEND MONEY");
    }
}
