package org.csspec.auth.db.schema;

/**
 * role of the user on the scale of 1 - 10
 * where 1 means ADMIN and user with role 10 has lowest access to API
 */
public enum UserRole {
    ADMIN(1, "ADMIN"),

    TEACHER(4, "TEACHER"),
    // ...
    // some other roles, needs discussion
    // ...

    STUDENT(7,"STUDENT"),

    UNKNOWN(10, "UNKNOWN");

    private int role;
    private String value;

    UserRole(int role, String value) {
        this.role = role;
        this.value = value;
    }

    public int getRole() {
        return role;
    }

    public String getValue() {
        return value;
    }
}
