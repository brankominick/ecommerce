package com.wcupa.app.service;

import com.wcupa.app.domain.Product;
import com.wcupa.app.domain.Customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcupa.app.domain.Cart;
import com.wcupa.app.domain.CartItem;
import com.wcupa.app.domain.Order;

public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final Map<String, Order> orders = new HashMap<>();

    public String createOrder(Customer customer, Cart cart, String shippingAddress) {
        String confirmation = UUID.randomUUID().toString();
        Order newOrder = new Order(confirmation, customer, cart, shippingAddress);
        orders.put(confirmation, newOrder);
        return confirmation;
    }

}
