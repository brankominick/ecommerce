package com.wcupa.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcupa.app.utils.ProductTree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Collections;

import com.wcupa.app.db.DatabaseConnection;
import com.wcupa.app.domain.core.Cart;
import com.wcupa.app.domain.core.CartItem;
import com.wcupa.app.domain.core.Product;

import java.sql.*;

public class ProductService {
    private List<Product> products;
    private ProductTree productTreeByPrice = new ProductTree();
    private ProductTree productTreeById = new ProductTree(Comparator.comparingInt(Product::getId));
    private ProductTree productTreeByName = new ProductTree(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER));
    private ProductTree productTreeByRating = new ProductTree(Comparator.comparing(Product::getAverageRating));

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private ProductService() {
        loadProductsFromDatabase();
    }

    private static class Holder {
        private static final ProductService INSTANCE = new ProductService();
    }

    public static ProductService getInstance() {
        return Holder.INSTANCE;
    }

    public void refresh() {
        loadProductsFromDatabase();
    }

    private void loadProductsFromDatabase() {
        this.products = getAllProducts();
        for (Product p : products) {
            this.productTreeByPrice.insert(p);
            this.productTreeById.insert(p);
            this.productTreeByName.insert(p);
            this.productTreeByRating.insert(p);
        }
    }

    public List<Product> getProductsSortedByPriceAsc() {
        return productTreeByPrice.inOrder(productTreeByPrice.getRoot());
    }

    public List<Product> getProductsSortedByPriceDesc() {
        List<Product> result = new ArrayList<>(getProductsSortedByPriceAsc());
        Collections.reverse(result);
        return result;
    }

    public List<Product> getProductsSortedByRatingAsc() {
        return productTreeByRating.inOrder(productTreeByPrice.getRoot());
    }

    public List<Product> getProductsSortedByRatingDesc() {
        List<Product> result = new ArrayList<>(getProductsSortedByRatingAsc());
        Collections.reverse(result);
        return result;
    }

    public List<Product> getProductsSortedByIdAsc() {
        return productTreeById.inOrder(productTreeById.getRoot());
    }

    public List<Product> getProductsSortedByIdDesc() {
        List<Product> result = new ArrayList<>(getProductsSortedByIdAsc());
        Collections.reverse(result);
        return result;
    }

    public List<Product> getProductsSortedByNameAsc() {
        return productTreeByName.inOrder(productTreeByName.getRoot());
    }

    public List<Product> getProductsSortedByNameDesc() {
        List<Product> result = new ArrayList<>(getProductsSortedByNameAsc());
        Collections.reverse(result);
        return result;
    }

    public List<String> getAllCategories() {
        List<String> result = new ArrayList<>();
        String getQuery = "SELECT * FROM tags";
        try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement stmt = con.prepareStatement(getQuery)) {
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                result.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            logger.error("Error while getting all categories.\nError: {}", e.toString());
        }
        return result;
    }

    private ArrayList<Product> getAllProducts() {
        ArrayList<Product> result = new ArrayList<>();
        String getQuery = "SELECT * FROM products";
        try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement stmt = con.prepareStatement(getQuery)) {
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                result.add(buildProductFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            logger.error("Error while getting all products.\nError: {}", e.toString());
        }
        return result;
    }

    public boolean subtractCartFromInventory(Cart cart) {
        boolean result = true;
        Map<Integer, Integer> idQuant = cart.getItems().stream()
                                   .collect(Collectors.toMap(item -> item.getProduct().getId(), CartItem::getQuantity));
        List<Integer> ids = new ArrayList<>(idQuant.keySet());
        String transactionSQL = "START TRANSACTION; SAVEPOINT start;";
        String getProductSQL = "SELECT quantity FROM products WHERE id = ?";
        String commitSQL = "COMMIT";
        String rollbackSQL = "ROLLBACK start;";
        try (Connection con = DatabaseConnection.getConnection();) {
            con.setAutoCommit(false);
            PreparedStatement startTransaction = con.prepareStatement(transactionSQL);
            startTransaction.executeQuery().close();

            for (int i = 1; i <= ids.size(); i++) {
                PreparedStatement stmt = con.prepareStatement(getProductSQL);
                stmt.setInt(i, ids.get(i));
                ResultSet rs = stmt.executeQuery();
                rs.updateInt("quantity", idQuant.get(ids.get(i)));
                rs.close();
            }

            PreparedStatement commitStatement = con.prepareStatement(commitSQL);
            commitStatement.executeQuery().close();

        } catch (SQLException e) {
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement stmt = con.prepareStatement(rollbackSQL);
                stmt.executeQuery().close();
            } catch (SQLException rollbackError) {
                logger.error("Error during rollback:\n{}", rollbackError);
            }
            logger.error("Error while updating inventory: \n{}", e);
        }

        return result;
    }

    public Product getProductById(int id) {
        Product result = null;
        String getQuery = "SELECT * FROM products WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement stmt = con.prepareStatement(getQuery)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            result = buildProductFromResultSet(resultSet);
        } catch (SQLException e) {
            logger.error("Could not get product with ID {}.\nError: {}", id, e);
        }
        return result;
    }

    public List<String> getTagsByProductId(int id) {
        List<String> result = new ArrayList<>();
        String getQueryJunction = "SELECT t.name FROM tags t JOIN products_tags pt ON pt.tag_id = t.id WHERE pt.product_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement stmt = con.prepareStatement(getQueryJunction)) {
            stmt.setInt(1, id);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                result.add(resultSet.getString("name"));
            }
        } catch (SQLException e) {
            logger.error("Could not get tags with product ID {}.\nError: {}", id, e.toString());
        }
        return result;
    }

    public Product buildProductFromResultSet(ResultSet resultSet) {
        Product result = null;
        try {
            int productId = resultSet.getInt("id");
            String name = resultSet.getString("name");
            double price = resultSet.getDouble("price");
            String description = resultSet.getString("description");
            int quantity = resultSet.getInt("quantity");
            String imagePath = resultSet.getString("image_path");
            List<String> tags = getTagsByProductId(productId);
            double averageRating = ReviewService.getInstance().getAverageRatingForProduct(productId);
            result = new Product(productId, name, price, description, tags, quantity, imagePath, averageRating);
        } catch (SQLException e) {
            logger.error("Error while building product.\nError: {}", e.toString());
        }
        return result;
    }
}
