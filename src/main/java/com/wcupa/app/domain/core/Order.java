package com.wcupa.app.domain.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class Order {
    private final double shippingFee = 5;
    private double tax;
    private String confirmation;
    private String customerName;
    private String customerEmail;
    private List<CartItem> contents;
    private LocalDateTime orderPlaced;
    private double subTotal;
    private double total;
    private String shippingAddress;
    private OrderStatus orderStatus;

    private static final Logger logger = LoggerFactory.getLogger(Order.class);

    public enum OrderStatus {
        RECEIVED,
        PROCESSED,
        SHIPPED,
        DELIVERED,
        CANCELLED
    }
    
    public Order(String confirmation, Customer customer, Cart cart, String shippingAddress, double tax) {
        this.confirmation = confirmation;
        this.customerName = String.format("%s %s", customer.getFirstName(), customer.getLastName());
        this.customerEmail = customer.getEmail();
        this.contents = cart.getItems();
        this.orderPlaced = LocalDateTime.now();
        this.tax = tax;
        this.subTotal = cart.getTotalAfterDiscounts();
        this.total = this.subTotal + shippingFee + tax;
        this.shippingAddress = shippingAddress;
        this.orderStatus = OrderStatus.RECEIVED;
    }

    public Order(String confirmation, String customerName, String customerEmail, List<CartItem> contents,
                    LocalDateTime orderPlaced, double tax, double subTotal, double total, String shippingAddress, OrderStatus orderStatus) {
        this.confirmation = confirmation;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.contents = contents;
        this.orderPlaced = orderPlaced;
        this.tax = tax;
        this.subTotal = subTotal;
        this.total = total;
        this.shippingAddress = shippingAddress;
        this.orderStatus = orderStatus;
    }


    public double getShippingFee() {
        return shippingFee;
    }

    public double getTax() {
        return tax;
    }

    public String getConfirmation() {
        return confirmation;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public List<CartItem> getContents() {
        return contents;
    }

    public LocalDateTime getOrderPlaced() {
        return orderPlaced;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public double getTotal() {
        return total;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public boolean isorderStatus() {
        return orderStatus == OrderStatus.DELIVERED;
    }

}
