package org.csspec.auth.exceptions;

public class InsufficientAuthorizationException extends Exception {
    @Override
    public String toString() {
        return "Authorization was not sufficient to access the resource";
    }
}
