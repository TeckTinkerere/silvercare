package com.silvercare.servlet;

import com.silvercare.dao.BookingDAO;
import com.silvercare.dao.FeedbackDAO;
import com.silvercare.dao.ServiceDAO;
import com.silvercare.dao.UserDAO;
import com.silvercare.dao.AuditLogDAO;
import com.silvercare.model.Service;
import com.silvercare.model.Booking;
import com.silvercare.model.User;
import com.silvercare.model.Feedback;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.List;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import com.silvercare.util.FileService;

/**
 * Servlet for Admin Dashboard and admin page routing.
 * Refactored to use local JDBC DAOs to satisfy MVC requirements.
 */
@WebServlet({ "/admin/dashboard", "/admin/services", "/admin/categories", "/admin/bookings", "/admin/feedback",
        "/admin/add-user", "/admin/edit-user", "/admin/save-user", "/admin/delete-user",
        "/admin/reset-password", "/admin/user-details-json", "/admin/booking-details", "/admin/export-bookings",
        "/admin/export-users", "/admin/export-feedback", "/admin/logs" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 25 * 1024 * 1024)
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check if user is admin (Access Control)
        HttpSession session = request.getSession();
        Integer adminId = (Integer) session.getAttribute("admin_id");

        if (adminId == null) {
            response.sendRedirect(request.getContextPath() + "/FrontEnd/login.jsp?error=unauthorized");
            return;
        }

        String path = request.getServletPath();
        String jspPath = null;

        try {
            switch (path) {
                case "/admin/dashboard":
                    loadDashboardStats(request);
                    jspPath = "/FrontEnd/admin/dashboard.jsp";
                    break;
                case "/admin/services":
                    loadServicesData(request);
                    jspPath = "/FrontEnd/admin/manageServices.jsp";
                    break;
                case "/admin/categories":
                    loadCategoriesData(request);
                    jspPath = "/FrontEnd/admin/manageServiceCategories.jsp";
                    break;
                case "/admin/bookings":
                    loadBookingsData(request);
                    jspPath = "/FrontEnd/admin/viewBookings.jsp";
                    break;
                case "/admin/booking-details":
                    loadBookingDetails(request);
                    jspPath = "/FrontEnd/admin/bookingDetails.jsp";
                    break;
                case "/admin/feedback":
                    loadFeedbackData(request);
                    jspPath = "/FrontEnd/admin/viewFeedback.jsp";
                    break;
                case "/admin/users":
                    loadUsersData(request);
                    jspPath = "/FrontEnd/admin/manageUsers.jsp";
                    break;
                case "/admin/add-service":
                    loadCategoriesData(request);
                    jspPath = "/FrontEnd/admin/addService.jsp";
                    break;
                case "/admin/edit-service":
                    loadServiceForEdit(request);
                    loadCategoriesData(request);
                    jspPath = "/FrontEnd/admin/editService.jsp";
                    break;
                case "/admin/delete-service":
                    handleDeleteService(request, response);
                    return;
                case "/admin/save-service":
                    handleSaveService(request, response);
                    return;
                case "/admin/save-category":
                    handleSaveCategory(request, response);
                    return;
                case "/admin/delete-category":
                    handleDeleteCategory(request, response);
                    return;
                case "/admin/add-user":
                    jspPath = "/FrontEnd/admin/addUser.jsp";
                    break;
                case "/admin/edit-user":
                    loadUserForEdit(request);
                    jspPath = "/FrontEnd/admin/editUser.jsp";
                    break;
                case "/admin/delete-user":
                    handleDeleteUser(request, response);
                    return;
                case "/admin/save-user":
                    handleSaveUser(request, response);
                    return;
                case "/admin/reset-password":
                    handleResetPassword(request, response);
                    return;
                case "/admin/user-details-json":
                    handleGetUserDetailsJson(request, response);
                    return;
                case "/admin/export-bookings":
                    handleExportBookings(request, response);
                    return;
                case "/admin/export-users":
                    handleExportUsers(request, response);
                    return;
                case "/admin/export-feedback":
                    handleExportFeedback(request, response);
                    return;
                case "/admin/logs":
                    loadLogsData(request);
                    jspPath = "/FrontEnd/admin/viewLogs.jsp";
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                    return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "System Error: " + e.getMessage());
            jspPath = "/FrontEnd/admin/dashboard.jsp";
        }

        // Forward to the appropriate JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher(jspPath);
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        if ("/admin/save-service".equals(path)) {
            handleSaveService(request, response);
        } else if ("/admin/save-category".equals(path)) {
            handleSaveCategory(request, response);
        } else if ("/admin/save-user".equals(path)) {
            handleSaveUser(request, response);
        } else if ("/admin/reset-password".equals(path)) {
            handleResetPassword(request, response);
        } else {
            doGet(request, response);
        }
    }

    /**
     * Handle saving (creating or updating) a service
     */
    private void handleSaveCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("category_name");
        String description = request.getParameter("description");
        String icon = request.getParameter("icon");

        try {
            ServiceDAO dao = new ServiceDAO();
            dao.addCategory(name, description, icon);

            // Log interaction
            Integer adminId = (Integer) request.getSession().getAttribute("admin_id");
            new AuditLogDAO().logAction(adminId, "Create Category", "Name: " + name);

            response.sendRedirect(request.getContextPath() + "/admin/categories?status=success");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/categories?status=error");
        }
    }

    private void handleDeleteCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        if (id != null) {
            try {
                ServiceDAO dao = new ServiceDAO();
                dao.deleteCategory(Integer.parseInt(id));

                // Log interaction
                Integer adminId = (Integer) request.getSession().getAttribute("admin_id");
                new AuditLogDAO().logAction(adminId, "Delete Category", "ID: " + id);

                response.sendRedirect(request.getContextPath() + "/admin/categories?status=deleted");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/admin/categories?status=error");
            }
        }
    }

    private void handleSaveService(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String idStr = request.getParameter("id");
            String name = request.getParameter("service_name");
            String description = request.getParameter("description");
            String priceStr = request.getParameter("price");
            int categoryId = Integer.parseInt(request.getParameter("category_id"));
            boolean active = request.getParameter("active") != null;

            Part filePart = request.getPart("image_file");
            String imagePath = FileService.uploadImage(filePart, getServletContext().getRealPath("/"));

            ServiceDAO dao = new ServiceDAO();
            Service service = new Service();
            service.setName(name);
            service.setDescription(description);
            service.setPrice(new BigDecimal(priceStr));
            service.setCategoryId(categoryId);
            service.setActive(active);

            if (idStr != null && !idStr.isEmpty()) {
                int id = Integer.parseInt(idStr);
                service.setId(id);
                // Preserve old image if new one not uploaded
                if (imagePath == null) {
                    Service old = dao.getServiceById(id);
                    service.setImagePath(old.getImagePath());
                } else {
                    service.setImagePath(imagePath);
                }
                dao.updateService(service);
            } else {
                service.setImagePath(imagePath != null ? imagePath : "images/default-service.jpg");
                dao.addService(service);
            }

            // Log interaction
            Integer adminId = (Integer) request.getSession().getAttribute("admin_id");
            String action = (idStr != null && !idStr.isEmpty()) ? "Update Service" : "Create Service";
            new AuditLogDAO().logAction(adminId, action, "Service: " + name);

            response.sendRedirect(request.getContextPath() + "/admin/services?status=success");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/services?status=error&msg=" + e.getMessage());
        }
    }

    private void handleDeleteService(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        if (id != null) {
            try {
                ServiceDAO dao = new ServiceDAO();
                dao.deleteService(Integer.parseInt(id));

                // Log interaction
                Integer adminId = (Integer) request.getSession().getAttribute("admin_id");
                new AuditLogDAO().logAction(adminId, "Delete Service", "ID: " + id);

                response.sendRedirect(request.getContextPath() + "/admin/services?status=deleted");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/admin/services?status=error");
            }
        }
    }

    private void loadServicesData(HttpServletRequest request) throws Exception {
        ServiceDAO dao = new ServiceDAO();
        request.setAttribute("services", dao.getAllServices());
    }

    private void loadDashboardStats(HttpServletRequest request) throws Exception {
        BookingDAO bookingDAO = new BookingDAO();
        Map<String, Object> stats = bookingDAO.getDashboardStats();

        request.setAttribute("revenue", stats.get("revenue"));
        request.setAttribute("bookingCount", stats.get("bookings"));
        request.setAttribute("serviceCount", stats.get("services"));
        request.setAttribute("clientCount", stats.get("clients"));

        // Add monthly revenue for charts
        request.setAttribute("monthlyRevenue", bookingDAO.getMonthlyRevenue());
        request.setAttribute("topServices", bookingDAO.getTopServices());

        // Top clients report (by total spend)
        request.setAttribute("topClients", bookingDAO.getTopClients());

        // Service ratings report (best and lowest rated)
        FeedbackDAO feedbackDAO = new FeedbackDAO();
        request.setAttribute("serviceRatings", feedbackDAO.getServiceRatings());
    }

    private void loadCategoriesData(HttpServletRequest request) throws Exception {
        ServiceDAO dao = new ServiceDAO();
        request.setAttribute("categories", dao.getAllCategories());
    }

    private void loadBookingsData(HttpServletRequest request) throws Exception {
        BookingDAO bookingDAO = new BookingDAO();
        request.setAttribute("bookings", bookingDAO.getAllBookings());
    }

    private void handleExportBookings(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            BookingDAO bookingDAO = new BookingDAO();
            List<Booking> bookings = bookingDAO.getAllBookings();

            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=silvercare_bookings_export.csv");

            java.io.PrintWriter writer = response.getWriter();
            // CSV Header
            writer.println("Booking ID,Customer Name,Booking Date,Status,Total Amount,GST Amount");

            for (com.silvercare.model.Booking b : bookings) {
                StringBuilder sb = new StringBuilder();
                sb.append(b.getId()).append(",");
                sb.append("\"").append(b.getCustomerName()).append("\",");
                sb.append(b.getBookingDate()).append(",");
                sb.append(b.getStatus()).append(",");
                sb.append(b.getTotalAmount()).append(",");
                sb.append(b.getGstAmount());
                writer.println(sb.toString());
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to export bookings");
        }
    }

    private void handleExportUsers(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            UserDAO userDAO = new UserDAO();
            List<User> users = userDAO.getAllCustomers();

            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=silvercare_users_export.csv");

            java.io.PrintWriter writer = response.getWriter();
            writer.println("User ID,Full Name,Email,Phone,Address,Gender,Role,Tutorial Completed");

            for (com.silvercare.model.User u : users) {
                StringBuilder sb = new StringBuilder();
                sb.append(u.getId()).append(",");
                sb.append("\"").append(u.getFullName()).append("\",");
                sb.append("\"").append(u.getEmail()).append("\",");
                sb.append("\"").append(u.getPhone() != null ? u.getPhone() : "").append("\",");
                sb.append("\"").append(u.getAddress() != null ? u.getAddress() : "").append("\",");
                sb.append(u.getGender()).append(",");
                sb.append(u.getRole()).append(",");
                sb.append(u.isTutorialCompleted());
                writer.println(sb.toString());
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to export users");
        }
    }

    private void handleExportFeedback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            FeedbackDAO feedbackDAO = new FeedbackDAO();
            List<Feedback> feedbackList = feedbackDAO.getAllFeedback();

            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=silvercare_feedback_export.csv");

            java.io.PrintWriter writer = response.getWriter();
            writer.println("Feedback ID,Customer ID,Service ID,Rating,Comments,Created At");

            for (com.silvercare.model.Feedback f : feedbackList) {
                StringBuilder sb = new StringBuilder();
                sb.append(f.getFeedbackId()).append(",");
                sb.append(f.getCustomerId()).append(",");
                sb.append(f.getServiceId()).append(",");
                sb.append(f.getRating()).append(",");
                sb.append("\"").append(f.getComment() != null ? f.getComment().replace("\"", "\"\"") : "")
                        .append("\",");
                sb.append(f.getCreatedAt());
                writer.println(sb.toString());
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to export feedback");
        }
    }

    private void loadFeedbackData(HttpServletRequest request) throws Exception {
        FeedbackDAO feedbackDAO = new FeedbackDAO();
        request.setAttribute("serviceRatings", feedbackDAO.getServiceRatings());
    }

    private void loadServiceForEdit(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        if (id != null) {
            ServiceDAO dao = new ServiceDAO();
            request.setAttribute("service", dao.getServiceById(Integer.parseInt(id)));
        }
    }

    private void loadUsersData(HttpServletRequest request) throws Exception {
        UserDAO userDAO = new UserDAO();
        String areaFilter = request.getParameter("area");
        if (areaFilter != null && !areaFilter.trim().isEmpty()) {
            request.setAttribute("users", userDAO.getUsersByArea(areaFilter.trim()));
            request.setAttribute("areaFilter", areaFilter.trim());
        } else {
            request.setAttribute("users", userDAO.getAllUsers());
        }
    }

    private void loadUserForEdit(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        if (id != null) {
            UserDAO userDAO = new UserDAO();
            request.setAttribute("editUser", userDAO.getUserById(Integer.parseInt(id)));
        }
    }

    private void handleSaveUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            UserDAO userDAO = new UserDAO();
            String idStr = request.getParameter("id");
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");

            if (idStr != null && !idStr.isEmpty()) {
                com.silvercare.model.User user = new com.silvercare.model.User();
                user.setId(Integer.parseInt(idStr));
                user.setFullName(fullName);
                user.setEmail(email);
                user.setPhone(phone);
                user.setAddress(address);
                userDAO.updateProfile(user);
            } else {
                com.silvercare.model.User user = new com.silvercare.model.User();
                user.setFullName(fullName);
                user.setEmail(email);
                user.setPhone(phone);
                user.setAddress(address);
                String password = request.getParameter("password");
                userDAO.register(user, password);
            }

            // Log interaction
            Integer adminId = (Integer) request.getSession().getAttribute("admin_id");
            String logAction = (idStr != null && !idStr.isEmpty()) ? "Update User" : "Create User";
            new AuditLogDAO().logAction(adminId, logAction,
                    "User Email: " + email);

            response.sendRedirect(request.getContextPath() + "/admin/users?status=success");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/users?status=error");
        }
    }

    private void handleDeleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        if (id != null) {
            try {
                UserDAO userDAO = new UserDAO();
                userDAO.deleteUser(Integer.parseInt(id));

                // Log interaction
                Integer adminId = (Integer) request.getSession().getAttribute("admin_id");
                new AuditLogDAO().logAction(adminId, "Delete User", "ID: " + id);

                response.sendRedirect(request.getContextPath() + "/admin/users?status=deleted");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/admin/users?status=error");
            }
        }
    }

    private void handleResetPassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(request.getContextPath() + "/admin/users?status=reset_success");
    }

    private void handleGetUserDetailsJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        if (id != null) {
            try {
                UserDAO userDAO = new UserDAO();
                com.silvercare.model.User user = userDAO.getUserById(Integer.parseInt(id));
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                // Simple JSON serialization
                StringBuilder json = new StringBuilder("{");
                if (user != null) {
                    json.append("\"id\":").append(user.getId()).append(",");
                    json.append("\"fullName\":\"").append(user.getFullName()).append("\",");
                    json.append("\"email\":\"").append(user.getEmail()).append("\",");
                    json.append("\"phone\":\"").append(user.getPhone() != null ? user.getPhone() : "")
                            .append("\",");
                    json.append("\"address\":\"").append(user.getAddress() != null ? user.getAddress() : "")
                            .append("\",");
                    json.append("\"role\":\"").append(user.getRole()).append("\"");
                }
                json.append("}");
                response.getWriter().write(json.toString());
            } catch (Exception e) {
                response.setStatus(500);
                response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
            }
        }
    }

    private void loadBookingDetails(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        if (id != null) {
            BookingDAO dao = new BookingDAO();
            request.setAttribute("booking", dao.getBookingById(Integer.parseInt(id)));
        }
    }

    private void loadLogsData(HttpServletRequest request) throws Exception {
        com.silvercare.dao.AuditLogDAO logDAO = new com.silvercare.dao.AuditLogDAO();
        request.setAttribute("logs", logDAO.getAllLogs());
    }
}
