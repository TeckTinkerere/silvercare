package com.silvercare.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.silvercare.model.Booking;
import com.silvercare.model.Service;
import com.silvercare.dao.BookingDAO;
import com.silvercare.dao.ServiceDAO;

/**
 * REST Controller for Admin Dashboard
 * Provides statistics and overview data
 */
@RestController
@RequestMapping("/admin/dashboard")
public class AdminDashboardRestController {

    @Autowired
    private com.silvercare.util.AdminReportDBUtil adminReportDBUtil;

    @Autowired
    private BookingDAO bookingDAO;

    @Autowired
    private ServiceDAO serviceDAO;

    /**
     * Get dashboard statistics
     * GET /admin/dashboard/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getDashboardStats() {
        try {
            Map<String, Object> stats = adminReportDBUtil.getDashboardQuickStats();

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", stats));

        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    /**
     * Get recent bookings
     * GET /admin/dashboard/recent-bookings
     */
    @GetMapping("/recent-bookings")
    public ResponseEntity<?> getRecentBookings() {
        try {
            List<Booking> recentBookings = bookingDAO.getRecentBookings(5);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", recentBookings,
                    "count", recentBookings.size()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error: " + e.getMessage()));
        }
    }

    /**
     * Get overview data
     * GET /admin/dashboard/overview
     */
    @GetMapping("/overview")
    public ResponseEntity<?> getOverview() {
        try {
            Map<String, Object> overview = new HashMap<>();

            // Get top services
            List<Service> services = serviceDAO.getAllServices();
            overview.put("topServices", services.stream().limit(5).toList());

            // Get categories
            overview.put("categories", serviceDAO.getCategories());

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", overview));

        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }
}
