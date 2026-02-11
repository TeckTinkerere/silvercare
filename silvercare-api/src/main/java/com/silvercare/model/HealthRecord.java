package com.silvercare.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;

/**
 * Health Record model for comprehensive elderly health monitoring
 * Integrates with IoT devices and manual input for complete health tracking
 */
public class HealthRecord {
    private int id;
    private int customerId;
    private Timestamp recordDate;
    private String recordType; // "vital_signs", "medication", "symptoms", "emergency", "routine_check"
    
    // Vital Signs
    private Integer systolicBP;
    private Integer diastolicBP;
    private Integer heartRate;
    private BigDecimal temperature; // Celsius
    private Integer oxygenSaturation;
    private BigDecimal bloodSugar;
    private BigDecimal weight;
    
    // Medication Tracking
    private String medicationName;
    private String dosage;
    private Timestamp medicationTime;
    private boolean medicationTaken;
    private String medicationNotes;
    
    // Symptoms and Observations
    private String symptoms;
    private String painLevel; // "none", "mild", "moderate", "severe"
    private String moodAssessment; // "excellent", "good", "fair", "poor"
    private String mobilityStatus; // "independent", "assisted", "wheelchair", "bedridden"
    
    // Emergency Information
    private boolean isEmergency;
    private String emergencyType;
    private String emergencyDescription;
    private boolean emergencyResponseSent;
    
    // Care Provider Information
    private int careProviderId;
    private String careProviderNotes;
    private String recommendedActions;
    
    // Device Integration
    private String deviceId; // IoT device identifier
    private String deviceType; // "blood_pressure_monitor", "glucose_meter", "smart_watch", "fall_detector"
    private boolean isDeviceReading;
    
    // Alerts and Notifications
    private boolean requiresAttention;
    private String alertLevel; // "low", "medium", "high", "critical"
    private boolean familyNotified;
    private boolean doctorNotified;
    
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Related records for comprehensive tracking
    private List<String> attachedImages = new ArrayList<>();
    private List<String> attachedDocuments = new ArrayList<>();
    
    // Constructors
    public HealthRecord() {}
    
    public HealthRecord(int customerId, String recordType) {
        this.customerId = customerId;
        this.recordType = recordType;
        this.recordDate = new Timestamp(System.currentTimeMillis());
        this.requiresAttention = false;
        this.isEmergency = false;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    
    public Timestamp getRecordDate() { return recordDate; }
    public void setRecordDate(Timestamp recordDate) { this.recordDate = recordDate; }
    
    public String getRecordType() { return recordType; }
    public void setRecordType(String recordType) { this.recordType = recordType; }
    
    // Vital Signs Getters/Setters
    public Integer getSystolicBP() { return systolicBP; }
    public void setSystolicBP(Integer systolicBP) { 
        this.systolicBP = systolicBP;
        checkVitalSignsAlerts();
    }
    
    public Integer getDiastolicBP() { return diastolicBP; }
    public void setDiastolicBP(Integer diastolicBP) { 
        this.diastolicBP = diastolicBP;
        checkVitalSignsAlerts();
    }
    
    public Integer getHeartRate() { return heartRate; }
    public void setHeartRate(Integer heartRate) { 
        this.heartRate = heartRate;
        checkVitalSignsAlerts();
    }
    
    public BigDecimal getTemperature() { return temperature; }
    public void setTemperature(BigDecimal temperature) { 
        this.temperature = temperature;
        checkVitalSignsAlerts();
    }
    
    public Integer getOxygenSaturation() { return oxygenSaturation; }
    public void setOxygenSaturation(Integer oxygenSaturation) { 
        this.oxygenSaturation = oxygenSaturation;
        checkVitalSignsAlerts();
    }
    
    public BigDecimal getBloodSugar() { return bloodSugar; }
    public void setBloodSugar(BigDecimal bloodSugar) { 
        this.bloodSugar = bloodSugar;
        checkVitalSignsAlerts();
    }
    
    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }
    
    // Medication Getters/Setters
    public String getMedicationName() { return medicationName; }
    public void setMedicationName(String medicationName) { this.medicationName = medicationName; }
    
    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }
    
    public Timestamp getMedicationTime() { return medicationTime; }
    public void setMedicationTime(Timestamp medicationTime) { this.medicationTime = medicationTime; }
    
    public boolean isMedicationTaken() { return medicationTaken; }
    public void setMedicationTaken(boolean medicationTaken) { this.medicationTaken = medicationTaken; }
    
    public String getMedicationNotes() { return medicationNotes; }
    public void setMedicationNotes(String medicationNotes) { this.medicationNotes = medicationNotes; }
    
    // Symptoms and Observations Getters/Setters
    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    
    public String getPainLevel() { return painLevel; }
    public void setPainLevel(String painLevel) { this.painLevel = painLevel; }
    
    public String getMoodAssessment() { return moodAssessment; }
    public void setMoodAssessment(String moodAssessment) { this.moodAssessment = moodAssessment; }
    
    public String getMobilityStatus() { return mobilityStatus; }
    public void setMobilityStatus(String mobilityStatus) { this.mobilityStatus = mobilityStatus; }
    
    // Emergency Getters/Setters
    public boolean isEmergency() { return isEmergency; }
    public void setEmergency(boolean isEmergency) { 
        this.isEmergency = isEmergency;
        if (isEmergency) {
            this.alertLevel = "critical";
            this.requiresAttention = true;
        }
    }
    
    public String getEmergencyType() { return emergencyType; }
    public void setEmergencyType(String emergencyType) { this.emergencyType = emergencyType; }
    
    public String getEmergencyDescription() { return emergencyDescription; }
    public void setEmergencyDescription(String emergencyDescription) { this.emergencyDescription = emergencyDescription; }
    
    public boolean isEmergencyResponseSent() { return emergencyResponseSent; }
    public void setEmergencyResponseSent(boolean emergencyResponseSent) { this.emergencyResponseSent = emergencyResponseSent; }
    
    // Care Provider Getters/Setters
    public int getCareProviderId() { return careProviderId; }
    public void setCareProviderId(int careProviderId) { this.careProviderId = careProviderId; }
    
    public String getCareProviderNotes() { return careProviderNotes; }
    public void setCareProviderNotes(String careProviderNotes) { this.careProviderNotes = careProviderNotes; }
    
    public String getRecommendedActions() { return recommendedActions; }
    public void setRecommendedActions(String recommendedActions) { this.recommendedActions = recommendedActions; }
    
    // Device Integration Getters/Setters
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    
    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
    
    public boolean isDeviceReading() { return isDeviceReading; }
    public void setDeviceReading(boolean isDeviceReading) { this.isDeviceReading = isDeviceReading; }
    
    // Alert Getters/Setters
    public boolean isRequiresAttention() { return requiresAttention; }
    public void setRequiresAttention(boolean requiresAttention) { this.requiresAttention = requiresAttention; }
    
    public String getAlertLevel() { return alertLevel; }
    public void setAlertLevel(String alertLevel) { this.alertLevel = alertLevel; }
    
    public boolean isFamilyNotified() { return familyNotified; }
    public void setFamilyNotified(boolean familyNotified) { this.familyNotified = familyNotified; }
    
    public boolean isDoctorNotified() { return doctorNotified; }
    public void setDoctorNotified(boolean doctorNotified) { this.doctorNotified = doctorNotified; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
    
    public List<String> getAttachedImages() { return attachedImages; }
    public void setAttachedImages(List<String> attachedImages) { this.attachedImages = attachedImages; }
    
    public List<String> getAttachedDocuments() { return attachedDocuments; }
    public void setAttachedDocuments(List<String> attachedDocuments) { this.attachedDocuments = attachedDocuments; }
    
    /**
     * Check vital signs and set appropriate alert levels
     */
    private void checkVitalSignsAlerts() {
        boolean hasAlert = false;
        String alertLevel = "low";
        
        // Blood Pressure Checks
        if (systolicBP != null && diastolicBP != null) {
            if (systolicBP > 180 || diastolicBP > 110) {
                hasAlert = true;
                alertLevel = "critical";
            } else if (systolicBP > 140 || diastolicBP > 90) {
                hasAlert = true;
                alertLevel = "high";
            }
        }
        
        // Heart Rate Checks
        if (heartRate != null) {
            if (heartRate > 120 || heartRate < 50) {
                hasAlert = true;
                alertLevel = "high";
            }
        }
        
        // Temperature Checks
        if (temperature != null) {
            if (temperature.compareTo(new BigDecimal("39.0")) > 0 || 
                temperature.compareTo(new BigDecimal("35.0")) < 0) {
                hasAlert = true;
                alertLevel = "high";
            }
        }
        
        // Oxygen Saturation Checks
        if (oxygenSaturation != null && oxygenSaturation < 90) {
            hasAlert = true;
            alertLevel = "critical";
        }
        
        // Blood Sugar Checks
        if (bloodSugar != null) {
            if (bloodSugar.compareTo(new BigDecimal("15.0")) > 0 || 
                bloodSugar.compareTo(new BigDecimal("3.0")) < 0) {
                hasAlert = true;
                alertLevel = "high";
            }
        }
        
        this.requiresAttention = hasAlert;
        if (hasAlert) {
            this.alertLevel = alertLevel;
        }
    }
    
    /**
     * Get a summary of current health status
     */
    public String getHealthSummary() {
        StringBuilder summary = new StringBuilder();
        
        if (systolicBP != null && diastolicBP != null) {
            summary.append("BP: ").append(systolicBP).append("/").append(diastolicBP).append(" ");
        }
        if (heartRate != null) {
            summary.append("HR: ").append(heartRate).append(" ");
        }
        if (temperature != null) {
            summary.append("Temp: ").append(temperature).append("°C ");
        }
        if (oxygenSaturation != null) {
            summary.append("O2: ").append(oxygenSaturation).append("% ");
        }
        
        return summary.toString().trim();
    }
}