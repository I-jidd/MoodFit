package com.example.moodfit.models.enums;

public enum DifficultyLevel {
    BEGINNER("Beginner", 1),
    INTERMEDIATE("Intermediate", 2),
    ADVANCED("Advanced", 3);

    private final String displayName;
    private final int level;

    DifficultyLevel(String displayName, int level) {
        this.displayName = displayName;
        this.level = level;
    }

    public String getDisplayName() { return displayName; }
    public int getLevel() { return level; }
}
