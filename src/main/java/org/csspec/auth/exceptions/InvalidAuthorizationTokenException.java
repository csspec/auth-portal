package org.csspec.auth.exceptions;


public class InvalidAuthorizationTokenException extends Exception {
    @Override
    public String toString() {
        return "token sent was invalid";
    }
}
