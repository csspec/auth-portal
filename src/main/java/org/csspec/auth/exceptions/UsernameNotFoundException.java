package org.csspec.auth.exceptions;

import lombok.Data;

/**
 * Created by Prince on 10/6/2016.
 */
public @Data
class UsernameNotFoundException extends Exception {

    private String username;

    public UsernameNotFoundException(String username) {
        this.username = username;
    }
}
