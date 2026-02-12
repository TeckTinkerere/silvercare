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

import com.silvercare.dao.AuditLogDAO;
import com.silvercare.model.AuditLog;

/**
 * REST Controller for Audit Log operations
 * Exposes REST APIs for audit log management
 */
@RestController
@RequestMapping("/admin/logs")
public class AuditLogRestController {

    @Autowired
    private AuditLogDAO auditLogDAO;

    /**
     * Get all audit logs
     * GET /admin/logs
     */
    @GetMapping
    public ResponseEntity<?> getAllLogs() {
        try {
            List<AuditLog> logs = auditLogDAO.getAllLogs();
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", logs,
                    "count", logs.size()));
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    /**
     * Log an admin action
     * POST /admin/logs
     */
    @PostMapping
    public ResponseEntity<?> logAction(@RequestBody Map<String, Object> request) {
        try {
            Integer adminId = request.get("adminId") != null
                    ? ((Number) request.get("adminId")).intValue()
                    : null;
            String action = (String) request.get("action");
            String details = (String) request.get("details");

            if (adminId == null || action == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "adminId and action are required"));
            }

            auditLogDAO.logAction(adminId, action, details);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Action logged successfully"));
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }
}
