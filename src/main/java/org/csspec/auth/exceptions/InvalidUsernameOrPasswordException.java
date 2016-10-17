package org.csspec.auth.exceptions;

public class InvalidUsernameOrPasswordException extends Throwable {
    @Override
    public String toString() {
        return "Username or password is/are incorrect";
    }
}
