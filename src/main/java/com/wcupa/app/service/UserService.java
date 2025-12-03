package com.wcupa.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcupa.app.domain.core.User;

import java.util.HashMap;
import java.util.Map;

public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final Map<String, User> users = new HashMap<>();

    private UserService() {}

    private static class Holder {
        private static final UserService INSTANCE = new UserService();
    }

    public static UserService getInstance() {
        return Holder.INSTANCE;
    }

    public boolean createAccount(User user) {
        boolean result = false;
        if (!users.containsKey(user.getEmail())) {
            result = true;
            users.put(user.getEmail(), user);
            logger.info("User created: {}", user.toString());
        }
        return result;
    }

    public User login(String email, String password) {
        User user = users.get(email);
        if (user != null && user.getPassword().equals(password)) {
            logger.info("User {} logged in!", user.toString());
        }
        else {
            user = null;
        }
        return user;
    }

    public User getUser(String email) {
        return users.get(email);
    }
}
