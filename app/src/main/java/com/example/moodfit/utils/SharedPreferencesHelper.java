/**
 * ENHANCED: Added better error handling and verification methods
 */
package com.example.moodfit.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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

public class SharedPreferencesHelper {
    private static final String TAG = "SharedPreferencesHelper";
    private static final String PREFS_NAME = "MoodFitPrefs";

    // Keys
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

    // ==================== ENHANCED USER DATA METHODS ====================

    /**
     * ENHANCED: Save user data with verification
     */
    public boolean saveUser(User user) {
        try {
            if (user == null) {
                Log.e(TAG, "Cannot save null user");
                return false;
            }

            String userJson = gson.toJson(user);
            boolean success = prefs.edit().putString(KEY_USER_DATA, userJson).commit(); // Use commit() for immediate save

            if (success) {
                Log.d(TAG, "User saved successfully: " + user.getUsername());

                // Verify the save was successful
                User verifyUser = getUser();
                if (verifyUser != null && user.getUsername().equals(verifyUser.getUsername())) {
                    Log.d(TAG, "User save verification successful");
                    return true;
                } else {
                    Log.e(TAG, "User save verification failed");
                    return false;
                }
            } else {
                Log.e(TAG, "Failed to save user to SharedPreferences");
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception while saving user", e);
            return false;
        }
    }

    /**
     * ENHANCED: Retrieve user data with better error handling
     */
    public User getUser() {
        try {
            String userJson = prefs.getString(KEY_USER_DATA, null);
            if (userJson != null && !userJson.isEmpty()) {
                User user = gson.fromJson(userJson, User.class);
                Log.d(TAG, "User retrieved successfully: " + (user != null ? user.getUsername() : "null"));
                return user;
            } else {
                Log.d(TAG, "No user data found in preferences");
            }
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "Failed to parse user JSON", e);
            // Clear corrupted data
            clearUser();
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error retrieving user", e);
        }
        return null;
    }

    /**
     * ENHANCED: Check if user data exists with validation
     */
    public boolean hasUser() {
        User user = getUser();
        boolean hasValidUser = user != null && user.getUsername() != null && !user.getUsername().trim().isEmpty();
        Log.d(TAG, "Has valid user: " + hasValidUser);
        return hasValidUser;
    }

    // ==================== ENHANCED ONBOARDING METHODS ====================

    /**
     * ENHANCED: Mark onboarding as completed with verification
     */
    public boolean setOnboardingCompleted(boolean completed) {
        try {
            boolean success = prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, completed).commit();

            if (success) {
                Log.d(TAG, "Onboarding completion status set to: " + completed);

                // Verify the save
                boolean verified = prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false);
                if (verified == completed) {
                    Log.d(TAG, "Onboarding completion verification successful");
                    return true;
                } else {
                    Log.e(TAG, "Onboarding completion verification failed");
                    return false;
                }
            } else {
                Log.e(TAG, "Failed to save onboarding completion status");
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception while setting onboarding completion", e);
            return false;
        }
    }

    /**
     * ENHANCED: Check if onboarding is completed with logging
     */
    public boolean isOnboardingCompleted() {
        try {
            boolean completed = prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false);
            Log.d(TAG, "Onboarding completed status: " + completed);
            return completed;
        } catch (Exception e) {
            Log.e(TAG, "Error checking onboarding completion", e);
            return false;
        }
    }

    /**
     * ENHANCED: Save onboarding progress with verification
     */
    public boolean saveOnboardingProgress(OnboardingData onboardingData) {
        try {
            if (onboardingData != null) {
                String progressJson = gson.toJson(onboardingData);
                boolean success = prefs.edit().putString(KEY_ONBOARDING_PROGRESS, progressJson).commit();

                if (success) {
                    Log.d(TAG, "Onboarding progress saved at step: " + onboardingData.getCurrentStep());
                    return true;
                } else {
                    Log.e(TAG, "Failed to save onboarding progress");
                    return false;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception while saving onboarding progress", e);
        }
        return false;
    }

    /**
     * ENHANCED: Get saved onboarding progress with better error handling
     */
    public OnboardingData getOnboardingProgress() {
        try {
            String progressJson = prefs.getString(KEY_ONBOARDING_PROGRESS, null);
            if (progressJson != null && !progressJson.isEmpty()) {
                OnboardingData progress = gson.fromJson(progressJson, OnboardingData.class);
                Log.d(TAG, "Onboarding progress retrieved: step " + (progress != null ? progress.getCurrentStep() : "null"));
                return progress;
            } else {
                Log.d(TAG, "No onboarding progress found");
            }
        } catch (JsonSyntaxException e) {
            Log.e(TAG, "Failed to parse onboarding progress JSON", e);
            clearOnboardingProgress();
        } catch (Exception e) {
            Log.e(TAG, "Unexpected error retrieving onboarding progress", e);
        }
        return null;
    }

    /**
     * ENHANCED: Clear onboarding progress with verification
     */
    public boolean clearOnboardingProgress() {
        try {
            boolean success = prefs.edit().remove(KEY_ONBOARDING_PROGRESS).commit();
            Log.d(TAG, "Onboarding progress cleared: " + success);
            return success;
        } catch (Exception e) {
            Log.e(TAG, "Error clearing onboarding progress", e);
            return false;
        }
    }

    // ==================== DIAGNOSTIC METHODS ====================

    /**
     * NEW: Comprehensive diagnostic method for debugging
     */
    public void logDiagnosticInfo() {
        Log.d(TAG, "=== DIAGNOSTIC INFO ===");
        Log.d(TAG, "Has user: " + hasUser());
        Log.d(TAG, "Onboarding completed: " + isOnboardingCompleted());
        Log.d(TAG, "First launch: " + isFirstLaunch());

        User user = getUser();
        if (user != null) {
            Log.d(TAG, "User ID: " + user.getUserId());
            Log.d(TAG, "Username: " + user.getUsername());
            Log.d(TAG, "Is first time user: " + user.isFirstTimeUser());
        } else {
            Log.d(TAG, "No user found");
        }

        OnboardingData progress = getOnboardingProgress();
        if (progress != null) {
            Log.d(TAG, "Onboarding progress exists - Step: " + progress.getCurrentStep());
            Log.d(TAG, "Progress completed: " + progress.isOnboardingCompleted());
        } else {
            Log.d(TAG, "No onboarding progress found");
        }
        Log.d(TAG, "=====================");
    }

    /**
     * NEW: Verify app state is consistent
     */
    public boolean verifyAppState() {
        boolean hasUser = hasUser();
        boolean onboardingCompleted = isOnboardingCompleted();

        Log.d(TAG, "App state verification - Has user: " + hasUser + ", Onboarding completed: " + onboardingCompleted);

        // Both should be true or both should be false for a consistent state
        boolean isConsistent = hasUser == onboardingCompleted;

        if (!isConsistent) {
            Log.w(TAG, "INCONSISTENT APP STATE DETECTED!");
            Log.w(TAG, "Has user: " + hasUser + ", Onboarding completed: " + onboardingCompleted);

            // Attempt to fix inconsistent state
            if (hasUser && !onboardingCompleted) {
                Log.w(TAG, "User exists but onboarding not marked complete - fixing...");
                setOnboardingCompleted(true);
                isConsistent = true;
            } else if (!hasUser && onboardingCompleted) {
                Log.w(TAG, "Onboarding marked complete but no user - resetting...");
                setOnboardingCompleted(false);
                isConsistent = true;
            }
        }

        return isConsistent;
    }

    // ==================== EXISTING METHODS (unchanged) ====================

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