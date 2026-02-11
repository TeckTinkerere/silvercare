package com.silvercare.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.silvercare.model.Feedback;
import com.silvercare.dao.FeedbackDAO;

/**
 * REST Controller for Feedback operations
 * Exposes REST APIs for feedback management
 */
@RestController
@RequestMapping("/feedback")
public class FeedbackRestController {

    @Autowired
    private FeedbackDAO feedbackDAO;

    /**
     * Get feedback for a specific customer and service, or all feedback
     * GET /api/feedback?customerId={id}&serviceId={id}
     * GET /api/feedback?serviceId={id}
     * GET /api/feedback (admin - returns all feedback)
     */
    @GetMapping
    public ResponseEntity<?> getFeedback(
            @RequestParam(value = "customerId", required = false) Integer customerId,
            @RequestParam(value = "serviceId", required = false) Integer serviceId,
            @RequestParam(value = "bookingId", required = false) Integer bookingId) {
        try {
            if (customerId != null && serviceId != null) {
                Feedback feedback = feedbackDAO.getFeedback(customerId, serviceId);
                if (feedback != null) {
                    return ResponseEntity.ok(Map.of(
                            "status", "success",
                            "data", feedback));
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Map.of("error", "Feedback not found"));
                }
            } else if (serviceId != null) {
                List<Feedback> feedbackList = feedbackDAO.getFeedbackByService(serviceId);
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "data", feedbackList,
                        "count", feedbackList.size()));
            } else {
                // Admin use case - return all feedback
                List<Feedback> feedbackList = feedbackDAO.getAllFeedback();
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "data", feedbackList,
                        "count", feedbackList.size()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    /**
     * Submit new feedback
     * POST /api/feedback
     */
    @PostMapping
    public ResponseEntity<?> submitFeedback(@RequestBody Feedback feedback) {
        try {
            if (feedback.getCustomerId() == 0 || feedback.getServiceId() == 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Customer ID and Service ID are required"));
            }

            // Check if feedback already exists
            Feedback existing = feedbackDAO.getFeedback(feedback.getCustomerId(), feedback.getServiceId());
            boolean success;

            if (existing != null) {
                // Update existing feedback
                feedback.setId(existing.getId());
                success = feedbackDAO.updateFeedback(feedback);
            } else {
                // Create new feedback
                success = feedbackDAO.saveFeedback(feedback);
            }

            if (success) {
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Feedback submitted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Failed to submit feedback"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }
}
