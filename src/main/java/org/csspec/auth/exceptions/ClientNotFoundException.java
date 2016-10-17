package org.csspec.auth.exceptions;

/**
 * Created by Prince on 10/5/2016.
 */
public class ClientNotFoundException extends Exception {
    private String id;
    public ClientNotFoundException(String id) {
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
