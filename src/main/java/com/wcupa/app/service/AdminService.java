package com.wcupa.app.service;

import java.util.List;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.security.GeneralSecurityException;
import java.sql.Connection;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcupa.app.db.DatabaseConnection;
import com.wcupa.app.domain.analytics.TotalRevenueReport;
import com.wcupa.app.domain.core.Order;
import com.wcupa.app.domain.core.Product;
import com.wcupa.app.domain.core.User;
import com.wcupa.app.session.Session;

public class AdminService {
    private ProductService productService;
    private OrderService orderService;
    private ReportingService reportingService; 
    private Session session;

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    private AdminService() {}

    private static class Holder {
        private static final AdminService INSTANCE = new AdminService();
    }

    public static AdminService getInstance() {
        return Holder.INSTANCE;
    }

    public void setProductService(ProductService ps) {
        productService = ps;
    }

    public ProductService getProductService() {
        return productService;
    }

    public void setOrderService(OrderService os) {
        orderService = os;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setReportingService(ReportingService rs) {
        reportingService = rs;
    }

    public ReportingService getReportingService() {
        return reportingService;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    // --- Products ---

    public boolean addNewProduct(Product product) {
        if (!session.getUser().isAdmin()) {
            throw new SecurityException("Access Denied. User is not recognized as an admin.");
        }
        boolean result = true;
        String insertProduct = "INSERT INTO products (name, price, description, quantity, image_path) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement stmt = con.prepareStatement(insertProduct)) {
            stmt.setString(1, product.getName());
            stmt.setDouble(2, product.getPrice());
            stmt.setString(3, product.getDescription());
            stmt.setInt(4, product.getQuantity());
            stmt.setString(5, product.getImagePath());
            result = (stmt.executeUpdate() > 0);
        } catch (SQLException e) {
            logger.error("Could not add new product {}\nError: {}", product.toString(), e.toString());
            result = false;
        }
        return result;
    }

    public boolean updateExistingProduct(int productId, String name, double price, int quantity, String description, String imagePath) {
        if (!session.getUser().isAdmin()) {
            throw new SecurityException("Access Denied. User is not recognized as an admin.");
        }
        boolean result = true;
        String updateProduct = "UPDATE products SET name = ?, price = ?, description = ?, quantity = ?, image_path = ? WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement stmt = con.prepareStatement(updateProduct)) {
            stmt.setString(1, name);
            stmt.setDouble(2, price);
            stmt.setString(3, description);
            stmt.setInt(4, quantity);
            stmt.setString(5, imagePath);
            stmt.setInt(6, productId);
            result = (stmt.executeUpdate() > 0);
        } catch (SQLException e) {
            logger.error("Could not update product with id {}.\nError: {}", productId, e.toString());
            result = false;
        }
        return result;
    }

    public boolean updateProductQuantity(int productId, int newQuantity) {
        if (!session.getUser().isAdmin()) {
            throw new SecurityException("Access Denied. User is not recognized as an admin.");
        }
        boolean result = true;
        String updateQuantity = "UPDATE products SET quantity = ? WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement stmt = con.prepareStatement(updateQuantity)) {
            stmt.setInt(1, newQuantity);
            stmt.setInt(2, productId);
            result = (stmt.executeUpdate() > 0);
        } catch (SQLException e) {
            logger.error("Could not update product with id {} to quantity {}.\nError: {}", productId, newQuantity, e.toString());
            result = false;
        }
        return result;
    }

    public boolean deleteProduct(int productId) {
        if (!session.getUser().isAdmin()) {
            throw new SecurityException("Access Denied. User is not recognized as an admin.");
        }
        boolean result = true;
        String deleteProduct = "DELETE FROM products WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement stmt = con.prepareStatement(deleteProduct)) {
            stmt.setInt(1, productId);
            result = (stmt.executeUpdate() > 0);
        } catch (SQLException e) {
            logger.error("Could not delete product with id {}.\nError: {}", productId, e.toString());
            result = false;
        }
        return result;
    }

    public void refreshProductService() {
        if (!session.getUser().isAdmin()) {
            throw new SecurityException("Access Denied. User is not recognized as an admin.");
        }
        productService.refresh();
    }

    public Product getProductById(int id) {
        return productService.getProductById(id);
    }

    public List<Product> getProductsSortedByIdDesc() {
        return productService.getProductsSortedByIdDesc();
    }

    // --- Orders ---

    public boolean updateOrderStatus(int orderId, Order.OrderStatus status) {
        if (!session.getUser().isAdmin()) {
            throw new SecurityException("Access Denied. User is not recognized as an admin.");
        }
        boolean result = true;
        String updateOrder = "UPDATE orders SET delivered = ? WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement stmt = con.prepareStatement(updateOrder)) {
            stmt.setString(1, status.toString());
            stmt.setInt(2, orderId);
            result = (stmt.executeUpdate() > 0);
        } catch (SQLException e) {
            logger.error("Could not update status of order with id {} to {}\nError: {}", orderId, status, e.toString());
            result = false;
        }
        return result;
    }

    // --- Reports --- 
    // TODO: Out of stock products, Total orders placed, Most frequently ordered product, Total revenue

    public TotalRevenueReport getTotalRevenueReport(LocalDate start, LocalDate end) {
        return reportingService.getTotalRevenueReport(start, end);
    }
}
