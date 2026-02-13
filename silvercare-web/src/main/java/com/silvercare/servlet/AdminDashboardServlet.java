package com.silvercare.servlet;

import com.silvercare.util.ApiClient;
import com.silvercare.model.User;
import com.silvercare.model.Service;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import com.silvercare.util.FileService;

/**
 * Servlet for Admin Dashboard and admin page routing.
 * Refactored to use ApiClient REST calls to the silvercare-api controllers.
 */
@WebServlet({ "/admin/dashboard", "/admin/services", "/admin/categories", "/admin/bookings", "/admin/feedback",
        "/admin/users", "/admin/add-user", "/admin/edit-user", "/admin/save-user", "/admin/delete-user",
        "/admin/reset-password", "/admin/user-details-json", "/admin/booking-details", "/admin/export-bookings",
        "/admin/export-users", "/admin/export-feedback", "/admin/logs",
        "/admin/add-service", "/admin/edit-service", "/admin/delete-service", "/admin/save-service",
        "/admin/save-category", "/admin/delete-category" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 25 * 1024 * 1024)
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Gson gson = ApiClient.getGson();

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
            System.err.println("ADMIN DASHBOARD ERROR: " + e.getMessage());
            request.setAttribute("error", "System Error: " + e.getMessage() + ". Please check server logs.");
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
        } else if ("/admin/delete-user".equals(path)) {
            handleDeleteUser(request, response);
        } else if ("/admin/delete-service".equals(path)) {
            handleDeleteService(request, response);
        } else if ("/admin/delete-category".equals(path)) {
            handleDeleteCategory(request, response);
        } else {
            doGet(request, response);
        }
    }

    // ========== Helper: Log admin action via REST API ==========
    private void logAdminAction(Integer adminId, String action, String details) {
        try {
            Map<String, Object> logData = new HashMap<>();
            logData.put("adminId", adminId);
            logData.put("action", action);
            logData.put("details", details);
            ApiClient.post("/admin/logs", logData, String.class);
        } catch (Exception e) {
            e.printStackTrace(); // Non-critical, don't block main flow
        }
    }
    
    // ========== Helper: Parse seasonal multiplier with validation ==========
    private java.math.BigDecimal parseMultiplier(String multiplierStr) {
        try {
            if (multiplierStr == null || multiplierStr.trim().isEmpty()) {
                return java.math.BigDecimal.ONE;
            }
            java.math.BigDecimal value = new java.math.BigDecimal(multiplierStr);
            // Validate range 0.1 to 10.0
            if (value.compareTo(new java.math.BigDecimal("0.1")) < 0 || 
                value.compareTo(new java.math.BigDecimal("10.0")) > 0) {
                return java.math.BigDecimal.ONE;
            }
            return value;
        } catch (Exception e) {
            return java.math.BigDecimal.ONE;
        }
    }

    // ========== Category Operations ==========

    private void handleSaveCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idStr = request.getParameter("category_id");
        String name = request.getParameter("category_name");
        String description = request.getParameter("description");
        String icon = request.getParameter("icon");

        try {
            Integer adminId = (Integer) request.getSession().getAttribute("admin_id");
            Map<String, String> categoryData = new HashMap<>();
            categoryData.put("name", name);
            categoryData.put("description", description);
            categoryData.put("icon", icon);

            ApiClient.ApiResponse<String> apiResponse;

            if (idStr != null && !idStr.isEmpty()) {
                // Update existing category — handle decimal IDs (e.g. "3.0")
                int catId = (int) Double.parseDouble(idStr);
                apiResponse = ApiClient.put(
                        "/admin/services/categories/" + catId, categoryData, String.class, adminId);
            } else {
                // Create new category
                apiResponse = ApiClient.post(
                        "/admin/services/categories", categoryData, String.class, adminId);
            }

            if (apiResponse.isSuccess()) {
                String action = (idStr != null && !idStr.isEmpty()) ? "Update Category" : "Create Category";
                logAdminAction(adminId, action, "Name: " + name);
                response.sendRedirect(request.getContextPath() + "/admin/categories?status=success");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/categories?status=error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/categories?status=error");
        }
    }

    private void handleDeleteCategory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        if (id != null) {
            try {
                // Handle decimal IDs (e.g. "3.0")
                int catId = (int) Double.parseDouble(id);
                Integer adminId = (Integer) request.getSession().getAttribute("admin_id");
                ApiClient.ApiResponse<String> apiResponse = ApiClient.delete(
                        "/admin/services/categories/" + catId, String.class, adminId);

                if (apiResponse.isSuccess()) {
                    logAdminAction(adminId, "Delete Category", "ID: " + id);
                    response.sendRedirect(request.getContextPath() + "/admin/categories?status=deleted");
                } else {
                    response.sendRedirect(request.getContextPath() + "/admin/categories?status=error");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/admin/categories?status=error");
            }
        }
    }

    // ========== Service Operations ==========

    private void handleSaveService(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Integer adminId = (Integer) request.getSession().getAttribute("admin_id");
            String idStr = request.getParameter("id");
            // Handle decimal IDs (e.g. "5.0")
            if (idStr != null && !idStr.isEmpty()) {
                try {
                    idStr = String.valueOf((int) Double.parseDouble(idStr));
                    System.out.println("AdminDashboard: Parsed ID " + request.getParameter("id") + " to " + idStr);
                } catch (NumberFormatException ignored) {
                }
            }
            String name = request.getParameter("service_name");
            String description = request.getParameter("description");
            String priceStr = request.getParameter("price");
            int categoryId = (int) Double.parseDouble(request.getParameter("category_id"));

            System.out.println("=== SAVE SERVICE DEBUG ===");
            System.out.println("ID: " + idStr);
            System.out.println("Name: " + name);
            System.out.println("Description: " + description);
            System.out.println("Price: " + priceStr);
            System.out.println("Category ID: " + categoryId);
            System.out.println("Is Update: " + (idStr != null && !idStr.isEmpty()));

            // Try file upload first, fall back to text-based image_url
            String imagePath = null;
            try {
                Part filePart = request.getPart("image_file");
                imagePath = FileService.uploadImage(filePart, getServletContext().getRealPath("/"));
            } catch (Exception e) {
                // Not a multipart request — ignore
            }

            // Fall back to text-based image URL if no file was uploaded
            if (imagePath == null) {
                String imageUrl = request.getParameter("image_url");
                if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                    imagePath = imageUrl.trim();
                }
            }

            // Parse seasonal multipliers with default value 1.0
            String springMultStr = request.getParameter("spring_multiplier");
            String summerMultStr = request.getParameter("summer_multiplier");
            String autumnMultStr = request.getParameter("autumn_multiplier");
            String winterMultStr = request.getParameter("winter_multiplier");
            
            java.math.BigDecimal springMult = parseMultiplier(springMultStr);
            java.math.BigDecimal summerMult = parseMultiplier(summerMultStr);
            java.math.BigDecimal autumnMult = parseMultiplier(autumnMultStr);
            java.math.BigDecimal winterMult = parseMultiplier(winterMultStr);

            Map<String, Object> serviceData = new HashMap<>();
            serviceData.put("name", name);
            serviceData.put("description", description);
            // Parse price explicitly to avoid string issues
            try {
                serviceData.put("price", new java.math.BigDecimal(priceStr));
            } catch (Exception e) {
                // Fallback or default
                serviceData.put("price", 0.0);
            }
            serviceData.put("categoryId", categoryId);
            serviceData.put("springMultiplier", springMult);
            serviceData.put("summerMultiplier", summerMult);
            serviceData.put("autumnMultiplier", autumnMult);
            serviceData.put("winterMultiplier", winterMult);

            if (idStr != null && !idStr.isEmpty()) {
                int serviceId = Integer.parseInt(idStr);
                serviceData.put("id", serviceId);

                // If no new image, preserve old one by fetching it
                if (imagePath == null) {
                    ApiClient.ApiResponse<String> oldSvcResp = ApiClient.get(
                            "/services/" + serviceId, String.class);
                    if (oldSvcResp.isSuccess() && oldSvcResp.getData() != null) {
                        JsonObject json = gson.fromJson(oldSvcResp.getData(), JsonObject.class);
                        JsonObject data = json.getAsJsonObject("data");
                        if (data != null && data.has("imagePath")) {
                            serviceData.put("imagePath", data.get("imagePath").getAsString());
                        }
                    }
                } else {
                    serviceData.put("imagePath", imagePath);
                }

                // Update service via REST API
                System.out.println("Calling PUT /admin/services/" + serviceId);
                ApiClient.ApiResponse<String> updateResp = ApiClient.put("/admin/services/" + serviceId, serviceData,
                        String.class, adminId);
                System.out.println("Update response: " + updateResp.isSuccess() + " - " + updateResp.getData());
            } else {
                serviceData.put("imagePath", imagePath != null ? imagePath : "images/default-service.jpg");
                // Create service via REST API
                System.out.println("Calling POST /admin/services with data: " + serviceData);
                ApiClient.ApiResponse<String> createResp = ApiClient.post("/admin/services", serviceData, String.class,
                        adminId);
                System.out.println("Create response: " + createResp.isSuccess() + " - " + createResp.getData());
                System.out.println("Create response status: " + createResp.getStatusCode());
            }

            String action = (idStr != null && !idStr.isEmpty()) ? "Update Service" : "Create Service";
            logAdminAction(adminId, action, "Service: " + name);

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
                // Handle decimal IDs (e.g. "5.0")
                int serviceId = (int) Double.parseDouble(id);
                Integer adminId = (Integer) request.getSession().getAttribute("admin_id");
                ApiClient.ApiResponse<String> apiResponse = ApiClient.delete(
                        "/admin/services/" + serviceId, String.class, adminId);

                if (apiResponse.isSuccess()) {
                    logAdminAction(adminId, "Delete Service", "ID: " + id);
                    response.sendRedirect(request.getContextPath() + "/admin/services?status=deleted");
                } else {
                    response.sendRedirect(request.getContextPath() + "/admin/services?status=error");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/admin/services?status=error");
            }
        }
    }

    // ========== Data Loading Methods ==========

    private void loadServicesData(HttpServletRequest request) throws Exception {
        // Use admin endpoint to get ALL services (including inactive ones)
        System.out.println("=== loadServicesData ===");
        ApiClient.ApiResponse<String> apiResponse = ApiClient.get("/admin/services", String.class);
        System.out.println("API Response success: " + apiResponse.isSuccess());

        if (apiResponse.isSuccess() && apiResponse.getData() != null) {
            JsonObject json = gson.fromJson(apiResponse.getData(), JsonObject.class);
            Type listType = new TypeToken<List<Service>>() {
            }.getType();
            List<Service> services = gson.fromJson(json.getAsJsonArray("data"), listType);
            System.out.println("Services loaded: " + (services != null ? services.size() : 0));
            request.setAttribute("services", services);
        } else {
            System.out.println("API call failed or returned null");
            request.setAttribute("services", new ArrayList<Service>());
        }
    }

    private void loadDashboardStats(HttpServletRequest request) throws Exception {
        // Fetch dashboard stats via REST API
        ApiClient.ApiResponse<String> statsResponse = ApiClient.get("/admin/dashboard/stats", String.class);
        if (statsResponse.isSuccess() && statsResponse.getData() != null) {
            JsonObject stats = gson.fromJson(statsResponse.getData(), JsonObject.class);
            if (stats.has("data")) {
                JsonObject data = stats.getAsJsonObject("data");
                request.setAttribute("revenue",
                        data.has("totalRevenue") ? data.get("totalRevenue").getAsString() : "0");
                request.setAttribute("bookingCount",
                        data.has("bookingCount") ? data.get("bookingCount").getAsInt() : 0);
                request.setAttribute("serviceCount",
                        data.has("serviceCount") ? data.get("serviceCount").getAsInt() : 0);
                request.setAttribute("clientCount", data.has("userCount") ? data.get("userCount").getAsInt() : 0);
            } else {
                // Flat response format
                request.setAttribute("revenue",
                        stats.has("totalRevenue") ? stats.get("totalRevenue").getAsString() : "0");
                request.setAttribute("bookingCount",
                        stats.has("bookingCount") ? stats.get("bookingCount").getAsInt() : 0);
                request.setAttribute("serviceCount",
                        stats.has("serviceCount") ? stats.get("serviceCount").getAsInt() : 0);
                request.setAttribute("clientCount", stats.has("userCount") ? stats.get("userCount").getAsInt() : 0);
            }
        }

        // Monthly revenue via REST API
        ApiClient.ApiResponse<String> revenueResponse = ApiClient.get("/admin/reports/monthly-revenue", String.class);
        if (revenueResponse.isSuccess() && revenueResponse.getData() != null) {
            JsonObject json = gson.fromJson(revenueResponse.getData(), JsonObject.class);
            if (json.has("data")) {
                Type listType = new TypeToken<List<Map<String, Object>>>() {
                }.getType();
                request.setAttribute("monthlyRevenue", gson.fromJson(json.getAsJsonArray("data"), listType));
            }
        }

        // Top services via REST API
        ApiClient.ApiResponse<String> topSvcResponse = ApiClient.get("/admin/reports/popular-services", String.class);
        if (topSvcResponse.isSuccess() && topSvcResponse.getData() != null) {
            JsonObject json = gson.fromJson(topSvcResponse.getData(), JsonObject.class);
            if (json.has("data")) {
                Type listType = new TypeToken<List<Map<String, Object>>>() {
                }.getType();
                request.setAttribute("topServices", gson.fromJson(json.getAsJsonArray("data"), listType));
            }
        }

        // Top clients via REST API
        ApiClient.ApiResponse<String> topClientsResponse = ApiClient.get("/admin/reports/top-clients", String.class);
        if (topClientsResponse.isSuccess() && topClientsResponse.getData() != null) {
            JsonObject json = gson.fromJson(topClientsResponse.getData(), JsonObject.class);
            if (json.has("data")) {
                Type listType = new TypeToken<List<Map<String, Object>>>() {
                }.getType();
                request.setAttribute("topClients", gson.fromJson(json.getAsJsonArray("data"), listType));
            }
        }

        // Service ratings via REST API
        ApiClient.ApiResponse<String> ratingsResponse = ApiClient.get("/admin/reports/service-ratings",
                String.class);
        if (ratingsResponse.isSuccess() && ratingsResponse.getData() != null) {
            JsonObject json = gson.fromJson(ratingsResponse.getData(), JsonObject.class);
            if (json.has("data")) {
                Type listType = new TypeToken<List<Map<String, Object>>>() {
                }.getType();
                request.setAttribute("serviceRatings", gson.fromJson(json.getAsJsonArray("data"), listType));
            }
        }
    }

    private void loadCategoriesData(HttpServletRequest request) throws Exception {
        ApiClient.ApiResponse<String> apiResponse = ApiClient.get("/services/categories", String.class);
        if (apiResponse.isSuccess() && apiResponse.getData() != null) {
            JsonObject json = gson.fromJson(apiResponse.getData(), JsonObject.class);
            Type listType = new TypeToken<List<Map<String, Object>>>() {
            }.getType();
            List<Map<String, Object>> categories = gson.fromJson(json.getAsJsonArray("data"), listType);
            fixGsonDoubleIds(categories);
            request.setAttribute("categories", categories);
        } else {
            request.setAttribute("categories", new ArrayList<>());
        }
    }

    /**
     * Fix Gson's default behavior of deserializing JSON integers as Double
     * when using Map<String, Object>. Converts whole-number Doubles (5.0 -> 5)
     * back to Integer so JSP renders "5" not "5.0".
     */
    private void fixGsonDoubleIds(List<Map<String, Object>> list) {
        for (Map<String, Object> map : list) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                // Skip price fields - keep them as decimals
                if ("price".equals(entry.getKey()))
                    continue;

                if (entry.getValue() instanceof Double) {
                    Double d = (Double) entry.getValue();
                    if (d == Math.floor(d) && !Double.isInfinite(d)) {
                        entry.setValue(d.intValue());
                    }
                }
            }
        }
    }

    private void loadBookingsData(HttpServletRequest request) throws Exception {
        // No customerId => returns all bookings (admin use case)
        ApiClient.ApiResponse<String> apiResponse = ApiClient.get("/bookings", String.class);
        if (apiResponse.isSuccess() && apiResponse.getData() != null) {
            JsonObject json = gson.fromJson(apiResponse.getData(), JsonObject.class);
            Type listType = new TypeToken<List<Map<String, Object>>>() {
            }.getType();
            List<Map<String, Object>> bookings = gson.fromJson(json.getAsJsonArray("data"), listType);
            request.setAttribute("bookings", bookings);
        } else {
            request.setAttribute("bookings", new ArrayList<>());
        }
    }

    private void handleExportBookings(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            ApiClient.ApiResponse<String> apiResponse = ApiClient.get("/bookings", String.class);

            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=silvercare_bookings_export.csv");
            java.io.PrintWriter writer = response.getWriter();
            writer.println("Booking ID,Customer Name,Booking Date,Status,Total Amount,GST Amount");

            if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                JsonObject json = gson.fromJson(apiResponse.getData(), JsonObject.class);
                JsonArray dataArray = json.getAsJsonArray("data");

                if (dataArray != null) {
                    for (JsonElement elem : dataArray) {
                        JsonObject b = elem.getAsJsonObject();
                        StringBuilder sb = new StringBuilder();
                        sb.append(b.has("id") ? b.get("id").getAsInt() : "").append(",");
                        sb.append("\"").append(b.has("customerName") ? b.get("customerName").getAsString() : "")
                                .append("\",");
                        sb.append(b.has("bookingDate") ? b.get("bookingDate").getAsString() : "").append(",");
                        sb.append(b.has("status") ? b.get("status").getAsString() : "").append(",");
                        sb.append(b.has("totalAmount") ? b.get("totalAmount").getAsString() : "").append(",");
                        sb.append(b.has("gstAmount") ? b.get("gstAmount").getAsString() : "");
                        writer.println(sb.toString());
                    }
                }
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
            ApiClient.ApiResponse<String> apiResponse = ApiClient.get("/users", String.class);

            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=silvercare_users_export.csv");
            java.io.PrintWriter writer = response.getWriter();
            writer.println("User ID,Full Name,Email,Phone,Address,Gender,Role,Tutorial Completed");

            if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                // The /users endpoint returns a list directly (not wrapped in {data:...})
                Type listType = new TypeToken<List<Map<String, Object>>>() {
                }.getType();
                List<Map<String, Object>> users;
                try {
                    // Try parsing as wrapped response first
                    JsonObject json = gson.fromJson(apiResponse.getData(), JsonObject.class);
                    if (json.has("data")) {
                        users = gson.fromJson(json.getAsJsonArray("data"), listType);
                    } else {
                        users = gson.fromJson(apiResponse.getData(), listType);
                    }
                } catch (Exception e) {
                    users = gson.fromJson(apiResponse.getData(), listType);
                }

                for (Map<String, Object> u : users) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(u.getOrDefault("id", "")).append(",");
                    sb.append("\"").append(u.getOrDefault("fullName", "")).append("\",");
                    sb.append("\"").append(u.getOrDefault("email", "")).append("\",");
                    sb.append("\"").append(u.getOrDefault("phone", "")).append("\",");
                    sb.append("\"").append(u.getOrDefault("address", "")).append("\",");
                    sb.append(u.getOrDefault("gender", "")).append(",");
                    sb.append(u.getOrDefault("role", "")).append(",");
                    sb.append(u.getOrDefault("tutorialCompleted", ""));
                    writer.println(sb.toString());
                }
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
            ApiClient.ApiResponse<String> apiResponse = ApiClient.get("/feedback", String.class);

            response.setContentType("text/csv");
            response.setHeader("Content-Disposition", "attachment; filename=silvercare_feedback_export.csv");
            java.io.PrintWriter writer = response.getWriter();
            writer.println("Feedback ID,Customer ID,Service ID,Rating,Comments,Created At");

            if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                JsonObject json = gson.fromJson(apiResponse.getData(), JsonObject.class);
                JsonArray dataArray = json.has("data") ? json.getAsJsonArray("data") : new JsonArray();

                if (dataArray != null) {
                    for (JsonElement elem : dataArray) {
                        JsonObject f = elem.getAsJsonObject();
                        StringBuilder sb = new StringBuilder();
                        sb.append(f.has("feedbackId") ? f.get("feedbackId").getAsInt() : "").append(",");
                        sb.append(f.has("customerId") ? f.get("customerId").getAsInt() : "").append(",");
                        sb.append(f.has("serviceId") ? f.get("serviceId").getAsInt() : "").append(",");
                        sb.append(f.has("rating") ? f.get("rating").getAsInt() : "").append(",");
                        String comment = f.has("comment") ? f.get("comment").getAsString() : "";
                        sb.append("\"").append(comment.replace("\"", "\"\"")).append("\",");
                        sb.append(f.has("createdAt") ? f.get("createdAt").getAsString() : "");
                        writer.println(sb.toString());
                    }
                }
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to export feedback");
        }
    }

    private void loadFeedbackData(HttpServletRequest request) throws Exception {
        // Fetch customer feedback
        ApiClient.ApiResponse<String> feedbackResponse = ApiClient.get("/feedback", String.class);
        if (feedbackResponse.isSuccess() && feedbackResponse.getData() != null) {
            JsonObject json = gson.fromJson(feedbackResponse.getData(), JsonObject.class);
            if (json.has("data")) {
                Type listType = new TypeToken<List<Map<String, Object>>>() {
                }.getType();
                List<Map<String, Object>> feedback = gson.fromJson(json.getAsJsonArray("data"), listType);
                request.setAttribute("feedback", feedback);
            } else {
                request.setAttribute("feedback", new ArrayList<>());
            }
        } else {
            request.setAttribute("feedback", new ArrayList<>());
        }

        // Fetch public contact messages
        ApiClient.ApiResponse<String> contactResponse = ApiClient.get("/contact", String.class);
        if (contactResponse.isSuccess() && contactResponse.getData() != null) {
            JsonObject json = gson.fromJson(contactResponse.getData(), JsonObject.class);
            if (json.has("data")) {
                Type listType = new TypeToken<List<Map<String, Object>>>() {
                }.getType();
                List<Map<String, Object>> contactMessages = gson.fromJson(json.getAsJsonArray("data"), listType);
                request.setAttribute("contactMessages", contactMessages);
            } else {
                request.setAttribute("contactMessages", new ArrayList<>());
            }
        } else {
            request.setAttribute("contactMessages", new ArrayList<>());
        }
    }

    private void loadServiceForEdit(HttpServletRequest request) throws Exception {
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                // Handle decimal IDs (e.g. "5.0") by parsing to double then converting to int
                int id = (int) Double.parseDouble(idStr);
                ApiClient.ApiResponse<String> apiResponse = ApiClient.get("/admin/services/" + id, String.class);
                if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                    JsonObject json = gson.fromJson(apiResponse.getData(), JsonObject.class);
                    JsonObject data = json.getAsJsonObject("data");
                    Service service = gson.fromJson(data, Service.class);
                    request.setAttribute("service", service);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid service ID format");
            }
        }
    }

    private void loadUsersData(HttpServletRequest request) throws Exception {
        String areaFilter = request.getParameter("area");
        String endpoint;

        if (areaFilter != null && !areaFilter.trim().isEmpty()) {
            endpoint = "/users/search?area=" + java.net.URLEncoder.encode(areaFilter.trim(), "UTF-8");
            request.setAttribute("areaFilter", areaFilter.trim());
        } else {
            endpoint = "/users/all";
        }

        ApiClient.ApiResponse<String> apiResponse = ApiClient.get(endpoint, String.class);
        if (apiResponse.isSuccess() && apiResponse.getData() != null) {
            JsonObject json = gson.fromJson(apiResponse.getData(), JsonObject.class);
            if (json.has("data")) {
                Type listType = new TypeToken<List<Map<String, Object>>>() {
                }.getType();
                List<Map<String, Object>> users = gson.fromJson(json.getAsJsonArray("data"), listType);
                request.setAttribute("users", users);
            } else {
                // Try parsing as direct list
                Type listType = new TypeToken<List<Map<String, Object>>>() {
                }.getType();
                List<Map<String, Object>> users = gson.fromJson(apiResponse.getData(), listType);
                request.setAttribute("users", users);
            }
        } else {
            request.setAttribute("users", new ArrayList<>());
        }
    }

    private void loadUserForEdit(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        if (id != null) {
            // Handle decimal IDs (e.g. "3.0") by parsing as double then converting to int
            try {
                int userId = (int) Double.parseDouble(id);
                ApiClient.ApiResponse<String> apiResponse = ApiClient.get("/users/" + userId, String.class);
                if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                    User user = gson.fromJson(apiResponse.getData(), User.class);
                    request.setAttribute("userData", user);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid user ID format");
            }
        }
    }

    private void handleSaveUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String idStr = request.getParameter("id");
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String gender = request.getParameter("gender");
            String medicalInfo = request.getParameter("medicalInfo");

            Integer adminId = (Integer) request.getSession().getAttribute("admin_id");

            if (idStr != null && !idStr.isEmpty()) {
                // Update user via REST API
                Map<String, Object> updateData = new HashMap<>();
                updateData.put("id", Integer.parseInt(idStr));
                updateData.put("fullName", fullName);
                updateData.put("email", email);
                updateData.put("phone", phone);
                updateData.put("address", address);
                updateData.put("gender", gender);
                updateData.put("medicalInfo", medicalInfo);

                ApiClient.put("/users/profile", updateData, String.class, adminId);
            } else {
                // Register new user via REST API
                Map<String, String> regData = new HashMap<>();
                regData.put("fullName", fullName);
                regData.put("email", email);
                regData.put("phone", phone);
                regData.put("address", address);
                regData.put("gender", gender);
                if (medicalInfo != null && !medicalInfo.trim().isEmpty()) {
                    regData.put("medicalInfo", medicalInfo);
                }
                String password = request.getParameter("password");
                regData.put("password", password);

                ApiClient.post("/users/register", regData, String.class, adminId);
            }

            // Log interaction
            String logAction = (idStr != null && !idStr.isEmpty()) ? "Update User" : "Create User";
            logAdminAction(adminId, logAction, "User Email: " + email);

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
                Integer adminId = (Integer) request.getSession().getAttribute("admin_id");
                ApiClient.ApiResponse<String> apiResponse = ApiClient.delete("/users/" + id, String.class, adminId);

                if (apiResponse.isSuccess()) {
                    logAdminAction(adminId, "Delete User", "ID: " + id);
                    response.sendRedirect(request.getContextPath() + "/admin/users?status=deleted");
                } else {
                    response.sendRedirect(request.getContextPath() + "/admin/users?status=error");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/admin/users?status=error");
            }
        }
    }

    private void handleResetPassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String userIdStr = request.getParameter("userId");
            String newPassword = request.getParameter("newPassword");
            String role = request.getParameter("role");

            Map<String, Object> resetData = new HashMap<>();
            if (userIdStr != null)
                resetData.put("userId", Integer.parseInt(userIdStr));
            resetData.put("newPassword", newPassword);
            resetData.put("role", role != null ? role : "Customer");

            Integer adminId = (Integer) request.getSession().getAttribute("admin_id");
            ApiClient.ApiResponse<String> apiResponse = ApiClient.post(
                    "/users/reset-password", resetData, String.class, adminId);

            if (apiResponse.isSuccess()) {
                logAdminAction(adminId, "Reset Password", "User ID: " + userIdStr);
                response.sendRedirect(request.getContextPath() + "/admin/users?status=reset_success");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/users?status=error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/users?status=error");
        }
    }

    private void handleGetUserDetailsJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        if (id == null || id.trim().isEmpty()) {
            response.setStatus(400);
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("error", "User ID is required");
            response.getWriter().write(gson.toJson(errorResponse));
            return;
        }
        
        try {
            // Fetch user data
            ApiClient.ApiResponse<String> userResponse = ApiClient.get("/users/" + id, String.class);
            
            System.out.println("User API Response Status: " + userResponse.getStatusCode());
            System.out.println("User API Response Data: " + userResponse.getData());
            System.out.println("User API Response Error: " + userResponse.getError());

            if (userResponse.isSuccess() && userResponse.getData() != null) {
                // Parse user data
                JsonObject userData = gson.fromJson(userResponse.getData(), JsonObject.class);

                // Fetch bookings for this user
                JsonArray bookingsArray = new JsonArray();
                try {
                    ApiClient.ApiResponse<String> bookingsResponse = ApiClient.get(
                            "/bookings?customerId=" + id, String.class);
                    if (bookingsResponse.isSuccess() && bookingsResponse.getData() != null) {
                        JsonObject bookingsJson = gson.fromJson(bookingsResponse.getData(), JsonObject.class);
                        if (bookingsJson.has("data")) {
                            bookingsArray = bookingsJson.getAsJsonArray("data");
                        }
                    }
                } catch (Exception e) {
                    // Bookings fetch failed — non-critical, continue with empty bookings
                    e.printStackTrace();
                }

                // Build response in the format the modal JS expects: { user: {...}, bookings:
                // [...] }
                JsonObject result = new JsonObject();
                result.add("user", userData);
                result.add("bookings", bookingsArray);
                response.getWriter().write(gson.toJson(result));
            } else {
                response.setStatus(userResponse.getStatusCode());
                JsonObject errorResponse = new JsonObject();
                String errorMsg = userResponse.getError() != null ? userResponse.getError() : "User not found";
                errorResponse.addProperty("error", errorMsg);
                response.getWriter().write(gson.toJson(errorResponse));
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("error", "Server error: " + e.getMessage());
            response.getWriter().write(gson.toJson(errorResponse));
        }
    }

    private void loadBookingDetails(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        if (id != null) {
            // Handle decimal IDs (e.g., "22.0") by parsing as double then converting to int
            try {
                int bookingId = (int) Double.parseDouble(id);
                ApiClient.ApiResponse<String> apiResponse = ApiClient.get("/bookings/" + bookingId, String.class);
                if (apiResponse.isSuccess() && apiResponse.getData() != null) {
                    JsonObject json = gson.fromJson(apiResponse.getData(), JsonObject.class);
                    Type mapType = new TypeToken<Map<String, Object>>() {
                    }.getType();
                    Map<String, Object> booking = gson.fromJson(json.getAsJsonObject("data"), mapType);

                    // Fix Gson Double-to-Integer for booking ID and other fields
                    for (Map.Entry<String, Object> entry : booking.entrySet()) {
                        if (entry.getValue() instanceof Double && !"totalAmount".equals(entry.getKey())
                                && !"gstAmount".equals(entry.getKey())) {
                            Double d = (Double) entry.getValue();
                            if (d == Math.floor(d) && !Double.isInfinite(d)) {
                                entry.setValue(d.intValue());
                            }
                        }
                    }

                    // Fix nested details list
                    if (booking.containsKey("details")) {
                        Object detailsObj = booking.get("details");
                        if (detailsObj instanceof List) {
                            @SuppressWarnings("unchecked")
                            List<Map<String, Object>> details = (List<Map<String, Object>>) detailsObj;
                            for (Map<String, Object> detail : details) {
                                for (Map.Entry<String, Object> entry : detail.entrySet()) {
                                    if (entry.getValue() instanceof Double && !"unitPrice".equals(entry.getKey())) {
                                        Double d = (Double) entry.getValue();
                                        if (d == Math.floor(d) && !Double.isInfinite(d)) {
                                            entry.setValue(d.intValue());
                                        }
                                    }
                                }
                            }
                        }
                    }

                    request.setAttribute("booking", booking);
                }
            } catch (NumberFormatException e) {
                // If parsing fails, set error attribute
                request.setAttribute("error", "Invalid booking ID format");
            }
        }
    }

    private void loadLogsData(HttpServletRequest request) throws Exception {
        ApiClient.ApiResponse<String> apiResponse = ApiClient.get("/admin/logs", String.class);
        if (apiResponse.isSuccess() && apiResponse.getData() != null) {
            JsonObject json = gson.fromJson(apiResponse.getData(), JsonObject.class);
            if (json.has("data")) {
                Type listType = new TypeToken<List<Map<String, Object>>>() {
                }.getType();
                List<Map<String, Object>> logs = gson.fromJson(json.getAsJsonArray("data"), listType);
                request.setAttribute("logs", logs);
            }
        } else {
            request.setAttribute("logs", new ArrayList<>());
        }
    }
}
