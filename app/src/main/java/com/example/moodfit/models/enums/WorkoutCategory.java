package com.example.moodfit.models.enums;

public enum WorkoutCategory {
    CARDIO("Cardio"),
    STRENGTH("Strength"),
    FLEXIBILITY("Flexibility"),
    BREATHING("Breathing"),
    YOGA("Yoga"),
    HIIT("HIIT");

    private final String displayName;

    WorkoutCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() { return displayName; }
}
