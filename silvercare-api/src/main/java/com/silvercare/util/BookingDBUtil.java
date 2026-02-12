package com.silvercare.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

import com.silvercare.model.Booking;
import com.silvercare.model.BookingDetail;

/**
 * Utility bean containing SQL statements and database operations for Booking
 * entity
 * This follows the assignment requirement of separating SQL from DAO classes
 */
@Component
public class BookingDBUtil {

    // SQL Statements for Booking operations
    private static final String INSERT_BOOKING = "INSERT INTO silvercare.booking (customer_id, booking_date, total_amount, gst_amount, status, created_at) "
            +
            "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

    private static final String INSERT_BOOKING_DETAIL = "INSERT INTO silvercare.booking_detail (booking_id, service_id, quantity, unit_price, notes) "
            +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_BOOKINGS_BY_CUSTOMER = "SELECT * FROM silvercare.booking WHERE customer_id = ? ORDER BY created_at DESC";

    private static final String SELECT_BOOKING_BY_ID = "SELECT b.*, c.full_name as customer_name FROM silvercare.booking b "
            + "JOIN silvercare.customer c ON b.customer_id = c.customer_id WHERE b.booking_id = ?";

    private static final String SELECT_BOOKING_DETAILS = "SELECT bd.*, s.name as service_name FROM silvercare.booking_detail bd "
            +
            "JOIN silvercare.service s ON bd.service_id = s.service_id WHERE bd.booking_id = ?";

    private static final String UPDATE_BOOKING_STATUS = "UPDATE silvercare.booking SET status = ? WHERE booking_id = ?";

    private static final String SELECT_ALL_BOOKINGS = "SELECT b.*, c.full_name as customer_name FROM silvercare.booking b "
            +
            "JOIN silvercare.customer c ON b.customer_id = c.customer_id ORDER BY b.created_at DESC";

    private static final String SELECT_RECENT_BOOKINGS = "SELECT b.*, c.full_name as customer_name FROM silvercare.booking b "
            + "JOIN silvercare.customer c ON b.customer_id = c.customer_id ORDER BY b.created_at DESC LIMIT ?";

    private static final String VERIFY_BOOKING_FOR_FEEDBACK = "SELECT b.booking_date, b.status " +
            "FROM silvercare.BOOKING b " +
            "JOIN silvercare.BOOKING_DETAIL bd ON b.booking_id = bd.booking_id " +
            "WHERE b.booking_id = ? AND b.customer_id = ? AND bd.service_id = ?";

    /**
     * Create a new booking with transaction support
     */
    public int createBooking(Booking booking) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmtBooking = null;
        PreparedStatement pstmtDetails = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            pstmtBooking = conn.prepareStatement(INSERT_BOOKING, Statement.RETURN_GENERATED_KEYS);
            pstmtBooking.setInt(1, booking.getCustomerId());
            pstmtBooking.setTimestamp(2, booking.getBookingDate());
            pstmtBooking.setBigDecimal(3, booking.getTotalAmount());
            pstmtBooking.setBigDecimal(4, booking.getGstAmount());
            pstmtBooking.setString(5, "Pending");

            int rows = pstmtBooking.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Booking creation failed, no rows affected.");
            }

            rs = pstmtBooking.getGeneratedKeys();
            int bookingId = -1;
            if (rs.next()) {
                bookingId = rs.getInt(1);
                booking.setId(bookingId);
            } else {
                throw new SQLException("Booking creation failed, no ID obtained.");
            }

            pstmtDetails = conn.prepareStatement(INSERT_BOOKING_DETAIL);
            for (BookingDetail detail : booking.getDetails()) {
                pstmtDetails.setInt(1, bookingId);
                pstmtDetails.setInt(2, detail.getServiceId());
                pstmtDetails.setInt(3, detail.getQuantity());
                pstmtDetails.setBigDecimal(4, detail.getUnitPrice());
                pstmtDetails.setString(5, detail.getNotes());
                pstmtDetails.addBatch();
            }
            pstmtDetails.executeBatch();

            conn.commit();
            return bookingId;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (rs != null)
                rs.close();
            if (pstmtDetails != null)
                pstmtDetails.close();
            if (pstmtBooking != null)
                pstmtBooking.close();
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    /**
     * Get bookings by customer ID
     */
    public List<Booking> getBookingsByCustomer(int customerId) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
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

    /**
     * Get booking by ID
     */
    public Booking getBookingById(int bookingId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_BOOKING_BY_ID)) {
            pstmt.setInt(1, bookingId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Booking booking = mapResultSetToBooking(rs);
                    booking.setCustomerName(rs.getString("customer_name"));
                    booking.setDetails(getBookingDetails(bookingId));
                    return booking;
                }
            }
        }
        return null;
    }

    /**
     * Get all bookings (for admin)
     */
    public List<Booking> getAllBookings() throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_BOOKINGS);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Booking booking = mapResultSetToBooking(rs);
                booking.setCustomerName(rs.getString("customer_name"));
                bookings.add(booking);
            }
        }
        return bookings;
    }

    /**
     * Get recent bookings
     */
    public List<Booking> getRecentBookings(int limit) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_RECENT_BOOKINGS)) {
            pstmt.setInt(1, limit);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Booking booking = mapResultSetToBooking(rs);
                    booking.setCustomerName(rs.getString("customer_name"));
                    bookings.add(booking);
                }
            }
        }
        return bookings;
    }

    /**
     * Update booking status
     */
    public boolean updateBookingStatus(int bookingId, String status) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(UPDATE_BOOKING_STATUS)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, bookingId);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Get booking details
     */
    private List<BookingDetail> getBookingDetails(int bookingId) throws SQLException {
        List<BookingDetail> details = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_BOOKING_DETAILS)) {
            pstmt.setInt(1, bookingId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BookingDetail detail = new BookingDetail();
                    detail.setId(rs.getInt("detail_id"));
                    detail.setBookingId(rs.getInt("booking_id"));
                    detail.setServiceId(rs.getInt("service_id"));
                    detail.setQuantity(rs.getInt("quantity"));
                    detail.setUnitPrice(rs.getBigDecimal("unit_price"));
                    detail.setNotes(rs.getString("notes"));
                    detail.setServiceName(rs.getString("service_name"));
                    details.add(detail);
                }
            }
        }
        return details;
    }

    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getInt("booking_id"));
        booking.setCustomerId(rs.getInt("customer_id"));
        booking.setBookingDate(rs.getTimestamp("booking_date"));
        booking.setTotalAmount(rs.getBigDecimal("total_amount"));
        booking.setGstAmount(rs.getBigDecimal("gst_amount"));
        booking.setStatus(rs.getString("status"));
        booking.setCreatedAt(rs.getTimestamp("created_at"));
        return booking;
    }

    public Booking verifyBookingForFeedback(int bookingId, int customerId, int serviceId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(VERIFY_BOOKING_FOR_FEEDBACK)) {
            pstmt.setInt(1, bookingId);
            pstmt.setInt(2, customerId);
            pstmt.setInt(3, serviceId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Booking booking = new Booking();
                    booking.setBookingDate(rs.getTimestamp("booking_date"));
                    booking.setStatus(rs.getString("status"));
                    return booking;
                }
            }
        }
        return null;
    }
}
