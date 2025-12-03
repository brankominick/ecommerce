package com.wcupa.app.domain.core;

import java.util.Set;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public abstract class User {
    private String firstName;
    private String lastName; 
    private String email;
    private String password;
    private Role role;
    protected Set<Permission> permissions = new HashSet<Permission>();

    public User(String firstName, String lastName, String email, String password, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static User createUser(String firstName, String lastName, String email, String password, Role role) {
        switch (role) {
            case CUSTOMER:
                return new Customer(firstName, lastName, email, password);
            case ADMIN:
                return new Admin(firstName, lastName, email, password);
            default:
                throw new IllegalArgumentException("Invalid role: " + role.toString());
        }

    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return this.role;
    }

    public boolean isAdmin() {
        return (this.role == Role.ADMIN);
    }

    public Set<Permission> getPermissions() {
        return this.permissions;
    }

    public static User fromResultSet(ResultSet rs) throws SQLException {
    String role = rs.getString("role");
    return User.createUser(
        rs.getString("first_name"),
        rs.getString("last_name"),
        rs.getString("email"),
        rs.getString("password"),
        Role.valueOf(role));
    }

    @Override
    public String toString() {
        return String.format("Name: %s %s, Email: %s, Role: %s", firstName, lastName, email, role.toString());
    }
}
