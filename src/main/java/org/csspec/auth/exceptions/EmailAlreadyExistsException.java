package org.csspec.auth.exceptions;

public class EmailAlreadyExistsException extends Exception {
    private String email;

    public EmailAlreadyExistsException(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Email ID: '" + email + "' already exists in database.";
    }
}
