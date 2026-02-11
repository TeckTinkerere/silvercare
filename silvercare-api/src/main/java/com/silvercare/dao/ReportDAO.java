package com.silvercare.dao;

import com.silvercare.model.MonthlyReport;
import com.silvercare.util.ReportDBUtil;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Data Access Object for Reports
 * Delegates SQL operations to ReportDBUtil
 */
@Repository
public class ReportDAO {

    private final ReportDBUtil reportDBUtil;

    @Autowired
    public ReportDAO(ReportDBUtil reportDBUtil) {
        this.reportDBUtil = reportDBUtil;
    }

    public ReportDAO() {
        this.reportDBUtil = new ReportDBUtil();
    }

    public MonthlyReport getMonthlyReport(int year, int month) throws SQLException {
        return reportDBUtil.getMonthlyReport(year, month);
    }

    public MonthlyReport getCurrentMonthReport() throws SQLException {
        return reportDBUtil.getMonthlyReport(
                java.time.LocalDate.now().getYear(),
                java.time.LocalDate.now().getMonthValue());
    }
}
