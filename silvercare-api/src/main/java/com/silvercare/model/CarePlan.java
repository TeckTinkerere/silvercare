package com.silvercare.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Care Plan model for subscription-based elderly care services
 * Supports monthly, weekly, and daily care plans
 */
public class CarePlan {
    private int id;
    private String planName;
    private String description;
    private BigDecimal monthlyPrice;
    private BigDecimal weeklyPrice;
    private BigDecimal dailyPrice;
    private int maxVisitsPerMonth;
    private int maxHoursPerVisit;
    private boolean includesMedicalSupport;
    private boolean includesEmergencyResponse;
    private boolean includesMealPreparation;
    private boolean includesTransportation;
    private boolean includesCompanionship;
    private String careLevel; // "basic", "standard", "premium", "specialized"
    private boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Constructors
    public CarePlan() {}
    
    public CarePlan(String planName, String careLevel, BigDecimal monthlyPrice) {
        this.planName = planName;
        this.careLevel = careLevel;
        this.monthlyPrice = monthlyPrice;
        this.isActive = true;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public BigDecimal getMonthlyPrice() { return monthlyPrice; }
    public void setMonthlyPrice(BigDecimal monthlyPrice) { this.monthlyPrice = monthlyPrice; }
    
    public BigDecimal getWeeklyPrice() { return weeklyPrice; }
    public void setWeeklyPrice(BigDecimal weeklyPrice) { this.weeklyPrice = weeklyPrice; }
    
    public BigDecimal getDailyPrice() { return dailyPrice; }
    public void setDailyPrice(BigDecimal dailyPrice) { this.dailyPrice = dailyPrice; }
    
    public int getMaxVisitsPerMonth() { return maxVisitsPerMonth; }
    public void setMaxVisitsPerMonth(int maxVisitsPerMonth) { this.maxVisitsPerMonth = maxVisitsPerMonth; }
    
    public int getMaxHoursPerVisit() { return maxHoursPerVisit; }
    public void setMaxHoursPerVisit(int maxHoursPerVisit) { this.maxHoursPerVisit = maxHoursPerVisit; }
    
    public boolean isIncludesMedicalSupport() { return includesMedicalSupport; }
    public void setIncludesMedicalSupport(boolean includesMedicalSupport) { this.includesMedicalSupport = includesMedicalSupport; }
    
    public boolean isIncludesEmergencyResponse() { return includesEmergencyResponse; }
    public void setIncludesEmergencyResponse(boolean includesEmergencyResponse) { this.includesEmergencyResponse = includesEmergencyResponse; }
    
    public boolean isIncludesMealPreparation() { return includesMealPreparation; }
    public void setIncludesMealPreparation(boolean includesMealPreparation) { this.includesMealPreparation = includesMealPreparation; }
    
    public boolean isIncludesTransportation() { return includesTransportation; }
    public void setIncludesTransportation(boolean includesTransportation) { this.includesTransportation = includesTransportation; }
    
    public boolean isIncludesCompanionship() { return includesCompanionship; }
    public void setIncludesCompanionship(boolean includesCompanionship) { this.includesCompanionship = includesCompanionship; }
    
    public String getCareLevel() { return careLevel; }
    public void setCareLevel(String careLevel) { this.careLevel = careLevel; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean isActive) { this.isActive = isActive; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
    
    /**
     * Calculate price based on billing frequency
     */
    public BigDecimal getPriceForFrequency(String frequency) {
        switch (frequency.toLowerCase()) {
            case "monthly":
                return monthlyPrice;
            case "weekly":
                return weeklyPrice;
            case "daily":
                return dailyPrice;
            default:
                return monthlyPrice;
        }
    }
    
    /**
     * Check if plan includes specific service
     */
    public boolean includesService(String serviceType) {
        switch (serviceType.toLowerCase()) {
            case "medical":
                return includesMedicalSupport;
            case "emergency":
                return includesEmergencyResponse;
            case "meals":
                return includesMealPreparation;
            case "transport":
                return includesTransportation;
            case "companionship":
                return includesCompanionship;
            default:
                return false;
        }
    }
}