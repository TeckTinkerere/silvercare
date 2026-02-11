package com.silvercare.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.silvercare.dao.UserDAO;

import java.io.IOException;

/**
 * Traditional Servlet for User operations - Uses local UserDAO with JDBC
 * Refactored to comply with MVC requirements (Topic 6)
 */
@WebServlet({ "/UserServlet", "/logout", "/user/tutorial/complete" })
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("logout".equals(action) || "/logout".equals(request.getServletPath())) {
            handleLogout(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String servletPath = request.getServletPath();

        if (action == null && !"/user/tutorial/complete".equals(servletPath)) {
            response.sendRedirect(request.getContextPath() + "/FrontEnd/home.jsp");
            return;
        }

        if ("/user/tutorial/complete".equals(servletPath)) {
            handleCompleteTutorial(request, response);
            return;
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
     * Handle user login
     */
    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            UserDAO userDAO = new UserDAO();
            com.silvercare.model.User user = userDAO.authenticate(email, password);

            if (user != null) {
                // Set session attributes
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("user_id", user.getId());
                session.setAttribute("customer_id", user.getId()); // Valid for customers, removed for admin below
                session.setAttribute("cart_count", 0); // Initialize cart count

                String role = user.getRole();
                session.setAttribute("role", role); // Set role for LoginFilter

                if ("admin".equalsIgnoreCase(role)) {
                    session.setAttribute("admin_id", user.getId());
                    // Cleanup customer attributes for admin purity if desired, but keeping session
                    // flexible
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                    return;
                }

                boolean tutorialCompleted = user.isTutorialCompleted();
                // Explicitly set the session attribute expected by HomeServlet
                session.setAttribute("tutorial_completed", tutorialCompleted);

                // Redirect to home or tutorial (Customer flow)
                if (!tutorialCompleted) {
                    response.sendRedirect(request.getContextPath() + "/FrontEnd/tutorial.jsp");
                } else {
                    response.sendRedirect(request.getContextPath() + "/home");
                }
            } else {
                // Login failed - redirect back to login page with error
                // Use a proper error parameter
                response.sendRedirect(request.getContextPath()
                        + "/FrontEnd/login.jsp?error=invalid");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath()
                    + "/FrontEnd/login.jsp?error=system_error");
        }
    }

    /**
     * Handle user registration
     */
    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullname");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        try {
            com.silvercare.model.User user = new com.silvercare.model.User();
            user.setEmail(email);
            user.setFullName(fullName);
            user.setPhone(phone);
            user.setAddress(address);

            UserDAO userDAO = new UserDAO();
            userDAO.register(user, password);

            // Registration successful - redirect to login page
            response.sendRedirect(request.getContextPath()
                    + "/FrontEnd/login.jsp?success=registered");
        } catch (Exception e) {
            e.printStackTrace();
            // Registration failed - redirect back to registration page with error
            response.sendRedirect(request.getContextPath()
                    + "/FrontEnd/register.jsp?error=registration_failed&message="
                    + java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }

    /**
     * Handle profile update
     */
    private void handleUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        com.silvercare.model.User user = (com.silvercare.model.User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String fullName = request.getParameter("full_name");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String gender = request.getParameter("gender");
        String medicalInfo = request.getParameter("medical_info");

        try {
            user.setFullName(fullName);
            user.setPhone(phone);
            user.setAddress(address);
            user.setGender(gender);
            user.setMedicalInfo(medicalInfo);

            UserDAO userDAO = new UserDAO();
            userDAO.updateProfile(user);

            // Session object is updated by reference, but re-setting just in case
            session.setAttribute("user", user);

            response.sendRedirect(request.getContextPath() + "/FrontEnd/profile.jsp?update=success");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/FrontEnd/profile.jsp?error=update_failed");
        }
    }

    /**
     * Handle tutorial completion
     */
    private void handleCompleteTutorial(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        com.silvercare.model.User user = (com.silvercare.model.User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            UserDAO userDAO = new UserDAO();
            userDAO.updateTutorialStatus(user.getId(), true);

            // Update session to mark tutorial as completed
            user.setTutorialCompleted(true);
            session.setAttribute("user", user);
            // Explicitly update the session attribute expected by HomeServlet
            session.setAttribute("tutorial_completed", true);

            // Redirect to home page instead of returning JSON
            response.sendRedirect(request.getContextPath() + "/home");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/FrontEnd/tutorial.jsp?error=true");
        }
    }

    /**
     * Handle user logout
     */
    private void handleLogout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.invalidate();
        response.sendRedirect(request.getContextPath() + "/login?logout=success");
    }
}
