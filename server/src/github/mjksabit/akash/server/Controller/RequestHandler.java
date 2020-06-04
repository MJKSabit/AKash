package github.mjksabit.akash.server.Controller;

import github.mjksabit.akash.server.Model.DBModel;
import github.mjksabit.akash.server.Model.User;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestHandler {

    public static final String REQUEST_TYPE = "requestType";
    private static final String RESPONSE_TYPE = "responseType";
    private static final String RESPONSE_SUCCESS = "success";

    private static final String RESPONSE_UNKNOWN = "unknown";

    private static final String REQUEST_LOGIN = "login";
    private static final String REQUEST_SIGNUP = "signup";



    public String handle(JSONObject request) throws JSONException {
        System.out.println(request.toString());

        String requestType = request.getString(REQUEST_TYPE);

        switch (requestType) {
            case REQUEST_LOGIN:
                return logInRequest(request);
            case REQUEST_SIGNUP:
                return signUpRequest(request);
            default:
                JSONObject response = new JSONObject();
                response.put(RESPONSE_TYPE, RESPONSE_UNKNOWN);
                return response.toString();
        }
    }

    private String signUpRequest(JSONObject request) throws JSONException {
        String name = request.getString("name");
        String password = request.getString("password");
        String mobile = request.getString("mobile");

        boolean result = DBModel.getInstance().createUser(mobile, password, name);

        request.remove(REQUEST_TYPE);
        request.put(RESPONSE_TYPE, REQUEST_SIGNUP);

        request.put(RESPONSE_SUCCESS, result);

        return request.toString();
    }

    private String logInRequest(JSONObject request) throws JSONException {
        JSONObject response = new JSONObject();

        String mobile = request.getString("mobile");
        String password = request.getString("password");

        User user = DBModel.getInstance().getUser(mobile, password);

        response.put(RESPONSE_TYPE, REQUEST_LOGIN);
        response.put(RESPONSE_SUCCESS, user != null);

        return response.toString();
    }
}
