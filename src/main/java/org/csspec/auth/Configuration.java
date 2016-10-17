package org.csspec.auth;

import java.io.Console;
import java.util.Map;

public class Configuration {
    private Map<String, String> env = System.getenv();
    private final Console console = System.console();

    private static final String ACCOUNT_ID_STR = "ADMIN_ACCOUNT_ID";

    private static final String PASSWORD_STR = "ADMIN_PASSWORD";

    private static final String ACCOUNT_EMAIL = "ADMIN_EMAIL";

    private static final String THIS_CLIENT_ID = "THIS_CLIENT_ID";

    private static final String THIS_CLIENT_SECRET = "THIS_CLIENT_SECRET";

    private String askAccountId() {
        String res = console.readLine("Enter admin account id (admin): ");
        return res == null || res.length() == 0 ? "admin" : res;
    }

    private String askAccountPassword() {
        String password = "";
        do {
            password = new String(console.readPassword("Your password: "));
        } while (password.length() == 0);
        return password;
    }

    private String askEmailId() {
        return console.readLine("Enter admin's email id: ");
    }

    public String getAdminAccountId() {
        String id = env.get(ACCOUNT_ID_STR);
        return id == null ? askAccountId() : id;
    }

    public String getAdminPassword() {
        String password = env.get(PASSWORD_STR);
        return password == null ? askAccountPassword() : password;
    }

    public String getAccountEmail() {
        String email = env.get(ACCOUNT_EMAIL);
        return email == null ? askEmailId() : email;
    }

    private String askThisClientId() {
        String id = console.readLine("Enter client id to access the admin page as well as accounts page (csspec): ");
        return id.length() == 0 ? "csspec" : id;
    }

    private String askThisClientSecret() {
        String password = "";
        do {
            password = new String(console.readPassword("Enter client secret: "));
        } while (password.length() == 0);
        return password;
    }

    /**
     * this client refers to auth-portal itself for e.g. login page, admin page,
     * accounts page etc
     */
    public String getThisClientId() {
        String thisClientId = env.get(THIS_CLIENT_ID);
        return thisClientId == null ? askThisClientId() : thisClientId;
    }

    public String getThisClientSecret() {
        String clientSecret = env.get(THIS_CLIENT_SECRET);
        return clientSecret == null ? askThisClientSecret() : clientSecret;
    }

    public static final String CSS_ORG_COOKIE_NAME = "csspec_org";
}
