package com.example.moodfit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.moodfit.R;
import com.example.moodfit.models.Exercise;
import com.example.moodfit.models.User;
import com.example.moodfit.models.WorkoutSession;
import com.example.moodfit.models.enums.MoodType;
import com.example.moodfit.utils.DataManager;

import java.util.List;

/**
 * ExerciseDemoActivity - Tutorial and preview screen for individual exercises
 * Shows exercise instructions, tips, and preparation before starting the timer
 */
public class ExerciseDemoActivity extends AppCompatActivity {

    private static final String TAG = "ExerciseDemoActivity";

    // Data Management
    private DataManager dataManager;

    // UI Components
    private TextView tvExerciseName;
    private TextView tvExerciseDescription;
    private TextView tvExerciseInstructions;
    private TextView tvExerciseDuration;
    private TextView tvExerciseCalories;
    private TextView tvExerciseCategory;
    private TextView tvProgressIndicator;
    private ImageView ivExerciseIcon;
    private Button btnStartExercise;
    private Button btnSkipExercise;
    private CardView exerciseInfoCard;

    // Workout Session Data
    private String sessionId;
    private MoodType workoutMood;
    private int currentExerciseIndex;
    private int totalExercises;
    private int estimatedDuration;

    // Current Exercise Data
    private String exerciseName;
    private String exerciseDescription;
    private String exerciseInstructions;
    private int exerciseDuration;
    private int exerciseCalories;
    private String exerciseCategory;
    private String exerciseDifficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_demo);

        // Initialize data management
        initializeDataManager();

        // Extract intent data
        extractIntentData();

        // Initialize UI components
        initializeViews();

        // Setup event listeners
        setupEventListeners();

        // Display exercise information
        displayExerciseInfo();

        // Setup progress indicator
        updateProgressIndicator();
    }

    /**
     * Initialize data management components
     */
    private void initializeDataManager() {
        dataManager = new DataManager(this);
    }

    /**
     * Extract data from intent
     */
    private void extractIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            // Session data
            sessionId = intent.getStringExtra("session_id");
            String moodName = intent.getStringExtra("mood_type");
            if (moodName != null) {
                try {
                    workoutMood = MoodType.valueOf(moodName);
                } catch (IllegalArgumentException e) {
                    workoutMood = MoodType.NEUTRAL;
                }
            }

            // Progress data
            currentExerciseIndex = intent.getIntExtra("current_exercise_index", 0);
            totalExercises = intent.getIntExtra("total_exercises", 1);
            estimatedDuration = intent.getIntExtra("estimated_duration", 30);

            // Exercise data
            exerciseName = intent.getStringExtra("exercise_name");
            exerciseDescription = intent.getStringExtra("exercise_description");
            exerciseInstructions = intent.getStringExtra("exercise_instructions");
            exerciseDuration = intent.getIntExtra("exercise_duration", 10);
            exerciseCalories = intent.getIntExtra("exercise_calories", 50);
            exerciseCategory = intent.getStringExtra("exercise_category");
            exerciseDifficulty = intent.getStringExtra("exercise_difficulty");
        }
    }

    /**
     * Initialize all UI components
     */
    private void initializeViews() {
        // Exercise information
        tvExerciseName = findViewById(R.id.tv_exercise_name);
        tvExerciseDescription = findViewById(R.id.tv_exercise_description);
        tvExerciseInstructions = findViewById(R.id.tv_exercise_instructions);
        tvExerciseDuration = findViewById(R.id.tv_exercise_duration);
        tvExerciseCalories = findViewById(R.id.tv_exercise_calories);
        tvExerciseCategory = findViewById(R.id.tv_exercise_category);
        tvProgressIndicator = findViewById(R.id.tv_progress_indicator);

        // Exercise icon/image
        ivExerciseIcon = findViewById(R.id.iv_exercise_icon);

        // Buttons
        btnStartExercise = findViewById(R.id.btn_start_exercise);
        btnSkipExercise = findViewById(R.id.btn_skip_exercise);

        // Cards
        exerciseInfoCard = findViewById(R.id.exercise_info_card);
    }

    /**
     * Setup event listeners
     */
    private void setupEventListeners() {
        btnStartExercise.setOnClickListener(v -> startExerciseTimer());
        btnSkipExercise.setOnClickListener(v -> skipToNextExercise());
    }

    /**
     * Display exercise information in the UI
     */
    private void displayExerciseInfo() {
        // Set exercise details
        tvExerciseName.setText(exerciseName != null ? exerciseName : "Exercise");
        tvExerciseDescription.setText(exerciseDescription != null ? exerciseDescription : "Get ready for your workout!");
        tvExerciseInstructions.setText(exerciseInstructions != null ? exerciseInstructions : "Follow the timer and do your best!");

        // Set stats
        tvExerciseDuration.setText(exerciseDuration + " min");
        tvExerciseCalories.setText("~" + exerciseCalories + " cal");
        tvExerciseCategory.setText(exerciseCategory != null ? exerciseCategory : "Fitness");

        // Set exercise icon based on category
        setExerciseIcon();

        // Add entrance animation
        animateExerciseInfo();
    }

    /**
     * Set appropriate icon based on exercise category
     */
    private void setExerciseIcon() {
        if (exerciseCategory == null) {
            ivExerciseIcon.setImageResource(R.drawable.ic_fitness_splash);
            return;
        }

        switch (exerciseCategory.toLowerCase()) {
            case "cardio":
                ivExerciseIcon.setImageResource(R.drawable.ic_cardio);
                break;
            case "strength":
                ivExerciseIcon.setImageResource(R.drawable.ic_strength);
                break;
            case "flexibility":
                ivExerciseIcon.setImageResource(R.drawable.ic_flexibility);
                break;
            case "yoga":
                ivExerciseIcon.setImageResource(R.drawable.ic_yoga);
                break;
            case "hiit":
                ivExerciseIcon.setImageResource(R.drawable.ic_hiit);
                break;
            case "breathing":
                ivExerciseIcon.setImageResource(R.drawable.ic_breathing);
                break;
            default:
                ivExerciseIcon.setImageResource(R.drawable.ic_fitness_splash);
                break;
        }
    }

    /**
     * Animate exercise information appearance
     */
    private void animateExerciseInfo() {
        // Animate the exercise info card
        exerciseInfoCard.setAlpha(0f);
        exerciseInfoCard.setTranslationY(50f);
        exerciseInfoCard.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(500)
                .start();

        // Animate the icon
        ivExerciseIcon.setAlpha(0f);
        ivExerciseIcon.setScaleX(0.8f);
        ivExerciseIcon.setScaleY(0.8f);
        ivExerciseIcon.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(600)
                .setStartDelay(200)
                .start();

        // Animate buttons
        btnStartExercise.setAlpha(0f);
        btnStartExercise.setTranslationY(30f);
        btnStartExercise.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(400)
                .setStartDelay(400)
                .start();

        if (btnSkipExercise.getVisibility() == View.VISIBLE) {
            btnSkipExercise.setAlpha(0f);
            btnSkipExercise.setTranslationY(30f);
            btnSkipExercise.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(400)
                    .setStartDelay(500)
                    .start();
        }
    }

    /**
     * Update progress indicator
     */
    private void updateProgressIndicator() {
        String progressText = "Exercise " + (currentExerciseIndex + 1) + " of " + totalExercises;
        tvProgressIndicator.setText(progressText);

        // Show/hide skip button (don't allow skipping last exercise)
        if (currentExerciseIndex < totalExercises - 1) {
            btnSkipExercise.setVisibility(View.VISIBLE);
        } else {
            btnSkipExercise.setVisibility(View.GONE);
        }
    }

    /**
     * Start the exercise timer
     */
    private void startExerciseTimer() {
        Intent timerIntent = new Intent(this, WorkoutTimerActivity.class);

        // Pass session data
        timerIntent.putExtra("session_id", sessionId);
        timerIntent.putExtra("mood_type", workoutMood != null ? workoutMood.name() : "NEUTRAL");
        timerIntent.putExtra("current_exercise_index", currentExerciseIndex);
        timerIntent.putExtra("total_exercises", totalExercises);

        // Pass exercise-specific data
        timerIntent.putExtra("exercise_name", exerciseName);
        timerIntent.putExtra("exercise_duration", exerciseDuration);
        timerIntent.putExtra("single_exercise_mode", true);

        startActivityForResult(timerIntent, 2001);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * Skip to next exercise
     */
    private void skipToNextExercise() {
        // Show confirmation dialog
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Skip Exercise?")
                .setMessage("Are you sure you want to skip this exercise and move to the next one?")
                .setPositiveButton("Continue Exercise", (dialog, which) -> dialog.dismiss())
                .setNegativeButton("Skip", (dialog, which) -> {
                    proceedToNextExercise();
                })
                .show();
    }

    /**
     * Proceed to next exercise in the routine
     */
    private void proceedToNextExercise() {
        int nextExerciseIndex = currentExerciseIndex + 1;

        if (nextExerciseIndex >= totalExercises) {
            // All exercises completed - show completion screen
            showWorkoutCompletion();
        } else {
            // Move to next exercise demo
            // Return to MoodWorkoutActivity to handle next exercise
            Intent resultIntent = new Intent();
            resultIntent.putExtra("action", "next_exercise");
            resultIntent.putExtra("current_exercise_index", nextExerciseIndex);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }

    /**
     * Handle activity results from timer
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2001) { // Timer result
            if (resultCode == RESULT_OK) {
                // Exercise completed successfully
                handleExerciseCompletion(data);
            } else if (resultCode == RESULT_CANCELED) {
                // Exercise was cancelled - STAY on demo screen, don't finish
                handleExerciseCancellation(data);
            }
        }
    }

    /**
     * Handle successful exercise completion
     */
    private void handleExerciseCompletion(Intent data) {
        // Show completion feedback
        showExerciseCompletionFeedback();

        // Small delay before proceeding to next exercise
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            proceedToNextExercise();
        }, 1500);
    }

    /**
     * Handle exercise cancellation (back button from timer)
     */
    private void handleExerciseCancellation(Intent data) {
        // Show user-friendly message that they're back to exercise demo
        showExerciseCancellationFeedback();

        // Re-enable start button in case it was disabled
        btnStartExercise.setEnabled(true);
        btnStartExercise.setAlpha(1.0f);

        // Optional: Add visual feedback that exercise was not completed
        addCancellationVisualFeedback();
    }

    /**
     * Show feedback when exercise is completed
     */
    private void showExerciseCompletionFeedback() {
        // Create a temporary success overlay
        android.widget.TextView successMessage = new android.widget.TextView(this);
        successMessage.setText("✅ Exercise Completed!");
        successMessage.setTextSize(18);
        successMessage.setTextColor(getResources().getColor(R.color.success));
        successMessage.setGravity(android.view.Gravity.CENTER);
        successMessage.setBackgroundColor(getResources().getColor(R.color.background_primary));
        successMessage.setPadding(32, 16, 32, 16);

        // Add to layout temporarily
        android.view.ViewGroup rootLayout = findViewById(android.R.id.content);
        rootLayout.addView(successMessage);

        // Animate and remove after delay
        successMessage.setAlpha(0f);
        successMessage.animate()
                .alpha(1f)
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(300)
                .withEndAction(() -> {
                    successMessage.animate()
                            .alpha(0f)
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(300)
                            .withEndAction(() -> rootLayout.removeView(successMessage))
                            .start();
                })
                .start();
    }

    /**
     * Show feedback when exercise is cancelled
     */
    private void showExerciseCancellationFeedback() {
        // Show a gentle message that they can restart
        android.widget.Toast.makeText(this,
                "Exercise paused. You can restart when ready!",
                android.widget.Toast.LENGTH_SHORT).show();

        // Optional: Animate the start button to draw attention
        btnStartExercise.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(200)
                .withEndAction(() -> {
                    btnStartExercise.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(200)
                            .start();
                })
                .start();
    }

    /**
     * Add visual feedback for cancellation
     */
    private void addCancellationVisualFeedback() {
        // Add a subtle visual indicator that exercise was not completed
        exerciseInfoCard.animate()
                .alpha(0.8f)
                .setDuration(200)
                .withEndAction(() -> {
                    exerciseInfoCard.animate()
                            .alpha(1.0f)
                            .setDuration(200)
                            .start();
                })
                .start();
    }

    /**
     * Show workout completion screen
     */
    private void showWorkoutCompletion() {
        Intent completionIntent = new Intent(this, WorkoutCompleteActivity.class);
        completionIntent.putExtra("session_id", sessionId);
        completionIntent.putExtra("mood_type", workoutMood != null ? workoutMood.name() : "NEUTRAL");
        completionIntent.putExtra("total_exercises", totalExercises);
        completionIntent.putExtra("estimated_duration", estimatedDuration);

        startActivity(completionIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        // Finish this activity and all previous ones to return to home
        finishAffinity();
    }

    /**
     * Update the back button behavior to show confirmation
     */
    @Override
    public void onBackPressed() {
        // Show confirmation dialog with better context
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Leave Exercise?")
                .setMessage("Are you sure you want to go back? You can restart this exercise anytime.")
                .setPositiveButton("Stay Here", (dialog, which) -> dialog.dismiss())
                .setNegativeButton("Go Back", (dialog, which) -> {
                    // Return to MoodWorkoutActivity instead of finishing completely
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("action", "exercise_cancelled");
                    resultIntent.putExtra("current_exercise_index", currentExerciseIndex);
                    setResult(RESULT_CANCELED, resultIntent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                })
                .show();
    }
}