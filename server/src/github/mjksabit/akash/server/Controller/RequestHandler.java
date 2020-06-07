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

    public  static final String REQUEST_TYPE         = "requestType";
    private static final String RESPONSE_TYPE       = "responseType";
    private static final String RESPONSE_SUCCESS    = "success";
    private static final String RESPONSE_INFO       = "info";

    private static final String RESPONSE_UNKNOWN = "unknown";

    private static final String REQUEST_LOGIN               = "login";
    private static final String REQUEST_SIGNUP              = "signup";
    private static final String REQUEST_BALANCE             = "balance";
    private static final String REQUEST_CHANGE_PASSWORD     = "changepassword";
    private static final String REQUEST_SEND_MONEY          = "sendmoney";
    private static final String REQUEST_GET_TRANSACTION     = "gettransaction";
    private static final String REQUEST_GET_NOTIFICATION    = "getnotification";
    private static final String REQUEST_LOGOUT              = "logout";

    /**
     * Handles Request from {@link Client} provides Response
     *
     * @param request Request from Client in JSONFormat
     * @return Processed Request ( Response )
     * @throws JSONException
     */
    public String handle(JSONObject request) throws JSONException {

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

        // Reuse request JSON
        request.remove(REQUEST_TYPE);

        request.put(RESPONSE_TYPE, REQUEST_LOGOUT);
        request.put(RESPONSE_SUCCESS, true);

        // No logged in USER
        loggedInUser = null;

        return request.toString();
    }

    private String changePassword(JSONObject request) throws JSONException {
        JSONObject response = new JSONObject();
        response.put(RESPONSE_TYPE, REQUEST_CHANGE_PASSWORD);

        boolean successStatus = DBModel.getInstance().changePassword(
                request.getString("mobile"),
                request.getString("oldpassword"),
                request.getString("newpassword")
        );

        response.put(RESPONSE_SUCCESS, successStatus);

        return response.toString();
    }

    private String getTransaction(JSONObject request) throws JSONException {
        JSONObject response = new JSONObject();
        response.put(RESPONSE_TYPE, REQUEST_GET_TRANSACTION);

        ArrayList<Transaction> transactions =
                DBModel.getInstance().getTransactions(
                        loggedInUser.getMobileNumber(),
                        request.getInt("index"),
                        request.getInt("limit"),
                        request.getInt("filter")
                );

        boolean successStatus = (transactions != null);
        response.put(RESPONSE_SUCCESS, successStatus);

        if (successStatus) {
            JSONArray transactionsJSON = new JSONArray();

            for (Transaction transaction : transactions)
                transactionsJSON.put(transaction.getJSON()); // GetJSON of Transaction

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
        // Reuse response Object
        request.remove(REQUEST_TYPE);
        request.put(RESPONSE_TYPE, REQUEST_SIGNUP);

        String name     = request.getString("name");
        String password = request.getString("password");
        String mobile   = request.getString("mobile");

        boolean successStatus = DBModel.getInstance().createUser(mobile, password, name);

        request.put(RESPONSE_SUCCESS, successStatus);

        return request.toString();
    }

    private String logInRequest(JSONObject request) throws JSONException {
        JSONObject response = new JSONObject();
        response.put(RESPONSE_TYPE, REQUEST_LOGIN);

        String mobile   = request.getString("mobile");
        String password = request.getString("password");

        User user = DBModel.getInstance().getUser(mobile, password);

        boolean successStatus = user != null;
        response.put(RESPONSE_SUCCESS, successStatus);

        if (user != null) {
            response.put("name", user.getName());
            loggedInUser = user;
        }

        return response.toString();
    }

    private String sendMoneyRequest(JSONObject request) throws JSONException {
        JSONObject response = new JSONObject();
        response.put(RESPONSE_TYPE, REQUEST_SEND_MONEY);

        if (request.getString("receiver").equals(loggedInUser.getMobileNumber())) {
            response.put(RESPONSE_SUCCESS, false);
            response.put(RESPONSE_INFO, "Sender can not be Receiver!");

            return response.toString();
        }

        // Amount is Positive Check
        if (request.getDouble("amount") <= 0) {
            response.put(RESPONSE_SUCCESS, false);
            response.put(RESPONSE_INFO, "Amount less than or equal 0!");

            return response.toString();
        }

        // Lock the Database so that no other transaction can happent that time
        synchronized (DBModel.getInstance()) {

            double amount = request.getDouble("amount");

            // Transaction Amount can not be greater than Account Balance
            if (loggedInUser.getBalance() < amount) {
                response.put(RESPONSE_SUCCESS, false);
                response.put(RESPONSE_INFO, "Not enough money in the account!");

                return response.toString();
            }

            String sender       = loggedInUser.getMobileNumber();
            String receiver     = request.getString("receiver");
            String reference    = request.getString("reference");
            String type         = request.getString("type");

            // Receiver Empty means the money will be received by Someone Outside AKash
            if (receiver.isEmpty()) {
                System.err.println(">>> OUTER TRANSFER PROTOCOL: ");
                System.err.println("\t\t" + type + "\t::\t" + reference);
            }
            // Receiver Not Empty and User Does Not Exist, means Money can not be Transferred
            else if (!DBModel.getInstance().userExists(receiver)) {
                response.put(RESPONSE_SUCCESS, false);
                response.put(RESPONSE_INFO, "Receiver Not found!");

                return response.toString();
            }

            boolean successStatus = DBModel.getInstance().makeTransaction(
                    sender, receiver, amount, reference, type
            );

            response.put(RESPONSE_SUCCESS, successStatus);
            response.put(RESPONSE_INFO, "( SUCCESSFUL )");

            return response.toString();
        }
    }

    private String notification(JSONObject request) throws JSONException {
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
