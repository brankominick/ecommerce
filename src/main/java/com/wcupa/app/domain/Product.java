package com.wcupa.app.domain;

import java.util.List;
import java.util.ArrayList;

public class Product {

    private String name;
    private double price;
    private String description;
    private List<String> tags = new ArrayList<String>();
    private int quantity;
    private String imagePath;

    public Product(String name, double price, String description, List<String> tags, int quantity) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.tags = tags;
        this.quantity = quantity;
        this.imagePath = "";
    }

    public Product(String name, double price, String description, List<String> tags, int quantity, String imagePath) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.tags = tags;
        this.quantity = quantity;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public double getPrice() {
        return price;
    }
    
    public void setPrice(double newPrice) {
        this.price = newPrice;
    }

    public String getDescription() {
        return description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setDescription(String newDescription) {
        description = newDescription;
    }

    public List<String> getTags() {
        return tags;
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public void removeTag(String tag) {
        tags.remove(tag);
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String newImagePath) {
        imagePath = newImagePath;
    }

    @Override
    public String toString() {
        return String.format("Product Info\nName: %s\nPrice: %d\n Description: %s\n Quantity: %d\nTags: %s", name, price, description, quantity, tags.toString());
    }
}
