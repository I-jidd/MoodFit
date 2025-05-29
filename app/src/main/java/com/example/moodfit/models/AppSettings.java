package com.example.moodfit.models;

import com.example.moodfit.models.enums.DifficultyLevel;

public class AppSettings {
    private String userId;
    private boolean soundEnabled;
    private boolean notificationsEnabled;
    private boolean vibrationEnabled;
    private int reminderHour; // 24-hour format
    private int reminderMinute;
    private boolean weekendReminders;
    private String preferredTheme; // "light", "dark", "auto"
    private int timerDuration; // default timer duration in seconds
    private boolean showMotivationalQuotes;
    private DifficultyLevel defaultDifficulty;
    private boolean isOnboardingCompleted;
    private long lastBackupDate;
    private boolean autoStartTimer;
    private boolean showWorkoutSummary;

    // Constructor
    public AppSettings() {
        // Default settings
        this.soundEnabled = true;
        this.notificationsEnabled = true;
        this.vibrationEnabled = true;
        this.reminderHour = 18; // 6 PM
        this.reminderMinute = 0;
        this.weekendReminders = false;
        this.preferredTheme = "auto";
        this.timerDuration = 30; // 30 seconds
        this.showMotivationalQuotes = true;
        this.defaultDifficulty = DifficultyLevel.BEGINNER;
        this.isOnboardingCompleted = false;
        this.autoStartTimer = false;
        this.showWorkoutSummary = true;
    }

    public AppSettings(String userId) {
        this();
        this.userId = userId;
    }

    // Utility Methods
    public String getReminderTimeFormatted() {
        return String.format("%02d:%02d", reminderHour, reminderMinute);
    }

    public boolean isDarkTheme() {
        return "dark".equals(preferredTheme);
    }

    public boolean isLightTheme() {
        return "light".equals(preferredTheme);
    }

    public boolean isAutoTheme() {
        return "auto".equals(preferredTheme);
    }

    public void completeOnboarding() {
        this.isOnboardingCompleted = true;
    }

    public boolean needsBackup() {
        long oneWeekInMillis = 7 * 24 * 60 * 60 * 1000L;
        return (System.currentTimeMillis() - lastBackupDate) > oneWeekInMillis;
    }

    public void recordBackup() {
        this.lastBackupDate = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public boolean isSoundEnabled() { return soundEnabled; }
    public void setSoundEnabled(boolean soundEnabled) { this.soundEnabled = soundEnabled; }

    public boolean isNotificationsEnabled() { return notificationsEnabled; }
    public void setNotificationsEnabled(boolean notificationsEnabled) { this.notificationsEnabled = notificationsEnabled; }

    public boolean isVibrationEnabled() { return vibrationEnabled; }
    public void setVibrationEnabled(boolean vibrationEnabled) { this.vibrationEnabled = vibrationEnabled; }

    public int getReminderHour() { return reminderHour; }
    public void setReminderHour(int reminderHour) { this.reminderHour = Math.max(0, Math.min(23, reminderHour)); }

    public int getReminderMinute() { return reminderMinute; }
    public void setReminderMinute(int reminderMinute) { this.reminderMinute = Math.max(0, Math.min(59, reminderMinute)); }

    public boolean isWeekendReminders() { return weekendReminders; }
    public void setWeekendReminders(boolean weekendReminders) { this.weekendReminders = weekendReminders; }

    public String getPreferredTheme() { return preferredTheme; }
    public void setPreferredTheme(String preferredTheme) { this.preferredTheme = preferredTheme; }

    public int getTimerDuration() { return timerDuration; }
    public void setTimerDuration(int timerDuration) { this.timerDuration = Math.max(10, timerDuration); }

    public boolean isShowMotivationalQuotes() { return showMotivationalQuotes; }
    public void setShowMotivationalQuotes(boolean showMotivationalQuotes) { this.showMotivationalQuotes = showMotivationalQuotes; }

    public DifficultyLevel getDefaultDifficulty() { return defaultDifficulty; }
    public void setDefaultDifficulty(DifficultyLevel defaultDifficulty) { this.defaultDifficulty = defaultDifficulty; }

    public boolean isOnboardingCompleted() { return isOnboardingCompleted; }
    public void setOnboardingCompleted(boolean onboardingCompleted) { isOnboardingCompleted = onboardingCompleted; }

    public long getLastBackupDate() { return lastBackupDate; }
    public void setLastBackupDate(long lastBackupDate) { this.lastBackupDate = lastBackupDate; }

    public boolean isAutoStartTimer() { return autoStartTimer; }
    public void setAutoStartTimer(boolean autoStartTimer) { this.autoStartTimer = autoStartTimer; }

    public boolean isShowWorkoutSummary() { return showWorkoutSummary; }
    public void setShowWorkoutSummary(boolean showWorkoutSummary) { this.showWorkoutSummary = showWorkoutSummary; }
}