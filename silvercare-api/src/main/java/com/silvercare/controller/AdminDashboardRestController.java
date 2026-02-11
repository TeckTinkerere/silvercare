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
import com.silvercare.dao.FeedbackDAO;
import com.silvercare.dao.ServiceDAO;
import com.silvercare.dao.UserDAO;

/**
 * REST Controller for Admin Dashboard
 * Provides statistics and overview data
 */
@RestController
@RequestMapping("/admin/dashboard")
public class AdminDashboardRestController {

    @Autowired
    private ServiceDAO serviceDAO;

    @Autowired
    private BookingDAO bookingDAO;

    @Autowired
    private FeedbackDAO feedbackDAO;

    @Autowired
    private UserDAO userDAO;

    /**
     * Get dashboard statistics
     * GET /admin/dashboard/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getDashboardStats() {
        try {
            Map<String, Object> stats = new HashMap<>();

            // Get service count
            List<Service> services = serviceDAO.getAllServices();
            stats.put("serviceCount", services.size());
            stats.put("activeServiceCount", services.stream().filter(Service::isActive).count());

            // Get booking count - need to get all bookings
            int bookingCount = 0;
            int completedBookings = 0;
            int pendingBookings = 0;
            double totalRevenue = 0.0;

            try {
                // Get all customers to count their bookings
                var customers = userDAO.getAllCustomers();
                for (var customer : customers) {
                    try {
                        List<Booking> customerBookings = bookingDAO.getBookingsByCustomer(customer.getId());
                        bookingCount += customerBookings.size();

                        for (Booking booking : customerBookings) {
                            if ("Completed".equalsIgnoreCase(booking.getStatus())) {
                                completedBookings++;
                                totalRevenue += booking.getTotalAmount().doubleValue();
                            } else if ("Pending".equalsIgnoreCase(booking.getStatus()) ||
                                    "Confirmed".equalsIgnoreCase(booking.getStatus())) {
                                pendingBookings++;
                            }
                        }
                    } catch (Exception e) {
                        // Continue with next customer
                    }
                }
            } catch (Exception e) {
                // If we can't get bookings, set to 0
                bookingCount = 0;
            }

            stats.put("bookingCount", bookingCount);
            stats.put("completedBookings", completedBookings);
            stats.put("pendingBookings", pendingBookings);
            stats.put("totalRevenue", totalRevenue);

            // Get feedback count
            int feedbackCount = 0;
            try {
                // Count feedback for all services
                for (Service service : services) {
                    var feedbackList = feedbackDAO.getFeedbackByService(service.getId());
                    feedbackCount += feedbackList.size();
                }
            } catch (Exception e) {
                feedbackCount = 0;
            }
            stats.put("feedbackCount", feedbackCount);

            // Get user count
            int userCount = 0;
            try {
                userCount = userDAO.getAllCustomers().size();
            } catch (Exception e) {
                userCount = 0;
            }
            stats.put("userCount", userCount);

            // Get category count
            int categoryCount = 0;
            try {
                categoryCount = serviceDAO.getCategories().size();
            } catch (Exception e) {
                categoryCount = 0;
            }
            stats.put("categoryCount", categoryCount);

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
            // This would need a new method in BookingService to get recent bookings
            // For now, return empty list
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", List.of(),
                    "message", "Recent bookings feature coming soon"));
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
