package com.silvercare.model;

import java.math.BigDecimal;
import java.util.List;

public class MonthlyReport {
    private String month;
    private int year;
    private int totalBookings;
    private BigDecimal grossRevenue;
    private BigDecimal totalGst;
    private BigDecimal netRevenue;
    private BigDecimal employeeCosts;
    private BigDecimal otherExpenses;
    private BigDecimal totalExpenses;
    private BigDecimal netProfit;
    private BigDecimal profitMargin;
    
    // Detailed breakdowns
    private List<BookingSummary> bookings;
    private List<EmployeePayment> employeePayments;
    private List<Expense> expenses;

    // Constructors
    public MonthlyReport() {}

    // Getters and Setters
    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getTotalBookings() { return totalBookings; }
    public void setTotalBookings(int totalBookings) { this.totalBookings = totalBookings; }

    public BigDecimal getGrossRevenue() { return grossRevenue; }
    public void setGrossRevenue(BigDecimal grossRevenue) { this.grossRevenue = grossRevenue; }

    public BigDecimal getTotalGst() { return totalGst; }
    public void setTotalGst(BigDecimal totalGst) { this.totalGst = totalGst; }

    public BigDecimal getNetRevenue() { return netRevenue; }
    public void setNetRevenue(BigDecimal netRevenue) { this.netRevenue = netRevenue; }

    public BigDecimal getEmployeeCosts() { return employeeCosts; }
    public void setEmployeeCosts(BigDecimal employeeCosts) { this.employeeCosts = employeeCosts; }

    public BigDecimal getOtherExpenses() { return otherExpenses; }
    public void setOtherExpenses(BigDecimal otherExpenses) { this.otherExpenses = otherExpenses; }

    public BigDecimal getTotalExpenses() { return totalExpenses; }
    public void setTotalExpenses(BigDecimal totalExpenses) { this.totalExpenses = totalExpenses; }

    public BigDecimal getNetProfit() { return netProfit; }
    public void setNetProfit(BigDecimal netProfit) { this.netProfit = netProfit; }

    public BigDecimal getProfitMargin() { return profitMargin; }
    public void setProfitMargin(BigDecimal profitMargin) { this.profitMargin = profitMargin; }

    public List<BookingSummary> getBookings() { return bookings; }
    public void setBookings(List<BookingSummary> bookings) { this.bookings = bookings; }

    public List<EmployeePayment> getEmployeePayments() { return employeePayments; }
    public void setEmployeePayments(List<EmployeePayment> employeePayments) { 
        this.employeePayments = employeePayments; 
    }

    public List<Expense> getExpenses() { return expenses; }
    public void setExpenses(List<Expense> expenses) { this.expenses = expenses; }

    // Inner classes for detailed breakdowns
    public static class BookingSummary {
        private int bookingId;
        private String customerName;
        private String bookingDate;
        private BigDecimal amount;
        private String status;

        // Getters and Setters
        public int getBookingId() { return bookingId; }
        public void setBookingId(int bookingId) { this.bookingId = bookingId; }

        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }

        public String getBookingDate() { return bookingDate; }
        public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }

        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class EmployeePayment {
        private String employeeName;
        private BigDecimal hoursWorked;
        private BigDecimal hourlyRate;
        private BigDecimal totalPay;
        private String paymentStatus;

        // Getters and Setters
        public String getEmployeeName() { return employeeName; }
        public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

        public BigDecimal getHoursWorked() { return hoursWorked; }
        public void setHoursWorked(BigDecimal hoursWorked) { this.hoursWorked = hoursWorked; }

        public BigDecimal getHourlyRate() { return hourlyRate; }
        public void setHourlyRate(BigDecimal hourlyRate) { this.hourlyRate = hourlyRate; }

        public BigDecimal getTotalPay() { return totalPay; }
        public void setTotalPay(BigDecimal totalPay) { this.totalPay = totalPay; }

        public String getPaymentStatus() { return paymentStatus; }
        public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    }
}
