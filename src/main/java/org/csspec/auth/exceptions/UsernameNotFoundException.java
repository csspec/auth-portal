package org.csspec.auth.exceptions;

import lombok.Data;

public @Data
class UsernameNotFoundException extends Exception {

    private String username;

    public UsernameNotFoundException(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return username + " does not exist.";
    }
}
