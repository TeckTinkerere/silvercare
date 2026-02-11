package com.silvercare.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * Simple health check controller to satisfy Render deployment requirements.
 * This provides a standard /status endpoint.
 */
@RestController
public class HealthController {

    /**
     * Responds to /status (accessible via /s-api/status).
     */
    @GetMapping("/status")
    public Map<String, String> status() {
        return Map.of("status", "UP", "message", "SilverCare API is running");
    }
}
