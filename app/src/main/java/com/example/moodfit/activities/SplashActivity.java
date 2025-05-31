/**
 * ENHANCED: Added comprehensive debugging and state verification
 */
package com.example.moodfit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moodfit.R;
import com.example.moodfit.models.User;
import com.example.moodfit.utils.DataManager;
import com.example.moodfit.utils.SharedPreferencesHelper;

public class SplashActivity extends AppCompatActivity {

    // Constants
    private static final String TAG = "SplashActivity";
    private static final int SPLASH_DURATION = 3000; // 3 seconds to enjoy the animations
    private static final int MINIMUM_LOADING_TIME = 2000; // Minimum time to show splash

    // Data managers
    private DataManager dataManager;
    private SharedPreferencesHelper prefsHelper;

    // UI Components
    private ImageView appLogo;
    private TextView appName;
    private TextView tagline;
    private ProgressBar loadingIndicator;
    private TextView loadingText;

    // Timing
    private long startTime;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Hide system UI for immersive splash experience
        hideSystemUI();

        // Record start time
        startTime = System.currentTimeMillis();

        // Initialize components
        initializeViews();
        initializeDataManagers();

        // Start animations
        startSplashAnimations();

        // Initialize app and determine navigation
        initializeApp();

        android.util.Log.d("DEBUG_STATE", "=== CURRENT APP STATE ===");
        android.util.Log.d("DEBUG_STATE", "Onboarding completed: " + prefsHelper.isOnboardingCompleted());
        android.util.Log.d("DEBUG_STATE", "Has user: " + prefsHelper.hasUser());
        User debugUser = prefsHelper.getUser();
        if (debugUser != null) {
            android.util.Log.d("DEBUG_STATE", "User: " + debugUser.getUsername());
        }
    }

    /**
     * Hide system UI for immersive splash screen
     */
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

    /**
     * Initialize UI components - Updated to match your actual XML IDs
     */
    private void initializeViews() {
        appLogo = findViewById(R.id.iv_app_logo);
        appName = findViewById(R.id.tv_app_name);
        tagline = findViewById(R.id.tv_tagline);
        loadingIndicator = findViewById(R.id.progress_loading);
        loadingText = findViewById(R.id.tv_loading_text);

        handler = new Handler(Looper.getMainLooper());
    }

    /**
     * Initialize data management components
     */
    private void initializeDataManagers() {
        prefsHelper = new SharedPreferencesHelper(this);
        dataManager = new DataManager(this);
    }

    /**
     * Start splash screen animations using your existing animation files
     */
    private void startSplashAnimations() {
        try {
            // Logo entrance animation using the fixed splash_logo_entrance.xml
            Animation logoAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_logo_entrance);
            appLogo.startAnimation(logoAnimation);
        } catch (Exception e) {
            // Fallback: Simple fade in for logo
            android.util.Log.w(TAG, "Logo animation failed, using fallback", e);
            appLogo.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(800).start();
        }

        // App name with delay
        handler.postDelayed(() -> {
            try {
                Animation textAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_text_fade_in);
                appName.startAnimation(textAnimation);
                appName.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                // Fallback: Simple fade in for text
                android.util.Log.w(TAG, "Text animation failed, using fallback", e);
                appName.setVisibility(View.VISIBLE);
                appName.animate().alpha(1f).setDuration(600).start();
            }
        }, 400);

        // Tagline with delay
        handler.postDelayed(() -> {
            try {
                Animation taglineAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
                tagline.startAnimation(taglineAnimation);
                tagline.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                // Fallback: Simple fade in for tagline
                android.util.Log.w(TAG, "Tagline animation failed, using fallback", e);
                tagline.setVisibility(View.VISIBLE);
                tagline.animate().alpha(1f).setDuration(600).start();
            }
        }, 800);

        // Update loading text periodically
        startLoadingTextAnimation();
    }

    /**
     * Animate loading text with different messages
     */
    private void startLoadingTextAnimation() {
        final String[] loadingMessages = {
                "Getting ready...",
                "Loading your profile...",
                "Preparing workouts...",
                "Almost there..."
        };

        final int[] delays = {0, 800, 1600, 2200};

        for (int i = 0; i < loadingMessages.length; i++) {
            final int index = i;
            handler.postDelayed(() -> {
                if (!isFinishing() && loadingText != null) {
                    updateLoadingText(loadingMessages[index]);
                }
            }, delays[i]);
        }
    }

    /**
     * Update loading text with fade animation
     */
    private void updateLoadingText(String text) {
        if (loadingText != null) {
            loadingText.animate()
                    .alpha(0f)
                    .setDuration(200)
                    .withEndAction(() -> {
                        loadingText.setText(text);
                        loadingText.animate()
                                .alpha(1f)
                                .setDuration(200)
                                .start();
                    })
                    .start();
        }
    }

    /**
     * ENHANCED: Initialize app and determine navigation path with comprehensive debugging
     */
    private void initializeApp() {
        // Run initialization in background
        new Thread(() -> {
            try {
                android.util.Log.d(TAG, "Starting app initialization");

                // Initialize app (sets up default settings, etc.)
                dataManager.initializeApp();

                // ENHANCED: Comprehensive state verification
                android.util.Log.d(TAG, "=== APP STATE VERIFICATION ===");

                // Log diagnostic information
                prefsHelper.logDiagnosticInfo();

                // Verify app state consistency
                boolean stateConsistent = prefsHelper.verifyAppState();
                android.util.Log.d(TAG, "App state consistent: " + stateConsistent);

                // Simulate some loading time for better UX
                Thread.sleep(2300);

                // Determine navigation destination with enhanced logic
                final Intent targetIntent = determineNavigationDestination();

                // Ensure minimum splash time has passed
                long elapsedTime = System.currentTimeMillis() - startTime;
                long remainingTime = Math.max(0, MINIMUM_LOADING_TIME - elapsedTime);

                // Navigate after remaining time
                handler.postDelayed(() -> {
                    if (!isFinishing()) {
                        navigateToDestination(targetIntent);
                    }
                }, remainingTime);

            } catch (Exception e) {
                // Handle any initialization errors
                android.util.Log.e(TAG, "Error during app initialization", e);
                handler.post(() -> {
                    if (!isFinishing()) {
                        handleInitializationError(e);
                    }
                });
            }
        }).start();
    }

    /**
     * ENHANCED: Determine where to navigate based on app state with detailed logging
     */
    private Intent determineNavigationDestination() {
        android.util.Log.d(TAG, "Determining navigation destination...");

        // Check if onboarding is completed
        boolean onboardingCompleted = prefsHelper.isOnboardingCompleted();
        boolean hasUser = prefsHelper.hasUser();

        android.util.Log.d(TAG, "Onboarding completed: " + onboardingCompleted);
        android.util.Log.d(TAG, "Has user: " + hasUser);

        // ENHANCED: Additional validation
        if (hasUser) {
            User user = prefsHelper.getUser();
            if (user != null) {
                android.util.Log.d(TAG, "User details - ID: " + user.getUserId() +
                        ", Username: " + user.getUsername() +
                        ", First time: " + user.isFirstTimeUser());
            }
        }

        if (onboardingCompleted && hasUser) {
            // User has completed setup - go to home
            android.util.Log.d(TAG, "Navigation decision: HOME (setup complete)");
            updateLoadingText("Welcome back!");
            return new Intent(this, HomeActivity.class);
        } else {
            // New user or incomplete setup - go to onboarding
            android.util.Log.d(TAG, "Navigation decision: ONBOARDING (setup incomplete)");

            if (!onboardingCompleted && !hasUser) {
                android.util.Log.d(TAG, "Reason: Fresh install - no user, no onboarding");
            } else if (!onboardingCompleted && hasUser) {
                android.util.Log.w(TAG, "Reason: User exists but onboarding not complete (inconsistent state)");
            } else if (onboardingCompleted && !hasUser) {
                android.util.Log.w(TAG, "Reason: Onboarding complete but no user (inconsistent state)");
            }

            updateLoadingText("Setting up your experience...");
            return new Intent(this, UsernameSetupActivity.class);
        }
    }

    /**
     * Navigate to the determined destination
     */
    private void navigateToDestination(Intent intent) {
        android.util.Log.d(TAG, "Navigating to: " + intent.getComponent().getClassName());

        // Add transition flags
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);

        // Add smooth transition animation using your existing animations
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        finish();
    }

    /**
     * ENHANCED: Handle initialization errors gracefully with state reset
     */
    private void handleInitializationError(Exception e) {
        // Log error (in production, send to crash reporting)
        android.util.Log.e(TAG, "Initialization error", e);

        // Check if this might be a data corruption issue
        try {
            boolean hasCorruptedData = checkForDataCorruption();
            if (hasCorruptedData) {
                android.util.Log.w(TAG, "Data corruption detected, clearing corrupted data");
                clearCorruptedData();
            }
        } catch (Exception clearError) {
            android.util.Log.e(TAG, "Failed to clear corrupted data", clearError);
        }

        // Show error message briefly, then proceed to onboarding
        updateLoadingText("Something went wrong, setting up...");

        handler.postDelayed(() -> {
            Intent fallbackIntent = new Intent(this, UsernameSetupActivity.class);
            navigateToDestination(fallbackIntent);
        }, 1000);
    }

    /**
     * NEW: Check for potential data corruption
     */
    private boolean checkForDataCorruption() {
        try {
            // Try to access key data and see if it's valid
            boolean onboardingCompleted = prefsHelper.isOnboardingCompleted();
            User user = prefsHelper.getUser();

            // Check for inconsistent states that might indicate corruption
            if (onboardingCompleted && (user == null || user.getUsername() == null)) {
                android.util.Log.w(TAG, "Detected corrupted state: onboarding complete but invalid user");
                return true;
            }

            if (user != null && (user.getUserId() == null || user.getUsername() == null)) {
                android.util.Log.w(TAG, "Detected corrupted user data");
                return true;
            }

            return false;
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error checking for data corruption", e);
            return true; // Assume corruption if we can't even check
        }
    }

    /**
     * NEW: Clear corrupted data to allow fresh start
     */
    private void clearCorruptedData() {
        try {
            android.util.Log.w(TAG, "Clearing potentially corrupted data");

            // Clear user data
            prefsHelper.clearUser();

            // Reset onboarding state
            prefsHelper.setOnboardingCompleted(false);

            // Clear any onboarding progress
            prefsHelper.clearOnboardingProgress();

            android.util.Log.d(TAG, "Corrupted data cleared successfully");
        } catch (Exception e) {
            android.util.Log.e(TAG, "Failed to clear corrupted data", e);
        }
    }

    /**
     * Handle back press - disable during splash
     */
    @Override
    public void onBackPressed() {
        // Disable back button during splash screen
        // This prevents users from accidentally exiting during loading
    }

    /**
     * Clean up when activity is destroyed
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * Handle resume - restore immersive mode
     */
    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
    }

    /**
     * Show system UI when activity loses focus
     */
    @Override
    protected void onPause() {
        super.onPause();
        showSystemUI();
    }

    /**
     * Show system UI
     */
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
    }
}