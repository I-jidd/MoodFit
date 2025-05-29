package com.example.moodfit.utils;

import android.content.Context;
import com.example.moodfit.models.User;
import com.example.moodfit.models.UserProgress;
import com.example.moodfit.models.WorkoutSession;
import com.example.moodfit.models.AppSettings;
import com.example.moodfit.models.MotivationalQuote;
import com.example.moodfit.models.enums.MoodType;

import java.util.List;
import java.util.Random;

/**
 * Central data management utility
 * Handles business logic for data operations
 */
public class DataManager {

    private final SharedPreferencesHelper prefsHelper;
    private final Context context;

    public DataManager(Context context) {
        this.context = context;
        this.prefsHelper = new SharedPreferencesHelper(context);
    }

    // ==================== USER MANAGEMENT ====================

    /**
     * Get current user or create if needed
     */
    public User getCurrentUser() {
        User user = prefsHelper.getUser();
        if (user == null) {
            // Create default user if none exists
            user = new User();
            prefsHelper.saveUser(user);
        }
        return user;
    }

    /**
     * Update user streak
     */
    public void updateUserStreak() {
        prefsHelper.updateUser(user -> {
            if (user.hasWorkedOutToday()) {
                user.incrementStreak();
            } else {
                // Check if streak should be reset due to missed days
                long daysSinceLastWorkout = (System.currentTimeMillis() - user.getLastWorkoutDate())
                        / (24 * 60 * 60 * 1000);
                if (daysSinceLastWorkout > 1) {
                    user.resetStreak();
                }
            }
        });
    }

    /**
     * Record workout completion
     */
    public void recordWorkoutCompletion(WorkoutSession session) {
        // Save workout session
        prefsHelper.addWorkoutSession(session);

        // Update user stats
        prefsHelper.updateUser(user -> {
            user.addWorkout(session.getDurationMinutes());
            if (!user.hasWorkedOutToday()) {
                user.incrementStreak();
            }
        });

        // Update progress
        UserProgress progress = getUserProgress();
        progress.recordWorkout(session);
        prefsHelper.saveUserProgress(progress);
    }

    // ==================== PROGRESS TRACKING ====================

    /**
     * Get user progress or create if needed
     */
    public UserProgress getUserProgress() {
        UserProgress progress = prefsHelper.getUserProgress();
        if (progress == null) {
            User user = getCurrentUser();
            progress = new UserProgress(user.getUserId());
            prefsHelper.saveUserProgress(progress);
        }
        return progress;
    }

    /**
     * Get workouts completed this week
     */
    public int getWorkoutsThisWeek() {
        UserProgress progress = getUserProgress();
        return progress.getWorkoutsThisWeek();
    }

    /**
     * Get total workout minutes
     */
    public int getTotalWorkoutMinutes() {
        User user = getCurrentUser();
        return user.getTotalMinutes();
    }

    // ==================== MOTIVATIONAL QUOTES ====================

    /**
     * Get daily motivational quote
     */
    public MotivationalQuote getDailyQuote() {
        if (prefsHelper.needsNewDailyQuote()) {
            MotivationalQuote newQuote = generateRandomQuote();
            prefsHelper.saveDailyQuote(newQuote);
            return newQuote;
        }

        MotivationalQuote existingQuote = prefsHelper.getDailyQuote();
        return existingQuote != null ? existingQuote : generateRandomQuote();
    }

    /**
     * Generate random motivational quote
     */
    private MotivationalQuote generateRandomQuote() {
        String[] quotes = {
                "The only bad workout is the one that didn't happen.",
                "Your body can do it. It's your mind you need to convince.",
                "Fitness is not about being better than someone else. It's about being better than you used to be.",
                "The groundwork for all happiness is good health.",
                "Take care of your body. It's the only place you have to live.",
                "A healthy outside starts from the inside.",
                "Exercise is king. Nutrition is queen. Put them together and you've got a kingdom.",
                "The first wealth is health.",
                "To keep the body in good health is a duty... otherwise we shall not be able to keep our mind strong and clear.",
                "Physical fitness is not only one of the most important keys to a healthy body, it is the basis of dynamic and creative intellectual activity."
        };

        String[] authors = {
                "Daily Motivation",
                "Fitness Wisdom",
                "Health Quote",
                "Emerson",
                "Jim Rohn",
                "Robert Urich",
                "Jack LaLanne",
                "Emerson",
                "Buddha",
                "John F. Kennedy"
        };

        Random random = new Random();
        int index = random.nextInt(quotes.length);

        return new MotivationalQuote(quotes[index], authors[index], "fitness");
    }

    // ==================== APP INITIALIZATION ====================

    /**
     * Initialize app on first launch
     */
    public void initializeApp() {
        if (prefsHelper.isFirstLaunch()) {
            // Set up default app settings
            AppSettings defaultSettings = new AppSettings();
            prefsHelper.saveAppSettings(defaultSettings);

            // Mark first launch as completed
            prefsHelper.setFirstLaunchCompleted();
        }

        // Update last open date
        prefsHelper.updateLastOpenDate();

        // Update user app opens if user exists
        User user = prefsHelper.getUser();
        if (user != null) {
            user.recordAppOpen();
            prefsHelper.saveUser(user);
        }
    }

    /**
     * Check if user setup is complete
     */
    public boolean isUserSetupComplete() {
        return prefsHelper.isOnboardingCompleted() && prefsHelper.hasUser();
    }

    /**
     * Get app settings
     */
    public AppSettings getAppSettings() {
        return prefsHelper.getAppSettings();
    }

    /**
     * Save app settings
     */
    public void saveAppSettings(AppSettings settings) {
        prefsHelper.saveAppSettings(settings);
    }

    // ==================== DATA EXPORT/BACKUP ====================

    /**
     * Export all user data for backup
     */
    public String exportUserData() {
        return prefsHelper.exportData();
    }

    /**
     * Reset all app data
     */
    public void resetAllData() {
        prefsHelper.clearAllData();
    }

    /**
     * Check if app has stored data
     */
    public boolean hasStoredData() {
        return prefsHelper.hasStoredData();
    }
}