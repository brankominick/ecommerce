package com.wcupa.app.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Customer extends User {

    private static final Logger logger = LoggerFactory.getLogger(Customer.class);

    private Cart cart = new Cart();

    public Customer(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password, Role.CUSTOMER);
        this.permissions.add(Permission.SHOP);
        this.permissions.add(Permission.VIEW);
    }
    
}
