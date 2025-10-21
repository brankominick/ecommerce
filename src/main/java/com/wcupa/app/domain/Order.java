package com.wcupa.app.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class Order {
    private final double shippingFee = 5;
    private final double tax = 5;
    private String confirmation;
    private String customerName;
    private String customerEmail;
    private List<CartItem> contents;
    private LocalDateTime orderPlaced;
    private double subTotal;
    private double total;
    private String shippingAddress;
    private boolean delivered;

    private static final Logger logger = LoggerFactory.getLogger(Order.class);


    
    public Order(String confirmation, Customer customer, Cart cart, String shippingAddress) {
        this.confirmation = confirmation;
        this.customerName = String.format("%s %s", customer.getFirstName(), customer.getLastName());
        this.customerEmail = customer.getEmail();
        this.contents = cart.getItems();
        this.orderPlaced = LocalDateTime.now();
        this.subTotal = this.contents.stream()
                                     .map(item -> item.getCost())
                                     .reduce(Double::sum)
                                     .orElse(0.0);
        this.total = this.subTotal + shippingFee + tax;
        this.shippingAddress = shippingAddress;
        this.delivered = false;
        logger.info("Order placed!");
    }
}
