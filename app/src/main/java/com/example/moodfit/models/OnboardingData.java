package com.example.moodfit.models;

import com.example.moodfit.models.enums.DifficultyLevel;
import com.example.moodfit.models.enums.WorkoutCategory;

import java.util.ArrayList;
import java.util.List;

public class OnboardingData {
    private String username;
    private DifficultyLevel selectedDifficulty;
    private boolean soundPreference;
    private boolean notificationPreference;
    private List<WorkoutCategory> preferredCategories;
    private boolean onboardingCompleted;
    private int currentStep;
    public static final int TOTAL_STEPS = 4;

    // Steps: 1=Username, 2=Difficulty, 3=Preferences, 4=Categories
    public static final int STEP_USERNAME = 1;
    public static final int STEP_DIFFICULTY = 2;
    public static final int STEP_PREFERENCES = 3;
    public static final int STEP_CATEGORIES = 4;

    // Constructor
    public OnboardingData() {
        this.preferredCategories = new ArrayList<>();
        this.currentStep = STEP_USERNAME;
        this.soundPreference = true;
        this.notificationPreference = true;
        this.selectedDifficulty = DifficultyLevel.BEGINNER;
        this.onboardingCompleted = false;
    }

    // Utility Methods
    public boolean isUsernameValid() {
        return username != null &&
                username.trim().length() >= 2 &&
                username.trim().length() <= 20 &&
                username.matches("^[a-zA-Z0-9_\\s]+$"); // Allow letters, numbers, underscore, space
    }

    public void nextStep() {
        if (currentStep < TOTAL_STEPS) {
            currentStep++;
        }
    }

    public void previousStep() {
        if (currentStep > 1) {
            currentStep--;
        }
    }

    public boolean canProceedFromCurrentStep() {
        switch (currentStep) {
            case STEP_USERNAME:
                return isUsernameValid();
            case STEP_DIFFICULTY:
                return selectedDifficulty != null;
            case STEP_PREFERENCES:
                return true; // Always can proceed from preferences
            case STEP_CATEGORIES:
                return !preferredCategories.isEmpty();
            default:
                return false;
        }
    }

    public void completeOnboarding() {
        if (currentStep == TOTAL_STEPS && canProceedFromCurrentStep()) {
            this.onboardingCompleted = true;
        }
    }

    public User createUser() {
        if (!onboardingCompleted) {
            throw new IllegalStateException("Onboarding not completed");
        }

        User user = new User(username.trim());
        user.setPreferredDifficulty(selectedDifficulty);
        user.setSoundEnabled(soundPreference);
        user.setNotificationsEnabled(notificationPreference);
        return user;
    }

    public float getProgress() {
        return (float) currentStep / TOTAL_STEPS;
    }

    public String getCurrentStepTitle() {
        switch (currentStep) {
            case STEP_USERNAME:
                return "What should we call you?";
            case STEP_DIFFICULTY:
                return "What's your fitness level?";
            case STEP_PREFERENCES:
                return "Set your preferences";
            case STEP_CATEGORIES:
                return "Choose your interests";
            default:
                return "Setup";
        }
    }

    public String getCurrentStepDescription() {
        switch (currentStep) {
            case STEP_USERNAME:
                return "Enter a name that motivates you!";
            case STEP_DIFFICULTY:
                return "Don't worry, you can change this later";
            case STEP_PREFERENCES:
                return "Customize your experience";
            case STEP_CATEGORIES:
                return "Select workout types you enjoy";
            default:
                return "";
        }
    }

    public void addPreferredCategory(WorkoutCategory category) {
        if (!preferredCategories.contains(category)) {
            preferredCategories.add(category);
        }
    }

    public void removePreferredCategory(WorkoutCategory category) {
        preferredCategories.remove(category);
    }

    public boolean isCategorySelected(WorkoutCategory category) {
        return preferredCategories.contains(category);
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public DifficultyLevel getSelectedDifficulty() { return selectedDifficulty; }
    public void setSelectedDifficulty(DifficultyLevel selectedDifficulty) { this.selectedDifficulty = selectedDifficulty; }

    public boolean isSoundPreference() { return soundPreference; }
    public void setSoundPreference(boolean soundPreference) { this.soundPreference = soundPreference; }

    public boolean isNotificationPreference() { return notificationPreference; }
    public void setNotificationPreference(boolean notificationPreference) { this.notificationPreference = notificationPreference; }

    public List<WorkoutCategory> getPreferredCategories() { return new ArrayList<>(preferredCategories); }
    public void setPreferredCategories(List<WorkoutCategory> preferredCategories) {
        this.preferredCategories = new ArrayList<>(preferredCategories);
    }

    public boolean isOnboardingCompleted() { return onboardingCompleted; }
    public void setOnboardingCompleted(boolean onboardingCompleted) { this.onboardingCompleted = onboardingCompleted; }

    public int getCurrentStep() { return currentStep; }
    public void setCurrentStep(int currentStep) {
        this.currentStep = Math.max(1, Math.min(TOTAL_STEPS, currentStep));
    }

    public int getTotalSteps() { return TOTAL_STEPS; }
}