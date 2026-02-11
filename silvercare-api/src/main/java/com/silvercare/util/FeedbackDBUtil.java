package com.silvercare.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.silvercare.model.Feedback;

@Component
public class FeedbackDBUtil {

    private static final String SELECT_FEEDBACK = "SELECT * FROM silvercare.FEEDBACK WHERE customer_id = ? AND service_id = ?";
    private static final String INSERT_FEEDBACK = "INSERT INTO silvercare.FEEDBACK (customer_id, service_id, rating, comment, created_at) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
    private static final String UPDATE_FEEDBACK = "UPDATE silvercare.FEEDBACK SET rating = ?, comment = ? WHERE feedback_id = ?";

    // For admin or service details if needed
    private static final String SELECT_FEEDBACK_BY_SERVICE = "SELECT * FROM silvercare.FEEDBACK WHERE service_id = ? ORDER BY created_at DESC";

    public Feedback getFeedback(int customerId, int serviceId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_FEEDBACK)) {
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

    public boolean saveFeedback(Feedback feedback) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(INSERT_FEEDBACK)) {
            pstmt.setInt(1, feedback.getCustomerId());
            pstmt.setInt(2, feedback.getServiceId());
            pstmt.setInt(3, feedback.getRating());
            pstmt.setString(4, feedback.getComment());
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean updateFeedback(Feedback feedback) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(UPDATE_FEEDBACK)) {
            pstmt.setInt(1, feedback.getRating());
            pstmt.setString(2, feedback.getComment());
            pstmt.setInt(3, feedback.getId());
            return pstmt.executeUpdate() > 0;
        }
    }

    public List<Feedback> getFeedbackByService(int serviceId) throws SQLException {
        List<Feedback> feedbackList = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_FEEDBACK_BY_SERVICE)) {
            pstmt.setInt(1, serviceId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    feedbackList.add(mapResultSetToFeedback(rs));
                }
            }
        }
        return feedbackList;
    }

    private Feedback mapResultSetToFeedback(ResultSet rs) throws SQLException {
        Feedback f = new Feedback();
        f.setId(rs.getInt("feedback_id"));
        f.setCustomerId(rs.getInt("customer_id"));
        f.setServiceId(rs.getInt("service_id"));
        f.setRating(rs.getInt("rating"));
        f.setComment(rs.getString("comment"));
        f.setCreatedAt(rs.getTimestamp("created_at"));
        return f;
    }

    public List<Feedback> getAllFeedback() throws SQLException {
        List<Feedback> feedbackList = new ArrayList<>();
        String sql = "SELECT f.*, c.full_name as customer_name, s.name as service_name " +
                "FROM silvercare.FEEDBACK f " +
                "JOIN silvercare.CUSTOMER c ON f.customer_id = c.customer_id " +
                "JOIN silvercare.SERVICE s ON f.service_id = s.service_id " +
                "ORDER BY f.created_at DESC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Feedback feedback = mapResultSetToFeedback(rs);
                feedback.setCustomerName(rs.getString("customer_name"));
                feedback.setServiceName(rs.getString("service_name"));
                feedbackList.add(feedback);
            }
        }
        return feedbackList;
    }
}
