package com.wcupa.app.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcupa.app.db.DatabaseConnection;
import com.wcupa.app.domain.core.Cart;
import com.wcupa.app.domain.core.CartItem;
import com.wcupa.app.domain.core.Customer;
import com.wcupa.app.domain.core.Order;
import com.wcupa.app.domain.core.Product;
import com.wcupa.app.service.OrderService;

public class CheckoutService {

    private static final Logger logger = LoggerFactory.getLogger(CheckoutService.class);

    private CheckoutService() {}

    private static class Holder {
        private static final CheckoutService INSTANCE = new CheckoutService();
    }

    public static CheckoutService getInstance() {
        return Holder.INSTANCE;
    }

    public String submitOrder(Customer user, Cart cart, String address) {
        OrderService os = OrderService.getInstance();
        logger.info("Checking out!");
        String result = os.createOrder(user, cart, address);
        return result;
    }
    
}
