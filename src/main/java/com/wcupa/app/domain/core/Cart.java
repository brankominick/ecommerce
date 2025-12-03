package com.wcupa.app.domain.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Cart {

    private static final Logger logger = LoggerFactory.getLogger(Cart.class);
    private List<CartItem> items = new ArrayList<CartItem>();
    private List<Discount> discounts = new ArrayList<>();

    public List<CartItem> getItems() {
        return items;
    }

    public void addDiscount(Discount discount) {
        if (isDiscountValid(discount)) {
            discounts.add(discount);
        }
    }

    private boolean isDiscountValid(Discount discount) {
        boolean result = true;
        if (discounts.contains(discount)) {
            result = false;
        } else {
            switch (discount.getType()) {
                case PERCENTAGE -> result = (getTotalAfterDiscounts() - (getTotalCost() * discount.getDiscount()) > 0);
                case FIXED -> result = (getTotalAfterDiscounts() - discount.getDiscount()) > 0;
            }
        }
        return result;
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }

    public double getTotalAfterDiscounts() {
        double total = getTotalCost();
        if (!discounts.isEmpty()) {
            for (Discount d : discounts) {
                switch (d.getType()) {
                    case PERCENTAGE -> total -= total * d.getDiscount();
                    case FIXED -> total -= d.getDiscount();
                } 
            }
        }
        return total;
    }

    public double getTotalCost() {
        double total = getItems().stream()
                                 .map(item -> item.getCost())
                                 .reduce(Double::sum)
                                 .orElse(0.0);
        
        return total;
    }

    public int getNumberItems() {
        int total = getItems().stream()
                              .map(item -> item.getQuantity())
                              .reduce(Integer::sum)
                              .orElse(0);

        return total;
    }

    public void addProduct(Product product, int quantity) {
        logger.info("Adding {} {} to the cart.", quantity, product.getName());
        List<Integer> productsInCart = items.stream().map(item -> item.getProduct().getId()).collect(Collectors.toList());
        if (productsInCart.contains(product.getId())) {
            int ind = productsInCart.indexOf(product.getId());
            int current = items.get(ind).getQuantity();
            items.get(ind).setQuantity(current + quantity);
        } else {
            items.add(new CartItem(product, quantity));
        }
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
