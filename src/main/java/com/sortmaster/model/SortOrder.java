package com.sortmaster.model;

import com.sortmaster.exception.InvalidSortRequestException;

public enum SortOrder {
    ASCENDING("Ascending", "Smallest to Largest"),
    DESCENDING("Descending", "Largest to Smallest");

    private final String displayName;
    private final String description;

    SortOrder(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public static SortOrder fromString(String value) {
        if (value == null || value.isBlank()) {
            return ASCENDING;
        }
        String normalized = value.trim().toUpperCase();
        if (normalized.equals("ASC") || normalized.equals("ASCENDING")) {
            return ASCENDING;
        }
        if (normalized.equals("DESC") || normalized.equals("DESCENDING")) {
            return DESCENDING;
        }
        throw new InvalidSortRequestException("Unsupported sorting order: '" + value + "'. Supported orders are ASCENDING and DESCENDING.");
    }
}
