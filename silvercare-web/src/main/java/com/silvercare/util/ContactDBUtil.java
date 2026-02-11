package com.silvercare.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * Utility Bean for Contact database operations.
 * Separates SQL logic from DAO as per MVC requirements.
 */
public class ContactDBUtil {

    private static final String INSERT_CONTACT = "INSERT INTO silvercare.contact_messages (full_name, email, subject, message, created_at) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";

    public void saveContact(Map<String, Object> contactData) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(INSERT_CONTACT)) {
            pstmt.setString(1, (String) contactData.get("name"));
            pstmt.setString(2, (String) contactData.get("email"));
            pstmt.setString(3, (String) contactData.get("subject"));
            pstmt.setString(4, (String) contactData.get("message"));
            pstmt.executeUpdate();
        }
    }
}
