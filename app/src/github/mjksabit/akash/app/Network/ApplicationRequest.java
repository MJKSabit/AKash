package github.mjksabit.akash.app.Network;

import com.jfoenix.controls.JFXAlert;
import github.mjksabit.akash.app.Controller.Application;
import github.mjksabit.akash.app.Controller.SendMoney;
import github.mjksabit.akash.app.Main;
import github.mjksabit.akash.app.Model.Request;
import github.mjksabit.akash.app.Model.Transaction;
import github.mjksabit.akash.app.Model.User;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ServerSocket;

public class ApplicationRequest extends Request<Application> {

    private User user = null;

    public ApplicationRequest(Application requester) {
        super(requester);
    }

    public void balanceRequest() {
        JSONObject reqest = new JSONObject();
        try {
            reqest.put(REQUEST_TYPE, REQUEST_BALANCE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerConnect.getInstance().sendRequest(reqest);
        ServerConnect.getInstance().waitForResponse(REQUEST_BALANCE, (response) -> {
            if (response.getBoolean(RESPONSE_SUCCESS)) {
                double balance = response.getDouble("balance");
                Platform.runLater(() -> {
                    JFXAlert<Label> jfxAlert = new JFXAlert<>(Main.stage);
                    jfxAlert.setHideOnEscape(true);
                    Label label = new Label("Your Account Balance: "+ balance);
                    label.setPadding(new Insets(10));
                    jfxAlert.setContent(label);
                    jfxAlert.show();
//                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Your Account Balance: "+ balance);
//                    alert.showAndWait();
                });
            }
        });
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void loadTransaction(int index, int loadEverytime, int filter) {
        JSONObject request = new JSONObject();

        try {
            request.put(REQUEST_TYPE, REQUEST_GET_TRANSACTION);
            request.put("index", index);
            request.put("limit", loadEverytime);
            request.put("filter", filter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerConnect.getInstance().sendRequest(request);
        ServerConnect.getInstance().waitForResponse(REQUEST_GET_TRANSACTION, (response) -> {
            if(!response.getBoolean(RESPONSE_SUCCESS)) return;

            JSONArray transactions = response.getJSONArray("transactions");
            
            for (int i=0; i<transactions.length(); i++) {
                Transaction transaction = new Transaction(transactions.getJSONObject(i));
                Platform.runLater(() -> {
                    requester.addTransaction(transaction);
                });
            }
        });
    }

    public void loadNotification() {
        JSONObject request = new JSONObject();

        try {
            request.put(REQUEST_TYPE, REQUEST_GET_NOTIFICATION);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerConnect.getInstance().sendRequest(request);
        ServerConnect.getInstance().waitForResponse(REQUEST_GET_NOTIFICATION, (response) -> {
            Platform.runLater(() -> requester.clearNotifications());
            if(response.getBoolean(RESPONSE_SUCCESS)) {
                JSONArray notifications = response.getJSONArray("notifications");

                for (int i=0; i<notifications.length(); i++) {
                    String text = notifications.getString(i);
                    Platform.runLater(() -> requester.addNotification(text));
                }

                if(notifications.length()==0) {
                    Platform.runLater(() -> Main.showSuccess("No new notifications...", 2000));
                }
            }
            else {
                Platform.runLater(() -> Main.showError("Error fetching Notifications...", 2000));
            }
        });
    }

    public void exitRequest() {
        JSONObject request = new JSONObject();
        try {
            request.put(REQUEST_TYPE, "exit");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ServerConnect.getInstance().sendRequest(request);
    }

    public void logout() {
        JSONObject request = new JSONObject();
        try {
            request.put(REQUEST_TYPE, REQUEST_LOGOUT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        user = null;

        Main.replaceSceneContent("authentication");

        ServerConnect.getInstance().waitForResponse(REQUEST_LOGOUT, (json) -> Platform.runLater(() -> Main.showSuccess("Logged Out Successfully", 2000)));
        
        ServerConnect.getInstance().sendRequest(request);
    }
}
