package com.silvercare.dao;

import java.sql.SQLException;
import java.util.List;
import com.silvercare.model.AuditLog;
import com.silvercare.util.AuditLogDBUtil;

public class AuditLogDAO {

    private final AuditLogDBUtil auditLogDBUtil;

    public AuditLogDAO() {
        this.auditLogDBUtil = new AuditLogDBUtil();
    }

    public boolean logAction(int adminId, String action, String details) throws SQLException {
        return auditLogDBUtil.logAction(adminId, action, details);
    }

    public List<AuditLog> getAllLogs() throws SQLException {
        return auditLogDBUtil.getAllLogs();
    }
}
