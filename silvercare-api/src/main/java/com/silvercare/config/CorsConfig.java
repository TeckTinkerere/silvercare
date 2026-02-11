package com.silvercare.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global CORS configuration for B2B Integration (Advanced Feature B).
 * Allows external third-party portals to consume the REST API.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        String allowedOriginsEnv = System.getenv("ALLOWED_ORIGINS");
        String[] origins;
        if (allowedOriginsEnv != null && !allowedOriginsEnv.isEmpty()) {
            origins = allowedOriginsEnv.split(",");
        } else {
            origins = new String[] {
                    "http://localhost:8080",
                    "http://localhost:8081",
                    "http://localhost:3000",
                    "https://silvercare-web.onrender.com"
            };
        }

        registry.addMapping("/**") // Allow all endpoints, not just /api/**
                .allowedOrigins(origins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
