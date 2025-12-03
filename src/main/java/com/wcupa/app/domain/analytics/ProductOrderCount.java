package com.wcupa.app.domain.analytics;

import com.wcupa.app.domain.core.Product;

public class ProductOrderCount {
    private final int productId;
    private final String productName;
    private final int orderCount;
    private final int totalSold;

    ProductOrderCount(int productId, String productName, int orderCount, int totalSold) {
        this.productId = productId;
        this.productName = productName;
        this.orderCount = orderCount;
        this.totalSold = totalSold;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public int getTotalSold() {
        return totalSold;
    }
}
