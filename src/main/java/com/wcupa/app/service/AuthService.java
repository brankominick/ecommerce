package com.wcupa.app.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcupa.app.db.DatabaseConnection;
import com.wcupa.app.domain.core.User;

import java.sql.*;

public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private AuthService() {}

    private static class Holder {
        private static final AuthService INSTANCE = new AuthService();
    }

    public static AuthService getInstance() {
        return Holder.INSTANCE;
    }

    public User logIn(String username, String password) {
        User result = null;
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            result = User.fromResultSet(resultSet);
            logger.info("User {} logged in.", username);
        } catch (SQLException e) {
            logger.error("Could not log in with user {}\nError: {}", username, e);
            e.printStackTrace();
        }
        return result;
    }

    public boolean createAccount(String email, String password, String firstName, String lastName, String role) {
        boolean result = true;

        String checkSQL = "SELECT email FROM users WHERE email = ?";
        String insertSQL = "INSERT INTO users (email, password, first_name, last_name, role) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement checkStmt = con.prepareStatement(checkSQL)) {
            checkStmt.setString(1, email);
            ResultSet resultSet = checkStmt.executeQuery();
            if (resultSet.next()) {
                result = false;
                logger.error("Account with email {} already exists.", email);
            }

            if (result) {
                try (PreparedStatement insertStmt = con.prepareStatement(insertSQL)) {
                    insertStmt.setString(1, email);
                    insertStmt.setString(2, password);
                    insertStmt.setString(3, firstName);
                    insertStmt.setString(4, lastName);
                    insertStmt.setString(5, role);
                    insertStmt.executeUpdate();
                    logger.info("Account created with email {}", email);
                }
            }
        } catch (SQLException e) {
            logger.error("Could not create account with email {}\nError: {}", email, e);
            e.printStackTrace();
            result = false;
        }

        return result;
    }
    
}
