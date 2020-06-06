package github.mjksabit.akash.app.Model;

public class User {
    private String mobile;
    private Password password;
    private String name;

    public User(String mobile, String password, String name) {
        this.mobile = mobile;
        this.password = new Password(password);
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = new Password(password);
    }

    public void setName(String name) {
        this.name = name;
    }
}
