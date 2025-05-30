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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
 * ExerciseDemoActivity - Tutorial and preview screen for individual exercises
 * Shows exercise instructions, tips, and preparation before starting the timer
 * UPDATED: Now includes GIF loading functionality
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
    private TextView tvExerciseCategoryBadge; // NEW: For the badge in GIF area
    private ImageView ivExerciseIcon;
    private ImageView ivExerciseGifDemo; // NEW: For GIF display
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
    private String exerciseGifName; // NEW: GIF filename

    private int exercisesCompleted = 0;
    private int totalCaloriesBurned = 0;
    private long workoutStartTime;
    private boolean hasSkippedExercises = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_demo);

        // Record workout start time
        workoutStartTime = System.currentTimeMillis();

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
     * Extract data from intent - UPDATED to include GIF name
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

            // NEW: Get GIF name
            exerciseGifName = intent.getStringExtra("exercise_gif");
        }
    }

    /**
     * Initialize all UI components - UPDATED to include category badge
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

        // NEW: Exercise GIF view
        ivExerciseGifDemo = findViewById(R.id.iv_exercise_gif_demo);

        // NEW: Category badge in GIF area
        tvExerciseCategoryBadge = findViewById(R.id.tv_exercise_category_badge);

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
     * Display exercise information in the UI - FIXED: Now includes all necessary calls
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

        // NEW: Set dynamic category badge
        if (tvExerciseCategoryBadge != null) {
            String categoryText = exerciseCategory != null ? exerciseCategory : "Fitness";
            tvExerciseCategoryBadge.setText(categoryText);

            // Set category-specific background color
            setCategoryBadgeColor(categoryText);
        }

        // FIXED: These calls were missing!
        // Set exercise icon based on category
        setExerciseIcon();

        // Load exercise GIF
        loadExerciseGif();

        // Add entrance animation
        animateExerciseInfo();
    }

    /**
     * NEW: Set category badge background color based on exercise category
     */
    private void setCategoryBadgeColor(String category) {
        if (tvExerciseCategoryBadge == null || category == null) return;

        int backgroundColor;
        switch (category.toLowerCase()) {
            case "cardio":
                backgroundColor = getResources().getColor(R.color.success);
                break;
            case "strength":
                backgroundColor = getResources().getColor(R.color.primary_color);
                break;
            case "flexibility":
                backgroundColor = getResources().getColor(R.color.info);
                break;
            case "yoga":
                backgroundColor = getResources().getColor(R.color.mood_happy);
                break;
            case "hiit":
                backgroundColor = getResources().getColor(R.color.error);
                break;
            case "breathing":
                backgroundColor = getResources().getColor(R.color.accent_color);
                break;
            default:
                backgroundColor = getResources().getColor(R.color.primary_color);
                break;
        }
        // Create rounded background drawable programmatically
        android.graphics.drawable.GradientDrawable background = new android.graphics.drawable.GradientDrawable();
        background.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
        background.setColor(backgroundColor);
        background.setCornerRadius(24f); // 12dp converted to pixels
        background.setStroke(2, getResources().getColor(R.color.white)); // White border

        tvExerciseCategoryBadge.setBackground(background);
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
     * Load exercise GIF using Glide for animation support
     */
    private void loadExerciseGif() {
        if (ivExerciseGifDemo == null) {
            android.util.Log.w(TAG, "GIF ImageView not found");
            return;
        }

        // Generate GIF name if not provided
        if (exerciseGifName == null && exerciseName != null) {
            exerciseGifName = generateGifName(exerciseName);
        }

        // DEBUG: Log what we're looking for
        android.util.Log.d("GIF_DEBUG", "Exercise: " + exerciseName);
        android.util.Log.d("GIF_DEBUG", "Looking for GIF: " + exerciseGifName);

        if (exerciseGifName != null && !exerciseGifName.isEmpty()) {
            // Try to load GIF from drawable resources
            int gifResourceId = getResources().getIdentifier(exerciseGifName, "drawable", getPackageName());

            // DEBUG: Log if resource was found
            android.util.Log.d("GIF_DEBUG", "Resource ID: " + gifResourceId + " (0 means not found)");

            if (gifResourceId != 0) {
                // GIF exists, load it with Glide for animation
                Glide.with(this)
                        .asGif() // Explicitly load as GIF
                        .load(gifResourceId)
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.default_exercise_demo)
                                .error(R.drawable.default_exercise_demo))
                        .into(ivExerciseGifDemo);

                ivExerciseGifDemo.setVisibility(View.VISIBLE);

                // Hide the fallback icon since we have an animated GIF
                ivExerciseIcon.setVisibility(View.GONE);

                android.util.Log.d("GIF_DEBUG", "✅ Animated GIF loaded successfully with Glide!");
            } else {
                // GIF doesn't exist yet, show placeholder and icon
                android.util.Log.d("GIF_DEBUG", "❌ GIF not found, showing placeholder");
                showGifPlaceholder();
            }
        } else {
            // No GIF name, show placeholder
            android.util.Log.d("GIF_DEBUG", "❌ No GIF name provided");
            showGifPlaceholder();
        }
    }

    /**
     * NEW: Generate GIF filename from exercise name
     */
    private String generateGifName(String exerciseName) {
        if (exerciseName == null) return null;

        // Convert exercise name to lowercase and replace spaces with underscores
        return "gif_" + exerciseName.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "") // Remove special characters
                .replaceAll("\\s+", "_") // Replace spaces with underscores
                .replaceAll("_+", "_"); // Replace multiple underscores with single
    }

    /**
     * Show placeholder when GIF is not available - Updated to use Glide
     */
    private void showGifPlaceholder() {
        // Load placeholder with Glide for consistency
        Glide.with(this)
                .load(R.drawable.default_exercise_demo)
                .into(ivExerciseGifDemo);

        ivExerciseGifDemo.setVisibility(View.VISIBLE);
        ivExerciseGifDemo.setAlpha(0.7f);

        // Keep the icon visible as well
        ivExerciseIcon.setVisibility(View.VISIBLE);
    }

    /**
     * Control GIF animation (useful for performance)
     */
    private void pauseGifAnimation() {
        if (ivExerciseGifDemo != null && ivExerciseGifDemo.getDrawable() instanceof GifDrawable) {
            ((GifDrawable) ivExerciseGifDemo.getDrawable()).stop();
        }
    }

    private void resumeGifAnimation() {
        if (ivExerciseGifDemo != null && ivExerciseGifDemo.getDrawable() instanceof GifDrawable) {
            ((GifDrawable) ivExerciseGifDemo.getDrawable()).start();
        }
    }

    // Optional: Add lifecycle methods to manage GIF performance
    @Override
    protected void onPause() {
        super.onPause();
        pauseGifAnimation(); // Save battery when activity is not visible
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeGifAnimation(); // Resume animation when activity becomes visible
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

        // NEW: Animate the GIF view too
        if (ivExerciseGifDemo.getVisibility() == View.VISIBLE) {
            ivExerciseGifDemo.setAlpha(0f);
            ivExerciseGifDemo.setScaleX(0.8f);
            ivExerciseGifDemo.setScaleY(0.8f);
            ivExerciseGifDemo.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(600)
                    .setStartDelay(200)
                    .start();
        }

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
     * Skip to next exercise - UPDATED to track skipped exercises
     */
    private void skipToNextExercise() {
        // Show confirmation dialog
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Skip Exercise?")
                .setMessage("Are you sure you want to skip this exercise and move to the next one?")
                .setPositiveButton("Continue Exercise", (dialog, which) -> dialog.dismiss())
                .setNegativeButton("Skip", (dialog, which) -> {
                    // Mark that exercises were skipped
                    hasSkippedExercises = true;
                    proceedToNextExercise();
                })
                .show();
    }

    /**
     * Proceed to next exercise in the routine - UPDATED to track skipped exercises
     */
    private void proceedToNextExercise() {
        int nextExerciseIndex = currentExerciseIndex + 1;

        if (nextExerciseIndex >= totalExercises) {
            // All exercises completed - show completion screen with real data
            showWorkoutCompletion();
        } else {
            // Move to next exercise demo
            // Return to MoodWorkoutActivity to handle next exercise
            Intent resultIntent = new Intent();
            resultIntent.putExtra("action", "next_exercise");
            resultIntent.putExtra("current_exercise_index", nextExerciseIndex);

            // Pass current completion data
            resultIntent.putExtra("exercises_completed", exercisesCompleted);
            resultIntent.putExtra("calories_burned", totalCaloriesBurned);
            resultIntent.putExtra("exercises_skipped", hasSkippedExercises);

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
     * Handle successful exercise completion - UPDATED to track completion data
     */
    private void handleExerciseCompletion(Intent data) {
        // Increment completed exercises
        exercisesCompleted++;

        // Add calories for this exercise
        totalCaloriesBurned += exerciseCalories;

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
     * Show workout completion screen - UPDATED with real workout data
     */
    private void showWorkoutCompletion() {
        // Calculate actual workout duration
        long workoutEndTime = System.currentTimeMillis();
        int actualDurationMinutes = (int) ((workoutEndTime - workoutStartTime) / (1000 * 60));

        // Ensure we have at least 1 minute
        if (actualDurationMinutes < 1) {
            actualDurationMinutes = 1;
        }

        Intent completionIntent = new Intent(this, WorkoutCompleteActivity.class);

        // Basic workout info
        completionIntent.putExtra("session_id", sessionId);
        completionIntent.putExtra("mood_type", workoutMood != null ? workoutMood.name() : "NEUTRAL");
        completionIntent.putExtra("total_exercises", totalExercises);
        completionIntent.putExtra("estimated_duration", estimatedDuration);

        // NEW: Real completion data
        completionIntent.putExtra("actual_duration", actualDurationMinutes);
        completionIntent.putExtra("exercises_completed", exercisesCompleted);
        completionIntent.putExtra("calories_burned", totalCaloriesBurned);
        completionIntent.putExtra("exercises_skipped", hasSkippedExercises);

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