package com.silvercare.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.silvercare.model.User;
import com.silvercare.dao.UserDAO;
import com.silvercare.service.AuditLogService;

@RestController
@RequestMapping("/users")
public class UserRestController {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private AuditLogService auditLogService;

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userDAO.getAllCustomers();
            // Return simply the list or a wrapped object depending on reference but list is
            // standard
            return ResponseEntity.ok(users);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        try {
            List<User> users = userDAO.getAllCustomers();
            User user = users.stream().filter(u -> u.getId() == id).findFirst().orElse(null);

            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");

            if (email == null || password == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Email and password are required"));
            }

            User user = userDAO.login(email, password);
            if (user != null) {
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "user", user));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid credentials"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user,
            @RequestHeader(value = "X-Admin-Id", required = false) Integer adminId) {
        try {
            String password = user.getPassword(); // Assuming password is set in User object
            if (password == null || password.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Password is required"));
            }

            boolean success = userDAO.register(user, password);
            if (success) {
                if (adminId != null) {
                    auditLogService.logAction(adminId, "CREATE_USER",
                            "User Email: " + user.getEmail() + ", Name: " + user.getFullName());
                }
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "User registered successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Registration failed"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody User user,
            @RequestHeader(value = "X-Admin-Id", required = false) Integer adminId) {
        try {
            if (user.getId() == 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "User ID is required"));
            }

            boolean success = userDAO.updateProfile(user);
            if (success) {
                if (adminId != null) {
                    auditLogService.logAction(adminId, "UPDATE_USER",
                            "User ID: " + user.getId() + ", Email: " + user.getEmail());
                }
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Profile updated successfully",
                        "user", user));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Update failed"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    @PostMapping("/tutorial/complete")
    public ResponseEntity<?> completeTutorial(@RequestBody Map<String, Integer> request) {
        try {
            Integer userId = request.get("userId");
            if (userId == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "User ID is required"));
            }

            boolean success = userDAO.updateTutorialStatus(userId, true);
            if (success) {
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Tutorial marked as completed"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Update failed"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, Object> request,
            @RequestHeader(value = "X-Admin-Id", required = false) Integer adminId) {
        try {
            Integer userId = (request.get("userId") != null) ? ((Number) request.get("userId")).intValue() : null;
            String newPassword = (String) request.get("newPassword");
            String role = (String) request.get("role");

            if (userId == null || newPassword == null || newPassword.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "User ID and new password are required"));
            }

            boolean success = userDAO.resetPassword(userId, newPassword, role != null ? role : "Customer");
            if (success) {
                if (adminId != null) {
                    auditLogService.logAction(adminId, "RESET_PASSWORD", "User ID: " + userId);
                }
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Password reset successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Password reset failed"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    /**
     * Get ALL users including admins (for admin panel)
     * GET /users/all
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsersIncludingAdmins() {
        try {
            List<User> users = userDAO.getAllUsers();
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", users,
                    "count", users.size()));
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    /**
     * Search users by area (for admin panel)
     * GET /users/search?area=xxx
     */
    @GetMapping("/search")
    public ResponseEntity<?> getUsersByArea(@RequestParam("area") String area) {
        try {
            List<User> users = userDAO.getUsersByArea(area);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", users,
                    "count", users.size()));
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }

    /**
     * Delete a user (for admin panel)
     * DELETE /users/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id,
            @RequestHeader(value = "X-Admin-Id", required = false) Integer adminId) {
        try {
            boolean success = userDAO.deleteUser(id);
            if (success) {
                if (adminId != null) {
                    auditLogService.logAction(adminId, "DELETE_USER", "User ID: " + id);
                }
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "User deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "User not found or could not be deleted"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Database error: " + e.getMessage()));
        }
    }
}
