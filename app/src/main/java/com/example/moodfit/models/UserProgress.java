package com.example.moodfit.models;

import com.example.moodfit.models.enums.MoodType;
import com.example.moodfit.models.enums.WorkoutCategory;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserProgress {
    private String userId;
    private int currentStreak;
    private int longestStreak;
    private int totalWorkouts;
    private int totalMinutes;
    private int totalCalories;
    private long lastWorkoutDate;
    private Map<String, Integer> weeklyWorkouts; // Date -> workout count
    private Map<String, Integer> monthlyMinutes; // Month -> total minutes
    private Map<MoodType, Integer> moodFrequency;
    private Map<WorkoutCategory, Integer> categoryPreference;
    private List<Long> workoutDates;

    // Constructors
    public UserProgress() {
        this.weeklyWorkouts = new HashMap<>();
        this.monthlyMinutes = new HashMap<>();
        this.moodFrequency = new EnumMap<>(MoodType.class);
        this.categoryPreference = new EnumMap<>(WorkoutCategory.class);
        this.workoutDates = new ArrayList<>();
    }

    public UserProgress(String userId) {
        this();
        this.userId = userId;
    }

    // Utility Methods
    public void recordWorkout(WorkoutSession session) {
        this.totalWorkouts++;
        this.totalMinutes += session.getDurationMinutes();
        this.totalCalories += session.getCaloriesBurned();
        this.lastWorkoutDate = session.getEndTime();

        // Add to workout dates
        workoutDates.add(session.getEndTime());

        // Update mood frequency
        MoodType mood = session.getSelectedMood();
        if (mood != null) {
            moodFrequency.put(mood, moodFrequency.getOrDefault(mood, 0) + 1);
        }

        // Update category preferences
        for (Exercise exercise : session.getExercises()) {
            WorkoutCategory category = exercise.getCategory();
            categoryPreference.put(category, categoryPreference.getOrDefault(category, 0) + 1);
        }

        updateStreak();
    }

    private void updateStreak() {
        long today = System.currentTimeMillis();
        long dayInMillis = 24 * 60 * 60 * 1000;

        // Check if worked out today
        if (today - lastWorkoutDate < dayInMillis) {
            currentStreak++;
            if (currentStreak > longestStreak) {
                longestStreak = currentStreak;
            }
        } else if (today - lastWorkoutDate > dayInMillis * 2) {
            // Reset streak if missed more than one day
            currentStreak = 1;
        }
    }

    public int getWorkoutsThisWeek() {
        long now = System.currentTimeMillis();
        long weekStart = now - (7 * 24 * 60 * 60 * 1000L);

        return (int) workoutDates.stream()
                .filter(date -> date >= weekStart)
                .count();
    }

    public int getMinutesThisMonth() {
        // Implementation would calculate minutes for current month
        return monthlyMinutes.getOrDefault(getCurrentMonth(), 0);
    }

    private String getCurrentMonth() {
        // Format: "2024-01"
        return new java.text.SimpleDateFormat("yyyy-MM").format(new java.util.Date());
    }

    public MoodType getMostFrequentMood() {
        return moodFrequency.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(MoodType.NEUTRAL);
    }

    public WorkoutCategory getFavoriteCategory() {
        return categoryPreference.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(WorkoutCategory.CARDIO);
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public int getCurrentStreak() { return currentStreak; }
    public void setCurrentStreak(int currentStreak) { this.currentStreak = currentStreak; }

    public int getLongestStreak() { return longestStreak; }
    public void setLongestStreak(int longestStreak) { this.longestStreak = longestStreak; }

    public int getTotalWorkouts() { return totalWorkouts; }
    public void setTotalWorkouts(int totalWorkouts) { this.totalWorkouts = totalWorkouts; }

    public int getTotalMinutes() { return totalMinutes; }
    public void setTotalMinutes(int totalMinutes) { this.totalMinutes = totalMinutes; }

    public int getTotalCalories() { return totalCalories; }
    public void setTotalCalories(int totalCalories) { this.totalCalories = totalCalories; }

    public long getLastWorkoutDate() { return lastWorkoutDate; }
    public void setLastWorkoutDate(long lastWorkoutDate) { this.lastWorkoutDate = lastWorkoutDate; }

    public Map<MoodType, Integer> getMoodFrequency() { return new EnumMap<>(moodFrequency); }
    public Map<WorkoutCategory, Integer> getCategoryPreference() { return new EnumMap<>(categoryPreference); }
    public List<Long> getWorkoutDates() { return new ArrayList<>(workoutDates); }
}
