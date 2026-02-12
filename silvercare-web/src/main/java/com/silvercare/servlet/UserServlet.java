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
 * Servlet for User operations - Uses ApiClient to call REST APIs
 * Handles login, registration, profile update, tutorial completion, and logout.
 */
@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Gson gson = ApiClient.getGson();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("logout".equals(action)) {
            handleLogout(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            action = "";
        }

        switch (action) {
            case "login":
                handleLogin(request, response);
                break;
            case "register":
                handleRegister(request, response);
                break;
            case "update":
                handleUpdate(request, response);
                break;
            case "completeTutorial":
                handleCompleteTutorial(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/home");
                break;
        }
    }

    /**
     * Handle Login via REST API
     */
    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("error", "Email and password are required");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/FrontEnd/login.jsp");
            dispatcher.forward(request, response);
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
        RequestDispatcher dispatcher = request.getRequestDispatcher("/FrontEnd/login.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Handle Registration via REST API
     */
    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String gender = request.getParameter("gender");

        // Validate required fields
        if (fullName == null || email == null || password == null ||
                fullName.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("error", "Full name, email, and password are required");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/FrontEnd/register.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Build registration request body as a Map (since User.password is transient
        // for Gson)
        Map<String, String> regData = new HashMap<>();
        regData.put("fullName", fullName.trim());
        regData.put("email", email.trim());
        regData.put("password", password);
        if (phone != null && !phone.trim().isEmpty())
            regData.put("phone", phone.trim());
        if (address != null && !address.trim().isEmpty())
            regData.put("address", address.trim());
        if (gender != null && !gender.trim().isEmpty())
            regData.put("gender", gender.trim());

        ApiClient.ApiResponse<String> apiResponse = ApiClient.post("/users/register", regData, String.class);

        if (apiResponse.isSuccess()) {
            // Registration successful, redirect to login
            response.sendRedirect(request.getContextPath() + "/FrontEnd/login.jsp?registered=true");
        } else {
            String errorMsg = "Registration failed";
            if (apiResponse.getError() != null) {
                try {
                    JsonObject errorJson = gson.fromJson(apiResponse.getError(), JsonObject.class);
                    if (errorJson.has("error")) {
                        errorMsg = errorJson.get("error").getAsString();
                    }
                } catch (Exception e) {
                    // Use default error message
                }
            }
            request.setAttribute("error", errorMsg);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/FrontEnd/register.jsp");
            dispatcher.forward(request, response);
        }
    }

    /**
     * Handle Profile Update via REST API
     */
    private void handleUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/FrontEnd/login.jsp");
            return;
        }

        User currentUser = (User) session.getAttribute("user");

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        // Build update request body
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("id", currentUser.getId());
        updateData.put("fullName", fullName != null ? fullName.trim() : currentUser.getFullName());
        updateData.put("email", email != null ? email.trim() : currentUser.getEmail());
        updateData.put("phone", phone != null ? phone.trim() : currentUser.getPhone());
        updateData.put("address", address != null ? address.trim() : currentUser.getAddress());

        ApiClient.ApiResponse<String> apiResponse = ApiClient.put("/users/profile", updateData, String.class);

        if (apiResponse.isSuccess()) {
            // Update session with new user data
            try {
                JsonObject json = gson.fromJson(apiResponse.getData(), JsonObject.class);
                if (json.has("user")) {
                    User updatedUser = gson.fromJson(json.getAsJsonObject("user"), User.class);
                    session.setAttribute("user", updatedUser);
                    session.setAttribute("username", updatedUser.getFullName());
                } else {
                    // Manually update local user object
                    currentUser.setFullName((String) updateData.get("fullName"));
                    currentUser.setEmail((String) updateData.get("email"));
                    currentUser.setPhone((String) updateData.get("phone"));
                    currentUser.setAddress((String) updateData.get("address"));
                    session.setAttribute("user", currentUser);
                    session.setAttribute("username", currentUser.getFullName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            request.setAttribute("success", "Profile updated successfully");
        } else {
            request.setAttribute("error", "Failed to update profile");
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/FrontEnd/profile.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Handle Tutorial Completion via REST API
     */
    private void handleCompleteTutorial(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/FrontEnd/login.jsp");
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            userId = (Integer) session.getAttribute("customer_id");
        }

        if (userId != null) {
            Map<String, Integer> requestBody = new HashMap<>();
            requestBody.put("userId", userId);

            ApiClient.ApiResponse<String> apiResponse = ApiClient.post("/users/tutorial/complete", requestBody,
                    String.class);

            if (apiResponse.isSuccess()) {
                session.setAttribute("tutorial_completed", true);
            }
        }

        response.sendRedirect(request.getContextPath() + "/home");
    }

    /**
     * Handle Logout (session invalidation only - no API call needed)
     */
    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/home");
    }
}
