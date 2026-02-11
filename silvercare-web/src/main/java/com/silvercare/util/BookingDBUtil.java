package com.silvercare.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.silvercare.model.Booking;

/**
 * Utility Bean for Booking database operations.
 * Separates SQL logic from DAO as per MVC requirements.
 */
public class BookingDBUtil {

    private static final String INSERT_BOOKING = "INSERT INTO silvercare.booking (customer_id, booking_date, status, total_amount, gst_amount, payment_intent_id, created_at) "
            + "VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

    private static final String INSERT_BOOKING_DETAIL = "INSERT INTO silvercare.booking_detail (booking_id, service_id, quantity, unit_price, notes) "
            + "VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_BOOKINGS_BY_CUSTOMER = "SELECT * FROM silvercare.booking WHERE customer_id = ? ORDER BY booking_date DESC";

    private static final String SELECT_ALL_BOOKINGS = "SELECT b.*, c.full_name as customer_name FROM silvercare.booking b "
            +
            "JOIN silvercare.customer c ON b.customer_id = c.customer_id ORDER BY b.booking_date DESC";

    private static final String SELECT_BOOKING_BY_ID = "SELECT * FROM silvercare.booking WHERE booking_id = ?";

    private static final String SELECT_DETAILS_BY_BOOKING = "SELECT bd.*, s.name as service_name FROM silvercare.booking_detail bd "
            + "JOIN silvercare.service s ON bd.service_id = s.service_id " + "WHERE bd.booking_id = ?";

    private static final String MONTHLY_REVENUE_REPORT = "SELECT TO_CHAR(booking_date, 'Mon YYYY') as month, SUM(total_amount) as revenue "
            +
            "FROM silvercare.booking WHERE status != 'Cancelled' " +
            "GROUP BY TO_CHAR(booking_date, 'Mon YYYY'), DATE_TRUNC('month', booking_date) " +
            "ORDER BY DATE_TRUNC('month', booking_date) DESC LIMIT 6";

    private static final String TOP_SERVICES_REPORT = "SELECT s.name, COUNT(bd.service_id) as count " +
            "FROM silvercare.booking_detail bd " +
            "JOIN silvercare.service s ON bd.service_id = s.service_id " +
            "GROUP BY s.name ORDER BY count DESC LIMIT 5";

    private static final String TOP_CLIENTS_REPORT = "SELECT u.full_name, u.email, u.address, COUNT(b.booking_id) as booking_count, "
            +
            "SUM(b.total_amount) as total_spent " +
            "FROM silvercare.booking b " +
            "JOIN silvercare.customer u ON b.customer_id = u.customer_id " +
            "WHERE b.status != 'Cancelled' " +
            "GROUP BY u.full_name, u.email, u.address " +
            "ORDER BY total_spent DESC LIMIT 10";

    private static final String DASHBOARD_STATS = "SELECT " +
            "(SELECT SUM(total_amount) FROM silvercare.booking WHERE status != 'Cancelled') as total_revenue, " +
            "(SELECT COUNT(*) FROM silvercare.booking) as total_bookings, " +
            "(SELECT COUNT(*) FROM silvercare.customer) as total_clients, " +
            "(SELECT COUNT(*) FROM silvercare.service WHERE is_active = true) as total_services";

    public int createBooking(int customerId, Timestamp bookingDate, String status,
            double total, double gst, String paymentIntentId,
            List<Map<String, Object>> details) throws SQLException {

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            int bookingId = -1;
            try (PreparedStatement pstmt = conn.prepareStatement(INSERT_BOOKING, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, customerId);
                pstmt.setTimestamp(2, bookingDate);
                pstmt.setString(3, status);
                pstmt.setDouble(4, total);
                pstmt.setDouble(5, gst);
                pstmt.setString(6, paymentIntentId);
                pstmt.executeUpdate();

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        bookingId = rs.getInt(1);
                    }
                }
            }

            if (bookingId != -1) {
                try (PreparedStatement dStmt = conn.prepareStatement(INSERT_BOOKING_DETAIL)) {
                    for (Map<String, Object> detail : details) {
                        dStmt.setInt(1, bookingId);
                        dStmt.setInt(2, ((Number) detail.get("serviceId")).intValue());
                        dStmt.setInt(3, ((Number) detail.get("quantity")).intValue());
                        dStmt.setDouble(4, ((Number) detail.get("unitPrice")).doubleValue());
                        dStmt.setString(5, (String) detail.get("notes"));
                        dStmt.addBatch();
                    }
                    dStmt.executeBatch();
                }
            }

            conn.commit();
            return bookingId;
        } catch (SQLException e) {
            if (conn != null)
                conn.rollback();
            throw e;
        } finally {
            if (conn != null)
                conn.close();
        }
    }

    public List<Booking> getAllBookings() throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_BOOKINGS);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Booking b = new Booking();
                b.setId(rs.getInt("booking_id"));
                b.setCustomerId(rs.getInt("customer_id"));
                b.setCustomerName(rs.getString("customer_name"));
                b.setBookingDate(rs.getTimestamp("booking_date"));
                b.setStatus(rs.getString("status"));
                b.setTotalAmount(rs.getBigDecimal("total_amount"));
                b.setGstAmount(rs.getBigDecimal("gst_amount"));
                b.setCreatedAt(rs.getTimestamp("created_at"));
                bookings.add(b);
            }
        }
        return bookings;
    }

    public List<Map<String, Object>> getBookingsByCustomer(int customerId) throws SQLException {
        List<Map<String, Object>> bookings = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_BOOKINGS_BY_CUSTOMER)) {
            pstmt.setInt(1, customerId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
        }
        return bookings;
    }

    public Map<String, Object> getBookingById(int bookingId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_BOOKING_BY_ID)) {
            pstmt.setInt(1, bookingId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> booking = mapResultSetToBooking(rs);
                    booking.put("details", getBookingDetails(bookingId));
                    return booking;
                }
            }
        }
        return null;
    }

    private List<Map<String, Object>> getBookingDetails(int bookingId) throws SQLException {
        List<Map<String, Object>> details = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_DETAILS_BY_BOOKING)) {
            pstmt.setInt(1, bookingId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> d = new HashMap<>();
                    d.put("serviceId", rs.getInt("service_id"));
                    d.put("serviceName", rs.getString("service_name"));
                    d.put("quantity", rs.getInt("quantity"));
                    d.put("unitPrice", rs.getDouble("unit_price"));
                    d.put("notes", rs.getString("notes"));
                    details.add(d);
                }
            }
        }
        return details;
    }

    public List<Map<String, Object>> getMonthlyRevenue() throws SQLException {
        List<Map<String, Object>> stats = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(MONTHLY_REVENUE_REPORT);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("label", rs.getString("month"));
                row.put("value", rs.getDouble("revenue"));
                stats.add(row);
            }
        }
        return stats;
    }

    public List<Map<String, Object>> getTopServices() throws SQLException {
        List<Map<String, Object>> stats = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(TOP_SERVICES_REPORT);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("name", rs.getString("name"));
                row.put("count", rs.getInt("count"));
                stats.add(row);
            }
        }
        return stats;
    }

    public List<Map<String, Object>> getTopClients() throws SQLException {
        List<Map<String, Object>> clients = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(TOP_CLIENTS_REPORT);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("fullName", rs.getString("full_name"));
                row.put("email", rs.getString("email"));
                row.put("address", rs.getString("address"));
                row.put("bookingCount", rs.getInt("booking_count"));
                row.put("totalSpent", rs.getDouble("total_spent"));
                clients.add(row);
            }
        }
        return clients;
    }

    public Map<String, Object> getDashboardStats() throws SQLException {
        Map<String, Object> stats = new HashMap<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(DASHBOARD_STATS);
                ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                stats.put("revenue", rs.getDouble("total_revenue"));
                stats.put("bookings", rs.getInt("total_bookings"));
                stats.put("clients", rs.getInt("total_clients"));
                stats.put("services", rs.getInt("total_services"));
            }
        }
        return stats;
    }

    private Map<String, Object> mapResultSetToBooking(ResultSet rs) throws SQLException {
        Map<String, Object> b = new HashMap<>();
        b.put("id", rs.getInt("booking_id"));
        b.put("bookingId", rs.getInt("booking_id"));
        b.put("customerId", rs.getInt("customer_id"));
        b.put("bookingDate", rs.getTimestamp("booking_date"));
        b.put("status", rs.getString("status"));
        b.put("totalAmount", rs.getDouble("total_amount"));
        b.put("gstAmount", rs.getDouble("gst_amount"));

        // Safely check for payment_intent_id as it might be missing in some
        // environments
        try {
            b.put("paymentIntentId", rs.getString("payment_intent_id"));
        } catch (SQLException e) {
            // Column missing, ignore
            b.put("paymentIntentId", null);
        }
        return b;
    }
}
