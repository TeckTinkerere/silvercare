package com.silvercare.controller;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.silvercare.model.Booking;
import com.silvercare.model.BookingDetail;
import com.silvercare.dao.BookingDAO;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

/**
 * REST Controller for Booking operations
 * Exposes REST APIs for booking management
 */
@RestController
@RequestMapping("/bookings")
public class BookingRestController {

    @Autowired
    private BookingDAO bookingDAO;

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    /**
     * Get all bookings for a customer, or all bookings if no customerId provided
     * GET /api/bookings?customerId={id}
     * GET /api/bookings (admin - returns all bookings)
     */
    @GetMapping
    public ResponseEntity<?> getBookingsByCustomer(
            @RequestParam(value = "customerId", required = false) Integer customerId) {
        try {
            List<Booking> bookings;
            if (customerId != null) {
                bookings = bookingDAO.getBookingsByCustomer(customerId);
            } else {
                // Admin use case - return all bookings
                bookings = bookingDAO.getAllBookings();
            }
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", bookings,
                    "count", bookings.size()));
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    /**
     * Get booking by ID
     * GET /api/bookings/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable("id") int bookingId) {
        try {
            Booking booking = bookingDAO.getBookingById(bookingId);
            if (booking != null) {
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "data", booking));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Booking not found"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    /**
     * Create new booking
     * POST /api/bookings
     * Request body can include optional "paymentIntentId" field for payment
     * verification
     */
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Map<String, Object> requestData) {
        try {
            // Extract payment intent ID if provided
            String paymentIntentId = (String) requestData.get("paymentIntentId");

            // Extract booking data
            @SuppressWarnings("unchecked")
            Map<String, Object> bookingData = requestData.containsKey("booking")
                    ? (Map<String, Object>) requestData.get("booking")
                    : requestData;

            // Convert map to Booking object (assuming conversion logic exists)
            Booking booking = convertMapToBooking(bookingData);

            if (booking.getCustomerId() == 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Customer ID is required"));
            }

            // Optional: Verify payment if paymentIntentId is provided
            if (paymentIntentId != null && !paymentIntentId.isEmpty()) {
                System.out.println("🔍 Verifying payment: " + paymentIntentId);
                try {
                    PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId);
                    if (!"succeeded".equals(intent.getStatus())) {
                        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                                .body(Map.of("error", "Payment NOT successful. Status: " + intent.getStatus()));
                    }
                    booking.setStatus("Confirmed");
                } catch (Exception e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Map.of("error", "Error verifying Stripe payment: " + e.getMessage()));
                }
            }

            int bookingId = bookingDAO.createBooking(booking);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Booking created successfully",
                    "bookingId", bookingId));
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid request: " + e.getMessage()));
        }
    }

    /**
     * Create a Stripe PaymentIntent
     * POST /api/bookings/create-payment-intent
     */
    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody Map<String, Object> request) {
        try {
            Double amount = Double.parseDouble(request.get("amount").toString());
            String currency = request.get("currency") != null ? request.get("currency").toString() : "sgd";

            // Amount in cents
            long amountInCents = Math.round(amount * 100);

            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency(currency)
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build())
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

            return ResponseEntity.ok(Map.of(
                    "clientSecret", intent.getClientSecret(),
                    "paymentIntentId", intent.getId()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Stripe error: " + e.getMessage()));
        }
    }

    /**
     * Helper method to convert Map to Booking object
     * Handles JSON/Map data from REST requests and converts to typed Booking entity
     */
    private Booking convertMapToBooking(Map<String, Object> data) {
        Booking booking = new Booking();

        try {
            // Required fields
            if (data.containsKey("customerId")) {
                booking.setCustomerId(((Number) data.get("customerId")).intValue());
            }

            if (data.containsKey("bookingDate")) {
                Object dateObj = data.get("bookingDate");
                if (dateObj instanceof Timestamp) {
                    booking.setBookingDate((Timestamp) dateObj);
                } else if (dateObj instanceof String) {
                    booking.setBookingDate(Timestamp.valueOf((String) dateObj));
                } else if (dateObj instanceof Long) {
                    booking.setBookingDate(new Timestamp((Long) dateObj));
                }
            }

            if (data.containsKey("totalAmount")) {
                Object amountObj = data.get("totalAmount");
                if (amountObj instanceof BigDecimal) {
                    booking.setTotalAmount((BigDecimal) amountObj);
                } else if (amountObj instanceof Number) {
                    booking.setTotalAmount(new BigDecimal(amountObj.toString()));
                } else if (amountObj instanceof String) {
                    booking.setTotalAmount(new BigDecimal((String) amountObj));
                }
            }

            if (data.containsKey("gstAmount")) {
                Object gstObj = data.get("gstAmount");
                if (gstObj instanceof BigDecimal) {
                    booking.setGstAmount((BigDecimal) gstObj);
                } else if (gstObj instanceof Number) {
                    booking.setGstAmount(new BigDecimal(gstObj.toString()));
                } else if (gstObj instanceof String) {
                    booking.setGstAmount(new BigDecimal((String) gstObj));
                }
            }

            // Optional fields
            if (data.containsKey("status")) {
                booking.setStatus((String) data.get("status"));
            } else {
                booking.setStatus("Pending");
            }

            // Handle booking details if present
            if (data.containsKey("details")) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> detailsList = (List<Map<String, Object>>) data.get("details");
                List<BookingDetail> details = new ArrayList<>();

                for (Map<String, Object> detailMap : detailsList) {
                    BookingDetail detail = new BookingDetail();

                    if (detailMap.containsKey("serviceId")) {
                        detail.setServiceId(((Number) detailMap.get("serviceId")).intValue());
                    }

                    if (detailMap.containsKey("quantity")) {
                        detail.setQuantity(((Number) detailMap.get("quantity")).intValue());
                    }

                    if (detailMap.containsKey("unitPrice")) {
                        Object priceObj = detailMap.get("unitPrice");
                        if (priceObj instanceof BigDecimal) {
                            detail.setUnitPrice((BigDecimal) priceObj);
                        } else if (priceObj instanceof Number) {
                            detail.setUnitPrice(new BigDecimal(priceObj.toString()));
                        }
                    }

                    if (detailMap.containsKey("notes")) {
                        detail.setNotes((String) detailMap.get("notes"));
                    }

                    details.add(detail);
                }

                booking.setDetails(details);
            }

        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid booking data: " + e.getMessage(), e);
        }

        return booking;
    }

    /**
     * Update booking status
     * PUT /api/bookings/{id}/status
     */
    @PostMapping("/{id}/status")
    public ResponseEntity<?> updateBookingStatus(
            @PathVariable("id") int bookingId,
            @RequestBody Map<String, String> request) {
        try {
            String status = request.get("status");
            if (status == null || status.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Status is required"));
            }

            boolean success = bookingDAO.updateBookingStatus(bookingId, status);
            if (success) {
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Booking status updated"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Update failed"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }
}
