package com.silvercare.dao;

import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.silvercare.model.Booking;
import com.silvercare.util.BookingDBUtil;

/**
 * Data Access Object for Booking entity
 * Contains field mappings and delegates SQL operations to utility beans
 * This follows the assignment requirement of separating concerns
 */
@Repository
public class BookingDAO {

    private final BookingDBUtil bookingDBUtil;

    @Autowired
    public BookingDAO(BookingDBUtil bookingDBUtil) {
        this.bookingDBUtil = bookingDBUtil;
    }

    /**
     * Default constructor for legacy support
     */
    public BookingDAO() {
        this.bookingDBUtil = new BookingDBUtil();
    }

    /**
     * Create booking - delegates to utility bean
     */
    public int createBooking(Booking booking) throws SQLException {
        return bookingDBUtil.createBooking(booking);
    }

    /**
     * Get bookings by customer - delegates to utility bean
     */
    public List<Booking> getBookingsByCustomer(int customerId) throws SQLException {
        return bookingDBUtil.getBookingsByCustomer(customerId);
    }

    /**
     * Get booking by ID - delegates to utility bean
     */
    public Booking getBookingById(int bookingId) throws SQLException {
        return bookingDBUtil.getBookingById(bookingId);
    }

    /**
     * Get all bookings - delegates to utility bean
     */
    public List<Booking> getAllBookings() throws SQLException {
        return bookingDBUtil.getAllBookings();
    }

    /**
     * Get recent bookings - delegates to utility bean
     */
    public List<Booking> getRecentBookings(int limit) throws SQLException {
        return bookingDBUtil.getRecentBookings(limit);
    }

    /**
     * Update booking status - delegates to utility bean
     */
    public boolean updateBookingStatus(int bookingId, String status) throws SQLException {
        return bookingDBUtil.updateBookingStatus(bookingId, status);
    }

    public Booking verifyBookingForFeedback(int bookingId, int customerId, int serviceId) throws SQLException {
        return bookingDBUtil.verifyBookingForFeedback(bookingId, customerId, serviceId);
    }
}
