package com.silvercare.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.silvercare.util.AdminReportDBUtil;

/**
 * Data Access Object for Admin Reports
 * Contains field mappings and delegates SQL operations to utility beans
 * This follows the assignment requirement of separating concerns
 */
@Repository
public class AdminReportDAO {

    private final AdminReportDBUtil adminReportDBUtil;

    @Autowired
    public AdminReportDAO(AdminReportDBUtil adminReportDBUtil) {
        this.adminReportDBUtil = adminReportDBUtil;
    }

    /**
     * Default constructor for legacy support
     */
    public AdminReportDAO() {
        this.adminReportDBUtil = new AdminReportDBUtil();
    }

    /**
     * Get top clients - delegates to utility bean
     */
    public List<Map<String, Object>> getTopClients() throws SQLException {
        return adminReportDBUtil.getTopClients();
    }

    /**
     * Get service ratings - delegates to utility bean
     */
    public List<Map<String, Object>> getServiceRatings() throws SQLException {
        return adminReportDBUtil.getServiceRatings();
    }

    /**
     * Get bookings by date range - delegates to utility bean
     */
    public List<Map<String, Object>> getBookingsByDateRange(String startDate, String endDate) throws SQLException {
        return adminReportDBUtil.getBookingsByDateRange(startDate, endDate);
    }

    /**
     * Get popular services - delegates to utility bean
     */
    public List<Map<String, Object>> getPopularServices() throws SQLException {
        return adminReportDBUtil.getPopularServices();
    }

    /**
     * Get clients by area - delegates to utility bean
     */
    public List<Map<String, Object>> getClientsByArea() throws SQLException {
        return adminReportDBUtil.getClientsByArea();
    }

    /**
     * Get monthly revenue - delegates to utility bean
     */
    public List<Map<String, Object>> getMonthlyRevenue() throws SQLException {
        return adminReportDBUtil.getMonthlyRevenue();
    }
}
