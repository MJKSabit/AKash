package github.mjksabit.akash.app.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import github.mjksabit.akash.app.Main;
import github.mjksabit.akash.app.Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ApplicationC {
    User user = null;
    ApplicationRequest request = null;

    @FXML
    public void initialize() {
        request = new ApplicationRequest(this);

        mainTabPane.getSelectionModel().selectedItemProperty().addListener((oldValue, oldTab, newTab) -> {
            if (newTab.textProperty().get().equals("Transactions")) {
                selectTransactionTab();
            } else if (newTab.textProperty().get().equals("Notification")) {
                selectNotificationTab();
            }
        });
    }

    private void selectTransactionTab() {
        System.out.println("Transaction Selected...");
    }

    private void selectNotificationTab() {
        System.out.println("Notification Selected...");
    }

    public void setUser(String mobile, String password, String name) {
        user = new User(mobile, password, name);
        buttonAccount.setText(name);
    }

    @FXML
    private JFXButton buttonAccount;

    @FXML
    private JFXButton buttonBalance;

    @FXML
    private JFXButton buttonToggle;

    @FXML
    private TabPane mainTabPane;

    @FXML
    private Tab tabHome;

    @FXML
    private JFXButton buttonSend;

    @FXML
    private JFXButton buttonMobileRecharge;

    @FXML
    private JFXButton buttonCashOut;

    @FXML
    private JFXButton buttonPayBill;

    @FXML
    private JFXButton buttonAddMoney;

    @FXML
    private JFXButton buttonDonate;

    @FXML
    private Tab tabTransactions;

    @FXML
    private JFXListView<HBox> listTransaction;

    @FXML
    private JFXButton buttonTransactionAll;

    @FXML
    private JFXButton buttonTransactionIn;

    @FXML
    private JFXButton buttonTransactionOut;

    @FXML
    private JFXButton buttonTransactionMore;

    @FXML
    private Tab tabNotification;

    @FXML
    private JFXListView<HBox> listNotification;

    @FXML
    void addMoney(ActionEvent event) {

    }

    @FXML
    void cashOut(ActionEvent event) {

    }

    @FXML
    void donateMoney(ActionEvent event) {

    }

    @FXML
    void loadMoreTransaction(ActionEvent event) {

    }

    @FXML
    void mobileRecharge(ActionEvent event) {

    }

    @FXML
    void payBill(ActionEvent event) {

    }

    @FXML
    void sendMoney(ActionEvent event) {

    }

    @FXML
    void showBalance(ActionEvent event) {
        request.balanceRequest();
    }

    @FXML
    void showProfile(ActionEvent event) {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UTILITY);
        try {
            FXMLLoader loader = Main.loadFXML("account");
            AccountC controller = new AccountC();
            loader.setController(controller);

            Scene scene = new Scene(loader.load());

            controller.setUser(user);
            controller.setStage(stage);

            stage.setScene(scene);
            stage.sizeToScene();
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void showTransactionAll(ActionEvent event) {

    }

    @FXML
    void showTransactionIn(ActionEvent event) {

    }

    @FXML
    void showTransactionOut(ActionEvent event) {

    }

}
