package org.csspec.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * represents the payload that will be carried in the JWT
 */
public class TokenPayload {
    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("expire")
    private long expireTime;

    public TokenPayload() { }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }
}
