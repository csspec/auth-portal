package org.csspec.auth.exceptions;

public class BadClientException extends Exception {
    private String id;
    public BadClientException(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "'" + id + "'" + " not found";
    }
}
