package com.silvercare.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.silvercare.dao.UserDAO;
import java.sql.SQLException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet for Login page - Uses JAX-RS Client to call REST API
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to login.jsp
        RequestDispatcher dispatcher = request.getRequestDispatcher("/FrontEnd/login.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Validate input
        if (email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Email and password are required");
            doGet(request, response);
            return;
        }

        // Create login request object
        Map<String, Object> loginRequest = new HashMap<>();
        loginRequest.put("email", email);
        loginRequest.put("password", password);

        // Call UserDAO for direct JDBC authentication (MVC Topic 6 compliance)
        UserDAO userDAO = new UserDAO();
        try {
            com.silvercare.model.User user = userDAO.authenticate(email, password);

            if (user != null) {
                Integer userId = user.getId();

                // Create session (Topic 4)
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                session.setAttribute("userId", userId);
                session.setAttribute("sessUserID", userId);
                session.setAttribute("username", user.getFullName());
                session.setAttribute("userRole", user.getRole());
                session.setAttribute("role", user.getRole());
                session.setAttribute("tutorial_completed", user.isTutorialCompleted());

                // Redirect based on role (Access Control Logic)
                String role = user.getRole();
                if ("admin".equalsIgnoreCase(role)) {
                    session.setAttribute("admin_id", userId);
                    session.setAttribute("admin_name", user.getFullName());
                    session.removeAttribute("customer_id");
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                } else {
                    session.setAttribute("customer_id", userId);
                    session.setAttribute("customer_name", user.getFullName());
                    session.setAttribute("profile_picture", user.getProfilePicture());
                    session.removeAttribute("admin_id");
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                }
            } else {
                request.setAttribute("error", "Invalid email or password");
                doGet(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Database error: " + e.getMessage());
            doGet(request, response);
        }
    }
}
