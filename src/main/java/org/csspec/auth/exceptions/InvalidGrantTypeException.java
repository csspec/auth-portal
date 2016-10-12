package org.csspec.auth.exceptions;

public class InvalidGrantTypeException extends Exception {
    @Override
    public String toString() {
        return "grant_type requested was insufficient or incorrect";
    }
}
