package com.silvercare.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import com.silvercare.model.Feedback;

/**
 * Utility Bean for Feedback database operations.
 * Separates SQL logic from DAO as per MVC requirements.
 */
public class FeedbackDBUtil {

    private static final String SELECT_FEEDBACK = "SELECT * FROM silvercare.feedback WHERE booking_id = ? AND customer_id = ? LIMIT 1";
    private static final String CHECK_FEEDBACK = "SELECT * FROM silvercare.feedback WHERE customer_id = ? AND service_id = ? LIMIT 1";
    private static final String UPDATE_FEEDBACK = "UPDATE silvercare.feedback SET rating = ?, comment = ?, updated_at = CURRENT_TIMESTAMP WHERE feedback_id = ?";
    private static final String INSERT_FEEDBACK = "INSERT INTO silvercare.feedback (customer_id, service_id, booking_id, rating, comment, created_at) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
    private static final String SELECT_SERVICE_RATINGS = "SELECT s.service_id, s.name as service_name, " +
            "AVG(f.rating) as avg_rating, COUNT(f.feedback_id) as feedback_count " +
            "FROM silvercare.feedback f " +
            "JOIN silvercare.service s ON f.service_id = s.service_id " +
            "GROUP BY s.service_id, s.name " +
            "ORDER BY avg_rating DESC";

    private static final String SELECT_ALL_FEEDBACK = "SELECT f.*, c.full_name as customer_name, s.name as service_name "
            +
            "FROM silvercare.feedback f " +
            "JOIN silvercare.customer c ON f.customer_id = c.customer_id " +
            "JOIN silvercare.service s ON f.service_id = s.service_id " +
            "ORDER BY f.created_at DESC";

    public Map<String, Object> getFeedbackByBooking(int bookingId, int customerId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_FEEDBACK)) {
            pstmt.setInt(1, bookingId);
            pstmt.setInt(2, customerId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFeedback(rs);
                }
            }
        }
        return null;
    }

    public Map<String, Object> checkExistingFeedback(int customerId, int serviceId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(CHECK_FEEDBACK)) {
            pstmt.setInt(1, customerId);
            pstmt.setInt(2, serviceId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFeedback(rs);
                }
            }
        }
        return null;
    }

    public void saveFeedback(Map<String, Object> feedbackData) throws SQLException {
        Integer feedbackId = (Integer) feedbackData.get("feedbackId");

        if (feedbackId != null && feedbackId > 0) {
            try (Connection conn = DBConnection.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(UPDATE_FEEDBACK)) {
                pstmt.setInt(1, (Integer) feedbackData.get("rating"));
                pstmt.setString(2, (String) feedbackData.get("comments"));
                pstmt.setInt(3, feedbackId);
                pstmt.executeUpdate();
            }
        } else {
            try (Connection conn = DBConnection.getConnection();
                    PreparedStatement pstmt = conn.prepareStatement(INSERT_FEEDBACK)) {
                pstmt.setInt(1, (Integer) feedbackData.get("customerId"));
                pstmt.setInt(2, (Integer) feedbackData.get("serviceId"));
                pstmt.setInt(3, (Integer) feedbackData.get("bookingId"));
                pstmt.setInt(4, (Integer) feedbackData.get("rating"));
                pstmt.setString(5, (String) feedbackData.get("comments"));
                pstmt.executeUpdate();
            }
        }
    }

    public List<Feedback> getAllFeedback() throws SQLException {
        List<Feedback> feedbackList = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_FEEDBACK);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Feedback f = new Feedback();
                f.setId(rs.getInt("feedback_id"));
                f.setCustomerId(rs.getInt("customer_id"));
                f.setServiceId(rs.getInt("service_id"));
                f.setRating(rs.getInt("rating"));
                f.setComment(rs.getString("comment"));
                f.setCreatedAt(rs.getTimestamp("created_at"));
                f.setCustomerName(rs.getString("customer_name"));
                f.setServiceName(rs.getString("service_name"));
                feedbackList.add(f);
            }
        }
        return feedbackList;
    }

    public java.util.List<Map<String, Object>> getServiceRatings() throws SQLException {
        java.util.List<Map<String, Object>> ratings = new java.util.ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_SERVICE_RATINGS);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("serviceId", rs.getInt("service_id"));
                row.put("serviceName", rs.getString("service_name"));
                row.put("avgRating", Math.round(rs.getDouble("avg_rating") * 10.0) / 10.0);
                row.put("feedbackCount", rs.getInt("feedback_count"));
                ratings.add(row);
            }
        }
        return ratings;
    }

    private Map<String, Object> mapResultSetToFeedback(ResultSet rs) throws SQLException {
        Map<String, Object> feedback = new HashMap<>();
        feedback.put("feedbackId", rs.getInt("feedback_id"));
        feedback.put("customerId", rs.getInt("customer_id"));
        feedback.put("serviceId", rs.getInt("service_id"));
        feedback.put("bookingId", rs.getInt("booking_id"));
        feedback.put("rating", rs.getInt("rating"));
        feedback.put("comments", rs.getString("comment"));
        feedback.put("createdAt", rs.getTimestamp("created_at"));
        return feedback;
    }
}
