package github.mjksabit.akash.app.Controller;

import javafx.application.Platform;
import org.json.JSONException;
import org.json.JSONObject;

public class ResponseHandler {
    private static final String RESPONSE_TYPE = "responseType";

    public ResponseHandler(JSONObject response) throws JSONException {
        System.out.println(response.getString(RESPONSE_TYPE));
    }
}
