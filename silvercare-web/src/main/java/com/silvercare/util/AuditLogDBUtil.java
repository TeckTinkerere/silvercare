package com.silvercare.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.silvercare.model.AuditLog;

public class AuditLogDBUtil {

    private static final String INSERT_LOG = "INSERT INTO silvercare.audit_log (admin_id, action, details) VALUES (?, ?, ?)";
    private static final String SELECT_ALL_LOGS = "SELECT al.*, c.full_name as admin_name FROM silvercare.audit_log al "
            +
            "JOIN silvercare.customer c ON al.admin_id = c.customer_id " +
            "ORDER BY al.created_at DESC";

    public boolean logAction(int adminId, String action, String details) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(INSERT_LOG)) {
            pstmt.setInt(1, adminId);
            pstmt.setString(2, action);
            pstmt.setString(3, details);
            return pstmt.executeUpdate() > 0;
        }
    }

    public List<AuditLog> getAllLogs() throws SQLException {
        List<AuditLog> logs = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_LOGS);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                AuditLog log = new AuditLog();
                log.setId(rs.getInt("log_id"));
                log.setAdminId(rs.getInt("admin_id"));
                log.setAdminName(rs.getString("admin_name"));
                log.setAction(rs.getString("action"));
                log.setDetails(rs.getString("details"));
                log.setCreatedAt(rs.getTimestamp("created_at"));
                logs.add(log);
            }
        }
        return logs;
    }
}
