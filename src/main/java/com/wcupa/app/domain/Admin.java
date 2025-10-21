package com.wcupa.app.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Admin extends User {

    private static final Logger logger = LoggerFactory.getLogger(Admin.class);

    public Admin(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password, Role.ADMIN);
        this.permissions.add(Permission.INVENTORY);
        this.permissions.add(Permission.VIEW);
    }
    
}
