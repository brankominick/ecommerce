package com.wcupa.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcupa.app.domain.Product;
import java.util.HashMap;
import java.util.Map;

public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final Map<String, Product> products = new HashMap<>();

    public boolean addProduct(Product product) {
        boolean newValue = false;
        Product result = products.put(product.getName(), product);
        logger.info("Product added to inventory: {}", product.toString());
        if (result.equals(product)) {
            newValue = true;
        }
        return newValue;
    }

    public Product getProductInfo(String name) {
        Product product = products.get(name);
        return product;
    }

    //public boolean updateProduct(Product product) {
    //    Product current = products.get(product.getName());
    //}

    public boolean deleteProduct(String name) {
        boolean deleted = false;
        Product result = products.remove(name);
        if (result != null) {
            deleted = true;
        }
        return deleted;
    }

    public void printAllProducts() {
        System.out.println(products.toString());
    }
}
