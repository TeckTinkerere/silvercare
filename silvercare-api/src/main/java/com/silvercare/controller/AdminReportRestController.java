package com.silvercare.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for Admin Reports
 * Provides endpoints for viewing and downloading reports
 */
@RestController
@RequestMapping("/admin/reports")
public class AdminReportRestController {

    @Autowired
    private com.silvercare.dao.AdminReportDAO adminReportDAO;

    /**
     * Get top clients report
     * GET /admin/reports/top-clients
     */
    @GetMapping("/top-clients")
    public ResponseEntity<?> getTopClients() {
        try {
            List<Map<String, Object>> data = adminReportDAO.getTopClients();
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", data,
                    "count", data.size()));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    /**
     * Get service ratings report
     * GET /admin/reports/service-ratings
     */
    @GetMapping("/service-ratings")
    public ResponseEntity<?> getServiceRatings() {
        try {
            List<Map<String, Object>> data = adminReportDAO.getServiceRatings();
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", data,
                    "count", data.size()));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    /**
     * Get popular services report
     * GET /admin/reports/popular-services
     */
    @GetMapping("/popular-services")
    public ResponseEntity<?> getPopularServices() {
        try {
            List<Map<String, Object>> data = adminReportDAO.getPopularServices();
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", data,
                    "count", data.size()));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    /**
     * Get monthly revenue report
     * GET /admin/reports/monthly-revenue
     */
    @GetMapping("/monthly-revenue")
    public ResponseEntity<?> getMonthlyRevenue() {
        try {
            List<Map<String, Object>> data = adminReportDAO.getMonthlyRevenue();
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", data,
                    "count", data.size()));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    /**
     * Get clients by area report
     * GET /admin/reports/clients-by-area
     */
    @GetMapping("/clients-by-area")
    public ResponseEntity<?> getClientsByArea() {
        try {
            List<Map<String, Object>> data = adminReportDAO.getClientsByArea();
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", data,
                    "count", data.size()));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    /**
     * Download report as Excel
     * GET /admin/reports/download?type=top-clients
     */
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadReport(
            @RequestParam("type") String reportType,
            @RequestParam(value = "start", required = false) String startDate,
            @RequestParam(value = "end", required = false) String endDate) {
        try {
            List<Map<String, Object>> data;
            String reportTitle;

            // Get data based on report type
            switch (reportType) {
                case "top-clients":
                    data = adminReportDAO.getTopClients();
                    reportTitle = "Top Clients Report";
                    break;
                case "service-ratings":
                    data = adminReportDAO.getServiceRatings();
                    reportTitle = "Service Ratings Report";
                    break;
                case "popular-services":
                    data = adminReportDAO.getPopularServices();
                    reportTitle = "Popular Services Report";
                    break;
                case "monthly-revenue":
                    data = adminReportDAO.getMonthlyRevenue();
                    reportTitle = "Monthly Revenue Report";
                    break;
                case "clients-by-area":
                    data = adminReportDAO.getClientsByArea();
                    reportTitle = "Clients by Area Report";
                    break;
                case "bookings":
                    if (startDate != null && endDate != null) {
                        data = adminReportDAO.getBookingsByDateRange(startDate, endDate);
                        reportTitle = "Bookings Report (" + startDate + " to " + endDate + ")";
                    } else {
                        return ResponseEntity.badRequest().build();
                    }
                    break;
                default:
                    return ResponseEntity.badRequest().build();
            }

            // Generate Excel file
            byte[] excelBytes = generateExcelReport(data, reportTitle);

            // Set headers for file download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            String filename = reportType + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xlsx";
            headers.setContentDispositionFormData("attachment", filename);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Generate Excel report from data
     */
    private byte[] generateExcelReport(List<Map<String, Object>> data, String reportTitle) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(reportTitle);

            // Create header style
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // Create data style
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);

            if (data.isEmpty()) {
                Row row = sheet.createRow(0);
                Cell cell = row.createCell(0);
                cell.setCellValue("No data available");
                return workbookToBytes(workbook);
            }

            // Create header row
            Row headerRow = sheet.createRow(0);
            List<String> columnNames = new java.util.ArrayList<>(data.get(0).keySet());
            for (int i = 0; i < columnNames.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(formatColumnName(columnNames.get(i)));
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 20 * 256); // Set column width
            }

            // Create data rows
            for (int i = 0; i < data.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Map<String, Object> rowData = data.get(i);

                for (int j = 0; j < columnNames.size(); j++) {
                    Cell cell = row.createCell(j);
                    Object value = rowData.get(columnNames.get(j));

                    if (value == null) {
                        cell.setCellValue("");
                    } else if (value instanceof Number) {
                        cell.setCellValue(((Number) value).doubleValue());
                    } else if (value instanceof Date) {
                        cell.setCellValue((Date) value);
                    } else {
                        cell.setCellValue(value.toString());
                    }
                    cell.setCellStyle(dataStyle);
                }
            }

            // Auto-size columns
            for (int i = 0; i < columnNames.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            return workbookToBytes(workbook);
        }
    }

    /**
     * Convert workbook to byte array
     */
    private byte[] workbookToBytes(Workbook workbook) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    /**
     * Format column name for display
     */
    private String formatColumnName(String columnName) {
        return columnName.substring(0, 1).toUpperCase() +
                columnName.substring(1).replace("_", " ");
    }
}
