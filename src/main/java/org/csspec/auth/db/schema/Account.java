package org.csspec.auth.db.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.csspec.auth.serializers.AccountSerializer;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Document
@JsonSerialize(using = AccountSerializer.class)
public class Account {
    private static Integer userId = 1;

    private String name;

    @Id
    private String id;

    @Indexed(unique = true)
    private String email;

    private String password;

    private UserRole role = UserRole.UNKNOWN;

    private List<String> grantedAuthorities = new ArrayList<>();

    public Account() {}

    public Account(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.id = (userId++).toString();
    }

    @Override
    public String toString() {
        return id + " " + name + " <"
                + email + ">\n";
    }

    public String getUsername() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }
}
