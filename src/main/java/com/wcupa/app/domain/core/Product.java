package com.wcupa.app.domain.core;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Product {

    private int id;
    private String name;
    private double price;
    private String description;
    private List<String> tags = new ArrayList<String>();
    private int quantity;
    private String imagePath;
    private double averageRating;

    public Product(int id, String name, double price, String description, List<String> tags, int quantity, String imagePath, double averageRating) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.tags = tags;
        this.quantity = quantity;
        this.imagePath = imagePath;
        this.averageRating = averageRating;
    }

    public Product(String name, double price, String description, List<String> tags, int quantity, String imagePath) {
        this(-1, name, price, description, tags, quantity, imagePath, -1);
    }

    public Product(String name, double price, String description, List<String> tags, int quantity) {
        this(name, price, description, tags, quantity, "");
    }

    public int getId() {
        return id;
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

    public double getAverageRating() {
        return averageRating;
    }

    @Override
    public String toString() {
        return String.format("Product Info\nID: %s\nName: %s\nPrice: %s\nDescription: %s\nRating: %s\nQuantity: %d\nTags: %s\n", String.valueOf(id), name, String.valueOf(price), description, String.valueOf(averageRating), quantity, tags.toString());
    }
}
