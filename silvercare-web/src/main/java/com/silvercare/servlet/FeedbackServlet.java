package com.silvercare.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Traditional Servlet for Feedback operations - Uses JAX-RS Client to call REST
 * APIs
 */
@WebServlet("/FeedbackServlet")
public class FeedbackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("form".equals(action)) {
            showFeedbackForm(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("save".equals(action)) {
            saveFeedback(request, response);
        } else {
            doGet(request, response);
        }
    }

    private void showFeedbackForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer customerId = (Integer) session.getAttribute("customer_id");

        if (customerId == null) {
            response.sendRedirect(request.getContextPath() + "/FrontEnd/login.jsp");
            return;
        }

        String serviceIdParam = request.getParameter("service_id");
        String bookingIdParam = request.getParameter("booking_id");

        if (serviceIdParam == null || bookingIdParam == null) {
            response.sendRedirect(request.getContextPath() + "/FrontEnd/bookings.jsp?error=missing_params");
            return;
        }

        int serviceId = Integer.parseInt(serviceIdParam);
        int bookingId = Integer.parseInt(bookingIdParam);

        try {
            // Get booking details via local DAO
            com.silvercare.dao.BookingDAO bookingDAO = new com.silvercare.dao.BookingDAO();
            Map<String, Object> bookingData = bookingDAO.getBookingById(bookingId);

            if (bookingData == null) {
                response.sendRedirect(request.getContextPath() + "/FrontEnd/bookings.jsp?error=unauthorized_booking");
                return;
            }

            Integer bookingCustomerId = (Integer) bookingData.get("customerId");

            // Verify booking belongs to customer
            if (bookingCustomerId == null || !bookingCustomerId.equals(customerId)) {
                response.sendRedirect(
                        request.getContextPath() + "/BookingServlet?action=list&error=unauthorized_booking");
                return;
            }

            // Get Service Details via local DAO
            com.silvercare.dao.ServiceDAO serviceDAO = new com.silvercare.dao.ServiceDAO();
            com.silvercare.model.Service service = serviceDAO.getServiceById(serviceId);

            if (service == null) {
                response.sendRedirect(request.getContextPath() + "/FrontEnd/bookings.jsp?error=service_not_found");
                return;
            }

            // Check for existing feedback via local DAO
            com.silvercare.dao.FeedbackDAO feedbackDAO = new com.silvercare.dao.FeedbackDAO();
            Map<String, Object> feedbackData = feedbackDAO.checkExistingFeedback(customerId, serviceId);

            boolean isEditMode = false;
            Integer existingRating = null;
            String existingComment = null;

            if (feedbackData != null) {
                isEditMode = true;
                existingRating = (Integer) feedbackData.get("rating");
                existingComment = (String) feedbackData.get("comment");
            }

            request.setAttribute("serviceId", serviceId);
            request.setAttribute("bookingId", bookingId);
            request.setAttribute("serviceName", service.getName());

            // Handle timestamp robustly
            Object bDate = bookingData.get("bookingDate");
            request.setAttribute("bookingDate", bDate);
            request.setAttribute("bookingStatus", bookingData.get("status"));
            request.setAttribute("isEditMode", isEditMode);

            if (isEditMode) {
                request.setAttribute("existingRating", existingRating);
                request.setAttribute("existingComment", existingComment);
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("/FrontEnd/feedbackForm.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = e.getMessage() != null ? e.getMessage() : "internal_error";
            response.sendRedirect(request.getContextPath() + "/BookingServlet?action=list&error=database_error&msg="
                    + java.net.URLEncoder.encode(errorMsg, "UTF-8"));
        }
    }

    private void saveFeedback(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer customerId = (Integer) session.getAttribute("customer_id");

        if (customerId == null) {
            response.sendRedirect(request.getContextPath() + "/FrontEnd/login.jsp");
            return;
        }

        int serviceId = Integer.parseInt(request.getParameter("service_id"));
        int bookingId = Integer.parseInt(request.getParameter("booking_id"));
        String ratingParam = request.getParameter("rating");
        Integer rating = (ratingParam != null && !ratingParam.isEmpty()) ? Integer.parseInt(ratingParam) : null;
        String comment = request.getParameter("comment");

        try {
            com.silvercare.dao.FeedbackDAO feedbackDAO = new com.silvercare.dao.FeedbackDAO();

            // Check for existing feedback via local DAO
            Map<String, Object> existingFeedback = feedbackDAO.checkExistingFeedback(customerId, serviceId);
            boolean isUpdate = existingFeedback != null;

            if (!isUpdate && rating == null) {
                response.sendRedirect(
                        request.getContextPath() + "/FeedbackServlet?action=form&service_id=" + serviceId +
                                "&booking_id=" + bookingId + "&error=missing_rating");
                return;
            }

            // Prepare feedback data
            Map<String, Object> feedbackData = new HashMap<>();
            feedbackData.put("customerId", customerId);
            feedbackData.put("serviceId", serviceId);
            feedbackData.put("comment", comment);

            // Include rating (UI ensures it is present even in edit mode via hidden field)
            if (rating != null) {
                feedbackData.put("rating", rating);
            }

            // Save/update feedback via local DAO
            feedbackDAO.saveFeedback(feedbackData);
            response.sendRedirect(request.getContextPath() + "/FrontEnd/feedbackSuccess.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/BookingServlet?action=list&error=internal_error");
        }
    }
}
