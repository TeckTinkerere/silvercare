package com.silvercare.model;

/**
 * Enum representing the four seasons supported by the seasonal pricing system.
 * Includes a NEUTRAL fallback season for when the weather API is unavailable.
 */
public enum Season {
    SPRING("spring", "Spring"),
    SUMMER("summer", "Summer"),
    AUTUMN("autumn", "Autumn"),
    WINTER("winter", "Winter"),
    NEUTRAL("neutral", "Standard"); // Fallback season

    private final String columnName;
    private final String displayName;

    /**
     * Constructor for Season enum.
     * 
     * @param columnName  The database column prefix for this season (e.g.,
     *                    "spring")
     * @param displayName The human-readable display name for this season (e.g.,
     *                    "Spring")
     */
    Season(String columnName, String displayName) {
        this.columnName = columnName;
        this.displayName = displayName;
    }

    /**
     * Get the database column prefix for this season.
     * 
     * @return The column name (e.g., "spring", "summer")
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * Get the human-readable display name for this season.
     * 
     * @return The display name (e.g., "Spring", "Summer")
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get the database column name for the seasonal multiplier of this season.
     * 
     * @return The multiplier column name (e.g., "spring_multiplier",
     *         "summer_multiplier")
     */
    public String getMultiplierColumn() {
        return columnName + "_multiplier";
    }
}
