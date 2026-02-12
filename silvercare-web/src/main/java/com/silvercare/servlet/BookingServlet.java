package com.silvercare.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.silvercare.util.ApiClient;
import com.silvercare.model.Service;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet for Booking operations - Uses ApiClient to call REST APIs
 */
@WebServlet("/BookingServlet")
public class BookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Gson gson = ApiClient.getGson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null)
            action = "list";

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("customer_id") == null) {
            response.sendRedirect(request.getContextPath() + "/FrontEnd/login.jsp");
            return;
        }

        switch (action) {
            case "form":
                showBookingForm(request, response);
                break;
            case "details":
                showBookingDetails(request, response);
                break;
            case "list":
            default:
                listBookings(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("save".equals(action)) {
            saveBooking(request, response);
        } else if ("createPaymentIntent".equals(action)) {
            handleCreatePaymentIntent(request, response);
        } else if ("prepareSelection".equals(action)) {
            prepareSelection(request, response);
        } else {
            doGet(request, response);
        }
    }

    /**
     * Show booking form with service details - via REST API
     */
    private void showBookingForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String serviceIdStr = request.getParameter("serviceId");

        // Handle single service booking
        if (serviceIdStr != null) {
            try {
                ApiClient.ApiResponse<String> apiResponse = ApiClient.get("/services/" + serviceIdStr, String.class);
                if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                    JsonObject json = gson.fromJson(apiResponse.getData(), JsonObject.class);
                    Service service = gson.fromJson(json.getAsJsonObject("data"), Service.class);
                    request.setAttribute("service", service);

                    // Clear any previous selection when doing a single service booking
                    session.removeAttribute("selected_booking_items");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Handle multiple service selection from cart
            @SuppressWarnings("unchecked")
            List<Integer> selectedIds = (List<Integer>) session.getAttribute("selected_booking_items");
            if (selectedIds != null && !selectedIds.isEmpty()) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");
                if (cart != null) {
                    List<Map<String, Object>> selectedItems = new ArrayList<>();
                    for (Map<String, Object> item : cart) {
                        int id = ((Number) item.get("id")).intValue();
                        if (selectedIds.contains(id)) {
                            selectedItems.add(item);
                        }
                    }
                    request.setAttribute("cartItems", selectedItems);
                }
            }
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/FrontEnd/bookingForm.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * List bookings for current customer - via REST API
     */
    private void listBookings(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer customerId = (Integer) session.getAttribute("customer_id");

        try {
            ApiClient.ApiResponse<String> apiResponse = ApiClient.get(
                    "/bookings?customerId=" + customerId, String.class);

            if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                JsonObject json = gson.fromJson(apiResponse.getData(), JsonObject.class);
                JsonArray dataArray = json.getAsJsonArray("data");
                Type listType = new TypeToken<List<Map<String, Object>>>() {
                }.getType();
                List<Map<String, Object>> bookings = gson.fromJson(dataArray, listType);
                request.setAttribute("bookings", bookings);
            } else {
                request.setAttribute("bookings", new ArrayList<>());
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("bookings", new ArrayList<>());
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/FrontEnd/bookings.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Show booking details - via REST API
     */
    private void showBookingDetails(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr != null) {
            try {
                // Handle potential decimal IDs (e.g., "31.0")
                int id;
                try {
                    id = Integer.parseInt(idStr);
                } catch (NumberFormatException e) {
                    id = (int) Double.parseDouble(idStr);
                }

                ApiClient.ApiResponse<String> apiResponse = ApiClient.get("/bookings/" + id, String.class);
                if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                    JsonObject json = gson.fromJson(apiResponse.getData(), JsonObject.class);
                    Type mapType = new TypeToken<Map<String, Object>>() {
                    }.getType();
                    Map<String, Object> booking = gson.fromJson(json.getAsJsonObject("data"), mapType);
                    request.setAttribute("booking", booking);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/FrontEnd/bookingDetails.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Save booking - via REST API
     */
    private void saveBooking(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer customerId = (Integer) session.getAttribute("customer_id");

        if (customerId == null) {
            response.sendRedirect(request.getContextPath() + "/FrontEnd/login.jsp");
            return;
        }

        try {
            String bookingDate = request.getParameter("bookingDate");
            String bookingTime = request.getParameter("bookingTime");
            String paymentIntentId = request.getParameter("paymentIntentId");

            // Combine date and time into proper timestamp format: yyyy-MM-dd HH:mm:ss
            String bookingDateTime = bookingDate + " " + bookingTime + ":00";

            System.out.println("DEBUG BookingServlet: bookingDate = " + bookingDate);
            System.out.println("DEBUG BookingServlet: bookingTime = " + bookingTime);
            System.out.println("DEBUG BookingServlet: Combined bookingDateTime = " + bookingDateTime);

            // Build booking details from cart (selected items) or form
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");
            @SuppressWarnings("unchecked")
            List<Integer> selectedIds = (List<Integer>) session.getAttribute("selected_booking_items");

            List<Map<String, Object>> details = new ArrayList<>();
            double subtotal = 0;

            if (selectedIds != null && !selectedIds.isEmpty() && cart != null) {
                for (Map<String, Object> cartItem : cart) {
                    int id = ((Number) cartItem.get("id")).intValue();
                    if (selectedIds.contains(id)) {
                        Map<String, Object> detail = new HashMap<>();
                        detail.put("serviceId", id);
                        int qty = cartItem.containsKey("qty") ? ((Number) cartItem.get("qty")).intValue() : 1;
                        detail.put("quantity", qty);
                        double price = ((Number) cartItem.get("price")).doubleValue();
                        detail.put("unitPrice", price);
                        detail.put("notes", cartItem.getOrDefault("notes", ""));
                        details.add(detail);
                        subtotal += price * qty;
                    }
                }
            } else {
                // Single service booking
                String serviceIdStr = request.getParameter("serviceId");
                String qtyStr = request.getParameter("quantity");
                String notes = request.getParameter("notes");

                if (serviceIdStr != null) {
                    int serviceId = Integer.parseInt(serviceIdStr);
                    String durationStr = request.getParameter("duration");

                    int qty = 1;
                    if (qtyStr != null && !qtyStr.isEmpty()) {
                        qty = Integer.parseInt(qtyStr);
                    } else if (durationStr != null && !durationStr.isEmpty()) {
                        try {
                            qty = Integer.parseInt(durationStr);
                        } catch (NumberFormatException e) {
                            qty = 1;
                        }
                    }

                    // Fetch service price via REST API
                    ApiClient.ApiResponse<String> svcResponse = ApiClient.get("/services/" + serviceId, String.class);
                    double unitPrice = 0;
                    if (svcResponse.isSuccess() && svcResponse.getData() != null) {
                        JsonObject svcJson = gson.fromJson(svcResponse.getData(), JsonObject.class);
                        JsonObject svcData = svcJson.getAsJsonObject("data");
                        if (svcData != null && svcData.has("price")) {
                            unitPrice = svcData.get("price").getAsDouble();
                        }
                    }

                    Map<String, Object> detail = new HashMap<>();
                    detail.put("serviceId", serviceId);
                    detail.put("quantity", qty);
                    detail.put("unitPrice", unitPrice);
                    detail.put("notes", notes != null ? notes : "");
                    details.add(detail);
                    subtotal = unitPrice * qty;
                }
            }

            double gstRate = 0.09;
            double gstAmount = subtotal * gstRate;
            double totalAmount = subtotal + gstAmount;

            // Build request body for REST API
            Map<String, Object> bookingData = new HashMap<>();
            bookingData.put("customerId", customerId);
            bookingData.put("bookingDate", bookingDateTime);
            bookingData.put("status", "Confirmed");
            bookingData.put("totalAmount", totalAmount);
            bookingData.put("gstAmount", gstAmount);
            bookingData.put("details", details);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("booking", bookingData);
            if (paymentIntentId != null && !paymentIntentId.isEmpty()) {
                requestBody.put("paymentIntentId", paymentIntentId);
            }

            ApiClient.ApiResponse<String> apiResponse = ApiClient.post("/bookings", requestBody, String.class);

            if (apiResponse.isSuccess()) {
                // Clear selected items and potentially update cart
                if (selectedIds != null && cart != null) {
                    cart.removeIf(item -> selectedIds.contains(((Number) item.get("id")).intValue()));
                    session.setAttribute("cart", cart);
                    session.setAttribute("cart_count", cart.size());
                } else {
                    session.removeAttribute("cart");
                    session.removeAttribute("cart_count");
                }
                session.removeAttribute("selected_booking_items");

                JsonObject responseJson = gson.fromJson(apiResponse.getData(), JsonObject.class);
                int bookingId = responseJson.has("bookingId") ? responseJson.get("bookingId").getAsInt() : 0;

                response.sendRedirect(request.getContextPath() +
                        "/FrontEnd/bookingSuccess.jsp?bookingId=" + bookingId);
            } else {
                request.setAttribute("error", "Failed to create booking. Please try again.");
                showBookingForm(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error creating booking: " + e.getMessage());
            showBookingForm(request, response);
        }
    }

    /**
     * Create Stripe PaymentIntent - via REST API
     */
    private void handleCreatePaymentIntent(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            List<Map<String, Object>> items = new ArrayList<>();
            HttpSession session = request.getSession();
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> cart = (List<Map<String, Object>>) session.getAttribute("cart");
            @SuppressWarnings("unchecked")
            List<Integer> selectedIds = (List<Integer>) session.getAttribute("selected_booking_items");

            if (selectedIds != null && !selectedIds.isEmpty() && cart != null) {
                // From selected items in cart
                for (Map<String, Object> cartItem : cart) {
                    int id = ((Number) cartItem.get("id")).intValue();
                    if (selectedIds.contains(id)) {
                        Map<String, Object> item = new HashMap<>();
                        item.put("serviceId", id);
                        int qty = cartItem.containsKey("qty") ? ((Number) cartItem.get("qty")).intValue() : 1;
                        item.put("quantity", qty);
                        items.add(item);
                    }
                }
            } else {
                // From Form - check for both serviceId and service_id
                String serviceIdStr = request.getParameter("serviceId");
                if (serviceIdStr == null) {
                    serviceIdStr = request.getParameter("service_id");
                }

                if (serviceIdStr != null) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("serviceId", Integer.parseInt(serviceIdStr));

                    String durationStr = request.getParameter("duration");
                    String quantityStr = request.getParameter("quantity");

                    int qty = 1;
                    if (quantityStr != null && !quantityStr.isEmpty()) {
                        qty = Integer.parseInt(quantityStr);
                    } else if (durationStr != null && !durationStr.isEmpty()) {
                        qty = Integer.parseInt(durationStr);
                    }

                    item.put("quantity", qty);
                    items.add(item);
                }
            }

            String currency = request.getParameter("currency");
            if (currency == null)
                currency = "sgd";

            Map<String, Object> paymentData = new HashMap<>();
            paymentData.put("items", items);
            paymentData.put("currency", currency);

            // Call REST API to create PaymentIntent (Server-side calc)
            ApiClient.ApiResponse<String> apiResponse = ApiClient.post(
                    "/bookings/create-payment-intent", paymentData, String.class);

            if (apiResponse.isSuccess()) {
                response.getWriter().write(apiResponse.getData());
            } else {
                response.setStatus(500);
                response.getWriter().write("{\"error\":\"Failed to create payment intent\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Prepare selected items from cart for booking session
     */
    private void prepareSelection(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] selectedItems = request.getParameterValues("selectedItems");
        HttpSession session = request.getSession();

        if (selectedItems != null && selectedItems.length > 0) {
            List<Integer> ids = new ArrayList<>();
            for (String id : selectedItems) {
                ids.add(Integer.parseInt(id));
            }
            session.setAttribute("selected_booking_items", ids);
            response.sendRedirect(request.getContextPath() + "/BookingServlet?action=form");
        } else {
            response.sendRedirect(request.getContextPath() + "/CartServlet");
        }
    }
}
