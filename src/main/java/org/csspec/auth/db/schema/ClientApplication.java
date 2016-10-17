package org.csspec.auth.db.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document
public class ClientApplication {

    @JsonProperty("client_id")
    @Id
    private String clientId = "";

    @JsonIgnore
    private String clientSecret = "";

    private String name = "";

    private String description = "";

    private Integer refreshTokenValiditySeconds;

    private Integer accessTokenValiditySeconds;

    private String uri = "";

    private Set<String> scope = new HashSet<>();

    private Set<String> registeredRedirectUri = new HashSet<>();

    @JsonIgnore
    private HashMap<String, Object> additionalInformation = new HashMap<>();

    @JsonIgnore
    private Set<String> authorizedGrantTypes = new HashSet<>();

    @JsonIgnore
    @Transient
    private Collection<String> authorities = new ArrayList<>();

    /*
    allowedServices: services which this application can use
     */
    @JsonIgnore
    private Collection<String> allowedServices = new ArrayList<>();

    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    public ClientApplication(String clientId, String description) {
        this.clientId = clientId;
        this.description = description;
    }

    public ClientApplication() {
    }

    public void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public boolean isSecretRequired() {
        return true;
    }

    public boolean isScoped() {
        return true;
    }

    public Integer getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    public void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }

    public Set<String> getScope() {
        return scope;
    }

    public void setScope(Set<String> scope) {
        this.scope = scope;
    }

    public Set<String> getRegisteredRedirectUri() {
        return registeredRedirectUri;
    }

    public void setRegisteredRedirectUri(Set<String> registeredRedirectUri) {
        this.registeredRedirectUri = registeredRedirectUri;
    }

    public Map<String, Object> getAdditionalInformation() {
        return additionalInformation;
    }

    public Set<String> getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    public void setAuthorizedGrantTypes(Set<String> authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes;
    }

    public Collection<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<String> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "client_id: " + getClientId() + "\nclient_secret: " + getClientSecret();
    }

    public Collection<String> getAllowedServices() {
        return allowedServices;
    }

    public void setAllowedServices(Collection<String> allowedServices) {
        this.allowedServices = allowedServices;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ClientApplication)) {
            return false;
        }
        ClientApplication other = (ClientApplication)object;
        return this.getClientId().equals(other.getClientId())
                && this.getClientSecret().equals(other.getClientSecret());
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
