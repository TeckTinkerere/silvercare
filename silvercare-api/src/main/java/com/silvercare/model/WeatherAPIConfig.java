package com.silvercare.model;

/**
 * Configuration model for Weather API settings.
 */
public class WeatherAPIConfig {
    private String endpoint;
    private String apiKey;
    private String location;

    public WeatherAPIConfig() {
    }

    public WeatherAPIConfig(String endpoint, String apiKey, String location) {
        this.endpoint = endpoint;
        this.apiKey = apiKey;
        this.location = location;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Validate that all required fields are present.
     * 
     * @return true if configuration is valid
     */
    public boolean isValid() {
        return endpoint != null && !endpoint.trim().isEmpty()
                && apiKey != null && !apiKey.trim().isEmpty()
                && location != null && !location.trim().isEmpty();
    }
}
