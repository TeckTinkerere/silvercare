package com.silvercare.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.silvercare.util.ApiClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet for Contact page - Uses ApiClient to call REST APIs
 */
@WebServlet("/contact")
public class ContactServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("success".equals(action)) {
            request.getRequestDispatcher("/FrontEnd/contactSuccess.jsp").forward(request, response);
            return;
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/FrontEnd/contact.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone"); // Added phone
        String subject = request.getParameter("subject");
        String message = request.getParameter("message");

        // Build request body
        Map<String, String> contactData = new HashMap<>();
        contactData.put("fullName", name); // Changed from "name" to "fullName"
        contactData.put("email", email);
        contactData.put("phone", phone); // Added phone
        contactData.put("subject", subject);
        contactData.put("message", message);

        // Call REST API via ApiClient
        ApiClient.ApiResponse<String> apiResponse = ApiClient.post("/contact", contactData, String.class);

        if (apiResponse.isSuccess()) {
            // Redirect to success page to prevent re-submission on refresh
            response.sendRedirect(request.getContextPath() + "/contact?action=success");
        } else {
            request.setAttribute("error", "Failed to send your message: " + apiResponse.getError());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/FrontEnd/contact.jsp");
            dispatcher.forward(request, response);
        }
    }
}
