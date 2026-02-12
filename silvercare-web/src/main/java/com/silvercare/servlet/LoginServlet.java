package com.silvercare.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.silvercare.util.ApiClient;
import com.silvercare.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet for Login page - Uses ApiClient to call REST APIs
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Gson gson = ApiClient.getGson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to login page
        RequestDispatcher dispatcher = request.getRequestDispatcher("/FrontEnd/login.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Validate inputs
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Email and password are required");
            doGet(request, response);
            return;
        }

        // Call REST API via ApiClient
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", email.trim());
        credentials.put("password", password);

        ApiClient.ApiResponse<String> apiResponse = ApiClient.post("/users/login", credentials, String.class);

        if (apiResponse.isSuccess() && apiResponse.getData() != null) {
            try {
                JsonObject json = gson.fromJson(apiResponse.getData(), JsonObject.class);
                User user = gson.fromJson(json.getAsJsonObject("user"), User.class);

                if (user != null) {
                    // Set up session attributes
                    HttpSession session = request.getSession(true);
                    Integer userId = user.getId();

                    session.setAttribute("user", user);
                    session.setAttribute("userId", userId);
                    session.setAttribute("sessUserID", userId);
                    session.setAttribute("username", user.getFullName());
                    session.setAttribute("userRole", user.getRole());
                    session.setAttribute("role", user.getRole());
                    session.setAttribute("tutorial_completed", user.isTutorialCompleted());

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
                        response.sendRedirect(request.getContextPath() + "/home");
                    }
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Login failed
        request.setAttribute("error", "Invalid email or password");
        doGet(request, response);
    }
}
