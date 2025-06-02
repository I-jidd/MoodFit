package com.example.moodfit.models;

import com.example.moodfit.models.enums.DifficultyLevel;

import java.util.Calendar;

public class User {
    private String userId;
    private String username;
    private int currentStreak;
    private int bestStreak;
    private int totalWorkouts;
    private int totalMinutes;
    private long lastWorkoutDate;
    private boolean soundEnabled;
    private boolean notificationsEnabled;
    private DifficultyLevel preferredDifficulty;
    private long accountCreatedDate;
    private boolean isFirstTimeUser;
    private long lastOpenDate;
    private int totalAppOpens;

    // Constructors
    public User() {
        this.userId = generateUserId();
        this.currentStreak = 0;
        this.bestStreak = 0;
        this.totalWorkouts = 0;
        this.totalMinutes = 0;
        this.soundEnabled = true;
        this.notificationsEnabled = true;
        this.preferredDifficulty = DifficultyLevel.BEGINNER;
        this.accountCreatedDate = System.currentTimeMillis();
        this.isFirstTimeUser = true;
        this.lastOpenDate = System.currentTimeMillis();
        this.totalAppOpens = 1;
    }

    public User(String username) {
        this();
        this.username = username;
        this.isFirstTimeUser = false; // Setup completed
    }

    // Utility Methods
    private String generateUserId() {
        return "user_" + System.currentTimeMillis();
    }

    public void completeOnboarding(String username) {
        this.username = username;
        this.isFirstTimeUser = false;
    }

    public void incrementStreak() {
        this.currentStreak++;
        if (this.currentStreak > this.bestStreak) {
            this.bestStreak = this.currentStreak;
        }
    }

    public void resetStreak() {
        this.currentStreak = 0;
    }

    public void addWorkout(int durationMinutes) {
        this.totalWorkouts++;
        this.totalMinutes += durationMinutes;
        this.lastWorkoutDate = System.currentTimeMillis();
    }

    public boolean hasWorkedOutToday() {
        // If never worked out (lastWorkoutDate is 0), return false
        if (lastWorkoutDate == 0) {
            return false;
        }

        Calendar today = Calendar.getInstance();
        Calendar lastWorkout = Calendar.getInstance();
        lastWorkout.setTimeInMillis(this.lastWorkoutDate);

        // Check if same calendar day (not 24-hour period)
        return today.get(Calendar.YEAR) == lastWorkout.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == lastWorkout.get(Calendar.DAY_OF_YEAR);
    }

    public void recordAppOpen() {
        this.totalAppOpens++;
        this.lastOpenDate = System.currentTimeMillis();
    }

    public String getWelcomeMessage() {
        if (username == null || username.trim().isEmpty()) {
            return "Welcome back!";
        }

        // Get time-based greeting
        java.util.Calendar cal = java.util.Calendar.getInstance();
        int hour = cal.get(java.util.Calendar.HOUR_OF_DAY);

        String greeting;
        if (hour < 12) {
            greeting = "Good morning";
        } else if (hour < 17) {
            greeting = "Good afternoon";
        } else {
            greeting = "Good evening";
        }

        return greeting + ", " + username + "!";
    }

    public String getMotivationalMessage() {
        if (currentStreak == 0) {
            return "Ready to start your fitness journey?";
        } else if (currentStreak == 1) {
            return "Great start! Keep the momentum going!";
        } else if (currentStreak < 7) {
            return "You're building a great habit!";
        } else if (currentStreak < 30) {
            return "Amazing streak! You're on fire! 🔥";
        } else {
            return "Incredible dedication! You're a fitness champion!";
        }
    }

    public int getDaysUsingApp() {
        long daysSinceCreation = (System.currentTimeMillis() - accountCreatedDate) / (24 * 60 * 60 * 1000);
        return Math.max(1, (int) daysSinceCreation);
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public int getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(int currentStreak) { this.currentStreak = currentStreak; }

    public int getBestStreak() { return bestStreak; }
    public void setBestStreak(int bestStreak) { this.bestStreak = bestStreak; }

    public int getTotalWorkouts() { return totalWorkouts; }
    public void setTotalWorkouts(int totalWorkouts) { this.totalWorkouts = totalWorkouts; }

    public int getTotalMinutes() { return totalMinutes; }
    public void setTotalMinutes(int totalMinutes) { this.totalMinutes = totalMinutes; }

    public long getLastWorkoutDate() { return lastWorkoutDate; }
    public void setLastWorkoutDate(long lastWorkoutDate) { this.lastWorkoutDate = lastWorkoutDate; }

    public boolean isSoundEnabled() { return soundEnabled; }
    public void setSoundEnabled(boolean soundEnabled) { this.soundEnabled = soundEnabled; }

    public boolean isNotificationsEnabled() { return notificationsEnabled; }
    public void setNotificationsEnabled(boolean notificationsEnabled) { this.notificationsEnabled = notificationsEnabled; }

    public DifficultyLevel getPreferredDifficulty() { return preferredDifficulty; }
    public void setPreferredDifficulty(DifficultyLevel preferredDifficulty) { this.preferredDifficulty = preferredDifficulty; }

    public long getAccountCreatedDate() { return accountCreatedDate; }
    public void setAccountCreatedDate(long accountCreatedDate) { this.accountCreatedDate = accountCreatedDate; }

    public boolean isFirstTimeUser() { return isFirstTimeUser; }
    public void setFirstTimeUser(boolean firstTimeUser) { isFirstTimeUser = firstTimeUser; }

    public long getLastOpenDate() { return lastOpenDate; }
    public void setLastOpenDate(long lastOpenDate) { this.lastOpenDate = lastOpenDate; }

    public int getTotalAppOpens() { return totalAppOpens; }
    public void setTotalAppOpens(int totalAppOpens) { this.totalAppOpens = totalAppOpens; }
}