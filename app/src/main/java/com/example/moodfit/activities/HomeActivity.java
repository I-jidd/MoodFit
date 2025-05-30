package com.example.moodfit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.moodfit.R;
import com.example.moodfit.models.User;
import com.example.moodfit.models.UserProgress;
import com.example.moodfit.models.MotivationalQuote;
import com.example.moodfit.utils.DataManager;

/**
 * HomeActivity - Main dashboard of the MoodFit app
 * Provides access to all major features and displays user progress
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
    private UserProgress userProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize data manager
        initializeDataManager();

        // Initialize UI components
        initializeViews();

        // Load and display user data
        loadUserData();

        // Setup navigation listeners
        setupNavigationListeners();

        // Load daily motivational quote
        loadDailyQuote();
    }

    /**
     * Initialize data management components
     */
    private void initializeDataManager() {
        dataManager = new DataManager(this);
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
     * Load user data and update UI
     */
    private void loadUserData() {
        try {
            // Get current user
            currentUser = dataManager.getCurrentUser();
            userProgress = dataManager.getUserProgress();

            // Update user greeting
            updateUserGreeting();

            // Update stats display
            updateStatsDisplay();

            // Update streak information
            updateStreakDisplay();

        } catch (Exception e) {
            android.util.Log.e(TAG, "Error loading user data", e);
            showDefaultContent();
        }
    }

    /**
     * Update the welcome message and greeting
     */
    private void updateUserGreeting() {
        if (currentUser != null) {
            // Set personalized welcome message
            tvWelcomeMessage.setText(currentUser.getWelcomeMessage());

            // Set motivational greeting based on user's progress
            String greetingMessage = getContextualGreeting();
            tvGreetingMessage.setText(greetingMessage);
        } else {
            // Fallback for new users
            tvWelcomeMessage.setText("Welcome to MoodFit!");
            tvGreetingMessage.setText("Ready to start your fitness journey?");
        }
    }

    /**
     * Get contextual greeting based on user activity
     */
    private String getContextualGreeting() {
        if (currentUser == null) {
            return "Ready to start your fitness journey?";
        }

        // Check if user worked out today
        if (currentUser.hasWorkedOutToday()) {
            return "Great job today! Keep the momentum going!";
        }

        // Check current streak for motivation
        int streak = currentUser.getCurrentStreak();
        if (streak == 0) {
            return "Ready to start a new streak?";
        } else if (streak < 3) {
            return "Let's keep building that streak!";
        } else if (streak < 7) {
            return "You're on fire! 🔥 Keep it up!";
        } else {
            return "Incredible dedication! You're unstoppable!";
        }
    }

    /**
     * Update the stats display
     */
    private void updateStatsDisplay() {
        if (currentUser != null && userProgress != null) {
            // Workouts this week
            int workoutsThisWeek = dataManager.getWorkoutsThisWeek();
            tvWorkoutsThisWeek.setText(String.valueOf(workoutsThisWeek));

            // Total minutes
            int totalMinutes = dataManager.getTotalWorkoutMinutes();
            tvTotalMinutes.setText(String.valueOf(totalMinutes));

            // Best streak
            int bestStreak = currentUser.getBestStreak();
            tvBestStreak.setText(String.valueOf(bestStreak));
        } else {
            // Show zeros for new users
            tvWorkoutsThisWeek.setText("0");
            tvTotalMinutes.setText("0");
            tvBestStreak.setText("0");
        }
    }

    /**
     * Update streak display in header
     */
    private void updateStreakDisplay() {
        if (currentUser != null) {
            int currentStreak = currentUser.getCurrentStreak();
            tvCurrentStreak.setText(String.valueOf(currentStreak));
        } else {
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
            Intent intent = new Intent(this, ExerciseDemoActivity.class);
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

    /**
     * Load and display daily motivational quote
     */
    private void loadDailyQuote() {
        try {
            MotivationalQuote dailyQuote = dataManager.getDailyQuote();
            if (dailyQuote != null) {
                String quoteText = "\"" + dailyQuote.getText() + "\"";
                tvDailyQuote.setText(quoteText);
            } else {
                // Fallback quote
                tvDailyQuote.setText("\"The only bad workout is the one that didn't happen.\"");
            }
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error loading daily quote", e);
            tvDailyQuote.setText("\"Take care of your body. It's the only place you have to live.\"");
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
     * Refresh data when returning to this activity
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Update user streak (in case they completed a workout in another activity)
        dataManager.updateUserStreak();

        // Refresh user data
        loadUserData();

        // Refresh daily quote (in case it's a new day)
        loadDailyQuote();
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
     * Clean up resources
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Any cleanup if needed
    }

    /**
     * Handle activity results from child activities
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Refresh data if a workout was completed
        if (resultCode == RESULT_OK) {
            loadUserData();
        }
    }

    /**
     * Utility method to show a welcome animation for first-time users
     */
    private void showWelcomeAnimation() {
        if (currentUser != null && currentUser.isFirstTimeUser()) {
            // Add a subtle welcome animation
            View rootView = findViewById(android.R.id.content);
            rootView.setAlpha(0f);
            rootView.animate()
                    .alpha(1f)
                    .setDuration(800)
                    .setStartDelay(200)
                    .start();
        }
    }

    /**
     * Method to manually refresh data (can be called from settings or other activities)
     */
    public void refreshData() {
        loadUserData();
        loadDailyQuote();
    }
}