package com.wcupa.app.domain.core;

public class Discount {

    public enum DiscountType {
        PERCENTAGE,
        FIXED
    }

    private String name;
    private DiscountType type;
    private double discount;
    private boolean active;

    public Discount(String name, DiscountType type, double discount, boolean active) {
        this.name = name;
        this.type = type;
        this.discount = discount;
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public DiscountType getType() {
        return type;
    }

    public double getDiscount() {
        return discount;
    }

    public boolean isActive() {
        return active;
    }
    
}
