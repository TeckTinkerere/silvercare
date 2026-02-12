package com.silvercare.model;

import java.math.BigDecimal;
import com.google.gson.annotations.SerializedName;

/**
 * Service model for the Main Web App.
 * Used by Servlets and DAOs for MVC data transfer.
 */
public class Service {
    private int id;

    @SerializedName(value = "categoryId", alternate = { "category_id" })
    private int categoryId;

    private String name;
    private String description;
    private BigDecimal price;
    private String imagePath;

    // Transient field for display
    private String categoryName;

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
}
