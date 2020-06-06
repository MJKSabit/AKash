package github.mjksabit.akash.server.Controller;

import github.mjksabit.akash.server.Model.DBModel;
import github.mjksabit.akash.server.Model.Transaction;
import github.mjksabit.akash.server.Model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RequestHandler {

    private User loggedInUser = null;

    public static final String REQUEST_TYPE = "requestType";
    private static final String RESPONSE_TYPE = "responseType";
    private static final String RESPONSE_SUCCESS = "success";
    private static final String RESPONSE_INFO = "info";

    private static final String RESPONSE_UNKNOWN = "unknown";

    private static final String REQUEST_LOGIN = "login";
    private static final String REQUEST_SIGNUP = "signup";
    private static final String REQUEST_BALANCE = "balance";
    protected static final String REQUEST_CHANGE_PASSWORD = "changepassword";
    protected static final String REQUEST_SEND_MONEY = "sendmoney";
    protected static final String REQUEST_GET_TRANSACTION = "gettransaction";
    protected static final String REQUEST_GET_NOTIFICATION = "getnotification";
    protected static final String REQUEST_LOGOUT = "logout";


    public String handle(JSONObject request) throws JSONException {
        System.out.println(request.toString());

        String requestType = request.getString(REQUEST_TYPE);

        switch (requestType) {
            case REQUEST_LOGIN:
                return logInRequest(request);
            case REQUEST_SIGNUP:
                return signUpRequest(request);
            case REQUEST_BALANCE:
                return balanceRequest(request);
            case REQUEST_SEND_MONEY:
                return sendMoneyRequest(request);
            case REQUEST_GET_TRANSACTION:
                return getTransaction(request);
            case REQUEST_GET_NOTIFICATION:
                return notification(request);
            case REQUEST_CHANGE_PASSWORD:
                return changePassword(request);
            case REQUEST_LOGOUT:
                return logOut(request);
            default:
                JSONObject response = new JSONObject();
                response.put(RESPONSE_TYPE, requestType);
                response.put(RESPONSE_SUCCESS, false);
                return response.toString();
        }
    }

    private String logOut(JSONObject request) throws JSONException {
        request.remove(REQUEST_TYPE);
        request.put(RESPONSE_TYPE, REQUEST_LOGOUT);
        request.put(RESPONSE_SUCCESS, true);

        loggedInUser = null;

        return request.toString();
    }

    private String changePassword(JSONObject request) throws JSONException {
        JSONObject response = new JSONObject();
        response.put(RESPONSE_TYPE, REQUEST_CHANGE_PASSWORD);

        response.put(RESPONSE_SUCCESS, DBModel.getInstance().changePassword(
                request.getString("mobile"), request.getString("oldpassword"), request.getString("newpassword")
        ));

        return response.toString();
    }

    private String getTransaction(JSONObject request) throws JSONException {
        JSONObject response = new JSONObject();
        response.put(RESPONSE_TYPE, REQUEST_GET_TRANSACTION);

        ArrayList<Transaction> transactions = DBModel.getInstance().getTransactions(
                loggedInUser.getMobileNumber(), request.getInt("index"), request.getInt("limit"), request.getInt("filter"));

        response.put(RESPONSE_SUCCESS, transactions!=null);
        if(transactions != null ) {
            JSONArray transactionsJSON = new JSONArray();

            for (Transaction transaction : transactions)
                transactionsJSON.put(transaction.getJSON());

            response.put("transactions", transactionsJSON);
        }

        return response.toString();
    }

    private String balanceRequest(JSONObject request) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(RESPONSE_TYPE, request.getString(REQUEST_TYPE));
        jsonObject.put(RESPONSE_SUCCESS, true);
        jsonObject.put("balance", loggedInUser.getBalance());

        return jsonObject.toString();
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

        if(user!=null) {
            response.put("name", user.getName());
            loggedInUser = user;
        }

        return response.toString();
    }

    private String sendMoneyRequest(JSONObject request) throws JSONException {
        JSONObject response = new JSONObject();

        response.put(RESPONSE_TYPE, REQUEST_SEND_MONEY);

        if (request.getDouble("amount") <= 0) {
            response.put(RESPONSE_SUCCESS, false);
            response.put(RESPONSE_INFO, "Amount less than 0!");
            return response.toString();
        }

        synchronized (DBModel.getInstance()) {
            double amount = request.getDouble("amount");
            if (loggedInUser.getBalance() < amount) {
                response.put(RESPONSE_SUCCESS, false);
                response.put(RESPONSE_INFO, "Not enough money in the account!");
                return response.toString();
            }

            String sender, receiver, reference, type;
            sender = loggedInUser.getMobileNumber();
            receiver = request.getString("receiver");
            reference = request.getString("reference");
            type = request.getString("type");

            if(receiver.isEmpty()) {
                System.err.println("Handle Outer Transfer Protocol: ");
                System.err.println("--------------------------------");
                System.err.println(type+"\t::\t"+reference);
            }
            else if(!DBModel.getInstance().userExists(receiver)) {
                response.put(RESPONSE_SUCCESS, false);
                response.put(RESPONSE_INFO, "Receiver Not found!");
                return response.toString();
            }

            response.put(RESPONSE_SUCCESS , DBModel.getInstance().makeTransaction(
                    sender, receiver, amount, reference, type
            ));

            response.put(RESPONSE_INFO, "Database Update");

            return response.toString();
        }
    }

    protected String notification(JSONObject request) throws JSONException {
        JSONObject response = new JSONObject();
        response.put(RESPONSE_TYPE, REQUEST_GET_NOTIFICATION);

        ArrayList<String> notifications = DBModel.getInstance().getNotifications();

        JSONArray array = new JSONArray();
        for (String data : notifications)
            array.put(data);

        response.put("notifications", array);
        response.put(RESPONSE_SUCCESS, true);

        return response.toString();
    }


}
