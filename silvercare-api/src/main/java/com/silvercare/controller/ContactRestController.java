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
import org.springframework.web.bind.annotation.RestController;

import com.silvercare.model.ContactMessage;
import com.silvercare.dao.ContactMessageDAO;

/**
 * REST Controller for Contact Message operations
 * Exposes REST APIs for contact message management
 */
@RestController
@RequestMapping("/contact")
public class ContactRestController {

    @Autowired
    private ContactMessageDAO contactMessageDAO;

    /**
     * Submit a new contact message
     * POST /api/contact
     */
    @PostMapping
    public ResponseEntity<?> submitContactMessage(@RequestBody ContactMessage message) {
        try {
            // Validate required fields
            if (message.getFullName() == null || message.getFullName().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Full name is required"));
            }
            if (message.getEmail() == null || message.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Email is required"));
            }
            if (message.getMessage() == null || message.getMessage().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Message is required"));
            }

            boolean success = contactMessageDAO.saveContactMessage(message);

            if (success) {
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Contact message submitted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Failed to submit contact message"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    /**
     * Get all contact messages (admin only)
     * GET /api/contact
     */
    @GetMapping
    public ResponseEntity<?> getAllContactMessages() {
        try {
            List<ContactMessage> messages = contactMessageDAO.getAllContactMessages();
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", messages,
                    "count", messages.size()));
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }
}
