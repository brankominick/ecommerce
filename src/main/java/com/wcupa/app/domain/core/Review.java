package com.wcupa.app.domain.core;

import java.time.LocalDateTime;

public class Review {
    private int id;
    private int productId;
    private String customerEmail;
    private int rating; 
    private String comment;
    private LocalDateTime createTime;

    public Review(int id, int productId, String customerEmail, int rating, String comment, LocalDateTime createTime) {
        this.id = id;
        this.productId = productId;
        this.customerEmail = customerEmail;
        this.rating = rating;
        this.comment = comment;
        this.createTime = createTime;
    }

    public Review(int productId, String customerEmail, int rating, String comment) {
        this(-1, productId, customerEmail, rating, comment, LocalDateTime.now());
    }

    public int getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
}
