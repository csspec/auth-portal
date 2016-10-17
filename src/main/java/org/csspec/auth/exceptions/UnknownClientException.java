package org.csspec.auth.exceptions;

public class UnknownClientException extends Exception {
    private String clientId;

    public UnknownClientException(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }
}
