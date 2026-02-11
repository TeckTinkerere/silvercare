package com.silvercare.controller;

import java.sql.SQLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.silvercare.model.Booking;
import com.silvercare.dao.BookingDAO;

@RestController
@RequestMapping("/poll/status")
public class StatusRestController {

    @Autowired
    private BookingDAO bookingDAO;

    @GetMapping
    public ResponseEntity<?> pollStatus(@RequestParam(value = "bookingId", required = false) String bookingIdStr) {
        if (bookingIdStr == null || bookingIdStr.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            int bookingId = Integer.parseInt(bookingIdStr);
            Booking booking = bookingDAO.getBookingById(bookingId);

            if (booking != null) {
                return ResponseEntity.ok(Map.of("status", booking.getStatus()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Booking not found"));
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Invalid ID format"));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error: " + e.getMessage()));
        }
    }
}
