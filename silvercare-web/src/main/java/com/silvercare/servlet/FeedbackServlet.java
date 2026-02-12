package com.silvercare.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.silvercare.util.ApiClient;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet for Feedback operations - Uses ApiClient to call REST APIs
 */
@WebServlet("/FeedbackServlet")
public class FeedbackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Gson gson = ApiClient.getGson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("form".equals(action)) {
            showFeedbackForm(request, response);
        } else {
            listFeedback(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("save".equals(action)) {
            saveFeedback(request, response);
        } else {
            doGet(request, response);
        }
    }

    /**
     * Helper to parse ID which might be a decimal string (e.g. "31.0")
     */
    private int parseId(String idStr) {
        if (idStr == null || idStr.trim().isEmpty())
            return 0;
        try {
            return Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            try {
                return (int) Double.parseDouble(idStr);
            } catch (Exception e2) {
                return 0;
            }
        }
    }

    /**
     * Show feedback form with booking/service context - via REST API
     */
    private void showFeedbackForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("customer_id") == null) {
            response.sendRedirect(request.getContextPath() + "/FrontEnd/login.jsp");
            return;
        }

        // Support both underscored and camelCase for better compatibility
        String bookingIdStr = request.getParameter("booking_id");
        if (bookingIdStr == null)
            bookingIdStr = request.getParameter("bookingId");

        String serviceIdStr = request.getParameter("service_id");
        if (serviceIdStr == null)
            serviceIdStr = request.getParameter("serviceId");

        Integer customerId = (Integer) session.getAttribute("customer_id");

        try {
            int bookingId = parseId(bookingIdStr);
            int serviceId = parseId(serviceIdStr);

            // Fetch booking details via REST API
            if (bookingId > 0) {
                ApiClient.ApiResponse<String> bookingResponse = ApiClient.get(
                        "/bookings/" + bookingId, String.class);
                if (bookingResponse.isSuccess() && bookingResponse.getData() != null) {
                    JsonObject json = gson.fromJson(bookingResponse.getData(), JsonObject.class);
                    Type mapType = new TypeToken<Map<String, Object>>() {
                    }.getType();
                    Map<String, Object> booking = gson.fromJson(json.getAsJsonObject("data"), mapType);

                    request.setAttribute("booking", booking);
                    request.setAttribute("bookingId", bookingId);
                    request.setAttribute("bookingDate", booking.get("bookingDate"));
                    request.setAttribute("bookingStatus", booking.get("status"));
                }
            }

            // Fetch service details via REST API
            if (serviceId > 0) {
                ApiClient.ApiResponse<String> svcResponse = ApiClient.get(
                        "/services/" + serviceId, String.class);
                if (svcResponse.isSuccess() && svcResponse.getData() != null) {
                    JsonObject json = gson.fromJson(svcResponse.getData(), JsonObject.class);
                    Type mapType = new TypeToken<Map<String, Object>>() {
                    }.getType();
                    Map<String, Object> service = gson.fromJson(json.getAsJsonObject("data"), mapType);

                    request.setAttribute("service", service);
                    request.setAttribute("serviceId", serviceId);
                    request.setAttribute("serviceName", service.get("name"));
                }

                // Check if feedback already exists via REST API
                ApiClient.ApiResponse<String> feedbackResponse = ApiClient.get(
                        "/feedback?customerId=" + customerId + "&serviceId=" + serviceId, String.class);

                if (feedbackResponse.isSuccess() && feedbackResponse.getData() != null) {
                    JsonObject json = gson.fromJson(feedbackResponse.getData(), JsonObject.class);
                    if (json.has("data")) {
                        JsonElement dataElement = json.get("data");
                        JsonObject feedbackObj = null;

                        if (dataElement.isJsonObject()) {
                            feedbackObj = dataElement.getAsJsonObject();
                        } else if (dataElement.isJsonArray() && dataElement.getAsJsonArray().size() > 0) {
                            feedbackObj = dataElement.getAsJsonArray().get(0).getAsJsonObject();
                        }

                        if (feedbackObj != null) {
                            request.setAttribute("existingFeedback", true);
                            request.setAttribute("isEditMode", true);
                            if (feedbackObj.has("rating")) {
                                request.setAttribute("existingRating", feedbackObj.get("rating").getAsInt());
                            }
                            if (feedbackObj.has("comment") && !feedbackObj.get("comment").isJsonNull()) {
                                request.setAttribute("existingComment", feedbackObj.get("comment").getAsString());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/FrontEnd/feedbackForm.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * List feedback for current customer - via REST API
     */
    private void listFeedback(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("customer_id") == null) {
            response.sendRedirect(request.getContextPath() + "/FrontEnd/login.jsp");
            return;
        }

        Integer customerId = (Integer) session.getAttribute("customer_id");

        try {
            ApiClient.ApiResponse<String> apiResponse = ApiClient.get(
                    "/feedback?customerId=" + customerId, String.class);

            if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                JsonObject json = gson.fromJson(apiResponse.getData(), JsonObject.class);
                if (json.has("data")) {
                    JsonElement dataElement = json.get("data");
                    Type listType = new TypeToken<List<Map<String, Object>>>() {
                    }.getType();
                    if (dataElement.isJsonArray()) {
                        List<Map<String, Object>> feedbackList = gson.fromJson(dataElement, listType);
                        request.setAttribute("feedbackList", feedbackList);
                    } else {
                        request.setAttribute("feedbackList", new ArrayList<>());
                    }
                }
            } else {
                request.setAttribute("feedbackList", new ArrayList<>());
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("feedbackList", new ArrayList<>());
        }

        response.sendRedirect(request.getContextPath() + "/BookingServlet?action=list");
    }

    /**
     * Save feedback - via REST API
     */
    private void saveFeedback(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("customer_id") == null) {
            response.sendRedirect(request.getContextPath() + "/FrontEnd/login.jsp");
            return;
        }

        Integer customerId = (Integer) session.getAttribute("customer_id");

        String serviceIdStr = request.getParameter("service_id");
        if (serviceIdStr == null)
            serviceIdStr = request.getParameter("serviceId");

        String ratingStr = request.getParameter("rating");
        String comment = request.getParameter("comment");

        String bookingIdStr = request.getParameter("booking_id");
        if (bookingIdStr == null)
            bookingIdStr = request.getParameter("bookingId");

        // Build feedback request body
        Map<String, Object> feedbackData = new HashMap<>();
        feedbackData.put("customerId", customerId);
        if (serviceIdStr != null)
            feedbackData.put("serviceId", parseId(serviceIdStr));
        if (ratingStr != null)
            feedbackData.put("rating", Integer.parseInt(ratingStr));
        if (comment != null)
            feedbackData.put("comment", comment);
        if (bookingIdStr != null)
            feedbackData.put("bookingId", parseId(bookingIdStr));

        ApiClient.ApiResponse<String> apiResponse = ApiClient.post("/feedback", feedbackData, String.class);

        if (apiResponse.isSuccess()) {
            request.setAttribute("success", "Thank you for your feedback!");
        } else {
            request.setAttribute("error", "Failed to submit feedback. Please try again.");
        }

        // Redirect back to feedback form or booking details
        if (bookingIdStr != null) {
            response.sendRedirect(request.getContextPath() +
                    "/BookingServlet?action=details&id=" + bookingIdStr + "&feedbackSaved=true");
        } else {
            RequestDispatcher dispatcher = request.getRequestDispatcher("/FrontEnd/feedbackForm.jsp");
            dispatcher.forward(request, response);
        }
    }
}
