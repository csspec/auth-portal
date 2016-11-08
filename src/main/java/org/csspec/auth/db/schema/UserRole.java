package org.csspec.auth.db.schema;

/**
 * role of the user on the scale of 1 - 10
 * where 1 means ADMIN and user with role 10 has lowest access to API
 */
public enum UserRole {
    // admin
    ADMIN(1, "ADMIN"),

    // DAA i.e. Dean Academic Affairs
    DAA(2, "DAA"),

    // teacher/professor/assistant professor/...
    TEACHER(4, "TEACHER"),

    // class representative
    CR(5, "CR"),

    // society representative
    SR(6, "SR"),

    // hostel senior
    HS(7, "HS"),

    // student
    STUDENT(9,"STUDENT"),

    // and unknown ...
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
