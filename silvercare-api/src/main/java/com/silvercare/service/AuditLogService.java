package com.silvercare.service;

import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.silvercare.dao.AuditLogDAO;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogDAO auditLogDAO;

    public boolean logAction(int adminId, String action, String details) {
        try {
            return auditLogDAO.logAction(adminId, action, details);
        } catch (SQLException e) {
            System.err.println("Failed to log admin action: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
