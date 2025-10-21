package com.wcupa.app.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.ArrayList;

public class Cart {

    private static final Logger logger = LoggerFactory.getLogger(Cart.class);
    private List<CartItem> items = new ArrayList<CartItem>();

    public List<CartItem> getItems() {
        return items;
    }

    public void addProduct(Product product, int quantity) {
        logger.info("Adding {} {} to the cart.", quantity, product.getName());
        items.add(new CartItem(product, quantity));
    }
    
    public void removeProduct(Product product, int quantity) {
        logger.info("Removing {} {} from the cart.", quantity, product.getName());
        for (CartItem item : this.items) {
            if (item.getProduct().equals(product)) {
                int newQuantity = item.getQuantity()-quantity;
                if (newQuantity < 1) {
                    items.remove(item);
                }
                else {
                    item.setQuantity(newQuantity);
                }
                return; 
            }
        }
    }

    public void clearCart() {
        logger.info("Emptying the cart.");
        items.clear();
    }
}
