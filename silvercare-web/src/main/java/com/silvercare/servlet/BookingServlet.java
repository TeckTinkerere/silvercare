package com.silvercare.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.silvercare.dao.BookingDAO;
import com.silvercare.dao.ServiceDAO;
import com.silvercare.model.Service;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Traditional Servlet for Booking operations - Uses local DAOs and JDBC
 * to satisfy standard MVC requirements.
 */
@WebServlet({ "/BookingServlet", "/bookings" })
public class BookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "form":
                showBookingForm(request, response);
                break;
            case "list":
                listBookings(request, response);
                break;
            case "details":
                showBookingDetails(request, response);
                break;
            case "createPaymentIntent":
                handleCreatePaymentIntent(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/home");
                break;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("save".equals(action)) {
            saveBooking(request, response);
        } else {
            doGet(request, response);
        }
    }

    private void showBookingForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/FrontEnd/login.jsp?redirect=booking");
            return;
        }

        String serviceIdStr = request.getParameter("service_id");
        if (serviceIdStr != null && !serviceIdStr.isEmpty()) {
            try {
                int serviceId = Integer.parseInt(serviceIdStr);
                ServiceDAO serviceDAO = new ServiceDAO();
                Service service = serviceDAO.getServiceById(serviceId);

                if (service != null) {
                    request.setAttribute("service", service);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Check if cart exists for summary display
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");
            if (cart != null && !cart.isEmpty()) {
                request.setAttribute("cartItems", cart);
            }
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/FrontEnd/bookingForm.jsp");
        dispatcher.forward(request, response);
    }

    private void listBookings(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer customerId = (Integer) session.getAttribute("customer_id");

        if (customerId == null) {
            com.silvercare.model.User user = (com.silvercare.model.User) session.getAttribute("user");
            if (user != null) {
                customerId = user.getId();
            }
        }

        if (customerId == null) {
            response.sendRedirect(request.getContextPath() + "/FrontEnd/login.jsp?redirect=booking/list");
            return;
        }

        try {
            BookingDAO bookingDAO = new BookingDAO();
            List<Map<String, Object>> bookingsData = bookingDAO.getBookingsByCustomer(customerId);

            // Ensure bookingDate is a java.util.Date for JSP formatting
            if (bookingsData != null) {
                for (Map<String, Object> booking : bookingsData) {
                    Object bDate = booking.get("bookingDate");
                    if (bDate instanceof java.sql.Timestamp) {
                        booking.put("bookingDate", new java.util.Date(((java.sql.Timestamp) bDate).getTime()));
                    }
                }
            }
            request.setAttribute("bookings", bookingsData);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/FrontEnd/bookings.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/FrontEnd/home.jsp?error=booking_list_failed");
        }
    }

    private void showBookingDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer customerId = (Integer) session.getAttribute("customer_id");

        if (customerId == null) {
            response.sendRedirect(request.getContextPath() + "/FrontEnd/login.jsp");
            return;
        }

        String bookingIdStr = request.getParameter("id");
        if (bookingIdStr == null || bookingIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/BookingServlet?action=list&error=missing_id");
            return;
        }

        try {
            int bookingId = Integer.parseInt(bookingIdStr);
            BookingDAO bookingDAO = new BookingDAO();
            Map<String, Object> bookingData = bookingDAO.getBookingById(bookingId);

            if (bookingData != null) {
                Integer bookingCustomerId = ((Number) bookingData.get("customerId")).intValue();

                if (bookingCustomerId.equals(customerId)) {
                    // Normalize date
                    Object bDate = bookingData.get("bookingDate");
                    if (bDate instanceof java.sql.Timestamp) {
                        bookingData.put("bookingDate", new java.util.Date(((java.sql.Timestamp) bDate).getTime()));
                    }

                    request.setAttribute("booking", bookingData);
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/FrontEnd/bookingDetails.jsp");
                    dispatcher.forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/BookingServlet?action=list&error=unauthorized");
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/BookingServlet?action=list&error=not_found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/BookingServlet?action=list&error=not_found");
        }
    }

    private void saveBooking(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        com.silvercare.model.User user = (com.silvercare.model.User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/FrontEnd/login.jsp");
            return;
        }

        try {
            String dateStr = request.getParameter("bookingDate");
            String timeStr = request.getParameter("bookingTime");
            String durationStr = request.getParameter("duration");
            String frequency = request.getParameter("frequency");
            String notes = request.getParameter("notes");
            String serviceIdStr = request.getParameter("service_id");
            String contactName = request.getParameter("contactName");
            String contactPhone = request.getParameter("contactPhone");
            String serviceAddress = request.getParameter("serviceAddress");
            String paymentIntentId = request.getParameter("paymentIntentId");

            if (dateStr == null || timeStr == null || durationStr == null) {
                throw new IllegalArgumentException("Required fields missing");
            }

            int duration = Integer.parseInt(durationStr);
            LocalDateTime dt = LocalDateTime.parse(dateStr + "T" + timeStr);
            Timestamp bookingDate = Timestamp.valueOf(dt);

            List<Map<String, Object>> detailsList = new ArrayList<>();
            BigDecimal totalAmount = BigDecimal.ZERO;

            StringBuilder combinedNotes = new StringBuilder();
            combinedNotes.append("Contact: ").append(contactName).append("\n");
            combinedNotes.append("Phone: ").append(contactPhone).append("\n");
            combinedNotes.append("Address: ").append(serviceAddress).append("\n");
            combinedNotes.append("Frequency: ").append(frequency).append("\n");
            if (notes != null && !notes.isEmpty()) {
                combinedNotes.append("Special Instructions: ").append(notes);
            }
            String finalNotes = combinedNotes.toString();

            boolean isCartBooking = false;

            if (serviceIdStr != null && !serviceIdStr.isEmpty()) {
                int serviceId = Integer.parseInt(serviceIdStr);
                ServiceDAO serviceDAO = new ServiceDAO();
                Service service = serviceDAO.getServiceById(serviceId);

                if (service == null) {
                    throw new IllegalArgumentException("Service not found");
                }

                Map<String, Object> detail = new HashMap<>();
                detail.put("serviceId", serviceId);
                detail.put("quantity", duration);
                detail.put("unitPrice", service.getPrice().doubleValue());
                detail.put("notes", finalNotes);

                detailsList.add(detail);
                totalAmount = service.getPrice().multiply(new BigDecimal(duration));
            } else {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");
                if (cart == null || cart.isEmpty()) {
                    response.sendRedirect(request.getContextPath() + "/FrontEnd/cart.jsp?error=empty");
                    return;
                }

                for (Map<String, Object> item : cart) {
                    Integer sId = ((Number) item.get("id")).intValue();
                    Double price = ((Number) item.get("price")).doubleValue();

                    Map<String, Object> detail = new HashMap<>();
                    detail.put("serviceId", sId);
                    detail.put("quantity", duration);
                    detail.put("unitPrice", price);
                    detail.put("notes", finalNotes);

                    detailsList.add(detail);
                    totalAmount = totalAmount.add(BigDecimal.valueOf(price).multiply(new BigDecimal(duration)));
                }
                isCartBooking = true;
            }

            BigDecimal gstRate = new BigDecimal("0.09");
            BigDecimal gst = totalAmount.multiply(gstRate);
            BigDecimal grandTotal = totalAmount.add(gst);

            BookingDAO bookingDAO = new BookingDAO();
            int bookingId = bookingDAO.createBooking(
                    user.getId(),
                    bookingDate,
                    "Confirmed",
                    grandTotal.doubleValue(),
                    gst.doubleValue(),
                    paymentIntentId,
                    detailsList);

            if (bookingId != -1) {
                if (isCartBooking) {
                    session.removeAttribute("cart");
                    session.removeAttribute("cart_count");
                }
                response.sendRedirect(request.getContextPath() + "/FrontEnd/bookingSuccess.jsp?id=" + bookingId);
            } else {
                response.sendRedirect(request.getContextPath() + "/FrontEnd/bookingForm.jsp?error=booking_failed");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/FrontEnd/bookingForm.jsp?error=" + e.getMessage());
        }
    }

    private void handleCreatePaymentIntent(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Read body from request
            StringBuilder buffer = new StringBuilder();
            request.getReader().lines().forEach(buffer::append);
            String body = buffer.toString();

            // Parse amount and currency from body manually or via GSON
            com.google.gson.Gson gson = new com.google.gson.Gson();
            com.google.gson.JsonObject jsonBody = gson.fromJson(body, com.google.gson.JsonObject.class);
            double amount = jsonBody.get("amount").getAsDouble();
            String currency = jsonBody.has("currency") ? jsonBody.get("currency").getAsString() : "sgd";

            // Use local StripeUtil instead of external API
            Map<String, Object> result = com.silvercare.util.StripeUtil.createPaymentIntent(amount, currency);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(gson.toJson(result));
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
