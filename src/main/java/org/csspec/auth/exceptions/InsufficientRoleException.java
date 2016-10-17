package org.csspec.auth.exceptions;

import org.csspec.auth.db.schema.Account;
import org.csspec.auth.db.schema.UserRole;

public class InsufficientRoleException extends Exception {
    private Account account;
    private UserRole role;

    public InsufficientRoleException(Account account, UserRole role) {
        this.account = account;
        this.role = role;
    }


    public Account getAccount() {
        return account;
    }

    public UserRole getRole() {
        return role;
    }
}
