package com.example.moodfit.models.enums;

public enum MoodType {
    HAPPY("Happy", "😊", "#10B981"),
    NEUTRAL("Neutral", "😑", "#6B7280"),
    FRUSTRATED("Frustrated", "😤", "#F59E0B"),
    STRESSED("Stressed", "😩", "#EF4444");

    private final String displayName;
    private final String emoji;
    private final String colorHex;

    MoodType(String displayName, String emoji, String colorHex) {
        this.displayName = displayName;
        this.emoji = emoji;
        this.colorHex = colorHex;
    }

    public String getDisplayName() { return displayName; }
    public String getEmoji() { return emoji; }
    public String getColorHex() { return colorHex; }
}
