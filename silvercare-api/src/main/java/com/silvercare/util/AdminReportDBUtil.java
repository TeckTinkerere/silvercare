package com.silvercare.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Utility bean containing SQL statements and database operations for Admin
 * Reports
 * This follows the assignment requirement of separating SQL from DAO classes
 */
@Component
public class AdminReportDBUtil {

    // SQL Statements for Admin Report operations
    private static final String SELECT_TOP_CLIENTS = "SELECT c.full_name, c.email, c.address, COUNT(b.booking_id) as booking_count, SUM(b.total_amount) as total_spent "
            +
            "FROM silvercare.customer c " +
            "JOIN silvercare.booking b ON c.customer_id = b.customer_id " +
            "GROUP BY c.customer_id, c.full_name, c.email, c.address " +
            "ORDER BY total_spent DESC LIMIT 10";

    private static final String SELECT_SERVICE_RATINGS = "SELECT s.name, AVG(f.rating) as avg_rating, COUNT(f.feedback_id) as review_count "
            +
            "FROM silvercare.service s " +
            "INNER JOIN silvercare.feedback f ON s.service_id = f.service_id " +
            "GROUP BY s.service_id, s.name " +
            "ORDER BY avg_rating DESC";

    private static final String SELECT_BOOKINGS_BY_DATE_RANGE = "SELECT DATE(b.booking_date) as booking_date, COUNT(*) as booking_count, SUM(b.total_amount) as total_revenue "
            +
            "FROM silvercare.booking b " +
            "WHERE b.booking_date BETWEEN ? AND ? " +
            "GROUP BY DATE(b.booking_date) " +
            "ORDER BY booking_date DESC";

    private static final String SELECT_POPULAR_SERVICES = "SELECT s.name, COUNT(bd.detail_id) as booking_count, SUM(bd.quantity) as total_quantity "
            +
            "FROM silvercare.service s " +
            "JOIN silvercare.booking_detail bd ON s.service_id = bd.service_id " +
            "JOIN silvercare.booking b ON bd.booking_id = b.booking_id " +
            "GROUP BY s.service_id, s.name " +
            "ORDER BY booking_count DESC LIMIT 10";

    private static final String SELECT_CLIENTS_BY_AREA = "SELECT SUBSTRING(c.address, -6) as area_code, COUNT(*) as client_count "
            +
            "FROM silvercare.customer c " +
            "GROUP BY SUBSTRING(c.address, -6) " +
            "ORDER BY client_count DESC";

    private static final String SELECT_MONTHLY_REVENUE = "SELECT EXTRACT(YEAR FROM b.booking_date) as year, EXTRACT(MONTH FROM b.booking_date) as month, "
            +
            "COUNT(*) as booking_count, SUM(b.total_amount) as total_revenue " +
            "FROM silvercare.booking b " +
            "GROUP BY EXTRACT(YEAR FROM b.booking_date), EXTRACT(MONTH FROM b.booking_date) " +
            "ORDER BY year DESC, month DESC LIMIT 12";

    /**
     * Get top clients by total spending
     */
    public List<Map<String, Object>> getTopClients() throws SQLException {
        List<Map<String, Object>> clients = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_TOP_CLIENTS);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> client = new HashMap<>();
                client.put("fullName", rs.getString("full_name"));
                client.put("email", rs.getString("email"));
                client.put("address", rs.getString("address"));
                client.put("bookingCount", rs.getInt("booking_count"));
                client.put("totalSpent", rs.getBigDecimal("total_spent"));
                clients.add(client);
            }
        }
        return clients;
    }

    /**
     * Get service ratings and review counts
     */
    public List<Map<String, Object>> getServiceRatings() throws SQLException {
        List<Map<String, Object>> ratings = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_SERVICE_RATINGS);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> rating = new HashMap<>();
                rating.put("serviceName", rs.getString("name"));
                rating.put("avgRating", rs.getDouble("avg_rating"));
                rating.put("feedbackCount", rs.getInt("review_count"));
                ratings.add(rating);
            }
        }
        return ratings;
    }

    /**
     * Get bookings by date range
     */
    public List<Map<String, Object>> getBookingsByDateRange(String startDate, String endDate) throws SQLException {
        List<Map<String, Object>> bookings = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_BOOKINGS_BY_DATE_RANGE)) {
            pstmt.setString(1, startDate);
            pstmt.setString(2, endDate);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> booking = new HashMap<>();
                    booking.put("date", rs.getDate("booking_date"));
                    booking.put("count", rs.getInt("booking_count"));
                    booking.put("revenue", rs.getBigDecimal("total_revenue"));
                    bookings.add(booking);
                }
            }
        }
        return bookings;
    }

    /**
     * Get most popular services
     */
    public List<Map<String, Object>> getPopularServices() throws SQLException {
        List<Map<String, Object>> services = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_POPULAR_SERVICES);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> service = new HashMap<>();
                service.put("name", rs.getString("name"));
                service.put("bookingCount", rs.getInt("booking_count"));
                service.put("totalQuantity", rs.getInt("total_quantity"));
                services.add(service);
            }
        }
        return services;
    }

    /**
     * Get client distribution by area code
     */
    public List<Map<String, Object>> getClientsByArea() throws SQLException {
        List<Map<String, Object>> areas = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_CLIENTS_BY_AREA);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> area = new HashMap<>();
                area.put("areaCode", rs.getString("area_code"));
                area.put("clientCount", rs.getInt("client_count"));
                areas.add(area);
            }
        }
        return areas;
    }

    /**
     * Get monthly revenue report
     */
    public List<Map<String, Object>> getMonthlyRevenue() throws SQLException {
        List<Map<String, Object>> revenue = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_MONTHLY_REVENUE);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> month = new HashMap<>();
                month.put("year", rs.getInt("year"));
                month.put("month", rs.getInt("month"));
                month.put("bookingCount", rs.getInt("booking_count"));
                month.put("totalRevenue", rs.getBigDecimal("total_revenue"));
                revenue.add(month);
            }
        }
        return revenue;
    }

    /**
     * Get quick dashboard statistics (counts and revenue)
     * More efficient than loading all objects
     */
    public Map<String, Object> getDashboardQuickStats() throws SQLException {
        Map<String, Object> stats = new HashMap<>();

        String sql = "SELECT " +
                "(SELECT COUNT(*) FROM silvercare.service) as service_count, " +
                "(SELECT COUNT(*) FROM silvercare.booking) as booking_count, " +
                "(SELECT COUNT(*) FROM silvercare.booking WHERE status IN ('Pending', 'Confirmed')) as pending_booking_count, "
                +
                "(SELECT COUNT(*) FROM silvercare.booking WHERE status = 'Completed') as completed_booking_count, " +
                "(SELECT COALESCE(SUM(total_amount), 0) FROM silvercare.booking WHERE status IN ('Completed', 'Confirmed')) as total_revenue, "
                +
                "(SELECT COUNT(*) FROM silvercare.customer) as user_count, " +
                "(SELECT COUNT(*) FROM silvercare.feedback) as feedback_count";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                stats.put("serviceCount", rs.getInt("service_count"));
                stats.put("bookingCount", rs.getInt("booking_count"));
                stats.put("pendingBookings", rs.getInt("pending_booking_count"));
                stats.put("completedBookings", rs.getInt("completed_booking_count"));
                stats.put("totalRevenue", rs.getDouble("total_revenue"));
                stats.put("userCount", rs.getInt("user_count"));
                stats.put("feedbackCount", rs.getInt("feedback_count"));
            }
        }
        return stats;
    }
}
