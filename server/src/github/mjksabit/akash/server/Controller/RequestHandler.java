package github.mjksabit.akash.server.Controller;

import github.mjksabit.akash.server.Model.DBModel;
import github.mjksabit.akash.server.Model.User;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestHandler {

    public static final String REQUEST_TYPE = "requestType";
    private static final String RESPONSE_TYPE = "responseType";

    private static final String RESPONSE_UNKNOWN = "unknown";

    private static final String REQUEST_LOGIN = "login";
    private static final String RESPONSE_LOGIN_SUCCESS = "loginsuccess";
    private static final String RESPONSE_LOGIN_FAILED = "loginfailed";

    public String handle(JSONObject request) throws JSONException {
        System.out.println(request.toString());

        String requestType = request.getString(REQUEST_TYPE);

        switch (requestType) {
            case REQUEST_LOGIN:
                return logInRequest(request);
            default:
                JSONObject response = new JSONObject();
                response.put(RESPONSE_TYPE, RESPONSE_UNKNOWN);
                return response.toString();
        }
    }

    private String logInRequest(JSONObject request) throws JSONException {
        JSONObject response = new JSONObject();

        String mobile = request.getString("mobile");
        String password = request.getString("password");

        User user = DBModel.getInstance().getUser(mobile, password);

        if (user == null)  response.put(RESPONSE_TYPE, RESPONSE_LOGIN_FAILED);
        else response.put(RESPONSE_TYPE, RESPONSE_LOGIN_SUCCESS);

        return response.toString();
    }
}
