package com.silvercare.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.silvercare.dao.ContactDAO;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet for Contact page - Uses local ContactDAO
 */
@WebServlet("/ContactServlet")
public class ContactServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to contact.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("/FrontEnd/contact.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String subject = request.getParameter("subject");
        String message = request.getParameter("message");

        try {
            Map<String, Object> contactData = new HashMap<>();
            contactData.put("name", name);
            contactData.put("email", email);
            contactData.put("subject", subject);
            contactData.put("message", message);

            ContactDAO contactDAO = new ContactDAO();
            contactDAO.saveContact(contactData);

            response.sendRedirect(request.getContextPath() + "/FrontEnd/contact.jsp?success=message_sent");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Failed to send message. Please try again.");
            doGet(request, response);
        }
    }
}
