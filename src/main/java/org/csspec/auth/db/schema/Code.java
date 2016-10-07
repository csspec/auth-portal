package org.csspec.auth.db.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * In terms of OAuth 2.0, code is small temporary token issued to the client on the basis of its client_id only.
 * By using {@code code}, client can request the {@code access_token} by sending its {@code client_secret} and {code client_id}
 */
@Document
public class Code {
    @JsonIgnore
    private String clientId;

    @JsonIgnore
    private List<String> services;

    @JsonIgnore
    @Indexed(expireAfterSeconds = 60 * 2)
    private Date created;

    @Indexed(unique = true)
    private String code;

    public Code(String clientId, List<String> services, String code) {
        this.clientId = clientId;
        this.services = services;
        this.created = new Date();
        this.code = code;
    }

    public List<String> getServices() {
        return services;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
