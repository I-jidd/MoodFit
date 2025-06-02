package com.example.moodfit.utils;

import android.content.Context;
import com.example.moodfit.models.User;
import com.example.moodfit.models.UserProgress;
import com.example.moodfit.models.WorkoutSession;
import com.example.moodfit.models.AppSettings;
import com.example.moodfit.models.MotivationalQuote;
import com.example.moodfit.models.Exercise;
import com.example.moodfit.models.enums.MoodType;
import com.example.moodfit.models.enums.DifficultyLevel;
import com.example.moodfit.models.enums.WorkoutCategory;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;

/**
 * Enhanced Central data management utility
 * Handles business logic for data operations with comprehensive integration
 */
public class DataManager {

    private static final String TAG = "DataManager";
    private final SharedPreferencesHelper prefsHelper;
    private final Context context;

    // Cache for frequently accessed data
    private User currentUserCache;
    private UserProgress currentProgressCache;
    private AppSettings currentSettingsCache;

    public DataManager(Context context) {
        this.context = context.getApplicationContext(); // Use application context to avoid memory leaks
        this.prefsHelper = new SharedPreferencesHelper(this.context);
    }

    // ==================== USER MANAGEMENT ====================

    /**
     * Get current user or create if needed
     */
    public User getCurrentUser() {
        if (currentUserCache == null) {
            currentUserCache = prefsHelper.getUser();
            if (currentUserCache == null) {
                // Create default user if none exists
                currentUserCache = new User();
                prefsHelper.saveUser(currentUserCache);
            }
        }
        return currentUserCache;
    }

    /**
     * Update user and refresh cache
     */
    public void updateUser(User user) {
        if (user != null) {
            prefsHelper.saveUser(user);
            currentUserCache = user; // Update cache
        }
    }

    /**
     * Update user streak with proper business logic
     */
    public void updateUserStreak() {
        User user = getCurrentUser();

        // ✅ REMOVED the duplicate "worked out today" check
        // This method should only be called when we want to update the streak

        // Check if this is consecutive day
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);

        Calendar lastWorkout = Calendar.getInstance();
        lastWorkout.setTimeInMillis(user.getLastWorkoutDate());

        if (isSameDay(yesterday, lastWorkout) || user.getCurrentStreak() == 0) {
            // Consecutive day or starting new streak
            int oldStreak = user.getCurrentStreak();
            user.incrementStreak();
            android.util.Log.d(TAG, "🔥 Streak incremented from " + oldStreak + " to " + user.getCurrentStreak());
        } else {
            // Reset streak if gap is more than 1 day
            int oldStreak = user.getCurrentStreak();
            user.resetStreak();
            user.incrementStreak(); // Start new streak with today
            android.util.Log.d(TAG, "🔥 Streak reset and restarted. Was " + oldStreak + ", now " + user.getCurrentStreak());
        }

        updateUser(user);
    }

    /**
     * Record workout completion with comprehensive tracking
     */
    public void recordWorkoutCompletion(WorkoutSession session) {
        try {
            // Validate session
            if (session == null || !session.isCompleted()) {
                throw new IllegalArgumentException("Invalid or incomplete workout session");
            }

            // Save workout session
            prefsHelper.addWorkoutSession(session);

            // Update user stats
            User user = getCurrentUser();

            // Add to DataManager.recordWorkoutCompletion() before the other debug logs
            long timeDiff = System.currentTimeMillis() - user.getLastWorkoutDate();
            long minutesAgo = timeDiff / (1000 * 60);
            android.util.Log.d(TAG, "🔥 DEBUG - last workout was " + minutesAgo + " minutes ago");

// Convert to readable date
            java.util.Date lastWorkoutTime = new java.util.Date(user.getLastWorkoutDate());
            android.util.Log.d(TAG, "🔥 DEBUG - last workout time: " + lastWorkoutTime.toString());

            // ✅ ADD THESE DEBUG LOGS
            android.util.Log.d(TAG, "🔥 DEBUG - user.lastWorkoutDate: " + user.getLastWorkoutDate());
            android.util.Log.d(TAG, "🔥 DEBUG - current time: " + System.currentTimeMillis());
            android.util.Log.d(TAG, "🔥 DEBUG - hasWorkedOutToday BEFORE: " + user.hasWorkedOutToday());

            // Check if worked out today BEFORE updating lastWorkoutDate
            boolean alreadyWorkedOutToday = user.hasWorkedOutToday();

            // Update workout stats (this sets lastWorkoutDate to now)
            user.addWorkout(session.getDurationMinutes());

            // ✅ ADD THIS DEBUG LOG TOO
            android.util.Log.d(TAG, "🔥 DEBUG - hasWorkedOutToday AFTER: " + user.hasWorkedOutToday());

            // Only update streak if this is their first workout today
            if (!alreadyWorkedOutToday) {
                android.util.Log.d(TAG, "🔥 UPDATING STREAK - first workout today!");
                updateUserStreak();
            } else {
                android.util.Log.d(TAG, "🔥 SKIPPING STREAK UPDATE - already worked out today");
            }

            updateUser(user);

            // Update progress tracking
            UserProgress progress = getUserProgress();
            progress.recordWorkout(session);
            saveUserProgress(progress);

            android.util.Log.d(TAG, "Workout completion recorded successfully");

        } catch (Exception e) {
            android.util.Log.e(TAG, "Error recording workout completion", e);
        }
    }

    public void resetTodaysWorkout() {
        try {
            User user = getCurrentUser();
            user.setLastWorkoutDate(0); // Reset to never worked out
            updateUser(user);
            android.util.Log.d(TAG, "🔥 RESET - lastWorkoutDate set to 0");
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error resetting workout date", e);
        }
    }

    /**
     * Check if two calendar instances represent the same day
     */
    private boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    // ==================== PROGRESS TRACKING ====================

    /**
     * Get user progress or create if needed
     */
    public UserProgress getUserProgress() {
        if (currentProgressCache == null) {
            currentProgressCache = prefsHelper.getUserProgress();
            if (currentProgressCache == null) {
                User user = getCurrentUser();
                currentProgressCache = new UserProgress(user.getUserId());
                prefsHelper.saveUserProgress(currentProgressCache);
            }
        }
        return currentProgressCache;
    }

    /**
     * Save user progress and update cache
     */
    public void saveUserProgress(UserProgress progress) {
        if (progress != null) {
            prefsHelper.saveUserProgress(progress);
            currentProgressCache = progress;
        }
    }

    /**
     * Get workouts completed this week
     */
    public int getWorkoutsThisWeek() {
        try {
            UserProgress progress = getUserProgress();
            return progress.getWorkoutsThisWeek();
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error getting workouts this week", e);
            return 0;
        }
    }

    /**
     * Get total workout minutes
     */
    public int getTotalWorkoutMinutes() {
        try {
            User user = getCurrentUser();
            return user.getTotalMinutes();
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error getting total workout minutes", e);
            return 0;
        }
    }

    /**
     * Get workout sessions for analytics
     */
    public List<WorkoutSession> getAllWorkoutSessions() {
        try {
            return prefsHelper.getWorkoutSessions();
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error getting workout sessions", e);
            return new ArrayList<>();
        }
    }

    /**
     * Get recent workout sessions (last 30 days)
     */
    public List<WorkoutSession> getRecentWorkoutSessions() {
        try {
            List<WorkoutSession> allSessions = getAllWorkoutSessions();
            List<WorkoutSession> recentSessions = new ArrayList<>();

            long thirtyDaysAgo = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000);

            for (WorkoutSession session : allSessions) {
                if (session.getEndTime() >= thirtyDaysAgo) {
                    recentSessions.add(session);
                }
            }

            return recentSessions;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error getting recent workout sessions", e);
            return new ArrayList<>();
        }
    }

    // ==================== MOTIVATIONAL QUOTES ====================

    /**
     * Get daily motivational quote with caching
     */
    public MotivationalQuote getDailyQuote() {
        try {
            if (prefsHelper.needsNewDailyQuote()) {
                MotivationalQuote newQuote = generateRandomQuote();
                prefsHelper.saveDailyQuote(newQuote);
                return newQuote;
            }

            MotivationalQuote existingQuote = prefsHelper.getDailyQuote();
            return existingQuote != null ? existingQuote : generateRandomQuote();
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error getting daily quote", e);
            return generateFallbackQuote();
        }
    }

    /**
     * Generate random motivational quote with mood-specific options
     */
    private MotivationalQuote generateRandomQuote() {
        // Expanded quote collection with mood categories
        QuoteData[] quotes = {
                // General Motivation
                new QuoteData("The only bad workout is the one that didn't happen.", "Daily Motivation", "general"),
                new QuoteData("Your body can do it. It's your mind you need to convince.", "Fitness Wisdom", "general"),
                new QuoteData("Fitness is not about being better than someone else. It's about being better than you used to be.", "Health Quote", "general"),

                // Health & Wellness
                new QuoteData("The groundwork for all happiness is good health.", "Emerson", "health"),
                new QuoteData("Take care of your body. It's the only place you have to live.", "Jim Rohn", "health"),
                new QuoteData("A healthy outside starts from the inside.", "Robert Urich", "health"),

                // Motivation & Success
                new QuoteData("Exercise is king. Nutrition is queen. Put them together and you've got a kingdom.", "Jack LaLanne", "success"),
                new QuoteData("The first wealth is health.", "Emerson", "success"),
                new QuoteData("Physical fitness is not only one of the most important keys to a healthy body, it is the basis of dynamic and creative intellectual activity.", "John F. Kennedy", "success"),

                // Inspirational
                new QuoteData("Success isn't always about greatness. It's about consistency. Consistent hard work leads to success.", "Dwayne Johnson", "inspiration"),
                new QuoteData("The pain you feel today will be the strength you feel tomorrow.", "Fitness Wisdom", "inspiration"),
                new QuoteData("Don't limit your challenges, challenge your limits.", "Daily Motivation", "inspiration"),

                // Mind & Body
                new QuoteData("To keep the body in good health is a duty... otherwise we shall not be able to keep our mind strong and clear.", "Buddha", "mindfulness"),
                new QuoteData("A strong body makes the mind strong.", "Thomas Jefferson", "mindfulness"),
                new QuoteData("Happiness is the highest form of health.", "Dalai Lama", "mindfulness")
        };

        Random random = new Random();
        QuoteData selectedQuote = quotes[random.nextInt(quotes.length)];

        return new MotivationalQuote(selectedQuote.text, selectedQuote.author, selectedQuote.category);
    }

    /**
     * Helper class for quote data
     */
    private static class QuoteData {
        final String text;
        final String author;
        final String category;

        QuoteData(String text, String author, String category) {
            this.text = text;
            this.author = author;
            this.category = category;
        }
    }

    /**
     * Generate fallback quote in case of errors
     */
    private MotivationalQuote generateFallbackQuote() {
        return new MotivationalQuote(
                "Every workout brings you one step closer to your goals.",
                "MoodFit",
                "motivation"
        );
    }

    // ==================== APP SETTINGS MANAGEMENT ====================

    /**
     * Get app settings with caching
     */
    public AppSettings getAppSettings() {
        if (currentSettingsCache == null) {
            currentSettingsCache = prefsHelper.getAppSettings();
        }
        return currentSettingsCache;
    }

    /**
     * Save app settings and update cache
     */
    public void saveAppSettings(AppSettings settings) {
        if (settings != null) {
            prefsHelper.saveAppSettings(settings);
            currentSettingsCache = settings;
        }
    }

    // ==================== APP INITIALIZATION ====================

    /**
     * Initialize app on first launch with comprehensive setup
     */
    public void initializeApp() {
        try {
            if (prefsHelper.isFirstLaunch()) {
                // Set up default app settings
                AppSettings defaultSettings = new AppSettings();
                saveAppSettings(defaultSettings);

                // Mark first launch as completed
                prefsHelper.setFirstLaunchCompleted();

                android.util.Log.d(TAG, "App initialized for first launch");
            }

            // Update last open date
            prefsHelper.updateLastOpenDate();

            // Update user app opens if user exists
            User user = prefsHelper.getUser();
            if (user != null) {
                user.recordAppOpen();
                updateUser(user);
            }

        } catch (Exception e) {
            android.util.Log.e(TAG, "Error during app initialization", e);
        }
    }
    // ==================== DATA ANALYTICS ====================

    /**
     * Get user statistics for dashboard
     */
    public UserStats getUserStats() {
        try {
            User user = getCurrentUser();
            UserProgress progress = getUserProgress();

            return new UserStats(
                    user.getCurrentStreak(),
                    user.getBestStreak(),
                    user.getTotalWorkouts(),
                    user.getTotalMinutes(),
                    getWorkoutsThisWeek(),
                    progress.getTotalCalories(),
                    progress.getMostFrequentMood(),
                    progress.getFavoriteCategory()
            );
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error getting user stats", e);
            return new UserStats(); // Return default stats
        }
    }

    /**
     * User statistics data class
     */
    public static class UserStats {
        public final int currentStreak;
        public final int bestStreak;
        public final int totalWorkouts;
        public final int totalMinutes;
        public final int workoutsThisWeek;
        public final int totalCalories;
        public final MoodType mostFrequentMood;
        public final WorkoutCategory favoriteCategory;

        public UserStats(int currentStreak, int bestStreak, int totalWorkouts, int totalMinutes,
                         int workoutsThisWeek, int totalCalories, MoodType mostFrequentMood,
                         WorkoutCategory favoriteCategory) {
            this.currentStreak = currentStreak;
            this.bestStreak = bestStreak;
            this.totalWorkouts = totalWorkouts;
            this.totalMinutes = totalMinutes;
            this.workoutsThisWeek = workoutsThisWeek;
            this.totalCalories = totalCalories;
            this.mostFrequentMood = mostFrequentMood;
            this.favoriteCategory = favoriteCategory;
        }

        public UserStats() {
            this(0, 0, 0, 0, 0, 0, MoodType.NEUTRAL, WorkoutCategory.CARDIO);
        }
    }

    // ==================== DATA EXPORT/BACKUP ====================

    /**
     * Export all user data for backup
     */
    public String exportUserData() {
        try {
            return prefsHelper.exportData();
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error exporting user data", e);
            return null;
        }
    }

    /**
     * Reset all app data with confirmation
     */
    public boolean resetAllData() {
        try {
            prefsHelper.clearAllData();

            // Clear caches
            currentUserCache = null;
            currentProgressCache = null;
            currentSettingsCache = null;

            android.util.Log.d(TAG, "All app data reset successfully");
            return true;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error resetting app data", e);
            return false;
        }
    }

    /**
     * Check if app has stored data
     */
    public boolean hasStoredData() {
        return prefsHelper.hasStoredData();
    }

    /**
     * Clear caches (useful for testing or when data might be stale)
     */
    public void clearCaches() {
        currentUserCache = null;
        currentProgressCache = null;
        currentSettingsCache = null;
    }

    /**
     * Force refresh of cached data
     */
    public void refreshData() {
        clearCaches();
        getCurrentUser(); // This will reload from storage
        getUserProgress();
        getAppSettings();
    }
}