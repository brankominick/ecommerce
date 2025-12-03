package com.wcupa.app.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcupa.app.db.DatabaseConnection;
import com.wcupa.app.domain.core.Review;

public class ReviewService {
    
    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);
    
    private ReviewService() {}

    private static class Holder {
        private static final ReviewService INSTANCE = new ReviewService();
    }

    public static ReviewService getInstance() {
        return Holder.INSTANCE;
    }

    public boolean addReview(Review review) {
        boolean result = true;

        String insert = "INSERT INTO reviews (product_id, customer_email, rating, comment) VALUES (?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(insert)) {
                stmt.setInt(1, review.getProductId());
                stmt.setString(2, review.getCustomerEmail());
                stmt.setInt(3, review.getRating());
                stmt.setString(4, review.getComment());
                stmt.executeUpdate();
            } catch (SQLException e) {
                logger.error("Error while creating review. Error:\n{}", e.toString());
        }
        return result;
    }

    public List<Review> getReviewsForProduct(int productId) {
        List<Review> result = new ArrayList<>();
        String query = "SELECT * FROM reviews WHERE product_id = ? ORDER BY created_at DESC";

        try (Connection con = DatabaseConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, productId);
                ResultSet resultSet = stmt.executeQuery();
                while (resultSet.next()) {
                    result.add(new Review(
                        resultSet.getInt("id"), 
                        resultSet.getInt("product_id"), 
                        resultSet.getString("customer_email"), 
                        resultSet.getInt("rating"),
                        resultSet.getString("comment"),
                        resultSet.getTimestamp("created_at").toLocalDateTime()
                        ));
                }
            } catch (SQLException e) {
                logger.error("Error while getting reviews for product {}\nError: {}", productId, e.toString());
        }
        return result;
    }

    public double getAverageRatingForProduct(int productId) {
        double result = 0.0;
        String query = "SELECT AVG(rating) AS avg_rating FROM reviews WHERE product_id = ? ";

        try (Connection con = DatabaseConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, productId);
                ResultSet resultSet = stmt.executeQuery();
                resultSet.next();
                result = resultSet.getDouble("avg_rating");
            } catch (SQLException e) {
                logger.error("Could not get average rating for product {}.\nError: {}", productId, e.toString());
            }
        return result;
    }
}
