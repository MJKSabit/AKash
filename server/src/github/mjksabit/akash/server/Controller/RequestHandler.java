package github.mjksabit.akash.server.Controller;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestHandler {

    public static final String REQUEST_TYPE = "requestType";

    public String handle(JSONObject request) throws JSONException {

        System.out.println("Request Type : " + request.getString(REQUEST_TYPE));

        return request.getString(REQUEST_TYPE) + " response";
    }
}
