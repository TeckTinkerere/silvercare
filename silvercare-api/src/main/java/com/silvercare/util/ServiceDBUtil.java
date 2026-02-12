package com.silvercare.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

import com.silvercare.model.Service;

/**
 * Utility bean containing SQL statements and database operations for Service
 * entity
 * This follows the assignment requirement of separating SQL from DAO classes
 */
@Component
public class ServiceDBUtil {

    // SQL Statements for Service operations
    private static final String SELECT_ALL_SERVICES = "SELECT * FROM silvercare.service ORDER BY category_id, name";

    private static final String SELECT_ALL_CATEGORIES = "SELECT * FROM silvercare.service_category ORDER BY name";

    private static final String INSERT_SERVICE = "INSERT INTO silvercare.service (category_id, name, description, price, image_path) VALUES (?, ?, ?, ?, ?)";

    private static final String UPDATE_SERVICE = "UPDATE silvercare.service SET category_id = ?, name = ?, description = ?, price = ?, image_path = ? WHERE service_id = ?";

    private static final String DELETE_SERVICE = "DELETE FROM silvercare.service WHERE service_id = ?";

    private static final String INSERT_CATEGORY = "INSERT INTO silvercare.service_category (name, description, icon) VALUES (?, ?, ?)";

    private static final String UPDATE_CATEGORY = "UPDATE silvercare.service_category SET name = ?, description = ?, icon = ? WHERE category_id = ?";

    private static final String DELETE_CATEGORY = "DELETE FROM silvercare.service_category WHERE category_id = ?";

    private static final String SELECT_SERVICES_BY_CATEGORY = "SELECT * FROM silvercare.service WHERE category_id = ? ORDER BY name";

    private static final String SEARCH_SERVICES = "SELECT * FROM silvercare.service WHERE (name LIKE ? OR description LIKE ?) ORDER BY name";

    /**
     * Retrieve all active services
     */
    public List<Service> getAllServices() throws SQLException {
        List<Service> services = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_SERVICES);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                services.add(mapResultSetToService(rs));
            }
        }
        return services;
    }

    /**
     * Retrieve service by ID with category name
     */
    public Service getServiceById(int serviceId) throws SQLException {
        String sql = "SELECT s.*, c.name as category_name FROM silvercare.service s " +
                "LEFT JOIN silvercare.service_category c ON s.category_id = c.category_id " +
                "WHERE s.service_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, serviceId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Service service = mapResultSetToService(rs);
                    // Try to get category_name from the result set
                    try {
                        String categoryName = rs.getString("category_name");
                        if (categoryName != null) {
                            service.setCategoryName(categoryName);
                        }
                    } catch (SQLException e) {
                        // Column doesn't exist, ignore
                    }
                    return service;
                }
            }
        }
        return null;
    }

    /**
     * Retrieve all service categories
     */
    public List<Map<String, Object>> getAllCategories() throws SQLException {
        List<Map<String, Object>> categories = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_CATEGORIES);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> category = new HashMap<>();
                category.put("id", rs.getInt("category_id"));
                category.put("name", rs.getString("name"));
                category.put("description", rs.getString("description"));
                category.put("icon", rs.getString("icon"));
                categories.add(category);
            }
        }
        return categories;
    }

    /**
     * Add new service to database
     */
    public boolean addService(Service service) throws SQLException {
        System.out.println("=== ServiceDBUtil.addService ===");
        System.out.println("Service: " + service);
        System.out.println("Category ID: " + service.getCategoryId());
        System.out.println("Name: " + service.getName());
        System.out.println("Description: " + service.getDescription());
        System.out.println("Price: " + service.getPrice());
        System.out.println("Image Path: " + service.getImagePath());

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(INSERT_SERVICE)) {
            pstmt.setInt(1, service.getCategoryId());
            pstmt.setString(2, service.getName());
            pstmt.setString(3, service.getDescription());
            pstmt.setBigDecimal(4, service.getPrice());
            pstmt.setString(5, service.getImagePath());

            System.out.println("Executing SQL: " + INSERT_SERVICE);
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("SQLException in addService: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Update existing service
     */
    public boolean updateService(Service service) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(UPDATE_SERVICE)) {
            pstmt.setInt(1, service.getCategoryId());
            pstmt.setString(2, service.getName());
            pstmt.setString(3, service.getDescription());
            pstmt.setBigDecimal(4, service.getPrice());
            pstmt.setString(5, service.getImagePath());
            pstmt.setInt(6, service.getId());
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Soft delete service
     */
    public boolean deleteService(int serviceId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(DELETE_SERVICE)) {
            pstmt.setInt(1, serviceId);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Get services by category
     */
    public List<Service> getServicesByCategory(int categoryId) throws SQLException {
        List<Service> services = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_SERVICES_BY_CATEGORY)) {
            pstmt.setInt(1, categoryId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    services.add(mapResultSetToService(rs));
                }
            }
        }
        return services;
    }

    /**
     * Search services
     */
    public List<Service> searchServices(String searchTerm) throws SQLException {
        List<Service> services = new ArrayList<>();
        String searchPattern = "%" + searchTerm + "%";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SEARCH_SERVICES)) {
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    services.add(mapResultSetToService(rs));
                }
            }
        }
        return services;
    }

    public List<Service> getAllServicesWithCategory() throws SQLException {
        List<Service> services = new ArrayList<>();
        // Admin should see ALL services, not just active ones
        String sql = "SELECT s.*, c.name as category_name FROM silvercare.service s " +
                "LEFT JOIN silvercare.service_category c ON s.category_id = c.category_id " +
                "ORDER BY s.service_id";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Service service = mapResultSetToService(rs);
                service.setCategoryName(rs.getString("category_name"));
                services.add(service);
            }
        }
        return services;
    }

    /**
     * Add new category
     */
    public boolean addCategory(String name, String description, String icon) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(INSERT_CATEGORY)) {
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setString(3, icon);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Update existing category
     */
    public boolean updateCategory(int id, String name, String description, String icon) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(UPDATE_CATEGORY)) {
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setString(3, icon);
            pstmt.setInt(4, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Delete category
     */
    public boolean deleteCategory(int id) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(DELETE_CATEGORY)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    private Service mapResultSetToService(ResultSet rs) throws SQLException {
        Service service = new Service();
        service.setId(rs.getInt("service_id"));
        service.setCategoryId(rs.getInt("category_id"));
        service.setName(rs.getString("name"));
        service.setDescription(rs.getString("description"));
        service.setPrice(rs.getBigDecimal("price"));
        service.setImagePath(rs.getString("image_path"));
        return service;
    }
}
