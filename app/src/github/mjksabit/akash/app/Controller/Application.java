package github.mjksabit.akash.app.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import github.mjksabit.akash.app.Main;
import github.mjksabit.akash.app.Model.Controller;
import github.mjksabit.akash.app.Model.User;
import github.mjksabit.akash.app.Network.ApplicationRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Application extends Controller {
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
        request.setUser(user);
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
        Stage stage = new Stage();
        CashOut controller = (CashOut) Main.newWindowUtility("cashOut", stage, "Cash Out");
        controller.setUser(user);
        stage.showAndWait();
    }

    @FXML
    void donateMoney(ActionEvent event) {
        Stage stage = new Stage();
        Donate controller = (Donate) Main.newWindowUtility("donate", stage, "Donation");
        controller.setUser(user);
        stage.showAndWait();
    }

    @FXML
    void loadMoreTransaction(ActionEvent event) {

    }

    @FXML
    void mobileRecharge(ActionEvent event) {

    }

    @FXML
    void payBill(ActionEvent event) {
        Stage stage = new Stage();
        PayBill controller = (PayBill) Main.newWindowUtility("payBill", stage, "Pay Bill");
        controller.setUser(user);
        stage.showAndWait();
    }

    @FXML
    void sendMoney(ActionEvent event) {
        Stage stage = new Stage();
        SendMoney controller = (SendMoney) Main.newWindowUtility("sendMoney", stage, "Send Money");
        controller.setUser(user);
        stage.showAndWait();
    }

    @FXML
    void showBalance(ActionEvent event) {
        request.balanceRequest();
    }

    @FXML
    void showProfile(ActionEvent event) {
        Stage stage = new Stage();
        Account controller = (Account) Main.newWindowUtility("account", stage, "Account Info");
        controller.setUser(user);
        stage.showAndWait();
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
