package github.mjksabit.akash.app.Model;

import org.apache.commons.codec.digest.DigestUtils;

public class Password {
    private String password;
    private String passwordHash;

    public Password(String password) {
        this.password = password;
        this.passwordHash = DigestUtils.sha256Hex(password);
    }

    public String getPasswordHash() {
        return passwordHash;
    }


    @Override
    public boolean equals(Object password) {
        if (password instanceof String) password = new Password((String) password);
        if (!(password instanceof Password)) return false;
        return passwordHash.equals(((Password) password).getPasswordHash());
    }
}
