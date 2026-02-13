package com.silvercare.model;

import java.math.BigDecimal;

public class Service {
    private int id;
    private int categoryId;
    private String name;
    private String description;
    private BigDecimal price;
    private String imagePath;

    // Seasonal pricing fields
    private BigDecimal springMultiplier;
    private BigDecimal summerMultiplier;
    private BigDecimal autumnMultiplier;
    private BigDecimal winterMultiplier;

    // Transient fields for display
    private String categoryName;
    private BigDecimal seasonalPrice;
    private Season currentSeason;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public BigDecimal getSpringMultiplier() {
        return springMultiplier;
    }

    public void setSpringMultiplier(BigDecimal springMultiplier) {
        this.springMultiplier = springMultiplier;
    }

    public BigDecimal getSummerMultiplier() {
        return summerMultiplier;
    }

    public void setSummerMultiplier(BigDecimal summerMultiplier) {
        this.summerMultiplier = summerMultiplier;
    }

    public BigDecimal getAutumnMultiplier() {
        return autumnMultiplier;
    }

    public void setAutumnMultiplier(BigDecimal autumnMultiplier) {
        this.autumnMultiplier = autumnMultiplier;
    }

    public BigDecimal getWinterMultiplier() {
        return winterMultiplier;
    }

    public void setWinterMultiplier(BigDecimal winterMultiplier) {
        this.winterMultiplier = winterMultiplier;
    }

    public BigDecimal getSeasonalPrice() {
        return seasonalPrice;
    }

    public void setSeasonalPrice(BigDecimal seasonalPrice) {
        this.seasonalPrice = seasonalPrice;
    }

    public Season getCurrentSeason() {
        return currentSeason;
    }

    public void setCurrentSeason(Season currentSeason) {
        this.currentSeason = currentSeason;
    }

    /**
     * Get the multiplier for a specific season.
     * 
     * @param season The season to get multiplier for
     * @return Multiplier value, defaults to 1.0 if null
     */
    public BigDecimal getMultiplierForSeason(Season season) {
        if (season == null) {
            return BigDecimal.ONE;
        }
        
        switch (season) {
            case SPRING:
                return springMultiplier != null ? springMultiplier : BigDecimal.ONE;
            case SUMMER:
                return summerMultiplier != null ? summerMultiplier : BigDecimal.ONE;
            case AUTUMN:
                return autumnMultiplier != null ? autumnMultiplier : BigDecimal.ONE;
            case WINTER:
                return winterMultiplier != null ? winterMultiplier : BigDecimal.ONE;
            case NEUTRAL:
            default:
                return BigDecimal.ONE;
        }
    }

    /**
     * Check if seasonal pricing is active (multiplier != 1.0).
     * 
     * @param season The season to check
     * @return true if multiplier differs from 1.0
     */
    public boolean hasSeasonalPricing(Season season) {
        BigDecimal multiplier = getMultiplierForSeason(season);
        return multiplier.compareTo(BigDecimal.ONE) != 0;
    }
}
