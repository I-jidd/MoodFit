package com.example.moodfit.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.moodfit.models.MotivationalQuote;
import com.example.moodfit.models.WorkoutSession;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import com.example.moodfit.models.User;
import com.example.moodfit.models.OnboardingData;
import com.example.moodfit.models.AppSettings;
import com.example.moodfit.models.UserProgress;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for managing SharedPreferences operations
 * Handles serialization/deserialization of complex objects using Gson
 */
/**
 * Helper class for managing SharedPreferences operations
 * Handles serialization/deserialization of complex objects using Gson
 */
public class SharedPreferencesHelper {

    private static final String PREFS_NAME = "MoodFitPrefs";
    private static final String KEY_USER_DATA = "user_data";
    private static final String KEY_ONBOARDING_COMPLETED = "onboarding_completed";
    private static final String KEY_ONBOARDING_PROGRESS = "onboarding_progress";
    private static final String KEY_APP_SETTINGS = "app_settings";
    private static final String KEY_USER_PROGRESS = "user_progress";
    private static final String KEY_FIRST_LAUNCH = "first_launch";
    private static final String KEY_LAST_OPEN_DATE = "last_open_date";
    private static final String KEY_WORKOUT_SESSIONS = "workout_sessions";
    private static final String KEY_DAILY_QUOTE = "daily_quote";
    private static final String KEY_LAST_QUOTE_DATE = "last_quote_date";

    private final SharedPreferences prefs;
    private final Gson gson;

    public SharedPreferencesHelper(Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    // ==================== USER DATA ====================

    /**
     * Save user data to SharedPreferences
     */
    public void saveUser(User user) {
        try {
            if (user != null) {
                String userJson = gson.toJson(user);
                prefs.edit().putString(KEY_USER_DATA, userJson).apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieve user data from SharedPreferences
     */
    public User getUser() {
        try {
            String userJson = prefs.getString(KEY_USER_DATA, null);
            if (userJson != null && !userJson.isEmpty()) {
                return gson.fromJson(userJson, User.class);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Check if user data exists
     */
    public boolean hasUser() {
        return getUser() != null;
    }

    /**
     * Update specific user fields without replacing entire object
     */
    public void updateUser(UserUpdateCallback callback) {
        User user = getUser();
        if (user != null) {
            callback.update(user);
            saveUser(user);
        }
    }

    /**
     * Interface for updating user data
     */
    public interface UserUpdateCallback {
        void update(User user);
    }

    /**
     * Clear user data (for reset/logout functionality)
     */
    public void clearUser() {
        prefs.edit().remove(KEY_USER_DATA).apply();
    }

    // ==================== ONBOARDING ====================

    /**
     * Mark onboarding as completed
     */
    public void setOnboardingCompleted(boolean completed) {
        prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, completed).apply();
    }

    /**
     * Check if onboarding is completed
     */
    public boolean isOnboardingCompleted() {
        return prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false);
    }

    /**
     * Save onboarding progress (for resuming if user exits)
     */
    public void saveOnboardingProgress(OnboardingData onboardingData) {
        try {
            if (onboardingData != null) {
                String progressJson = gson.toJson(onboardingData);
                prefs.edit().putString(KEY_ONBOARDING_PROGRESS, progressJson).apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get saved onboarding progress
     */
    public OnboardingData getOnboardingProgress() {
        try {
            String progressJson = prefs.getString(KEY_ONBOARDING_PROGRESS, null);
            if (progressJson != null && !progressJson.isEmpty()) {
                return gson.fromJson(progressJson, OnboardingData.class);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Clear onboarding progress (after completion)
     */
    public void clearOnboardingProgress() {
        prefs.edit().remove(KEY_ONBOARDING_PROGRESS).apply();
    }

    // ==================== APP SETTINGS ====================

    /**
     * Save app settings
     */
    public void saveAppSettings(AppSettings settings) {
        try {
            if (settings != null) {
                String settingsJson = gson.toJson(settings);
                prefs.edit().putString(KEY_APP_SETTINGS, settingsJson).apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get app settings (creates default if none exist)
     */
    public AppSettings getAppSettings() {
        try {
            String settingsJson = prefs.getString(KEY_APP_SETTINGS, null);
            if (settingsJson != null && !settingsJson.isEmpty()) {
                return gson.fromJson(settingsJson, AppSettings.class);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        // Return default settings if none exist
        return new AppSettings();
    }

    // ==================== USER PROGRESS ====================

    /**
     * Save user progress data
     */
    public void saveUserProgress(UserProgress progress) {
        try {
            if (progress != null) {
                String progressJson = gson.toJson(progress);
                prefs.edit().putString(KEY_USER_PROGRESS, progressJson).apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get user progress data
     */
    public UserProgress getUserProgress() {
        try {
            String progressJson = prefs.getString(KEY_USER_PROGRESS, null);
            if (progressJson != null && !progressJson.isEmpty()) {
                return gson.fromJson(progressJson, UserProgress.class);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ==================== WORKOUT SESSIONS ====================

    /**
     * Save workout sessions list
     */
    public void saveWorkoutSessions(List<WorkoutSession> sessions) {
        try {
            if (sessions != null) {
                String sessionsJson = gson.toJson(sessions);
                prefs.edit().putString(KEY_WORKOUT_SESSIONS, sessionsJson).apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get workout sessions list
     */
    public List<WorkoutSession> getWorkoutSessions() {
        try {
            String sessionsJson = prefs.getString(KEY_WORKOUT_SESSIONS, null);
            if (sessionsJson != null && !sessionsJson.isEmpty()) {
                Type listType = new TypeToken<List<WorkoutSession>>(){}.getType();
                return gson.fromJson(sessionsJson, listType);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Add a single workout session
     */
    public void addWorkoutSession(WorkoutSession session) {
        List<WorkoutSession> sessions = getWorkoutSessions();
        sessions.add(session);
        saveWorkoutSessions(sessions);
    }

    // ==================== DAILY QUOTES ====================

    /**
     * Save daily motivational quote
     */
    public void saveDailyQuote(MotivationalQuote quote) {
        try {
            if (quote != null) {
                String quoteJson = gson.toJson(quote);
                prefs.edit()
                        .putString(KEY_DAILY_QUOTE, quoteJson)
                        .putLong(KEY_LAST_QUOTE_DATE, System.currentTimeMillis())
                        .apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get daily motivational quote
     */
    public MotivationalQuote getDailyQuote() {
        try {
            String quoteJson = prefs.getString(KEY_DAILY_QUOTE, null);
            if (quoteJson != null && !quoteJson.isEmpty()) {
                return gson.fromJson(quoteJson, MotivationalQuote.class);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Check if daily quote needs to be refreshed
     */
    public boolean needsNewDailyQuote() {
        long lastQuoteDate = prefs.getLong(KEY_LAST_QUOTE_DATE, 0);
        long today = System.currentTimeMillis();
        long dayInMillis = 24 * 60 * 60 * 1000L;

        return (today - lastQuoteDate) >= dayInMillis;
    }

    // ==================== APP USAGE TRACKING ====================

    /**
     * Check if this is the first app launch
     */
    public boolean isFirstLaunch() {
        return prefs.getBoolean(KEY_FIRST_LAUNCH, true);
    }

    /**
     * Mark first launch as completed
     */
    public void setFirstLaunchCompleted() {
        prefs.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply();
    }

    /**
     * Update last open date
     */
    public void updateLastOpenDate() {
        prefs.edit().putLong(KEY_LAST_OPEN_DATE, System.currentTimeMillis()).apply();
    }

    /**
     * Get last open date
     */
    public long getLastOpenDate() {
        return prefs.getLong(KEY_LAST_OPEN_DATE, 0);
    }

    // ==================== UTILITY METHODS ====================

    /**
     * Clear all app data (for reset functionality)
     */
    public void clearAllData() {
        prefs.edit().clear().apply();
    }

    /**
     * Get all preferences as a backup (for export functionality)
     */
    public String exportData() {
        try {
            return gson.toJson(prefs.getAll());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Check if app has any stored data
     */
    public boolean hasStoredData() {
        return hasUser() || isOnboardingCompleted();
    }
}