package com.silvercare.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.silvercare.model.Service;
import com.silvercare.dao.ServiceDAO;

@RestController
@RequestMapping("/services")
public class ServicesRestController {

    @Autowired
    private ServiceDAO serviceDAO;

    @GetMapping
    public ResponseEntity<?> getAllServices() {
        try {
            List<Service> services = serviceDAO.getAllServices();
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", services,
                    "count", services.size()));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    /**
     * Get service by ID
     * GET /api/services/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getServiceById(@PathVariable("id") int serviceId) {
        try {
            Service service = serviceDAO.getServiceById(serviceId);
            if (service != null) {
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "data", service));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Service not found"));
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        try {
            List<Map<String, Object>> categories = serviceDAO.getCategories();
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", categories,
                    "count", categories.size()));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<?> getServicesByCategory(@PathVariable("id") int categoryId) {
        try {
            List<Service> services = serviceDAO.getServicesByCategory(categoryId);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "categoryId", categoryId,
                    "data", services,
                    "count", services.size()));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchServices(@RequestParam(value = "q", required = false) String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Search term is required"));
        }

        try {
            List<Service> services = serviceDAO.searchServices(searchTerm.trim());
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "searchTerm", searchTerm,
                    "data", services,
                    "count", services.size()));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }
}
