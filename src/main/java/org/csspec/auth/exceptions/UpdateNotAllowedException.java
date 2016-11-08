package org.csspec.auth.exceptions;

import org.csspec.auth.db.schema.UserRole;

public class UpdateNotAllowedException extends Exception {
    private String username;

    private UserRole userRole;

    private UserRole requiredRole;

    private String message;

    public UpdateNotAllowedException(String username, UserRole role, UserRole required) {
        this.username = username;
        this.userRole = role;
        this.requiredRole = required;
        this.message = "User role does satisfy the required role";
    }

    public String getUsername() {
        return username;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public UserRole getRequiredRole() {
        return requiredRole;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return this.message + ", UserRole: " + userRole.getValue() + ", Required: " + requiredRole.getValue();
    }
}
