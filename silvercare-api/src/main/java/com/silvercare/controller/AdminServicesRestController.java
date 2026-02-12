package com.silvercare.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.silvercare.model.Service;
import com.silvercare.dao.ServiceDAO;
import com.silvercare.service.AuditLogService;

/**
 * REST Controller for Admin Service Management
 * Provides CRUD operations for services and categories
 */
@RestController
@RequestMapping("/admin/services")
public class AdminServicesRestController {

    @Autowired
    private ServiceDAO serviceDAO;

    @Autowired
    private AuditLogService auditLogService;

    /**
     * Get all services with category info
     * GET /admin/services
     */
    @GetMapping
    public ResponseEntity<?> getAllServices() {
        try {
            List<Service> services = serviceDAO.getAllServicesWithCategory();
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
     * GET /admin/services/{id}
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

    /**
     * Create new service
     * POST /admin/services
     */
    @PostMapping
    public ResponseEntity<?> createService(@RequestBody Service service,
            @RequestHeader(value = "X-Admin-Id", required = false) Integer adminId) {
        try {
            System.out.println("=== CREATE SERVICE REST ENDPOINT ===");
            System.out.println("Received service: " + service);
            System.out.println("Name: " + service.getName());
            System.out.println("Category ID: " + service.getCategoryId());
            System.out.println("Price: " + service.getPrice());

            if (service.getName() == null || service.getName().trim().isEmpty()) {
                System.out.println("ERROR: Service name is required");
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Service name is required"));
            }
            if (service.getPrice() == null || service.getPrice().doubleValue() <= 0) {
                System.out.println("ERROR: Valid price is required");
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Valid price is required"));
            }

            System.out.println("Calling serviceDAO.addService()");
            boolean success = serviceDAO.addService(service);
            System.out.println("addService result: " + success);

            if (success) {
                if (adminId != null) {
                    auditLogService.logAction(adminId, "CREATE_SERVICE",
                            "Service Name: " + service.getName() + ", Price: " + service.getPrice());
                }
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Service created successfully"));
            } else {
                System.out.println("ERROR: Failed to create service");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Failed to create service"));
            }
        } catch (SQLException e) {
            System.out.println("ERROR: SQLException - " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        } catch (Exception e) {
            System.out.println("ERROR: Exception - " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error: " + e.getMessage()));
        }
    }

    /**
     * Update existing service
     * PUT /admin/services/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateService(@PathVariable("id") int serviceId, @RequestBody Service service,
            @RequestHeader(value = "X-Admin-Id", required = false) Integer adminId) {
        try {
            service.setId(serviceId);

            if (service.getName() == null || service.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Service name is required"));
            }
            if (service.getPrice() == null || service.getPrice().doubleValue() <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Valid price is required"));
            }

            boolean success = serviceDAO.updateService(service);
            if (success) {
                if (adminId != null) {
                    auditLogService.logAction(adminId, "UPDATE_SERVICE",
                            "Service ID: " + serviceId + ", Name: " + service.getName());
                }
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Service updated successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Service not found"));
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    /**
     * Delete service (soft delete)
     * DELETE /admin/services/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteService(@PathVariable("id") int serviceId,
            @RequestHeader(value = "X-Admin-Id", required = false) Integer adminId) {
        try {
            boolean success = serviceDAO.deleteService(serviceId);
            if (success) {
                if (adminId != null) {
                    auditLogService.logAction(adminId, "DELETE_SERVICE", "Service ID: " + serviceId);
                }
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Service deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Service not found"));
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    /**
     * Get all categories
     * GET /admin/services/categories
     */
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

    /**
     * Get dashboard statistics
     * GET /admin/services/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getServiceStats() {
        try {
            List<Service> services = serviceDAO.getAllServices();
            List<Map<String, Object>> categories = serviceDAO.getCategories();

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", Map.of(
                            "totalServices", services.size(),
                            "totalCategories", categories.size())));
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    /**
     * Add category
     * POST /admin/services/categories
     */
    @PostMapping("/categories")
    public ResponseEntity<?> addCategory(@RequestBody Map<String, String> categoryData,
            @RequestHeader(value = "X-Admin-Id", required = false) Integer adminId) {
        try {
            String name = categoryData.get("name");
            String description = categoryData.get("description");
            String icon = categoryData.get("icon");

            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Category name is required"));
            }

            boolean success = serviceDAO.addCategory(name, description, icon);
            if (success) {
                if (adminId != null) {
                    auditLogService.logAction(adminId, "CREATE_CATEGORY", "Category Name: " + name);
                }
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Category added successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Failed to add category"));
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    /**
     * Update category
     * PUT /admin/services/categories/{id}
     */
    @PutMapping("/categories/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") int categoryId,
            @RequestBody Map<String, String> categoryData,
            @RequestHeader(value = "X-Admin-Id", required = false) Integer adminId) {
        try {
            String name = categoryData.get("name");
            String description = categoryData.get("description");
            String icon = categoryData.get("icon");

            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Category name is required"));
            }

            boolean success = serviceDAO.updateCategory(categoryId, name, description, icon);
            if (success) {
                if (adminId != null) {
                    auditLogService.logAction(adminId, "UPDATE_CATEGORY",
                            "Category ID: " + categoryId + ", Name: " + name);
                }
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Category updated successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Category not found"));
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    /**
     * Delete category
     * DELETE /admin/services/categories/{id}
     */
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") int categoryId,
            @RequestHeader(value = "X-Admin-Id", required = false) Integer adminId) {
        try {
            boolean success = serviceDAO.deleteCategory(categoryId);
            if (success) {
                if (adminId != null) {
                    auditLogService.logAction(adminId, "DELETE_CATEGORY", "Category ID: " + categoryId);
                }
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Category deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Category not found"));
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }
}
