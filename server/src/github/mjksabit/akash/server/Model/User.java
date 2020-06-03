package github.mjksabit.akash.server.Model;

public class User {
    private String mobileNumber, password, name;
    private double balance;

    public User(String mobileNumber, String password, String name) {
        this.mobileNumber = mobileNumber;
        this.password = password;
        this.name = name;
        balance = 0;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return DBModel.getInstance().getBalance(mobileNumber);
    }

    public void setName(String name) {
        this.name = name;
    }
}
