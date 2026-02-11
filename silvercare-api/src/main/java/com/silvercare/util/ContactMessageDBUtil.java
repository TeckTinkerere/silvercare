package com.silvercare.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.silvercare.model.ContactMessage;

/**
 * Utility bean containing SQL statements and database operations for
 * ContactMessage entity
 */
@Component
public class ContactMessageDBUtil {

    private static final String INSERT_MESSAGE = "INSERT INTO silvercare.CONTACT_MESSAGES (full_name, email, phone, subject, message, created_at) "
            +
            "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

    private static final String SELECT_ALL_MESSAGES = "SELECT * FROM silvercare.CONTACT_MESSAGES ORDER BY created_at DESC";

    /**
     * Save a new contact message
     */
    public boolean saveContactMessage(ContactMessage message) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(INSERT_MESSAGE)) {

            pstmt.setString(1, message.getFullName());
            pstmt.setString(2, message.getEmail());
            pstmt.setString(3, message.getPhone());
            pstmt.setString(4, message.getSubject());
            pstmt.setString(5, message.getMessage());

            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Get all contact messages (for admin)
     */
    public List<ContactMessage> getAllContactMessages() throws SQLException {
        List<ContactMessage> messages = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_MESSAGES);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                ContactMessage message = new ContactMessage();
                message.setId(rs.getInt("message_id"));
                message.setFullName(rs.getString("full_name"));
                message.setEmail(rs.getString("email"));
                message.setPhone(rs.getString("phone"));
                message.setSubject(rs.getString("subject"));
                message.setMessage(rs.getString("message"));
                message.setCreatedAt(rs.getTimestamp("created_at"));
                messages.add(message);
            }
        }

        return messages;
    }
}
