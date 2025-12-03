package com.wcupa.app.service;

import com.wcupa.app.db.DatabaseConnection;

import java.time.LocalDate;

import java.sql.*;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.cj.x.protobuf.MysqlxCrud.Order;
import com.wcupa.app.domain.analytics.*;

public class ReportingService {
    private ProductService productService;
    private OrderService orderService;

    private static final Logger logger = LoggerFactory.getLogger(ReportingService.class);

    // TODO: Out of stock products, Total orders placed, Most frequently ordered product, Total revenue

    private ReportingService() {}

    private static class Holder {
        private static final ReportingService INSTANCE = new ReportingService();
    }

    public static ReportingService getInstance() {
        return Holder.INSTANCE;
    }

    public void setProductService(ProductService ps) {
        this.productService = ps;
    }

    public void setOrderService(OrderService os) {
        this.orderService = os;
    }

    public TotalRevenueReport getTotalRevenueReport(LocalDate start, LocalDate end) {
        TotalRevenueReport report = null;
        String query = "SELECT SUM(sub_total) AS revenue, SUM(tax) AS tax, COUNT(id) AS count FROM orders WHERE order_placed BETWEEN ? AND ?";
        Timestamp startTs = Timestamp.valueOf(start.atStartOfDay());
        Timestamp endTs   = Timestamp.valueOf(end.plusDays(1).atStartOfDay().minusNanos(1));
        try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setTimestamp(1, startTs);
            stmt.setTimestamp(2, endTs);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            RevenueData data = new RevenueData(start, end, rs.getDouble("revenue"), rs.getDouble("tax"), rs.getInt("count"));
            report = new TotalRevenueReport(data);
        } catch (SQLException e) {
            logger.error("Error while fetching revenue report:\n{}", e.toString());
        }
        return report;
    }

}   


    /*    
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
     */
