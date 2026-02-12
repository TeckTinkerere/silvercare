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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.silvercare.model.Booking;
import com.silvercare.model.BookingDetail;
import com.silvercare.dao.BookingDAO;
import com.silvercare.service.AuditLogService;
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

    @Autowired
    private com.silvercare.dao.ServiceDAO serviceDAO;

    @Autowired
    private AuditLogService auditLogService;

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

            // Convert map to Booking object
            Booking booking = convertMapToBooking(bookingData);

            if (booking.getCustomerId() == 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Customer ID is required"));
            }

            // ---------------------------------------------------------
            // SECURITY FIX: Recalculate Total Amount on Backend
            // ---------------------------------------------------------
            BigDecimal calculatedSubtotal = BigDecimal.ZERO;

            if (booking.getDetails() != null) {
                for (BookingDetail detail : booking.getDetails()) {
                    com.silvercare.model.Service service = serviceDAO.getServiceById(detail.getServiceId());
                    if (service != null) {
                        BigDecimal price = service.getPrice();
                        BigDecimal lineTotal = price.multiply(new BigDecimal(detail.getQuantity()));

                        // Update detail unit price to match current DB price (optional, but good for
                        // consistency)
                        detail.setUnitPrice(price);

                        calculatedSubtotal = calculatedSubtotal.add(lineTotal);
                    } else {
                        return ResponseEntity.badRequest()
                                .body(Map.of("error", "Invalid Service ID: " + detail.getServiceId()));
                    }
                }
            }

            // Calculate GST (9%) and Total on backend
            BigDecimal gstRate = new BigDecimal("0.09");
            BigDecimal gstAmount = calculatedSubtotal.multiply(gstRate);
            BigDecimal totalAmount = calculatedSubtotal.add(gstAmount);

            // Enforce backend-calculated values
            booking.setTotalAmount(totalAmount);
            booking.setGstAmount(gstAmount);

            System.out
                    .println("✅ Backend calculated total: " + totalAmount + " (Subtotal: " + calculatedSubtotal + ")");

            // Optional: Verify payment if paymentIntentId is provided
            if (paymentIntentId != null && !paymentIntentId.isEmpty()) {
                System.out.println("🔍 Verifying payment: " + paymentIntentId);
                try {
                    PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId);
                    if (!"succeeded".equals(intent.getStatus())) {
                        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                                .body(Map.of("error", "Payment NOT successful. Status: " + intent.getStatus()));
                    }

                    // Optional: Check if paid amount matches calculated amount
                    long paidAmount = intent.getAmount(); // in cents
                    long expectedAmount = totalAmount.multiply(new BigDecimal("100")).longValue();

                    // Allow small tolerance? Or exact match?
                    // Stripe amount is integer cents.
                    if (Math.abs(paidAmount - expectedAmount) > 5) { // 5 cents tolerance for rounding diffs
                        System.out.println("⚠️ Warning: Payment amount (" + paidAmount + ") differs from calculated ("
                                + expectedAmount + ")");
                        // potentially reject or flag
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
                    "bookingId", bookingId,
                    "finalAmount", totalAmount));
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
     * Create a Stripe PaymentIntent with server-side price calculation
     * POST /api/bookings/create-payment-intent
     * Body: { "items": [ {"serviceId": 1, "quantity": 2}, ... ], "currency": "sgd"
     * }
     */
    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody Map<String, Object> request) {
        try {
            String currency = request.get("currency") != null ? request.get("currency").toString() : "sgd";

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> items = (List<Map<String, Object>>) request.get("items");

            if (items == null || items.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "No items provided"));
            }

            BigDecimal subtotal = BigDecimal.ZERO;

            // Calculate total from DB prices
            for (Map<String, Object> item : items) {
                int serviceId = ((Number) item.get("serviceId")).intValue();
                int quantity = ((Number) item.get("quantity")).intValue();

                com.silvercare.model.Service service = serviceDAO.getServiceById(serviceId);
                if (service != null) {
                    subtotal = subtotal.add(service.getPrice().multiply(new BigDecimal(quantity)));
                }
            }

            // Add 9% GST and round to 2 decimal places (cents)
            BigDecimal totalWithGst = subtotal.multiply(new BigDecimal("1.09")).setScale(2,
                    java.math.RoundingMode.HALF_UP);

            // Amount in cents
            long amountInCents = totalWithGst.multiply(new BigDecimal("100")).longValue();

            // Log for debugging
            System.out.println("Creating payment intent (Backend Calc) - Amount: $" + totalWithGst + " ("
                    + amountInCents + " cents)");

            // Stripe requires minimum 50 cents for SGD
            if (amountInCents < 50) {
                System.out.println(
                        "WARNING: Amount too small (" + amountInCents + " cents). Using minimum 50 cents for testing.");
                amountInCents = 50;
            }

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
                    "paymentIntentId", intent.getId(),
                    "amount", totalWithGst));
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
                System.out.println(
                        "DEBUG: bookingDate type: " + (dateObj != null ? dateObj.getClass().getName() : "null"));
                System.out.println("DEBUG: bookingDate value: " + dateObj);

                if (dateObj instanceof Timestamp) {
                    booking.setBookingDate((Timestamp) dateObj);
                } else if (dateObj instanceof String) {
                    String dateStr = (String) dateObj;
                    try {
                        // Try parsing as full timestamp: yyyy-MM-dd HH:mm:ss
                        System.out.println("DEBUG: Attempting to parse timestamp: " + dateStr);
                        booking.setBookingDate(Timestamp.valueOf(dateStr));
                        System.out.println("DEBUG: Successfully parsed timestamp");
                    } catch (IllegalArgumentException e) {
                        System.out.println("DEBUG: Failed to parse, trying to add default time");
                        // If that fails, try adding default time
                        if (!dateStr.contains(" ")) {
                            dateStr = dateStr + " 00:00:00";
                            System.out.println("DEBUG: Modified dateStr: " + dateStr);
                            booking.setBookingDate(Timestamp.valueOf(dateStr));
                        } else {
                            System.err.println("ERROR: Could not parse timestamp: " + dateStr);
                            throw new IllegalArgumentException(
                                    "Invalid timestamp format: " + dateStr + ". Expected format: yyyy-MM-dd HH:mm:ss",
                                    e);
                        }
                    }
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
            @RequestBody Map<String, String> request,
            @RequestHeader(value = "X-Admin-Id", required = false) Integer adminId) {
        try {
            String status = request.get("status");
            if (status == null || status.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Status is required"));
            }

            boolean success = bookingDAO.updateBookingStatus(bookingId, status);
            if (success) {
                if (adminId != null) {
                    auditLogService.logAction(adminId, "UPDATE_BOOKING_STATUS",
                            "Booking ID: " + bookingId + ", Status: " + status);
                }
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
