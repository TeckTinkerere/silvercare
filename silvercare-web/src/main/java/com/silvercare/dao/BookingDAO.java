package com.silvercare.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.silvercare.util.BookingDBUtil;
import com.silvercare.model.Booking;

/**
 * DAO for Booking entity in silvercare-web.
 * Delegates database operations to BookingDBUtil.
 */
public class BookingDAO {

    private BookingDBUtil bookingDBUtil;

    public BookingDAO() {
        this.bookingDBUtil = new BookingDBUtil();
    }

    public int createBooking(int customerId, Timestamp bookingDate, String status,
            double total, double gst, String paymentIntentId,
            List<Map<String, Object>> details) throws SQLException {
        return bookingDBUtil.createBooking(customerId, bookingDate, status, total, gst, paymentIntentId, details);
    }

    public List<Map<String, Object>> getBookingsByCustomer(int customerId) throws SQLException {
        return bookingDBUtil.getBookingsByCustomer(customerId);
    }

    public Map<String, Object> getBookingById(int bookingId) throws SQLException {
        return bookingDBUtil.getBookingById(bookingId);
    }

    public List<Map<String, Object>> getMonthlyRevenue() throws SQLException {
        return bookingDBUtil.getMonthlyRevenue();
    }

    public List<Map<String, Object>> getTopServices() throws SQLException {
        return bookingDBUtil.getTopServices();
    }

    public List<Map<String, Object>> getTopClients() throws SQLException {
        return bookingDBUtil.getTopClients();
    }

    public Map<String, Object> getDashboardStats() throws SQLException {
        return bookingDBUtil.getDashboardStats();
    }

    public List<Booking> getAllBookings() throws SQLException {
        return bookingDBUtil.getAllBookings();
    }
}
