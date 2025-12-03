package com.wcupa.app.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcupa.app.db.DatabaseConnection;
import com.wcupa.app.domain.core.Cart;
import com.wcupa.app.domain.core.CartItem;
import com.wcupa.app.domain.core.Customer;
import com.wcupa.app.domain.core.Order;
import com.wcupa.app.domain.core.Product;

public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private OrderService() {}

    private static class Holder {
        private static final OrderService INSTANCE = new OrderService();
    }

    public static OrderService getInstance() {
        return Holder.INSTANCE;
    }
    
    public String createOrder(Customer customer, Cart cart, String shippingAddress) {
        String confirmation = null;

        String updateInventorySQL = "UPDATE products SET quantity = quantity - ? WHERE id = ? AND quantity >= ?";
        String insertOrderSQL = "INSERT INTO orders (confirmation, customer_name, customer_email, order_placed, tax, sub_total, shipping_fee, total, shipping_address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String insertItemSQL = "INSERT INTO order_items (order_conf, product_id, quantity, unit_price, line_total) VALUES (?, ?, ?, ?, ?)";
        Connection con = null;

        try {
            con = DatabaseConnection.getConnection();
            con.setAutoCommit(false); // start transaction

            // Subtract inventory
            for (CartItem item : cart.getItems()) {
                try (PreparedStatement stmt = con.prepareStatement(updateInventorySQL)) {
                    stmt.setInt(1, item.getQuantity());
                    stmt.setInt(2, item.getProduct().getId());
                    stmt.setInt(3, item.getQuantity());
                    int updatedRows = stmt.executeUpdate();
                    if (updatedRows == 0) {
                        throw new SQLException("Not enough inventory for product: " + item.getProduct().getName());
                    }
                }
            }

            // Insert order
            confirmation = UUID.randomUUID().toString();
            double tax = TaxService.calculateTax(shippingAddress, cart.getTotalCost());
            Order newOrder = new Order(confirmation, customer, cart, shippingAddress, tax);
            try (PreparedStatement stmt = con.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, confirmation);
                stmt.setString(2, customer.getFirstName() + " " + customer.getLastName());
                stmt.setString(3, customer.getEmail());
                stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                stmt.setDouble(5, newOrder.getTax());
                stmt.setDouble(6, newOrder.getSubTotal());
                stmt.setDouble(7, newOrder.getShippingFee());
                stmt.setDouble(8, newOrder.getTotal());
                stmt.setString(9, shippingAddress);
                stmt.executeUpdate();

                // Insert order items
                try (PreparedStatement itemStmt = con.prepareStatement(insertItemSQL)) {
                    for (CartItem item : cart.getItems()) {
                        itemStmt.setString(1, String.valueOf(confirmation));
                        itemStmt.setInt(2, item.getProduct().getId());
                        itemStmt.setInt(3, item.getQuantity());
                        itemStmt.setDouble(4, item.getProduct().getPrice());
                        itemStmt.setDouble(5, item.getQuantity() * item.getProduct().getPrice());
                        itemStmt.addBatch();
                    }
                    itemStmt.executeBatch();
                }
            }

            con.commit(); // commit transaction
            logger.info("Order placed! Confirmation number: {}", confirmation);

        } catch (SQLException e) {
            confirmation = null;
            try {
                con.rollback(); // rollback everything
            } catch (SQLException rollbackEx) {
                logger.error("Failed to rollback transaction", rollbackEx);
            }
            logger.error("Failed to create order: {}", e);
        }

        return confirmation;
    }

    public boolean insertOrderItems(Order order, Cart cart) {
        boolean result = true;
        String insertSQL = "INSERT INTO order_items (order_confirmation, product_id, quantity, price_each) VALUES (?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement stmt = con.prepareStatement(insertSQL)) {
            for (CartItem item : cart.getItems()) {
                stmt.setString(1, order.getConfirmation());
                stmt.setInt(2, item.getProduct().getId());
                stmt.setInt(3, item.getQuantity());
                stmt.setDouble(4, item.getProduct().getPrice() / item.getQuantity()); 
                stmt.addBatch();
            } 
            stmt.executeBatch();
        }
        catch (SQLException e) {
            logger.error("Could not insert items for order {}", order.getConfirmation());
        }
        return result;
    }


    public Order getOrder(String confirmationId) {
        Order result = null;
        String getSQL = "SELECT * FROM orders WHERE confirmation = ?";
        try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement stmt = con.prepareStatement(getSQL)) {
            stmt.setString(1, confirmationId);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            result = buildOrderFromResultSet(resultSet);
            logger.info("Order with confirmation ID {} retrieved!", confirmationId);
        } catch (SQLException e) {
            logger.error("Could not get order with confirmation ID {}.\nError: {}", confirmationId, e.toString());
        }
        return result;
    }

    public ArrayList<CartItem> getOrderItems(String confirmationId) {
        ArrayList<CartItem> result = new ArrayList<>();
        String getSQL = "SELECT * FROM orders_items WHERE order_conf = ?";
        try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement stmt = con.prepareStatement(getSQL)) {
            stmt.setString(1, confirmationId);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                int productId = resultSet.getInt("product_id");
                int quantity = resultSet.getInt("quantity");
                Product product = ProductService.getInstance().getProductById(productId);
                CartItem item = new CartItem(product, quantity);
                result.add(item);
            }
        } catch (SQLException e) {
            logger.error("Could not get items with confirmation ID {}.\nError: {}", confirmationId, e);
        }
        return result;
    }

    public Order buildOrderFromResultSet(ResultSet resultSet) {
        Order result = null;
        try {
            String conf = resultSet.getString("confirmation");
            String firstName = resultSet.getString("customer_name").split(" ")[0];
            String lastName = resultSet.getString("customer_name").split(" ")[1];
            String email = resultSet.getString("customer_email");
            ArrayList<CartItem> contents = getOrderItems(conf);
            LocalDateTime orderPlaced = resultSet.getTimestamp("order_placed").toLocalDateTime();
            double subTotal = resultSet.getDouble("sub_total");
            double shippingFee = resultSet.getDouble("shipping_fee");
            double tax = resultSet.getDouble("tax");
            double total = resultSet.getDouble("total");
            String shippingAddress = resultSet.getString("shipping_address");
            Order.OrderStatus status = Order.OrderStatus.valueOf(resultSet.getString("status"));
            result = new Order(conf, String.format("%s %s", firstName, lastName), email, contents, orderPlaced, tax, subTotal, total, shippingAddress, status);
        } catch (SQLException e) {
            logger.error("Error while building order object from result set.\nError: {}", e.toString());
        }
        return result;
    }

    public List<Order> getOrdersForEmail(String email) {
        List<Order> result = new ArrayList<>();
        String getSQL = "SELECT * FROM orders WHERE customer_email = ?";
        try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement stmt = con.prepareStatement(getSQL)) {
            stmt.setString(1, email);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                result.add(buildOrderFromResultSet(resultSet));
            }
            logger.info("Retrieved {} orders for user with email {}!", result.size(), email);
        } catch (SQLException e) {
            logger.error("Could not get orders for user with email: {}.\nError: {}", email, e.toString());
        }
        return result;
    }

}
