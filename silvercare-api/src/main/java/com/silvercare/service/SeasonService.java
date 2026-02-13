package com.silvercare.service;

import com.silvercare.model.Season;
import com.silvercare.util.WeatherAPIClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;

@Service
public class SeasonService {

    @Autowired
    private WeatherAPIClient weatherAPIClient;

    @Value("${weather.api.endpoint:}")
    private String apiEndpoint;

    @Value("${weather.api.key:}")
    private String apiKey;

    @Value("${weather.api.location:Singapore}")
    private String location;

    private Season cachedSeason;
    // private LocalDateTime lastUpdated; // Removed unused field

    @PostConstruct
    public void init() {
        // Initial refresh on startup
        refreshSeason();
    }

    public Season getCurrentSeason() {
        if (cachedSeason == null) {
            refreshSeason();
        }
        return cachedSeason != null ? cachedSeason : Season.NEUTRAL;
    }

    @Scheduled(fixedRate = 3600000) // Refresh every hour
    public void refreshSeason() {
        try {
            if (apiEndpoint != null && !apiEndpoint.isEmpty() && apiKey != null && !apiKey.isEmpty()) {
                Season season = weatherAPIClient.fetchCurrentSeason(apiEndpoint, apiKey, location);
                if (season != null) {
                    this.cachedSeason = season;
                    // this.lastUpdated = LocalDateTime.now(); // Field removed
                    System.out.println("SeasonService (Backend): Refreshed season to " + season);
                    return;
                }
            }
        } catch (Exception e) {
            System.err.println("SeasonService (Backend): Failed to refresh season: " + e.getMessage());
        }

        // Fallback if API fails or not configured
        if (this.cachedSeason == null) {
            this.cachedSeason = calculateSeasonFromDate(LocalDateTime.now());
            System.out.println("SeasonService (Backend): Fallback to date-based season: " + this.cachedSeason);
        }
    }

    private Season calculateSeasonFromDate(LocalDateTime dateTime) {
        int month = dateTime.getMonthValue();
        if (month >= 3 && month <= 5)
            return Season.SPRING;
        else if (month >= 6 && month <= 8)
            return Season.SUMMER;
        else if (month >= 9 && month <= 11)
            return Season.AUTUMN;
        else
            return Season.WINTER;
    }
}
