package github.mjksabit.akash.app.Controller;

import com.jfoenix.controls.JFXAlert;
import github.mjksabit.akash.app.Main;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import org.json.JSONException;
import org.json.JSONObject;

public class ApplicationRequest {
    private static final String REQUEST_TYPE = "requestType";
    private static final String RESPONSE_SUCCESS = "success";

    private static final String REQUEST_BALANCE = "balance";

    ApplicationC requester;

    public ApplicationRequest(ApplicationC requester) {
        this.requester = requester;
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
}
