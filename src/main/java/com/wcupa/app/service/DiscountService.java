package com.wcupa.app.service;

import java.util.List;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcupa.app.db.DatabaseConnection;
import com.wcupa.app.domain.core.Discount;

public class DiscountService {
    
    private static final Logger logger = LoggerFactory.getLogger(DiscountService.class);

    private DiscountService() {}

    private static class Holder {
        private static final DiscountService INSTANCE = new DiscountService();
    }

    public static DiscountService getInstance() {
        return Holder.INSTANCE;
    }

    public List<Discount> getDiscounts() {
        List<Discount> result = new ArrayList<>();
        String getSQL = "SELECT * FROM discounts";
        try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement stmt = con.prepareStatement(getSQL)) {
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                Discount.DiscountType type = Discount.DiscountType.valueOf(resultSet.getString("type"));
                double discount = resultSet.getDouble("discount");
                boolean active = resultSet.getBoolean("active");
                result.add(new Discount(name, type, discount, active));
            }
            logger.info("Retrieved {} discounts!", result.size());
        } catch (SQLException e) {
            logger.error("Could not get discounts from database.\nError: {}", e.toString());
        }
        return result;
    }

    public Discount getActiveDiscountByName(String name) {
        Discount result = null;
        String getSQL = "SELECT * FROM discounts WHERE name = ? AND active = true";
        try (Connection con = DatabaseConnection.getConnection();
        PreparedStatement stmt = con.prepareStatement(getSQL)) {
            stmt.setString(1, name);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            String discountName = resultSet.getString("name");
            Discount.DiscountType type = Discount.DiscountType.valueOf(resultSet.getString("type"));
            double discount = resultSet.getDouble("discount");
            boolean active = resultSet.getBoolean("active");
            result = new Discount(discountName, type, discount, active);
            logger.info("Retrieved active discount!");
        } catch (SQLException e) {
            logger.error("Active discount not found.\nError: {}", e.toString());
        }
        return result;
    }

}
