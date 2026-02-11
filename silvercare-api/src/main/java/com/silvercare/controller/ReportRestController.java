package com.silvercare.controller;

import com.silvercare.model.MonthlyReport;
import com.silvercare.dao.ReportDAO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;

/**
 * REST Controller for Financial Reports
 */
@RestController
@RequestMapping("/reports")
public class ReportRestController {

    @Autowired
    private ReportDAO reportDAO;

    /**
     * Get monthly report as JSON
     * GET /reports/monthly?year=2026&month=2
     */
    @GetMapping("/monthly")
    public ResponseEntity<?> getMonthlyReport(
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month) {
        try {
            // Default to current month if not specified
            if (year == null || month == null) {
                LocalDate now = LocalDate.now();
                year = (year == null) ? now.getYear() : year;
                month = (month == null) ? now.getMonthValue() : month;
            }

            // Validate month
            if (month < 1 || month > 12) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Month must be between 1 and 12"));
            }

            MonthlyReport report = reportDAO.getMonthlyReport(year, month);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", report));
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    /**
     * Download monthly report as CSV or Excel
     * GET
     * /reports/monthly/download?year=2026&month=2&format=excel&filename=MyReport
     */
    @GetMapping("/monthly/download")
    public ResponseEntity<?> downloadMonthlyReport(
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month,
            @RequestParam(value = "format", defaultValue = "csv") String format,
            @RequestParam(value = "filename", required = false) String customFilename) {
        try {
            // Default to current month if not specified
            if (year == null || month == null) {
                LocalDate now = LocalDate.now();
                year = (year == null) ? now.getYear() : year;
                month = (month == null) ? now.getMonthValue() : month;
            }

            MonthlyReport report = reportDAO.getMonthlyReport(year, month);

            // Generate filename
            String filename;
            if (customFilename != null && !customFilename.trim().isEmpty()) {
                filename = sanitizeFilename(customFilename);
            } else {
                filename = String.format("SilverCare_Monthly_Report_%s_%d", report.getMonth(), year);
            }

            if ("excel".equalsIgnoreCase(format) || "xlsx".equalsIgnoreCase(format)) {
                byte[] excelData = generateExcel(report);
                filename += ".xlsx";

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(
                        MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
                headers.setContentDispositionFormData("attachment", filename);
                headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

                return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
            } else if ("csv".equalsIgnoreCase(format)) {
                byte[] csvData = generateCSV(report);
                filename += ".csv";

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.parseMediaType("text/csv"));
                headers.setContentDispositionFormData("attachment", filename);
                headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

                return new ResponseEntity<>(csvData, headers, HttpStatus.OK);
            } else {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Unsupported format. Use 'csv' or 'excel'"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error generating report: " + e.getMessage()));
        }
    }

    /**
     * Get current month report
     */
    @GetMapping("/current")
    public ResponseEntity<?> getCurrentMonthReport() {
        try {
            MonthlyReport report = reportDAO.getCurrentMonthReport();
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", report));
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    /**
     * Generate CSV report
     */
    private byte[] generateCSV(MonthlyReport report) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(baos);

        // Header
        writer.println("SilverCare Monthly Financial Report");
        writer.println("Month," + report.getMonth() + " " + report.getYear());
        writer.println();

        // Summary Section
        writer.println("FINANCIAL SUMMARY");
        writer.println("Metric,Amount (SGD)");
        writer.println("Total Bookings," + report.getTotalBookings());
        writer.println("Gross Revenue," + formatMoney(report.getGrossRevenue()));
        writer.println("GST (9%)," + formatMoney(report.getTotalGst()));
        writer.println("Net Revenue," + formatMoney(report.getNetRevenue()));
        writer.println();
        writer.println("EXPENSES");
        writer.println("Employee Costs," + formatMoney(report.getEmployeeCosts()));
        writer.println("Other Expenses," + formatMoney(report.getOtherExpenses()));
        writer.println("Total Expenses," + formatMoney(report.getTotalExpenses()));
        writer.println();
        writer.println("PROFIT/LOSS");
        writer.println("Net Profit," + formatMoney(report.getNetProfit()));
        writer.println("Profit Margin," + formatPercentage(report.getProfitMargin()) + "%");
        writer.println();

        // Bookings Detail
        if (report.getBookings() != null && !report.getBookings().isEmpty()) {
            writer.println("BOOKING DETAILS");
            writer.println("Booking ID,Customer Name,Booking Date,Amount,Status");
            for (MonthlyReport.BookingSummary booking : report.getBookings()) {
                writer.println(String.format("%d,%s,%s,%s,%s",
                        booking.getBookingId(),
                        escapeCsv(booking.getCustomerName()),
                        booking.getBookingDate(),
                        formatMoney(booking.getAmount()),
                        booking.getStatus()));
            }
            writer.println();
        }

        // Employee Payments Detail
        if (report.getEmployeePayments() != null && !report.getEmployeePayments().isEmpty()) {
            writer.println("EMPLOYEE PAYMENTS");
            writer.println("Employee Name,Hours Worked,Hourly Rate,Total Pay,Payment Status");
            for (MonthlyReport.EmployeePayment payment : report.getEmployeePayments()) {
                writer.println(String.format("%s,%.2f,%s,%s,%s",
                        escapeCsv(payment.getEmployeeName()),
                        payment.getHoursWorked(),
                        formatMoney(payment.getHourlyRate()),
                        formatMoney(payment.getTotalPay()),
                        payment.getPaymentStatus()));
            }
            writer.println();
        }

        // Other Expenses Detail
        if (report.getExpenses() != null && !report.getExpenses().isEmpty()) {
            writer.println("OTHER EXPENSES");
            writer.println("Type,Description,Amount,Date,Category");
            for (com.silvercare.model.Expense expense : report.getExpenses()) {
                writer.println(String.format("%s,%s,%s,%s,%s",
                        escapeCsv(expense.getExpenseType()),
                        escapeCsv(expense.getDescription()),
                        formatMoney(expense.getAmount()),
                        expense.getExpenseDate(),
                        escapeCsv(expense.getCategory())));
            }
        }

        writer.flush();
        writer.close();
        return baos.toByteArray();
    }

    private String formatMoney(BigDecimal amount) {
        if (amount == null)
            return "0.00";
        return String.format("%.2f", amount);
    }

    private String formatPercentage(BigDecimal percentage) {
        if (percentage == null)
            return "0.00";
        return String.format("%.2f", percentage);
    }

    private String escapeCsv(String value) {
        if (value == null)
            return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private String sanitizeFilename(String filename) {
        // Remove invalid characters for filenames
        return filename.replaceAll("[^a-zA-Z0-9_\\-]", "_");
    }

    /**
     * Generate Excel report using Apache POI
     */
    private byte[] generateExcel(MonthlyReport report) throws Exception {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            // Create styles
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle titleStyle = createTitleStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);
            CellStyle percentStyle = createPercentStyle(workbook);
            CellStyle profitStyle = createProfitStyle(workbook, report.getNetProfit());

            // Summary Sheet
            Sheet summarySheet = workbook.createSheet("Financial Summary");
            createSummarySheet(summarySheet, report, titleStyle, headerStyle, currencyStyle, percentStyle, profitStyle);

            // Bookings Sheet
            if (report.getBookings() != null && !report.getBookings().isEmpty()) {
                Sheet bookingsSheet = workbook.createSheet("Bookings");
                createBookingsSheet(bookingsSheet, report, headerStyle, currencyStyle);
            }

            // Employee Payments Sheet
            if (report.getEmployeePayments() != null && !report.getEmployeePayments().isEmpty()) {
                Sheet employeeSheet = workbook.createSheet("Employee Payments");
                createEmployeeSheet(employeeSheet, report, headerStyle, currencyStyle);
            }

            // Expenses Sheet
            if (report.getExpenses() != null && !report.getExpenses().isEmpty()) {
                Sheet expensesSheet = workbook.createSheet("Other Expenses");
                createExpensesSheet(expensesSheet, report, headerStyle, currencyStyle);
            }

            // Write to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            return baos.toByteArray();
        }
    }

    private void createSummarySheet(Sheet sheet, MonthlyReport report, CellStyle titleStyle,
            CellStyle headerStyle, CellStyle currencyStyle,
            CellStyle percentStyle, CellStyle profitStyle) {
        int rowNum = 0;

        // Title
        Row titleRow = sheet.createRow(rowNum++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("SilverCare Monthly Financial Report");
        titleCell.setCellStyle(titleStyle);

        Row subtitleRow = sheet.createRow(rowNum++);
        subtitleRow.createCell(0).setCellValue(report.getMonth() + " " + report.getYear());
        rowNum++; // Empty row

        // Revenue Section
        createSectionHeader(sheet, rowNum++, "REVENUE", headerStyle);
        rowNum = createDataRow(sheet, rowNum, "Total Bookings", report.getTotalBookings(), null);
        rowNum = createDataRow(sheet, rowNum, "Gross Revenue", report.getGrossRevenue(), currencyStyle);
        rowNum = createDataRow(sheet, rowNum, "GST (9%)", report.getTotalGst(), currencyStyle);
        rowNum = createDataRow(sheet, rowNum, "Net Revenue", report.getNetRevenue(), currencyStyle);
        rowNum++; // Empty row

        // Expenses Section
        createSectionHeader(sheet, rowNum++, "EXPENSES", headerStyle);
        rowNum = createDataRow(sheet, rowNum, "Employee Costs", report.getEmployeeCosts(), currencyStyle);
        rowNum = createDataRow(sheet, rowNum, "Other Expenses", report.getOtherExpenses(), currencyStyle);
        rowNum = createDataRow(sheet, rowNum, "Total Expenses", report.getTotalExpenses(), currencyStyle);
        rowNum++; // Empty row

        // Profit Section
        createSectionHeader(sheet, rowNum++, "PROFIT/LOSS", headerStyle);
        rowNum = createDataRow(sheet, rowNum, "Net Profit", report.getNetProfit(), profitStyle);
        rowNum = createDataRow(sheet, rowNum, "Profit Margin", report.getProfitMargin(), percentStyle);

        // Auto-size columns
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }

    private void createBookingsSheet(Sheet sheet, MonthlyReport report, CellStyle headerStyle,
            CellStyle currencyStyle) {
        int rowNum = 0;

        // Header
        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = { "Booking ID", "Customer Name", "Booking Date", "Amount", "Status" };
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Data
        for (MonthlyReport.BookingSummary booking : report.getBookings()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(booking.getBookingId());
            row.createCell(1).setCellValue(booking.getCustomerName());
            row.createCell(2).setCellValue(booking.getBookingDate());

            Cell amountCell = row.createCell(3);
            amountCell.setCellValue(booking.getAmount().doubleValue());
            amountCell.setCellStyle(currencyStyle);

            row.createCell(4).setCellValue(booking.getStatus());
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void createEmployeeSheet(Sheet sheet, MonthlyReport report, CellStyle headerStyle,
            CellStyle currencyStyle) {
        int rowNum = 0;

        // Header
        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = { "Employee Name", "Hours Worked", "Hourly Rate", "Total Pay", "Payment Status" };
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Data
        for (MonthlyReport.EmployeePayment payment : report.getEmployeePayments()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(payment.getEmployeeName());
            row.createCell(1).setCellValue(payment.getHoursWorked().doubleValue());

            Cell rateCell = row.createCell(2);
            rateCell.setCellValue(payment.getHourlyRate().doubleValue());
            rateCell.setCellStyle(currencyStyle);

            Cell payCell = row.createCell(3);
            payCell.setCellValue(payment.getTotalPay().doubleValue());
            payCell.setCellStyle(currencyStyle);

            row.createCell(4).setCellValue(payment.getPaymentStatus());
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void createExpensesSheet(Sheet sheet, MonthlyReport report, CellStyle headerStyle,
            CellStyle currencyStyle) {
        int rowNum = 0;

        // Header
        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = { "Type", "Description", "Amount", "Date", "Category" };
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Data
        for (com.silvercare.model.Expense expense : report.getExpenses()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(expense.getExpenseType());
            row.createCell(1).setCellValue(expense.getDescription());

            Cell amountCell = row.createCell(2);
            amountCell.setCellValue(expense.getAmount().doubleValue());
            amountCell.setCellStyle(currencyStyle);

            row.createCell(3).setCellValue(expense.getExpenseDate().toString());
            row.createCell(4).setCellValue(expense.getCategory() != null ? expense.getCategory() : "N/A");
        }

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void createSectionHeader(Sheet sheet, int rowNum, String title, CellStyle style) {
        Row row = sheet.createRow(rowNum);
        Cell cell = row.createCell(0);
        cell.setCellValue(title);
        cell.setCellStyle(style);
    }

    private int createDataRow(Sheet sheet, int rowNum, String label, Object value, CellStyle valueStyle) {
        Row row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue(label);

        Cell valueCell = row.createCell(1);
        if (value instanceof BigDecimal) {
            valueCell.setCellValue(((BigDecimal) value).doubleValue());
        } else if (value instanceof Integer) {
            valueCell.setCellValue((Integer) value);
        } else {
            valueCell.setCellValue(value.toString());
        }

        if (valueStyle != null) {
            valueCell.setCellStyle(valueStyle);
        }

        return rowNum + 1;
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        style.setFont(font);
        return style;
    }

    private CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("$#,##0.00"));
        return style;
    }

    private CellStyle createPercentStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("0.00\"%\""));
        return style;
    }

    private CellStyle createProfitStyle(Workbook workbook, BigDecimal netProfit) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        if (netProfit.compareTo(BigDecimal.ZERO) >= 0) {
            font.setColor(IndexedColors.GREEN.getIndex());
        } else {
            font.setColor(IndexedColors.RED.getIndex());
        }
        style.setFont(font);
        style.setDataFormat(workbook.createDataFormat().getFormat("$#,##0.00"));
        return style;
    }
}
