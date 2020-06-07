package github.mjksabit.akash.app.Model;

import org.json.JSONException;
import org.json.JSONObject;

import javax.xml.namespace.QName;

public class Transaction {
    private String id;
    private String otherThanMe;
    private String type;
    private String reference;
    private boolean cashOut;
    private double amount;

    public Transaction(String id, String otherThanMe, String type, String reference, boolean cashOut, double amount) {
        this.id = id;
        this.otherThanMe = otherThanMe;
        this.type = type;
        this.reference = reference;
        this.cashOut = cashOut;
        this.amount = amount;
    }

    public Transaction(JSONObject transaction) throws JSONException {
        this(
                transaction.getString("id"),
                transaction.getString("name"),
                transaction.getString("type"),
                transaction.getString("reference"),
                transaction.getBoolean("isCashOut"),
                transaction.getDouble("amount")
        );
    }

    public JSONObject getJSON() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("name", otherThanMe);
        object.put("type", type);
        object.put("reference", reference);
        object.put("isCashOut", cashOut);
        object.put("amount", amount);
        return object;
    }

    public double getAmount() {
        return amount;
    }

    public String getId() {
        return id;
    }

    public String getOtherThanMe() {
        return otherThanMe;
    }

    public String getType() {
        return type;
    }

    public String getReference() {
        return reference;
    }

    public boolean isCashOut() {
        return cashOut;
    }
}
