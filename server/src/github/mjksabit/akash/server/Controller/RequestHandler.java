package github.mjksabit.akash.server.Controller;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestHandler {

    public static final String REQUEST_TYPE = "requestType";
    private static final String RESPONSE_TYPE = "responseType";

    public String handle(JSONObject request) throws JSONException {

        System.out.println("Request Type : " + request.getString(REQUEST_TYPE));

        JSONObject response = new JSONObject();
        response.put(RESPONSE_TYPE, "Connected Response");

        return response.toString();
    }
}
