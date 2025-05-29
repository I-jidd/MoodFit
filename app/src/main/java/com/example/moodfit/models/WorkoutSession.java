package com.example.moodfit.models;

import com.example.moodfit.models.enums.MoodType;

import java.util.ArrayList;
import java.util.List;

public class WorkoutSession {
    private String sessionId;
    private String userId;
    private long startTime;
    private long endTime;
    private int durationMinutes;
    private MoodType selectedMood;
    private List<Exercise> exercises;
    private int caloriesBurned;
    private boolean completed;
    private String notes;
    private int userRating; // 1-5 stars

    // Constructors
    public WorkoutSession() {
        this.sessionId = generateSessionId();
        this.exercises = new ArrayList<>();
        this.startTime = System.currentTimeMillis();
    }

    public WorkoutSession(String userId, MoodType selectedMood) {
        this();
        this.userId = userId;
        this.selectedMood = selectedMood;
    }

    // Utility Methods
    private String generateSessionId() {
        return "session_" + System.currentTimeMillis();
    }

    public void startWorkout() {
        this.startTime = System.currentTimeMillis();
    }

    public void endWorkout() {
        this.endTime = System.currentTimeMillis();
        this.durationMinutes = (int) ((endTime - startTime) / (1000 * 60));
        this.completed = true;
    }

    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
        // Update estimated calories
        this.caloriesBurned += exercise.getEstimatedCalories();
    }

    public boolean isActive() {
        return startTime > 0 && endTime == 0;
    }

    public long getDurationInMillis() {
        if (endTime > 0) {
            return endTime - startTime;
        } else if (startTime > 0) {
            return System.currentTimeMillis() - startTime;
        }
        return 0;
    }

    // Getters and Setters
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }

    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { this.endTime = endTime; }

    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }

    public MoodType getSelectedMood() { return selectedMood; }
    public void setSelectedMood(MoodType selectedMood) { this.selectedMood = selectedMood; }

    public List<Exercise> getExercises() { return new ArrayList<>(exercises); }
    public void setExercises(List<Exercise> exercises) { this.exercises = new ArrayList<>(exercises); }

    public int getCaloriesBurned() { return caloriesBurned; }
    public void setCaloriesBurned(int caloriesBurned) { this.caloriesBurned = caloriesBurned; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public int getUserRating() { return userRating; }
    public void setUserRating(int userRating) { this.userRating = Math.max(1, Math.min(5, userRating)); }
}

