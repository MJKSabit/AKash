package github.mjksabit.akash.app.Model;

public class User {
    private String mobile;
    private String password;
    private String name;

    public User(String mobile, String password, String name) {
        this.mobile = mobile;
        this.password = password;
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }
}
