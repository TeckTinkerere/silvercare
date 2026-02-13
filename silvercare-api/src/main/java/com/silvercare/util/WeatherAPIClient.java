package com.silvercare.util;

import com.google.gson.Gson;
import com.silvercare.model.Season;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

/**
 * Client for fetching current season from weather API.
 * Implements retry logic with exponential backoff.
 */
@Component
public class WeatherAPIClient {
    private final Gson gson;
    private static final int TIMEOUT_MS = 5000;
    private static final int MAX_RETRIES = 3;
    private static final int BASE_BACKOFF_MS = 1000;

    public WeatherAPIClient() {
        this.gson = new Gson();
    }

    /**
     * Fetch current season from weather API with retry logic.
     * 
     * @param endpoint API endpoint
     * @param apiKey   API key
     * @param location Query location
     * @return Current season based on API response
     * @throws IOException if all retry attempts fail
     */
    public Season fetchCurrentSeason(String endpoint, String apiKey, String location) throws IOException {
        int attempt = 0;
        IOException lastException = null;

        while (attempt < MAX_RETRIES) {
            try {
                String response = makeAPICall(endpoint, apiKey, location);
                return parseSeasonFromResponse(response);
            } catch (IOException e) {
                lastException = e;
                attempt++;

                if (attempt < MAX_RETRIES) {
                    int backoffMs = BASE_BACKOFF_MS * (int) Math.pow(2, attempt - 1);
                    System.out.println(
                            "WeatherAPIClient: Attempt " + attempt + " failed, retrying in " + backoffMs + "ms");
                    try {
                        Thread.sleep(backoffMs);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new IOException("Interrupted during retry backoff", ie);
                    }
                }
            }
        }

        throw new IOException("Failed to fetch season after " + MAX_RETRIES + " attempts", lastException);
    }

    /**
     * Make HTTP call to weather API using HttpURLConnection.
     */
    private String makeAPICall(String endpoint, String apiKey, String location) throws IOException {
        String urlString = String.format("%s?q=%s&appid=%s", endpoint, location, apiKey);
        URL url;
        try {
            url = new java.net.URI(urlString).toURL();
        } catch (java.net.URISyntaxException e) {
            throw new IOException("Invalid URL syntax: " + urlString, e);
        }
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(TIMEOUT_MS);
            conn.setReadTimeout(TIMEOUT_MS);

            int statusCode = conn.getResponseCode();

            if (statusCode != 200) {
                throw new IOException("API returned status code: " + statusCode);
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            return response.toString();
        } finally {
            conn.disconnect();
        }
    }

    /**
     * Parse season from API JSON response.
     */
    public Season parseSeasonFromResponse(String jsonResponse) {
        try {
            JsonObject json = gson.fromJson(jsonResponse, JsonObject.class);

            if (json.has("season")) {
                String seasonStr = json.get("season").getAsString().toUpperCase();
                return Season.valueOf(seasonStr);
            }

            return calculateSeasonFromDate(LocalDate.now());

        } catch (Exception e) {
            System.err.println("Error parsing season from response: " + e.getMessage());
            return calculateSeasonFromDate(LocalDate.now());
        }
    }

    private Season calculateSeasonFromDate(LocalDate date) {
        int month = date.getMonthValue();

        if (month >= 3 && month <= 5) {
            return Season.SPRING;
        } else if (month >= 6 && month <= 8) {
            return Season.SUMMER;
        } else if (month >= 9 && month <= 11) {
            return Season.AUTUMN;
        } else {
            return Season.WINTER;
        }
    }
}
