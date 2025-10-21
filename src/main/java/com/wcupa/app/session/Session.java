package com.wcupa.app.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;
import java.util.Set;
import java.util.List;

import com.wcupa.app.domain.*;
import com.wcupa.app.service.*;
import com.wcupa.app.utils.*;

public class Session {
    private static final Logger logger = LoggerFactory.getLogger(Session.class);

    private User user;
    private Cart cart;

    public UserService userService = new UserService();
    public ProductService productService = new ProductService();
    public OrderService orderService = new OrderService();

    public Session() {

    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean signedIn() {
        return (user != null);
    }

    public Set<Permission> getPermissions() {
        return user.getPermissions();
    }

    public boolean verifyProductsAvailable() {
        boolean result = true;
        List<CartItem> items = cart.getItems();
        for (CartItem item : items) {
            Product prod = item.getProduct();
            int quant = item.getQuantity();
            Product prodInventory = productService.getProductInfo(prod.getName());
            if (prodInventory != null) {
                if (prodInventory.getQuantity() < quant) {
                    result = false;
                }
            }
        }
        return result;
    }

    public boolean updateInventory() {
        boolean result = true;
        List<CartItem> items = cart.getItems();
        for (CartItem item : items) {
            Product prod = item.getProduct();
            int quant = item.getQuantity();
            Product prodInventory = productService.getProductInfo(prod.getName());
            prod.setQuantity(prodInventory.getQuantity() - quant);
            productService.addProduct(prod);
        }
        return result;
    }

    public boolean placeOrder(String shippingAddress) {
        boolean result = true;
        if (verifyProductsAvailable()) {
            updateInventory();
            orderService.createOrder((Customer) user, cart, shippingAddress);
        }
        return result;
    }

}
