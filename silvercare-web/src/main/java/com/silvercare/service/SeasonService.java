package com.silvercare.service;

import com.silvercare.model.Season;
import com.silvercare.util.ApiClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Singleton service for season detection via API.
 * Refactored to call silvercare-api instead of direct DB access.
 */
public class SeasonService {
    private static SeasonService instance;
    private static final Gson gson = ApiClient.getGson();

    private SeasonService() {
    }

    /**
     * Get singleton instance.
     * 
     * @return SeasonService instance
     */
    public static synchronized SeasonService getInstance() {
        if (instance == null) {
            instance = new SeasonService();
        }
        return instance;
    }

    /**
     * Get current season from API.
     * 
     * @return Current season
     */
    public Season getCurrentSeason() {
        try {
            ApiClient.ApiResponse<String> response = ApiClient.get("/season", String.class);

            if (response.isSuccess() && response.getData() != null) {
                // Parse "SPRING", "SUMMER", etc. from JSON response
                // Expected format: {"status":"success", "data":"SUMMER", ...}
                JsonObject json = gson.fromJson(response.getData(), JsonObject.class);
                if (json.has("data")) {
                    String seasonStr = json.get("data").getAsString();
                    return Season.valueOf(seasonStr);
                }
            }
        } catch (Exception e) {
            System.err.println("SeasonService (Frontend): Failed to fetch season from API: " + e.getMessage());
        }

        return Season.NEUTRAL;
    }
}
