package com.silvercare.dao;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.silvercare.model.Feedback;
import com.silvercare.util.FeedbackDBUtil;

@Repository
public class FeedbackDAO {

    private final FeedbackDBUtil feedbackDBUtil;

    @Autowired
    public FeedbackDAO(FeedbackDBUtil feedbackDBUtil) {
        this.feedbackDBUtil = feedbackDBUtil;
    }

    public FeedbackDAO() {
        this.feedbackDBUtil = new FeedbackDBUtil();
    }

    public Feedback getFeedback(int customerId, int serviceId) throws SQLException {
        return feedbackDBUtil.getFeedback(customerId, serviceId);
    }

    public boolean saveFeedback(Feedback feedback) throws SQLException {
        return feedbackDBUtil.saveFeedback(feedback);
    }

    public boolean updateFeedback(Feedback feedback) throws SQLException {
        return feedbackDBUtil.updateFeedback(feedback);
    }

    public List<Feedback> getFeedbackByService(int serviceId) throws SQLException {
        return feedbackDBUtil.getFeedbackByService(serviceId);
    }

    public List<Feedback> getAllFeedback() throws SQLException {
        return feedbackDBUtil.getAllFeedback();
    }
}
