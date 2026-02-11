package com.silvercare.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.silvercare.model.Service;

/**
 * Utility Bean for Service database operations.
 * Separates SQL logic from DAO as per MVC requirements.
 */
public class ServiceDBUtil {

    private static final String SELECT_ALL_SERVICES = "SELECT s.*, c.name as category_name FROM silvercare.service s " +
            "LEFT JOIN silvercare.service_category c ON s.category_id = c.category_id " +
            "WHERE s.is_active = true ORDER BY c.name, s.name";

    private static final String SELECT_SERVICE_BY_ID = "SELECT s.*, c.name as category_name FROM silvercare.service s "
            + "LEFT JOIN silvercare.service_category c ON s.category_id = c.category_id " +
            "WHERE s.service_id = ?";

    private static final String SELECT_CATEGORIES = "SELECT * FROM silvercare.service_category ORDER BY name";

    private static final String SEARCH_SERVICES = "SELECT s.*, c.name as category_name FROM silvercare.service s " +
            "LEFT JOIN silvercare.service_category c ON s.category_id = c.category_id " +
            "WHERE (s.name ILIKE ? OR s.description ILIKE ?) AND s.is_active = true";

    private static final String INSERT_SERVICE = "INSERT INTO silvercare.service (category_id, name, description, price, image_path, is_active) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_SERVICE = "UPDATE silvercare.service SET category_id = ?, name = ?, description = ?, price = ?, image_path = ?, is_active = ? WHERE service_id = ?";

    private static final String DELETE_SERVICE = "DELETE FROM silvercare.service WHERE service_id = ?";

    private static final String INSERT_CATEGORY = "INSERT INTO silvercare.service_category (name, description, icon) VALUES (?, ?, ?)";

    private static final String DELETE_CATEGORY = "DELETE FROM silvercare.service_category WHERE category_id = ?";

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

    public Service getServiceById(int id) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_SERVICE_BY_ID)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToService(rs);
                }
            }
        }
        return null;
    }

    public List<Map<String, Object>> getAllCategories() throws SQLException {
        List<Map<String, Object>> categories = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_CATEGORIES);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> cat = new HashMap<>();
                cat.put("id", rs.getInt("category_id"));
                cat.put("name", rs.getString("name"));
                cat.put("description", rs.getString("description"));
                cat.put("icon", rs.getString("icon"));
                categories.add(cat);
            }
        }
        return categories;
    }

    public List<Service> searchServices(String term) throws SQLException {
        List<Service> services = new ArrayList<>();
        String pattern = "%" + term + "%";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SEARCH_SERVICES)) {
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    services.add(mapResultSetToService(rs));
                }
            }
        }
        return services;
    }

    public void addService(Service s) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(INSERT_SERVICE)) {
            pstmt.setInt(1, s.getCategoryId());
            pstmt.setString(2, s.getName());
            pstmt.setString(3, s.getDescription());
            pstmt.setBigDecimal(4, s.getPrice());
            pstmt.setString(5, s.getImagePath());
            pstmt.setBoolean(6, s.isActive());
            pstmt.executeUpdate();
        }
    }

    public void updateService(Service s) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(UPDATE_SERVICE)) {
            pstmt.setInt(1, s.getCategoryId());
            pstmt.setString(2, s.getName());
            pstmt.setString(3, s.getDescription());
            pstmt.setBigDecimal(4, s.getPrice());
            pstmt.setString(5, s.getImagePath());
            pstmt.setBoolean(6, s.isActive());
            pstmt.setInt(7, s.getId());
            pstmt.executeUpdate();
        }
    }

    public void deleteService(int id) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(DELETE_SERVICE)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public void addCategory(String name, String description, String icon) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(INSERT_CATEGORY)) {
            pstmt.setString(1, name);
            pstmt.setString(2, description);
            pstmt.setString(3, icon);
            pstmt.executeUpdate();
        }
    }

    public void deleteCategory(int id) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(DELETE_CATEGORY)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    private Service mapResultSetToService(ResultSet rs) throws SQLException {
        Service s = new Service();
        s.setId(rs.getInt("service_id"));
        s.setCategoryId(rs.getInt("category_id"));
        s.setName(rs.getString("name"));
        s.setDescription(rs.getString("description"));
        s.setPrice(rs.getBigDecimal("price"));
        s.setImagePath(rs.getString("image_path"));
        s.setActive(rs.getBoolean("is_active"));

        try {
            s.setCategoryName(rs.getString("category_name"));
        } catch (SQLException e) {
            // Optional
        }
        return s;
    }
}
