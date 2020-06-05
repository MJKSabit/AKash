package github.mjksabit.akash.app.Model;

public abstract class Request <TypeC extends Controller> {
    protected static final String REQUEST_TYPE = "requestType";
    protected static final String RESPONSE_SUCCESS = "success";

    protected static final String REQUEST_BALANCE = "balance";
    protected static final String REQUEST_LOGIN = "login";
    protected static final String REQUEST_SIGNUP = "signup";
    protected static final String REQUEST_CHANGE_PASSWORD = "changepassword";
    protected static final String REQUEST_SEND_MONEY = "sendmoney";
    protected static final String REQUEST_GET_TRANSACTION = "gettransaction";
    protected static final String REQUEST_GET_NOTIFICATION = "getnotification";
    
    protected TypeC requester = null;

    public Request(TypeC requester) {
        this.requester = requester;
    }
}
