package github.mjksabit.akash.app.Model;

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
