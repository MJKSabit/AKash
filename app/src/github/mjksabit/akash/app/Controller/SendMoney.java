package github.mjksabit.akash.app.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextField;
import github.mjksabit.akash.app.Model.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;

public class SendMoney extends Controller {

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
    private JFXProgressBar sendProgress;

    @FXML
    private JFXTextField textAmount;

    @FXML
    void startSendMoneyProgress(DragEvent event) {

    }

    @FXML
    void stopSendMoneyProgress(DragEvent event) {

    }
}
