package org.csspec.auth.db.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Document
public class Account {
    @JsonProperty("name")
    private String name;

    @JsonProperty("id")
    @Id
    private String id;

    @JsonProperty("email")
    private String email;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private UserRole role = UserRole.UNKNOWN;

    @JsonIgnore
    private String salt;

    private List<String> grantedAuthorities = new ArrayList<>();

    public Account() {}

    public Account(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return id + " " + name + " <"
                + email + ">\n";
    }

    public String getUsername() {
        return name;
    }


    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public Collection<String> getAuthorities() {
        return grantedAuthorities;
    }

    public void setGrantedAuthorities(List<String> grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }


    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
