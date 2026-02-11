package com.silvercare.util;

import com.silvercare.model.Expense;
import com.silvercare.model.MonthlyReport;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Utility Bean for Report SQL operations
 */
@Component
public class ReportDBUtil {

    public MonthlyReport getMonthlyReport(int year, int month) throws SQLException {
        MonthlyReport report = new MonthlyReport();
        report.setYear(year);
        report.setMonth(getMonthName(month));

        YearMonth yearMonth = YearMonth.of(year, month);
        Date startDate = Date.valueOf(yearMonth.atDay(1));
        Date endDate = Date.valueOf(yearMonth.atEndOfMonth());

        try (Connection conn = DBConnection.getConnection()) {
            calculateRevenue(conn, report, startDate, endDate);
            calculateEmployeeCosts(conn, report, startDate, endDate);
            calculateOtherExpenses(conn, report, startDate, endDate);
            calculateTotals(report);
            report.setBookings(getBookingSummaries(conn, startDate, endDate));
            report.setEmployeePayments(getEmployeePayments(conn, startDate, endDate));
            report.setExpenses(getExpenses(conn, startDate, endDate));
        }

        return report;
    }

    private void calculateRevenue(Connection conn, MonthlyReport report, Date startDate, Date endDate)
            throws SQLException {
        String sql = "SELECT COUNT(*) as total_bookings, " +
                "COALESCE(SUM(total_amount), 0) as gross_revenue, " +
                "COALESCE(SUM(gst_amount), 0) as total_gst " +
                "FROM silvercare.booking " +
                "WHERE status = 'Confirmed' " +
                "AND created_at >= ? AND created_at < ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, new Timestamp(startDate.getTime()));
            stmt.setTimestamp(2, new Timestamp(endDate.getTime() + 86400000));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    report.setTotalBookings(rs.getInt("total_bookings"));
                    report.setGrossRevenue(rs.getBigDecimal("gross_revenue"));
                    report.setTotalGst(rs.getBigDecimal("total_gst"));
                    report.setNetRevenue(
                            report.getGrossRevenue().subtract(report.getTotalGst()));
                }
            }
        }
    }

    private void calculateEmployeeCosts(Connection conn, MonthlyReport report, Date startDate, Date endDate)
            throws SQLException {
        String sql = "SELECT COALESCE(SUM(esa.pay_amount), 0) as employee_costs " +
                "FROM silvercare.employee_service_assignments esa " +
                "JOIN silvercare.booking_detail bd ON esa.booking_detail_id = bd.booking_detail_id " +
                "JOIN silvercare.booking b ON bd.booking_id = b.booking_id " +
                "WHERE b.created_at >= ? AND b.created_at < ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, new Timestamp(startDate.getTime()));
            stmt.setTimestamp(2, new Timestamp(endDate.getTime() + 86400000));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    report.setEmployeeCosts(rs.getBigDecimal("employee_costs"));
                }
            }
        }
    }

    private void calculateOtherExpenses(Connection conn, MonthlyReport report, Date startDate, Date endDate)
            throws SQLException {
        String sql = "SELECT COALESCE(SUM(amount), 0) as other_expenses " +
                "FROM silvercare.expenses " +
                "WHERE expense_date >= ? AND expense_date <= ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, startDate);
            stmt.setDate(2, endDate);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    report.setOtherExpenses(rs.getBigDecimal("other_expenses"));
                }
            }
        }
    }

    private void calculateTotals(MonthlyReport report) {
        BigDecimal totalExpenses = report.getEmployeeCosts().add(report.getOtherExpenses());
        report.setTotalExpenses(totalExpenses);

        BigDecimal netProfit = report.getNetRevenue().subtract(totalExpenses);
        report.setNetProfit(netProfit);

        if (report.getGrossRevenue().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal profitMargin = netProfit
                    .divide(report.getGrossRevenue(), 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"));
            report.setProfitMargin(profitMargin);
        } else {
            report.setProfitMargin(BigDecimal.ZERO);
        }
    }

    private List<MonthlyReport.BookingSummary> getBookingSummaries(Connection conn, Date startDate, Date endDate)
            throws SQLException {
        List<MonthlyReport.BookingSummary> bookings = new ArrayList<>();
        String sql = "SELECT b.booking_id as id, c.full_name, b.booking_date, b.total_amount, b.status " +
                "FROM silvercare.booking b " +
                "JOIN silvercare.customer c ON b.customer_id = c.customer_id " +
                "WHERE b.status = 'Confirmed' " +
                "AND b.created_at >= ? AND b.created_at < ? " +
                "ORDER BY b.booking_date DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, new Timestamp(startDate.getTime()));
            stmt.setTimestamp(2, new Timestamp(endDate.getTime() + 86400000));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MonthlyReport.BookingSummary booking = new MonthlyReport.BookingSummary();
                    booking.setBookingId(rs.getInt("id"));
                    booking.setCustomerName(rs.getString("full_name"));
                    booking.setBookingDate(rs.getTimestamp("booking_date").toString());
                    booking.setAmount(rs.getBigDecimal("total_amount"));
                    booking.setStatus(rs.getString("status"));
                    bookings.add(booking);
                }
            }
        }
        return bookings;
    }

    private List<MonthlyReport.EmployeePayment> getEmployeePayments(Connection conn, Date startDate, Date endDate)
            throws SQLException {
        List<MonthlyReport.EmployeePayment> payments = new ArrayList<>();
        String sql = "SELECT e.full_name, " +
                "SUM(esa.hours_worked) as total_hours, " +
                "e.hourly_rate, " +
                "SUM(esa.pay_amount) as total_pay, " +
                "esa.payment_status " +
                "FROM silvercare.employee_service_assignments esa " +
                "JOIN silvercare.employees e ON esa.employee_id = e.employee_id " +
                "JOIN silvercare.booking_detail bd ON esa.booking_detail_id = bd.booking_detail_id " +
                "JOIN silvercare.booking b ON bd.booking_id = b.booking_id " +
                "WHERE b.created_at >= ? AND b.created_at < ? " +
                "GROUP BY e.employee_id, e.full_name, e.hourly_rate, esa.payment_status " +
                "ORDER BY e.full_name";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, new Timestamp(startDate.getTime()));
            stmt.setTimestamp(2, new Timestamp(endDate.getTime() + 86400000));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MonthlyReport.EmployeePayment payment = new MonthlyReport.EmployeePayment();
                    payment.setEmployeeName(rs.getString("full_name"));
                    payment.setHoursWorked(rs.getBigDecimal("total_hours"));
                    payment.setHourlyRate(rs.getBigDecimal("hourly_rate"));
                    payment.setTotalPay(rs.getBigDecimal("total_pay"));
                    payment.setPaymentStatus(rs.getString("payment_status"));
                    payments.add(payment);
                }
            }
        }
        return payments;
    }

    private List<Expense> getExpenses(Connection conn, Date startDate, Date endDate) throws SQLException {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM silvercare.expenses " +
                "WHERE expense_date >= ? AND expense_date <= ? " +
                "ORDER BY expense_date DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, startDate);
            stmt.setDate(2, endDate);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Expense expense = new Expense();
                    expense.setId(rs.getInt("id"));
                    expense.setExpenseType(rs.getString("expense_type"));
                    expense.setDescription(rs.getString("description"));
                    expense.setAmount(rs.getBigDecimal("amount"));
                    expense.setExpenseDate(rs.getDate("expense_date"));
                    expense.setCategory(rs.getString("category"));
                    expense.setCreatedBy(rs.getInt("created_by"));
                    expense.setCreatedAt(rs.getTimestamp("created_at"));
                    expenses.add(expense);
                }
            }
        }
        return expenses;
    }

    private String getMonthName(int month) {
        String[] months = { "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December" };
        return months[month - 1];
    }
}
