package com.silvercare.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.silvercare.util.FeedbackDBUtil;
import com.silvercare.model.Feedback;

/**
 * DAO for Feedback entity in silvercare-web.
 * Delegates database operations to FeedbackDBUtil.
 */
public class FeedbackDAO {

    private FeedbackDBUtil feedbackDBUtil;

    public FeedbackDAO() {
        this.feedbackDBUtil = new FeedbackDBUtil();
    }

    public Map<String, Object> getFeedbackByBooking(int bookingId, int customerId) throws SQLException {
        return feedbackDBUtil.getFeedbackByBooking(bookingId, customerId);
    }

    public Map<String, Object> checkExistingFeedback(int customerId, int serviceId) throws SQLException {
        return feedbackDBUtil.checkExistingFeedback(customerId, serviceId);
    }

    public void saveFeedback(Map<String, Object> feedbackData) throws SQLException {
        feedbackDBUtil.saveFeedback(feedbackData);
    }

    public List<Map<String, Object>> getServiceRatings() throws SQLException {
        return feedbackDBUtil.getServiceRatings();
    }

    public List<Feedback> getAllFeedback() throws SQLException {
        return feedbackDBUtil.getAllFeedback();
    }
}
