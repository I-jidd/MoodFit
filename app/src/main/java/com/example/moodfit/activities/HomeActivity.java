package com.example.moodfit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.moodfit.R;
import com.example.moodfit.models.User;
import com.example.moodfit.models.MotivationalQuote;
import com.example.moodfit.utils.DataManager;

/**
 * HomeActivity - Main dashboard of the MoodFit app
 * Provides access to all major features and displays user progress
 * UPDATED: Enhanced DataManager integration with proper error handling
 */
public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    // Data Management
    private DataManager dataManager;

    // Header UI Components
    private TextView tvWelcomeMessage;
    private TextView tvGreetingMessage;
    private TextView tvCurrentStreak;

    // Stats UI Components
    private TextView tvWorkoutsThisWeek;
    private TextView tvTotalMinutes;
    private TextView tvBestStreak;

    // Feature Navigation Cards
    private CardView cardMoodWorkout;
    private CardView cardRandomExercise;
    private CardView cardWorkoutTimer;
    private CardView cardExerciseDemos;
    private CardView cardProgressStreak;

    // Motivational Quote
    private TextView tvDailyQuote;

    // User Data
    private User currentUser;
    private DataManager.UserStats userStats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize data manager FIRST
        initializeDataManager();

        // Initialize UI components
        initializeViews();

        // Load and display user data
        loadUserData();

        // Setup navigation listeners
        setupNavigationListeners();

        // Load daily motivational quote
        loadDailyQuote();

        // Show welcome animation for first-time users
        checkAndShowWelcomeAnimation();
    }

    /**
     * Initialize data management components with error handling
     */
    private void initializeDataManager() {
        try {
            dataManager = new DataManager(this);

            // Ensure app is properly initialized
            dataManager.initializeApp();

        } catch (Exception e) {
            android.util.Log.e(TAG, "Error initializing data manager", e);

            // Show error to user and provide fallback
            showErrorMessage("Unable to load app data. Some features may not work correctly.");
        }
    }

    /**
     * Initialize all UI components
     */
    private void initializeViews() {
        // Header components
        tvWelcomeMessage = findViewById(R.id.tv_welcome_message);
        tvGreetingMessage = findViewById(R.id.tv_greeting_message);
        tvCurrentStreak = findViewById(R.id.tv_current_streak);

        // Stats components
        tvWorkoutsThisWeek = findViewById(R.id.tv_workouts_this_week);
        tvTotalMinutes = findViewById(R.id.tv_total_minutes);
        tvBestStreak = findViewById(R.id.tv_best_streak);

        // Feature navigation cards
        cardMoodWorkout = findViewById(R.id.card_mood_workout);
        cardRandomExercise = findViewById(R.id.card_random_exercise);
        cardWorkoutTimer = findViewById(R.id.card_workout_timer);
        cardExerciseDemos = findViewById(R.id.card_exercise_demos);
        cardProgressStreak = findViewById(R.id.card_progress_streak);

        // Motivational quote
        tvDailyQuote = findViewById(R.id.tv_daily_quote);
    }

    /**
     * Load user data and update UI with comprehensive error handling
     */
    private void loadUserData() {
        try {
            // Get current user from DataManager
            currentUser = dataManager.getCurrentUser();

            // Get comprehensive user statistics
            userStats = dataManager.getUserStats();

            // Update all UI components
            updateUserGreeting();
            updateStatsDisplay();
            updateStreakDisplay();

            android.util.Log.d(TAG, "User data loaded successfully");

        } catch (Exception e) {
            android.util.Log.e(TAG, "Error loading user data", e);
            showDefaultContent();
            showErrorMessage("Unable to load your profile data.");
        }
    }

    /**
     * Update the welcome message and greeting using DataManager
     */
    private void updateUserGreeting() {
        try {
            if (currentUser != null) {
                // Use the user's built-in welcome message
                tvWelcomeMessage.setText(currentUser.getWelcomeMessage());

                // Use the user's built-in motivational message
                String greetingMessage = currentUser.getMotivationalMessage();
                tvGreetingMessage.setText(greetingMessage);

            } else {
                // Fallback for edge cases
                tvWelcomeMessage.setText("Welcome to MoodFit!");
                tvGreetingMessage.setText("Ready to start your fitness journey?");
            }
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error updating user greeting", e);
            tvWelcomeMessage.setText("Welcome to MoodFit!");
            tvGreetingMessage.setText("Ready to get moving?");
        }
    }

    /**
     * Update the stats display using UserStats from DataManager
     */
    private void updateStatsDisplay() {
        try {
            if (userStats != null) {
                // Use comprehensive stats from DataManager
                tvWorkoutsThisWeek.setText(String.valueOf(userStats.workoutsThisWeek));
                tvTotalMinutes.setText(String.valueOf(userStats.totalMinutes));
                tvBestStreak.setText(String.valueOf(userStats.bestStreak));

            } else {
                // Fallback to individual DataManager calls
                tvWorkoutsThisWeek.setText(String.valueOf(dataManager.getWorkoutsThisWeek()));
                tvTotalMinutes.setText(String.valueOf(dataManager.getTotalWorkoutMinutes()));

                if (currentUser != null) {
                    tvBestStreak.setText(String.valueOf(currentUser.getBestStreak()));
                } else {
                    tvBestStreak.setText("0");
                }
            }
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error updating stats display", e);
            // Show zeros for error cases
            tvWorkoutsThisWeek.setText("0");
            tvTotalMinutes.setText("0");
            tvBestStreak.setText("0");
        }
    }

    /**
     * Update streak display in header
     */
    private void updateStreakDisplay() {
        try {
            if (userStats != null) {
                tvCurrentStreak.setText(String.valueOf(userStats.currentStreak));
            } else if (currentUser != null) {
                tvCurrentStreak.setText(String.valueOf(currentUser.getCurrentStreak()));
            } else {
                tvCurrentStreak.setText("0");
            }
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error updating streak display", e);
            tvCurrentStreak.setText("0");
        }
    }

    /**
     * Setup click listeners for feature navigation
     */
    private void setupNavigationListeners() {
        // Mood-based workout
        cardMoodWorkout.setOnClickListener(v -> {
            Intent intent = new Intent(this, MoodWorkoutActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // Random exercise
        cardRandomExercise.setOnClickListener(v -> {
            Intent intent = new Intent(this, RandomExerciseActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // Workout timer
        cardWorkoutTimer.setOnClickListener(v -> {
            Intent intent = new Intent(this, WorkoutTimerActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // Exercise demos
        cardExerciseDemos.setOnClickListener(v -> {
            Intent intent = new Intent(this, TutorialsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // Progress and streak tracking
        cardProgressStreak.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProgressStreakActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // Add click effects for better UX
        addClickEffects();
    }

    /**
     * Add visual click effects to cards
     */
    private void addClickEffects() {
        CardView[] cards = {
                cardMoodWorkout, cardRandomExercise, cardWorkoutTimer,
                cardExerciseDemos, cardProgressStreak
        };

        for (CardView card : cards) {
            if (card != null) {
                card.setOnTouchListener((v, event) -> {
                    switch (event.getAction()) {
                        case android.view.MotionEvent.ACTION_DOWN:
                            v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start();
                            break;
                        case android.view.MotionEvent.ACTION_UP:
                        case android.view.MotionEvent.ACTION_CANCEL:
                            v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start();
                            break;
                    }
                    return false; // Let the click listener handle the actual click
                });
            }
        }
    }

    /**
     * Load and display daily motivational quote using DataManager
     */
    private void loadDailyQuote() {
        try {
            MotivationalQuote dailyQuote = dataManager.getDailyQuote();

            if (dailyQuote != null && dailyQuote.getText() != null) {
                String quoteText = "\"" + dailyQuote.getText() + "\"";
                tvDailyQuote.setText(quoteText);
            } else {
                // Fallback quote
                tvDailyQuote.setText("\"Take care of your body. It's the only place you have to live.\"");
            }

        } catch (Exception e) {
            android.util.Log.e(TAG, "Error loading daily quote", e);
            tvDailyQuote.setText("\"Every workout brings you one step closer to your goals.\"");
        }
    }

    /**
     * Show default content for new users or error cases
     */
    private void showDefaultContent() {
        tvWelcomeMessage.setText("Welcome to MoodFit!");
        tvGreetingMessage.setText("Ready to start your fitness journey?");
        tvCurrentStreak.setText("0");
        tvWorkoutsThisWeek.setText("0");
        tvTotalMinutes.setText("0");
        tvBestStreak.setText("0");
    }

    /**
     * Check and show welcome animation for first-time users
     */
    private void checkAndShowWelcomeAnimation() {
        try {
            if (currentUser != null && currentUser.isFirstTimeUser()) {
                showWelcomeAnimation();
            }
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error checking first-time user status", e);
        }
    }

    /**
     * Show welcome animation for first-time users
     */
    private void showWelcomeAnimation() {
        View rootView = findViewById(android.R.id.content);
        if (rootView != null) {
            rootView.setAlpha(0f);
            rootView.animate()
                    .alpha(1f)
                    .setDuration(800)
                    .setStartDelay(200)
                    .start();
        }
    }

    /**
     * Show error message to user
     */
    private void showErrorMessage(String message) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show();
    }

    /**
     * Refresh data when returning to this activity
     */
    @Override
    protected void onResume() {
        super.onResume();

        try {
            // Update user streak using DataManager (handles all business logic)
            dataManager.updateUserStreak();

            // Refresh user data
            loadUserData();

            // Refresh daily quote
            loadDailyQuote();

        } catch (Exception e) {
            android.util.Log.e(TAG, "Error during onResume", e);
        }
    }

    /**
     * Handle back button press - minimize app instead of closing
     */
    @Override
    public void onBackPressed() {
        // Move app to background instead of closing
        moveTaskToBack(true);
    }

    /**
     * Handle activity results from child activities
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Refresh data if a workout was completed
        if (resultCode == RESULT_OK) {
            try {
                // Force refresh of cached data
                dataManager.refreshData();
                loadUserData();
            } catch (Exception e) {
                android.util.Log.e(TAG, "Error refreshing data after activity result", e);
            }
        }
    }

    /**
     * Method to manually refresh data (can be called from settings or other activities)
     */
    public void refreshData() {
        try {
            dataManager.refreshData();
            loadUserData();
            loadDailyQuote();
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error during manual data refresh", e);
            showErrorMessage("Unable to refresh data. Please try again.");
        }
    }

    /**
     * Clean up resources
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up DataManager caches if needed
        if (dataManager != null) {
            dataManager.clearCaches();
        }
    }
}