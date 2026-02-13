package com.silvercare.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Promotion model for seasonal promotional content.
 * Represents marketing campaigns and awareness initiatives tied to specific seasons.
 * 
 * Requirements: 10.1, 10.4
 */
public class Promotion {
    private int id;
    private String title;
    private String description;
    private String imagePath;
    private String linkUrl;
    private boolean active;
    private LocalDateTime createdAt;
    
    // Season associations
    private Set<Season> seasons;
    
    public Promotion() {
        this.seasons = new HashSet<>();
        this.active = true;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    /**
     * Set the promotion title with validation.
     * 
     * @param title Title between 5 and 100 characters
     * @throws IllegalArgumentException if title is invalid
     */
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (title.length() < 5 || title.length() > 100) {
            throw new IllegalArgumentException("Title must be between 5 and 100 characters");
        }
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Set the promotion description with validation.
     * 
     * @param description Description between 10 and 500 characters
     * @throws IllegalArgumentException if description is invalid
     */
    public void setDescription(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }
        if (description.length() < 10 || description.length() > 500) {
            throw new IllegalArgumentException("Description must be between 10 and 500 characters");
        }
        this.description = description;
    }
    
    public String getImagePath() {
        return imagePath;
    }
    
    /**
     * Set the promotion image path with validation.
     * 
     * @param imagePath URL or path to the promotion image
     * @throws IllegalArgumentException if imagePath is invalid URL format
     */
    public void setImagePath(String imagePath) {
        if (imagePath != null && !imagePath.trim().isEmpty()) {
            if (!isValidUrl(imagePath)) {
                throw new IllegalArgumentException("Image path must be a valid URL");
            }
        }
        this.imagePath = imagePath;
    }
    
    public String getLinkUrl() {
        return linkUrl;
    }
    
    /**
     * Set the promotion link URL with validation.
     * 
     * @param linkUrl URL to link when promotion is clicked
     * @throws IllegalArgumentException if linkUrl is invalid URL format
     */
    public void setLinkUrl(String linkUrl) {
        if (linkUrl != null && !linkUrl.trim().isEmpty()) {
            if (!isValidUrl(linkUrl)) {
                throw new IllegalArgumentException("Link URL must be a valid URL");
            }
        }
        this.linkUrl = linkUrl;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public Set<Season> getSeasons() {
        return seasons;
    }
    
    public void setSeasons(Set<Season> seasons) {
        if (seasons == null) {
            this.seasons = new HashSet<>();
        } else {
            this.seasons = seasons;
        }
    }
    
    /**
     * Add a season to this promotion.
     * 
     * @param season Season to add
     */
    public void addSeason(Season season) {
        if (season != null) {
            this.seasons.add(season);
        }
    }
    
    /**
     * Remove a season from this promotion.
     * 
     * @param season Season to remove
     */
    public void removeSeason(Season season) {
        this.seasons.remove(season);
    }
    
    /**
     * Check if promotion is active for a given season.
     * 
     * @param season Season to check
     * @return true if promotion applies to this season and is active
     */
    public boolean isActiveForSeason(Season season) {
        if (!active) {
            return false;
        }
        if (season == null) {
            return false;
        }
        return seasons.contains(season);
    }
    
    /**
     * Validate URL format.
     * 
     * @param url URL string to validate
     * @return true if valid URL format
     */
    private boolean isValidUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }
        // Basic URL validation - starts with http:// or https:// or is a relative path
        String trimmed = url.trim();
        return trimmed.startsWith("http://") 
            || trimmed.startsWith("https://") 
            || trimmed.startsWith("/")
            || trimmed.startsWith("./");
    }
    
    @Override
    public String toString() {
        return "Promotion{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", active=" + active +
                ", seasons=" + seasons +
                ", createdAt=" + createdAt +
                '}';
    }
}
