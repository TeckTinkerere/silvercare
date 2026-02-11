package com.silvercare.dao;

import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.silvercare.model.AuditLog;
import com.silvercare.util.AuditLogDBUtil;

@Repository
public class AuditLogDAO {

    private final AuditLogDBUtil auditLogDBUtil;

    @Autowired
    public AuditLogDAO(AuditLogDBUtil auditLogDBUtil) {
        this.auditLogDBUtil = auditLogDBUtil;
    }

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
