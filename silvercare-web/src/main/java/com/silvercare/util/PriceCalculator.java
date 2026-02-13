package com.silvercare.util;

import com.silvercare.model.Season;
import com.silvercare.model.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Utility class for calculating seasonal prices with proper rounding.
 * Centralizes all seasonal pricing logic for consistency across the
 * application.
 */
public class PriceCalculator {

    private static final BigDecimal MIN_MULTIPLIER = new BigDecimal("0.1");
    private static final BigDecimal MAX_MULTIPLIER = new BigDecimal("10.0");

    /**
     * Calculate seasonal price for a service.
     * 
     * @param basePrice  The base price of the service
     * @param multiplier The seasonal multiplier
     * @return Calculated price rounded to 2 decimal places, or basePrice if inputs
     *         are null
     */
    public static BigDecimal calculateSeasonalPrice(BigDecimal basePrice, BigDecimal multiplier) {
        if (basePrice == null || multiplier == null) {
            return basePrice;
        }
        return basePrice.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Calculate and set seasonal prices for a list of services.
     * Modifies the services in place by setting their seasonalPrice and
     * currentSeason fields.
     * 
     * @param services List of services to process
     * @param season   Current season
     */
    public static void applySeasonalPricing(List<Service> services, Season season) {
        if (services == null || season == null) {
            return;
        }

        for (Service service : services) {
            BigDecimal multiplier = service.getMultiplierForSeason(season);
            BigDecimal seasonalPrice = calculateSeasonalPrice(service.getPrice(), multiplier);
            service.setSeasonalPrice(seasonalPrice);
            service.setCurrentSeason(season);
        }
    }

    /**
     * Validate multiplier value.
     * 
     * @param multiplier Value to validate
     * @return true if valid (between 0.1 and 10.0 inclusive), false otherwise
     */
    public static boolean isValidMultiplier(BigDecimal multiplier) {
        if (multiplier == null) {
            return false;
        }
        return multiplier.compareTo(MIN_MULTIPLIER) >= 0
                && multiplier.compareTo(MAX_MULTIPLIER) <= 0;
    }
}
