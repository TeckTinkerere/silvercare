package com.silvercare.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;
import com.silvercare.model.User;

/**
 * Utility Bean for User database operations.
 * Separates SQL logic from DAO as per MVC requirements.
 */
public class UserDBUtil {

    // SQL Constants
    private static final String SELECT_CUSTOMER_BY_EMAIL = "SELECT * FROM silvercare.customer WHERE email = ?";
    private static final String SELECT_ADMIN_BY_USERNAME = "SELECT * FROM silvercare.admin_user WHERE username = ?";
    private static final String SELECT_CUSTOMER_BY_ID = "SELECT * FROM silvercare.customer WHERE customer_id = ?";
    private static final String SELECT_ALL_CUSTOMERS = "SELECT * FROM silvercare.customer ORDER BY full_name";
    private static final String UPDATE_TUTORIAL_STATUS = "UPDATE silvercare.customer SET tutorial_completed = ? WHERE customer_id = ?";
    private static final String DELETE_CUSTOMER = "DELETE FROM silvercare.customer WHERE customer_id = ?";
    private static final String CHECK_EMAIL_EXISTS = "SELECT customer_id FROM silvercare.customer WHERE email = ?";
    private static final String INSERT_CUSTOMER = "INSERT INTO silvercare.customer (email, password_hash, full_name, phone, address, gender, medical_info, profile_picture, tutorial_completed, created_at) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP) RETURNING customer_id";
    private static final String SEARCH_CUSTOMERS_BY_ADDRESS = "SELECT * FROM silvercare.customer WHERE address ILIKE ? ORDER BY full_name";

    /**
     * Authenticate user (Customer or Admin)
     */
    public User authenticate(String email, String password) throws SQLException {
        // 1. Try Admin Login
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_ADMIN_BY_USERNAME)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String hashed = rs.getString("password_hash");
                    if (verifyPassword(password, hashed)) {
                        return mapResultSetToAdminModel(rs);
                    }
                }
            }
        }

        // 2. Try Customer Login
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_CUSTOMER_BY_EMAIL)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String hashed = rs.getString("password_hash");
                    if (verifyPassword(password, hashed)) {
                        return mapResultSetToUserModel(rs);
                    }
                }
            }
        }

        return null;
    }

    public User getUserById(int id) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_CUSTOMER_BY_ID)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUserModel(rs);
                }
            }
        }
        return null;
    }

    public List<User> getAllCustomers() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_CUSTOMERS);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                users.add(mapResultSetToUserModel(rs));
            }
        }
        return users;
    }

    public List<User> getAllUsers() throws SQLException {
        // Alias for getAllCustomers to maintain consistency with DAO
        return getAllCustomers();
    }

    public void updateTutorialStatus(int userId, boolean completed) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(UPDATE_TUTORIAL_STATUS)) {
            pstmt.setBoolean(1, completed);
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
        }
    }

    public void deleteUser(int id) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(DELETE_CUSTOMER)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public User register(User user, String plainPassword) throws SQLException {
        // Check email
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement checkStmt = conn.prepareStatement(CHECK_EMAIL_EXISTS)) {
            checkStmt.setString(1, user.getEmail());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    throw new SQLException("Email already registered.");
                }
            }
        }

        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(INSERT_CUSTOMER)) {
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, user.getFullName());
            pstmt.setString(4, user.getPhone());
            pstmt.setString(5, user.getAddress());
            pstmt.setString(6, user.getGender() != null ? user.getGender() : "Not Specified");
            pstmt.setString(7, user.getMedicalInfo() != null ? user.getMedicalInfo() : "");
            pstmt.setString(8, user.getProfilePicture() != null ? user.getProfilePicture() : "default-avatar.png");
            pstmt.setBoolean(9, false);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user.setId(rs.getInt("customer_id"));
                    user.setRole("customer");
                    user.setTutorialCompleted(false);
                    return user;
                }
            }
        }
        throw new SQLException("Failed to register user");
    }

    public void updateProfile(User user) throws SQLException {
        StringBuilder sql = new StringBuilder("UPDATE silvercare.customer SET ");
        List<String> updates = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        if (user.getFullName() != null) {
            updates.add("full_name = ?");
            params.add(user.getFullName());
        }
        if (user.getPhone() != null) {
            updates.add("phone = ?");
            params.add(user.getPhone());
        }
        if (user.getAddress() != null) {
            updates.add("address = ?");
            params.add(user.getAddress());
        }
        if (user.getGender() != null) {
            updates.add("gender = ?");
            params.add(user.getGender());
        }
        if (user.getMedicalInfo() != null) {
            updates.add("medical_info = ?");
            params.add(user.getMedicalInfo());
        }
        if (user.getProfilePicture() != null) {
            updates.add("profile_picture = ?");
            params.add(user.getProfilePicture());
        }

        if (updates.isEmpty())
            return;

        sql.append(String.join(", ", updates));
        sql.append(" WHERE customer_id = ?");
        params.add(user.getId());

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            pstmt.executeUpdate();
        }
    }

    public List<User> getUsersByArea(String areaFilter) throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SEARCH_CUSTOMERS_BY_ADDRESS)) {
            pstmt.setString(1, "%" + areaFilter + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    users.add(mapResultSetToUserModel(rs));
                }
            }
        }
        return users;
    }

    private boolean verifyPassword(String password, String storedHash) {
        if (storedHash == null || password == null)
            return false;
        if (storedHash.startsWith("$2a$") || storedHash.startsWith("$2b$") || storedHash.startsWith("$2y$")) {
            return BCrypt.checkpw(password, storedHash);
        }
        return password.equals(storedHash);
    }

    private User mapResultSetToUserModel(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("customer_id"));
        u.setEmail(rs.getString("email"));
        u.setFullName(rs.getString("full_name"));
        u.setPhone(rs.getString("phone"));
        u.setAddress(rs.getString("address"));
        u.setGender(rs.getString("gender"));
        u.setMedicalInfo(rs.getString("medical_info"));
        u.setProfilePicture(rs.getString("profile_picture"));
        u.setTutorialCompleted(rs.getBoolean("tutorial_completed"));
        u.setCreatedAt(rs.getTimestamp("created_at"));
        u.setRole("customer");
        return u;
    }

    private User mapResultSetToAdminModel(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("admin_id"));
        u.setEmail(rs.getString("username"));
        u.setFullName(rs.getString("full_name"));
        u.setRole("admin");
        return u;
    }
}
