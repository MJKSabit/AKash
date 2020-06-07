package github.mjksabit.akash.app.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXListView;
import github.mjksabit.akash.app.Main;
import github.mjksabit.akash.app.Model.*;
import github.mjksabit.akash.app.Network.ApplicationRequest;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Application extends Controller {
    User user = null;

    // RequestHandler :: Tightly Coupled with this Controller
    ApplicationRequest request = null;

    // TransactionList Source
    ObservableList<Transaction> transactions = FXCollections.observableArrayList();

    // NotificationList Source
    ObservableList<Notification> notifications = FXCollections.observableArrayList();

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void addNotification(String text) {
        notifications.add(new Notification(text));
    }

    public void clearNotifications() {
        notifications.clear();
    }

    private static final int FILTER_SHOW_ALL = 0;
    private static final int FILTER_SHOW_SENT = -1;
    private static final int FILTER_SHOW_RECEIVED = +1;

    private int transactionFilter = FILTER_SHOW_ALL;

    @FXML
    public void initialize() {

        // Sets Up RequestHandler
        request = new ApplicationRequest(this);

        // Tab Change Listener : Load Transaction or Notifications when selected
        mainTabPane.getSelectionModel().selectedItemProperty().addListener((oldValue, oldTab, newTab) -> {

            // Transactions Tab Selected
            if (newTab.textProperty().get().equals("Transactions")) {
                selectTransactionTab();
            }
            // Notifications Tab Selected
            else if (newTab.textProperty().get().equals("Notification")) {
                selectNotificationTab();
            }
        });

        // Option Drawer Initial Property
        optionDrawer.setSidePane(drawerContainer);
        optionDrawer.close();

        // Custom ListCell for TransactionsListView
        listTransaction.setCellFactory(transactionListView -> new TransactionViewListCell());

        // ObservableList Data Biding
        listTransaction.setItems(transactions);
        listNotification.setItems(notifications);
    }

    private void selectTransactionTab() {
        // Fresh Start
        transactions.clear();

        // Reuse Code to Load Transactions
        loadMoreTransaction(null);
    }

    private void selectNotificationTab() {

        // Show Loading until Notifications are loaded
        notifications.add(new Notification("Loading Notifications..."));

        // Reuse Code to Load Notifications
        request.loadNotification();
    }

    // Set User Data, A MUST DO
    public void setUser(String mobile, String password, String name) {
        user = new User(mobile, password, name);

        // Button Name to Show Account
        buttonAccount.setText(name);

        // Set User for RequestHandler
        request.setUser(user);
    }

    @FXML
    private Pane drawerContainer;

    @FXML
    private JFXDrawer optionDrawer;

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
    private ListView<Transaction> listTransaction;

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
    private JFXListView<Notification> listNotification;

    @FXML
    void bankTransfer(ActionEvent event) {
        Stage stage = new Stage();

        // Open New Window for BankTransfer
        BankTransfer controller = (BankTransfer) Main.newWindowUtility("bankTransfer", stage, "Bank Transfer");
        controller.setUser(user);

        stage.showAndWait();
    }

    @FXML
    void cashOut(ActionEvent event) {
        Stage stage = new Stage();

        // Open New Window for CashOut
        CashOut controller = (CashOut) Main.newWindowUtility("cashOut", stage, "Cash Out");
        controller.setUser(user);

        stage.showAndWait();
    }

    @FXML
    void donateMoney(ActionEvent event) {
        Stage stage = new Stage();

        // Open New Window for Donation
        Donate controller = (Donate) Main.newWindowUtility("donate", stage, "Donation");
        controller.setUser(user);

        stage.showAndWait();
    }


    private static final int loadEverytime = 10;

    @FXML
    void loadMoreTransaction(ActionEvent event) {
        request.loadTransaction(
                transactions.size(), // Index of Transaction
                loadEverytime,
                transactionFilter
        );
    }

    @FXML
    void mobileRecharge(ActionEvent event) {
        Stage stage = new Stage();

        // Open New Window for Mobile recharge
        MobileRecharge controller = (MobileRecharge) Main.newWindowUtility("mobileRecharge", stage, "Mobile Recharge");
        controller.setUser(user);

        stage.showAndWait();
    }

    @FXML
    void payBill(ActionEvent event) {
        Stage stage = new Stage();

        // Open New Window for Pay Bill
        PayBill controller = (PayBill) Main.newWindowUtility("payBill", stage, "Pay Bill");
        controller.setUser(user);

        stage.showAndWait();
    }

    @FXML
    void sendMoney(ActionEvent event) {
        Stage stage = new Stage();

        // Open New Window for Send Money
        SendMoney controller = (SendMoney) Main.newWindowUtility("sendMoney", stage, "Send Money");
        controller.setUser(user);

        stage.showAndWait();
    }

    @FXML
    void showBalance(ActionEvent event) {
        // Send Request to show Balance
        request.balanceRequest();
    }

    @FXML
    void showProfile(ActionEvent event) {
        Stage stage = new Stage();

        // Open New Window for Profile Editor
        Account controller = (Account) Main.newWindowUtility("account", stage, "Account Info");
        controller.setUser(user);

        stage.showAndWait();
    }

    @FXML
    void showTransactionAll(ActionEvent event) {
        transactionFilter = FILTER_SHOW_ALL;

        // Fresh Start
        transactions.clear();

        loadMoreTransaction(null);
    }

    @FXML
    void showTransactionIn(ActionEvent event) {
        transactionFilter = FILTER_SHOW_RECEIVED;

        // Fresh Start
        transactions.clear();

        loadMoreTransaction(null);
    }

    @FXML
    void showTransactionOut(ActionEvent event) {
        transactionFilter = FILTER_SHOW_SENT;

        // Fresh Start
        transactions.clear();

        loadMoreTransaction(null);
    }

    @FXML
    void toggleDrawer(ActionEvent event) {
        if (optionDrawer.isOpened()) {
            optionDrawer.close();

            // Option Drawer Hide Thread After Animation Done
            new Thread(() -> {
                while (optionDrawer.isClosing()) ;
                Platform.runLater(() -> optionDrawer.setVisible(false));
            }).start();

            // remove Opened Background Style
            buttonToggle.setStyle("-fx-background-color: inherit;");

        } else {

            // Open Drawer
            optionDrawer.open();
            optionDrawer.setVisible(true);

            // DRAWER_OPEN indicator style
            buttonToggle.setStyle("-fx-background-color: #2222");
        }
    }

    @FXML
    void exitAkash(ActionEvent event) {

        // Telling Server to Stop Listening
        request.exitRequest();

        // Exit
        System.exit(0);
    }

    @FXML
    void logOut(ActionEvent event) {

        // Send Log Out Request
        request.logout();

    }

}
