package com.silvercare.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.stereotype.Component;
import com.silvercare.model.User;

/**
 * Utility bean containing SQL statements and database operations for User
 * entity
 * This follows the assignment requirement of separating SQL from DAO classes
 */
@Component
public class UserDBUtil {

    // SQL Statements for User operations
    private static final String SELECT_CUSTOMER_BY_EMAIL = "SELECT * FROM silvercare.customer WHERE email = ?";

    private static final String SELECT_ADMIN_BY_USERNAME = "SELECT * FROM silvercare.admin_user WHERE username = ?";

    private static final String INSERT_CUSTOMER = "INSERT INTO silvercare.customer (email, password_hash, full_name, phone, address, gender, medical_info, profile_picture, tutorial_completed, created_at) "
            +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

    private static final String UPDATE_CUSTOMER_PROFILE = "UPDATE silvercare.customer SET full_name = ?, email = ?, phone = ?, address = ?, gender = ? WHERE customer_id = ?";

    private static final String UPDATE_CUSTOMER_PROFILE_WITH_MEDICAL = "UPDATE silvercare.customer SET full_name = ?, email = ?, phone = ?, address = ?, gender = ?, medical_info = ? WHERE customer_id = ?";

    private static final String UPDATE_CUSTOMER_PROFILE_WITH_PICTURE = "UPDATE silvercare.customer SET full_name = ?, email = ?, phone = ?, address = ?, gender = ?, profile_picture = ? WHERE customer_id = ?";

    private static final String UPDATE_CUSTOMER_PROFILE_FULL = "UPDATE silvercare.customer SET full_name = ?, email = ?, phone = ?, address = ?, gender = ?, medical_info = ?, profile_picture = ? WHERE customer_id = ?";

    private static final String SELECT_ALL_CUSTOMERS = "SELECT * FROM silvercare.customer";

    private static final String UPDATE_CUSTOMER_PASSWORD = "UPDATE silvercare.customer SET password_hash = ? WHERE customer_id = ?";

    private static final String UPDATE_ADMIN_PASSWORD = "UPDATE silvercare.admin_user SET password_hash = ? WHERE admin_id = ?";

    private static final String UPDATE_TUTORIAL_STATUS = "UPDATE silvercare.customer SET tutorial_completed = ? WHERE customer_id = ?";

    /**
     * Authenticate user login credentials
     */
    public User authenticateUser(String email, String password) throws SQLException {
        // First check customer table
        User user = authenticateCustomer(email, password);
        if (user != null) {
            return user;
        }

        // Then check admin table
        return authenticateAdmin(email, password);
    }

    /**
     * Authenticate customer credentials using BCrypt password verification
     */
    private User authenticateCustomer(String email, String password) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_CUSTOMER_BY_EMAIL)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");
                    if (verifyPassword(password, storedHash)) {
                        return mapCustomerResultSetToUser(rs);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Authenticate admin credentials using BCrypt password verification
     */
    private User authenticateAdmin(String email, String password) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_ADMIN_BY_USERNAME)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");
                    if (verifyPassword(password, storedHash)) {
                        return mapAdminResultSetToUser(rs);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Helper to verify password against stored hash, handling legacy plain-text
     * passwords
     */
    private boolean verifyPassword(String password, String storedHash) {
        if (storedHash == null || storedHash.isEmpty() || password == null) {
            return false;
        }

        // Check if it's a valid BCrypt hash
        if (storedHash.startsWith("$2a$") || storedHash.startsWith("$2b$") || storedHash.startsWith("$2y$")) {
            try {
                return org.mindrot.jbcrypt.BCrypt.checkpw(password, storedHash);
            } catch (Exception e) {
                // If malformed hash, fall back to plain text comparison
                return password.equals(storedHash);
            }
        }

        // If not a BCrypt hash, it's legacy plain text
        return password.equals(storedHash);
    }

    /**
     * Register a new customer
     */
    public boolean registerCustomer(User user, String password) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(INSERT_CUSTOMER)) {

            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, password); // Password must be hashed BEFORE passing here or INSIDE here.
            // Since this is DBUtil, it should technically just take data. Service does
            // logic.
            pstmt.setString(3, user.getFullName());
            pstmt.setString(4, user.getPhone());
            pstmt.setString(5, user.getAddress());
            pstmt.setString(6, user.getGender());
            pstmt.setString(7, user.getMedicalInfo());

            // Set defaults to match legacy JSP behavior
            String pic = user.getProfilePicture();
            if (pic == null || pic.isEmpty())
                pic = "default-avatar.png";
            pstmt.setString(8, pic);

            pstmt.setBoolean(9, false); // tutorial_completed default false

            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Update customer profile information
     */
    public boolean updateCustomerProfile(User user) throws SQLException {
        String sql = determineUpdateSQL(user);

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            setUpdateParameters(pstmt, user);
            return pstmt.executeUpdate() > 0;
        }
    }

    private String determineUpdateSQL(User user) {
        boolean hasMedical = user.getMedicalInfo() != null && !user.getMedicalInfo().trim().isEmpty();
        boolean hasPicture = user.getProfilePicture() != null && !user.getProfilePicture().trim().isEmpty();

        if (hasMedical && hasPicture) {
            return UPDATE_CUSTOMER_PROFILE_FULL;
        } else if (hasMedical) {
            return UPDATE_CUSTOMER_PROFILE_WITH_MEDICAL;
        } else if (hasPicture) {
            return UPDATE_CUSTOMER_PROFILE_WITH_PICTURE;
        } else {
            return UPDATE_CUSTOMER_PROFILE;
        }
    }

    private void setUpdateParameters(PreparedStatement pstmt, User user) throws SQLException {
        int paramIndex = 1;
        pstmt.setString(paramIndex++, user.getFullName());
        pstmt.setString(paramIndex++, user.getEmail());
        pstmt.setString(paramIndex++, user.getPhone());
        pstmt.setString(paramIndex++, user.getAddress());
        pstmt.setString(paramIndex++, user.getGender());

        boolean hasMedical = user.getMedicalInfo() != null && !user.getMedicalInfo().trim().isEmpty();
        boolean hasPicture = user.getProfilePicture() != null && !user.getProfilePicture().trim().isEmpty();

        if (hasMedical) {
            pstmt.setString(paramIndex++, user.getMedicalInfo());
        }
        if (hasPicture) {
            pstmt.setString(paramIndex++, user.getProfilePicture());
        }

        pstmt.setInt(paramIndex, user.getId());
    }

    private User mapCustomerResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("customer_id"));
        user.setEmail(rs.getString("email"));
        user.setFullName(rs.getString("full_name"));
        user.setPhone(rs.getString("phone"));
        user.setAddress(rs.getString("address"));
        user.setGender(rs.getString("gender"));
        user.setMedicalInfo(rs.getString("medical_info"));
        user.setProfilePicture(rs.getString("profile_picture"));
        user.setTutorialCompleted(rs.getBoolean("tutorial_completed"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setRole("Customer");
        return user;
    }

    private User mapAdminResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("admin_id"));
        user.setEmail(rs.getString("username"));
        user.setFullName(rs.getString("full_name"));
        user.setRole("Admin");
        user.setCreatedAt(rs.getTimestamp("created_at"));
        return user;
    }

    public java.util.List<User> getAllCustomers() throws SQLException {
        java.util.List<User> users = new java.util.ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_CUSTOMERS);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                users.add(mapCustomerResultSetToUser(rs));
            }
        }
        return users;
    }

    /**
     * Update user password
     */
    public boolean updateUserPassword(int userId, String passwordHash, String role) throws SQLException {
        String sql = "Admin".equalsIgnoreCase(role) ? UPDATE_ADMIN_PASSWORD : UPDATE_CUSTOMER_PASSWORD;
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, passwordHash);
            pstmt.setInt(2, userId);
            return pstmt.executeUpdate() > 0;
        }
    }

    private static final String SELECT_CUSTOMERS_BY_AREA = "SELECT * FROM silvercare.customer WHERE address LIKE ?";
    private static final String DELETE_CUSTOMER = "DELETE FROM silvercare.customer WHERE customer_id = ?";

    /**
     * Get users by area
     */
    public java.util.List<User> getUsersByArea(String area) throws SQLException {
        java.util.List<User> users = new java.util.ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_CUSTOMERS_BY_AREA)) {
            pstmt.setString(1, "%" + area + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapCustomerResultSetToUser(rs));
                }
            }
        }
        return users;
    }

    /**
     * Delete user by ID
     */
    public boolean deleteUser(int id) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(DELETE_CUSTOMER)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Update tutorial completion status safely without touching other profile data
     */
    public boolean updateTutorialStatus(int userId, boolean completed) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(UPDATE_TUTORIAL_STATUS)) {
            pstmt.setBoolean(1, completed);
            pstmt.setInt(2, userId);
            return pstmt.executeUpdate() > 0;
        }
    }
}
