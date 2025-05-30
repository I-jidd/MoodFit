package com.example.moodfit.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.moodfit.R;
import com.example.moodfit.models.Exercise;
import com.example.moodfit.models.User;
import com.example.moodfit.models.WorkoutSession;
import com.example.moodfit.models.enums.DifficultyLevel;
import com.example.moodfit.models.enums.MoodType;
import com.example.moodfit.models.enums.WorkoutCategory;
import com.example.moodfit.utils.DataManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * MoodWorkoutActivity - Personalized workout generator based on user's current mood
 * Provides mood-specific exercise recommendations and creates workout sessions
 * UPDATED: Now includes GIF name generation and passing
 */
public class MoodWorkoutActivity extends AppCompatActivity {

    private static final String TAG = "MoodWorkoutActivity";

    // Data Management
    private DataManager dataManager;
    private User currentUser;

    // UI Components - Mood Selection
    private ImageButton btnMoodHappy;
    private ImageButton btnMoodNeutral;
    private ImageButton btnMoodFrustrated;
    private ImageButton btnMoodStressed;

    // UI Components - Mood Labels
    private TextView tvMoodHappy;
    private TextView tvMoodNeutral;
    private TextView tvMoodFrustrated;
    private TextView tvMoodStressed;

    // UI Components - Workout Display
    private TextView tvWorkoutSuggestion;
    private Button btnStartWorkout;

    // Current State
    private MoodType selectedMood = null;
    private List<Exercise> recommendedExercises = new ArrayList<>();
    private WorkoutSession currentSession = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_workout_generator);

        // Initialize data management
        initializeDataManager();

        // Initialize UI components
        initializeViews();

        // Setup mood selection listeners
        setupMoodSelectionListeners();

        // Setup workout button listener
        setupWorkoutButtonListener();

        // Initialize with default state
        updateWorkoutSuggestion();
    }

    /**
     * Initialize data management components
     */
    private void initializeDataManager() {
        dataManager = new DataManager(this);
        currentUser = dataManager.getCurrentUser();
    }

    /**
     * Initialize all UI components
     */
    private void initializeViews() {
        // Mood selection buttons
        btnMoodHappy = findViewById(R.id.btn_mood_happy);
        btnMoodNeutral = findViewById(R.id.btn_mood_neutral);
        btnMoodFrustrated = findViewById(R.id.btn_mood_frustrated);
        btnMoodStressed = findViewById(R.id.btn_mood_stressed);

        // Mood labels (we'll need to add IDs to the XML or find them programmatically)
        findMoodLabels();

        // Workout suggestion display
        tvWorkoutSuggestion = findViewById(R.id.tv_workout_suggestion);
        btnStartWorkout = findViewById(R.id.btn_start_workout);

        // Initially disable start button
        btnStartWorkout.setEnabled(false);
        btnStartWorkout.setAlpha(0.5f);
    }

    /**
     * Find mood label TextViews programmatically since they're nested
     */
    private void findMoodLabels() {
        // Find the parent LinearLayout containing all mood buttons
        android.view.ViewGroup moodContainer = (android.view.ViewGroup) btnMoodHappy.getParent().getParent();

        // Get each mood container and find the TextView
        android.view.ViewGroup happyContainer = (android.view.ViewGroup) btnMoodHappy.getParent();
        android.view.ViewGroup neutralContainer = (android.view.ViewGroup) btnMoodNeutral.getParent();
        android.view.ViewGroup frustratedContainer = (android.view.ViewGroup) btnMoodFrustrated.getParent();
        android.view.ViewGroup stressedContainer = (android.view.ViewGroup) btnMoodStressed.getParent();

        // Find TextViews in each container
        tvMoodHappy = findTextViewInContainer(happyContainer);
        tvMoodNeutral = findTextViewInContainer(neutralContainer);
        tvMoodFrustrated = findTextViewInContainer(frustratedContainer);
        tvMoodStressed = findTextViewInContainer(stressedContainer);
    }

    /**
     * Helper method to find TextView in a container
     */
    private TextView findTextViewInContainer(android.view.ViewGroup container) {
        for (int i = 0; i < container.getChildCount(); i++) {
            android.view.View child = container.getChildAt(i);
            if (child instanceof TextView) {
                return (TextView) child;
            }
        }
        return null;
    }

    /**
     * Setup mood selection button listeners
     */
    private void setupMoodSelectionListeners() {
        btnMoodHappy.setOnClickListener(v -> selectMood(MoodType.HAPPY));
        btnMoodNeutral.setOnClickListener(v -> selectMood(MoodType.NEUTRAL));
        btnMoodFrustrated.setOnClickListener(v -> selectMood(MoodType.FRUSTRATED));
        btnMoodStressed.setOnClickListener(v -> selectMood(MoodType.STRESSED));
    }

    /**
     * Setup start workout button listener
     */
    private void setupWorkoutButtonListener() {
        btnStartWorkout.setOnClickListener(v -> startWorkout());
    }

    /**
     * Handle mood selection
     */
    private void selectMood(MoodType mood) {
        // Update selected mood
        selectedMood = mood;

        // Update visual feedback with enhanced highlighting
        updateMoodButtonStates();

        // Generate workout recommendations
        generateWorkoutRecommendations();

        // Update workout suggestion display
        updateWorkoutSuggestion();

        // Enable start workout button with animation
        enableStartWorkoutButton();

        // Add haptic feedback
        performHapticFeedback();

        // Add a small bounce effect to the entire mood selection area
        addSelectionBounceEffect();
    }

    /**
     * Add a subtle bounce effect when mood is selected
     */
    private void addSelectionBounceEffect() {
        // Find the mood selection container
        android.view.View moodContainer = (View) btnMoodHappy.getParent().getParent();

        // Add a subtle bounce animation
        moodContainer.animate()
                .scaleX(1.02f)
                .scaleY(1.02f)
                .setDuration(150)
                .withEndAction(() -> {
                    moodContainer.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(150)
                            .start();
                })
                .start();
    }

    /**
     * Update visual state of mood buttons
     */
    private void updateMoodButtonStates() {
        // Reset all buttons to default state
        resetMoodButtonStates();

        // Highlight selected button based on mood
        if (selectedMood != null) {
            ImageButton selectedButton = getMoodButton(selectedMood);
            if (selectedButton != null) {
                highlightMoodButton(selectedButton, selectedMood);
            }
        }
    }

    /**
     * Reset all mood buttons to default state
     */
    private void resetMoodButtonStates() {
        ImageButton[] buttons = {btnMoodHappy, btnMoodNeutral, btnMoodFrustrated, btnMoodStressed};
        TextView[] labels = {tvMoodHappy, tvMoodNeutral, tvMoodFrustrated, tvMoodStressed};

        for (ImageButton button : buttons) {
            if (button != null) {
                button.setBackgroundTintList(getResources().getColorStateList(R.color.card_background));
                button.setScaleX(1.0f);
                button.setScaleY(1.0f);
                button.setAlpha(0.7f);
                button.setElevation(2f);
            }
        }

        // Reset label styles
        for (TextView label : labels) {
            if (label != null) {
                label.setTextColor(getResources().getColor(R.color.text_secondary));
                label.setAlpha(0.7f);
                label.setScaleX(1.0f);
                label.setScaleY(1.0f);
            }
        }
    }

    /**
     * Get mood button for specific mood type
     */
    private ImageButton getMoodButton(MoodType mood) {
        switch (mood) {
            case HAPPY: return btnMoodHappy;
            case NEUTRAL: return btnMoodNeutral;
            case FRUSTRATED: return btnMoodFrustrated;
            case STRESSED: return btnMoodStressed;
            default: return null;
        }
    }

    /**
     * Get mood label for specific mood type
     */
    private TextView getMoodLabel(MoodType mood) {
        switch (mood) {
            case HAPPY: return tvMoodHappy;
            case NEUTRAL: return tvMoodNeutral;
            case FRUSTRATED: return tvMoodFrustrated;
            case STRESSED: return tvMoodStressed;
            default: return null;
        }
    }

    /**
     * Highlight selected mood button and label
     */
    private void highlightMoodButton(ImageButton button, MoodType mood) {
        if (button == null) return;

        // Get mood-specific color
        int color = Color.parseColor(mood.getColorHex());

        // Create a subtle colored background for the button
        android.graphics.drawable.GradientDrawable background = new android.graphics.drawable.GradientDrawable();
        background.setShape(android.graphics.drawable.GradientDrawable.OVAL);
        background.setColor(color);
        background.setAlpha(30); // Very subtle background
        background.setStroke(4, color); // Colored border

        button.setBackground(background);

        // Animate button selection
        button.animate()
                .scaleX(1.15f)
                .scaleY(1.15f)
                .alpha(1.0f)
                .setDuration(200)
                .start();

        // Add subtle elevation
        button.setElevation(8f);

        // Highlight the corresponding label
        TextView label = getMoodLabel(mood);
        if (label != null) {
            highlightMoodLabel(label, mood);
        }
    }

    /**
     * Highlight selected mood label
     */
    private void highlightMoodLabel(TextView label, MoodType mood) {
        if (label == null) return;

        // Get mood-specific color
        int color = Color.parseColor(mood.getColorHex());

        // Animate label highlighting
        label.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .alpha(1.0f)
                .setDuration(200)
                .start();

        // Change text color to mood color
        label.setTextColor(color);

        // Add subtle background to label
        android.graphics.drawable.GradientDrawable labelBackground = new android.graphics.drawable.GradientDrawable();
        labelBackground.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
        labelBackground.setColor(color);
        labelBackground.setAlpha(20); // Very subtle background
        labelBackground.setCornerRadius(12f);

        label.setBackground(labelBackground);
        label.setPadding(12, 4, 12, 4);

        // Add a subtle "pulse" effect
        addPulseEffect(label);
    }

    /**
     * Add a subtle pulse effect to the selected label
     */
    private void addPulseEffect(TextView label) {
        label.animate()
                .scaleX(1.15f)
                .scaleY(1.15f)
                .setDuration(300)
                .withEndAction(() -> {
                    label.animate()
                            .scaleX(1.1f)
                            .scaleY(1.1f)
                            .setDuration(300)
                            .start();
                })
                .start();
    }

    /**
     * Generate workout recommendations based on selected mood
     */
    private void generateWorkoutRecommendations() {
        if (selectedMood == null) {
            recommendedExercises.clear();
            return;
        }

        // Get user's difficulty level for personalization
        DifficultyLevel userDifficulty = currentUser != null ?
                currentUser.getPreferredDifficulty() : DifficultyLevel.BEGINNER;

        // Generate mood-specific exercises
        recommendedExercises = generateMoodSpecificExercises(selectedMood, userDifficulty);
    }

    /**
     * Generate exercises based on mood and difficulty
     */
    private List<Exercise> generateMoodSpecificExercises(MoodType mood, DifficultyLevel difficulty) {
        List<Exercise> exercises = new ArrayList<>();

        switch (mood) {
            case HAPPY:
                exercises.addAll(generateHappyMoodExercises(difficulty));
                break;
            case NEUTRAL:
                exercises.addAll(generateNeutralMoodExercises(difficulty));
                break;
            case FRUSTRATED:
                exercises.addAll(generateFrustratedMoodExercises(difficulty));
                break;
            case STRESSED:
                exercises.addAll(generateStressedMoodExercises(difficulty));
                break;
        }

        return exercises;
    }

    /**
     * Generate exercises for happy mood - high energy, fun activities - UPDATED with GIF names
     */
    private List<Exercise> generateHappyMoodExercises(DifficultyLevel difficulty) {
        List<Exercise> exercises = new ArrayList<>();

        Exercise cardioExercise = new Exercise("Dance Cardio",
                "Express your joy through movement with upbeat dance moves",
                difficulty, WorkoutCategory.CARDIO);
        cardioExercise.setEstimatedDurationMinutes(15);
        cardioExercise.setEstimatedCalories(120);
        cardioExercise.addSuitableMood(MoodType.HAPPY);
        cardioExercise.setImageUrl(generateGifName("Dance Cardio")); // NEW: Set GIF name
        exercises.add(cardioExercise);

        Exercise hiitExercise = new Exercise("Joy HIIT Circuit",
                "High-energy interval training to match your vibrant mood",
                difficulty, WorkoutCategory.HIIT);
        hiitExercise.setEstimatedDurationMinutes(20);
        hiitExercise.setEstimatedCalories(180);
        hiitExercise.addSuitableMood(MoodType.HAPPY);
        hiitExercise.setImageUrl(generateGifName("Joy HIIT Circuit")); // NEW: Set GIF name
        exercises.add(hiitExercise);

        return exercises;
    }

    /**
     * Generate exercises for neutral mood - balanced, general fitness - UPDATED with GIF names
     */
    private List<Exercise> generateNeutralMoodExercises(DifficultyLevel difficulty) {
        List<Exercise> exercises = new ArrayList<>();

        Exercise balancedWorkout = new Exercise("Balanced Body Workout",
                "A well-rounded routine combining strength and cardio",
                difficulty, WorkoutCategory.STRENGTH);
        balancedWorkout.setEstimatedDurationMinutes(25);
        balancedWorkout.setEstimatedCalories(150);
        balancedWorkout.addSuitableMood(MoodType.NEUTRAL);
        balancedWorkout.setImageUrl(generateGifName("Balanced Body Workout")); // NEW: Set GIF name
        exercises.add(balancedWorkout);

        Exercise flexibilityWorkout = new Exercise("Gentle Flexibility Flow",
                "Maintain your body's mobility with gentle stretching",
                difficulty, WorkoutCategory.FLEXIBILITY);
        flexibilityWorkout.setEstimatedDurationMinutes(18);
        flexibilityWorkout.setEstimatedCalories(80);
        flexibilityWorkout.addSuitableMood(MoodType.NEUTRAL);
        flexibilityWorkout.setImageUrl(generateGifName("Gentle Flexibility Flow")); // NEW: Set GIF name
        exercises.add(flexibilityWorkout);

        return exercises;
    }

    /**
     * Generate exercises for frustrated mood - intense, energy-releasing activities - UPDATED with GIF names
     */
    private List<Exercise> generateFrustratedMoodExercises(DifficultyLevel difficulty) {
        List<Exercise> exercises = new ArrayList<>();

        Exercise strengthTraining = new Exercise("Power Strength Training",
                "Channel your frustration into building strength and power",
                difficulty, WorkoutCategory.STRENGTH);
        strengthTraining.setEstimatedDurationMinutes(30);
        strengthTraining.setEstimatedCalories(200);
        strengthTraining.addSuitableMood(MoodType.FRUSTRATED);
        strengthTraining.setImageUrl(generateGifName("Power Strength Training")); // NEW: Set GIF name
        exercises.add(strengthTraining);

        Exercise intensiveHiit = new Exercise("Frustration-Busting HIIT",
                "High-intensity intervals to release built-up tension",
                difficulty, WorkoutCategory.HIIT);
        intensiveHiit.setEstimatedDurationMinutes(25);
        intensiveHiit.setEstimatedCalories(220);
        intensiveHiit.addSuitableMood(MoodType.FRUSTRATED);
        intensiveHiit.setImageUrl(generateGifName("Frustration-Busting HIIT")); // NEW: Set GIF name
        exercises.add(intensiveHiit);

        return exercises;
    }

    /**
     * Generate exercises for stressed mood - calming, restorative activities - UPDATED with GIF names
     */
    private List<Exercise> generateStressedMoodExercises(DifficultyLevel difficulty) {
        List<Exercise> exercises = new ArrayList<>();

        Exercise yogaFlow = new Exercise("Stress-Relief Yoga",
                "Gentle yoga poses to calm your mind and relax your body",
                difficulty, WorkoutCategory.YOGA);
        yogaFlow.setEstimatedDurationMinutes(22);
        yogaFlow.setEstimatedCalories(90);
        yogaFlow.addSuitableMood(MoodType.STRESSED);
        yogaFlow.setImageUrl(generateGifName("Stress-Relief Yoga")); // NEW: Set GIF name
        exercises.add(yogaFlow);

        Exercise breathingExercise = new Exercise("Mindful Breathing",
                "Deep breathing techniques to reduce stress and anxiety",
                difficulty, WorkoutCategory.BREATHING);
        breathingExercise.setEstimatedDurationMinutes(12);
        breathingExercise.setEstimatedCalories(40);
        breathingExercise.addSuitableMood(MoodType.STRESSED);
        breathingExercise.setImageUrl(generateGifName("Mindful Breathing")); // NEW: Set GIF name
        exercises.add(breathingExercise);

        return exercises;
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
     * Update workout suggestion display based on selected mood
     */
    private void updateWorkoutSuggestion() {
        if (selectedMood == null || recommendedExercises.isEmpty()) {
            tvWorkoutSuggestion.setText("Select your mood to get a personalized workout!");
            resetWorkoutSuggestionCard();
            return;
        }

        // Get mood-specific message and exercise recommendation
        String moodMessage = getMoodSpecificMessage(selectedMood);
        Exercise recommendedExercise = getRandomRecommendedExercise();

        String suggestionText = moodMessage + "\n\n" +
                "Recommended: " + recommendedExercise.getName() + "\n" +
                recommendedExercise.getDescription() + "\n\n" +
                "Duration: " + recommendedExercise.getEstimatedDurationMinutes() + " min • " +
                "Calories: ~" + recommendedExercise.getEstimatedCalories();

        tvWorkoutSuggestion.setText(suggestionText);

        // Add visual feedback to the workout suggestion card
        animateWorkoutSuggestionCard();
    }

    /**
     * Animate the workout suggestion card when mood is selected
     */
    private void animateWorkoutSuggestionCard() {
        android.view.View cardView = (View) tvWorkoutSuggestion.getParent().getParent();

        // Get mood-specific color for subtle accent
        int color = Color.parseColor(selectedMood.getColorHex());

        // Add subtle colored border to the card
        if (cardView instanceof androidx.cardview.widget.CardView) {
            androidx.cardview.widget.CardView card = (androidx.cardview.widget.CardView) cardView;

            // Animate the card with a subtle bounce and color accent
            card.animate()
                    .scaleX(1.02f)
                    .scaleY(1.02f)
                    .setDuration(200)
                    .withEndAction(() -> {
                        card.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .setDuration(200)
                                .start();
                    })
                    .start();

            // Add subtle mood-colored accent to card elevation
            card.setCardBackgroundColor(mixColors(getResources().getColor(R.color.card_background), color, 0.05f));
        }
    }

    /**
     * Reset workout suggestion card to default state
     */
    private void resetWorkoutSuggestionCard() {
        android.view.View cardView = (View) tvWorkoutSuggestion.getParent().getParent();

        if (cardView instanceof androidx.cardview.widget.CardView) {
            androidx.cardview.widget.CardView card = (androidx.cardview.widget.CardView) cardView;
            card.setCardBackgroundColor(getResources().getColor(R.color.card_background));
        }
    }

    /**
     * Mix two colors with a given ratio
     */
    private int mixColors(int color1, int color2, float ratio) {
        final float inverseRatio = 1f - ratio;

        int r = (int) ((Color.red(color1) * inverseRatio) + (Color.red(color2) * ratio));
        int g = (int) ((Color.green(color1) * inverseRatio) + (Color.green(color2) * ratio));
        int b = (int) ((Color.blue(color1) * inverseRatio) + (Color.blue(color2) * ratio));

        return Color.rgb(r, g, b);
    }

    /**
     * Get mood-specific motivational message
     */
    private String getMoodSpecificMessage(MoodType mood) {
        switch (mood) {
            case HAPPY:
                return "Your positive energy is contagious! Let's channel it into an amazing workout! 🌟";
            case NEUTRAL:
                return "Perfect time for a balanced workout. Let's maintain your steady momentum! ⚖️";
            case FRUSTRATED:
                return "Turn that frustration into fuel! These intense exercises will help you release tension. 💪";
            case STRESSED:
                return "Take a deep breath. These calming exercises will help you find your center. 🧘‍♀️";
            default:
                return "Let's find the perfect workout for how you're feeling!";
        }
    }

    /**
     * Get a random exercise from recommendations
     */
    private Exercise getRandomRecommendedExercise() {
        if (recommendedExercises.isEmpty()) {
            // Fallback exercise
            return new Exercise("General Workout", "A balanced fitness routine",
                    DifficultyLevel.BEGINNER, WorkoutCategory.CARDIO);
        }

        Random random = new Random();
        return recommendedExercises.get(random.nextInt(recommendedExercises.size()));
    }

    /**
     * Re-enable start workout button (in case it was disabled)
     */
    private void enableStartWorkoutButton() {
        if (selectedMood != null && !recommendedExercises.isEmpty()) {
            btnStartWorkout.setEnabled(true);
            btnStartWorkout.setAlpha(1.0f);

            // Add subtle animation to draw attention
            btnStartWorkout.animate()
                    .scaleX(1.05f)
                    .scaleY(1.05f)
                    .setDuration(200)
                    .withEndAction(() -> {
                        btnStartWorkout.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .setDuration(200)
                                .start();
                    })
                    .start();
        }
    }

    /**
     * Start workout with selected mood and exercises - UPDATED to pass GIF names
     */
    private void startWorkout() {
        if (selectedMood == null || recommendedExercises.isEmpty()) {
            return;
        }

        try {
            // Create workout session
            String userId = currentUser != null ? currentUser.getUserId() : "anonymous";
            currentSession = new WorkoutSession(userId, selectedMood);

            // Add recommended exercises to session
            for (Exercise exercise : recommendedExercises) {
                currentSession.addExercise(exercise);
            }

            // Start the session
            currentSession.startWorkout();

            // Navigate to exercise demo/tutorial for the first exercise
            Intent demoIntent = new Intent(this, ExerciseDemoActivity.class);
            demoIntent.putExtra("mood_type", selectedMood.name());
            demoIntent.putExtra("session_id", currentSession.getSessionId());
            demoIntent.putExtra("current_exercise_index", 0);
            demoIntent.putExtra("total_exercises", recommendedExercises.size());
            demoIntent.putExtra("estimated_duration", getEstimatedTotalDuration());

            // Pass current exercise data
            Exercise firstExercise = recommendedExercises.get(0);
            demoIntent.putExtra("exercise_name", firstExercise.getName());
            demoIntent.putExtra("exercise_description", firstExercise.getDescription());
            demoIntent.putExtra("exercise_instructions", firstExercise.getInstructions());
            demoIntent.putExtra("exercise_duration", firstExercise.getEstimatedDurationMinutes());
            demoIntent.putExtra("exercise_calories", firstExercise.getEstimatedCalories());
            demoIntent.putExtra("exercise_category", firstExercise.getCategory().getDisplayName());
            demoIntent.putExtra("exercise_difficulty", firstExercise.getDifficulty().getDisplayName());

            // NEW: Pass GIF name
            String gifName = firstExercise.getImageUrl(); // Already set in exercise creation
            demoIntent.putExtra("exercise_gif", gifName);

            startActivityForResult(demoIntent, 1001);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        } catch (Exception e) {
            android.util.Log.e(TAG, "Error starting workout", e);
            showError("Unable to start workout. Please try again.");
        }
    }

    /**
     * Get estimated total duration for all recommended exercises
     */
    private int getEstimatedTotalDuration() {
        int totalDuration = 0;
        for (Exercise exercise : recommendedExercises) {
            totalDuration += exercise.getEstimatedDurationMinutes();
        }
        return Math.max(totalDuration, 10); // Minimum 10 minutes
    }

    /**
     * Provide haptic feedback for mood selection
     */
    private void performHapticFeedback() {
        try {
            android.os.Vibrator vibrator = (android.os.Vibrator) getSystemService(VIBRATOR_SERVICE);
            if (vibrator != null && vibrator.hasVibrator()) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    vibrator.vibrate(android.os.VibrationEffect.createOneShot(50, android.os.VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(50);
                }
            }
        } catch (Exception e) {
            // Haptic feedback is optional, don't crash if it fails
            android.util.Log.w(TAG, "Haptic feedback failed", e);
        }
    }

    /**
     * Show error message to user
     */
    private void showError(String message) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show();
    }

    /**
     * Handle results from exercise demo and timer activities - UPDATED
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001) { // Exercise demo result
            if (resultCode == RESULT_OK && data != null) {
                String action = data.getStringExtra("action");

                if ("next_exercise".equals(action)) {
                    // Move to next exercise
                    int nextExerciseIndex = data.getIntExtra("current_exercise_index", 0);
                    navigateToNextExercise(nextExerciseIndex);

                } else if ("workout_complete".equals(action)) {
                    // All exercises completed
                    completeWorkoutSession(data);
                    setResult(RESULT_OK); // Notify HomeActivity to refresh
                    showCompletionMessage();
                    finish();
                }
            } else if (resultCode == RESULT_CANCELED) {
                // User returned from exercise demo (either cancelled exercise or went back)
                handleExerciseDemoReturn(data);
            }
        }
    }

    /**
     * Handle return from exercise demo when cancelled
     */
    private void handleExerciseDemoReturn(Intent data) {
        if (data != null) {
            String action = data.getStringExtra("action");

            if ("exercise_cancelled".equals(action)) {
                // User cancelled a specific exercise but wants to stay in workout
                showExerciseCancelledFeedback();
                // Stay on mood workout screen - user can restart or choose different mood

            } else {
                // User went back from demo screen
                showReturnFromDemoFeedback();
            }
        } else {
            // Normal back navigation from demo
            showReturnFromDemoFeedback();
        }

        // Re-enable the start workout button in case it was disabled
        enableStartWorkoutButton();
    }

    /**
     * Show feedback when user cancels an exercise
     */
    private void showExerciseCancelledFeedback() {
        android.widget.Toast.makeText(this,
                "No worries! You can try the exercise again or choose a different mood.",
                android.widget.Toast.LENGTH_LONG).show();

        // Optional: Add gentle pulse to mood selection to encourage re-selection
        if (selectedMood != null) {
            ImageButton selectedButton = getMoodButton(selectedMood);
            if (selectedButton != null) {
                selectedButton.animate()
                        .scaleX(1.1f)
                        .scaleY(1.1f)
                        .setDuration(300)
                        .withEndAction(() -> {
                            selectedButton.animate()
                                    .scaleX(1.15f) // Back to selected state
                                    .scaleY(1.15f)
                                    .setDuration(300)
                                    .start();
                        })
                        .start();
            }
        }
    }

    /**
     * Show feedback when user returns from demo
     */
    private void showReturnFromDemoFeedback() {
        // Gentle feedback that they're back to mood selection
        android.widget.Toast.makeText(this,
                "Ready to start your workout?",
                android.widget.Toast.LENGTH_SHORT).show();
    }

    /**
     * Navigate to the next exercise in the sequence - UPDATED to pass GIF names
     */
    private void navigateToNextExercise(int exerciseIndex) {
        if (exerciseIndex >= recommendedExercises.size()) {
            // All exercises completed
            completeWorkoutSession(null);
            setResult(RESULT_OK);
            showCompletionMessage();
            finish();
            return;
        }

        // Get the next exercise
        Exercise nextExercise = recommendedExercises.get(exerciseIndex);

        // Navigate to exercise demo for next exercise
        Intent demoIntent = new Intent(this, ExerciseDemoActivity.class);
        demoIntent.putExtra("mood_type", selectedMood.name());
        demoIntent.putExtra("session_id", currentSession.getSessionId());
        demoIntent.putExtra("current_exercise_index", exerciseIndex);
        demoIntent.putExtra("total_exercises", recommendedExercises.size());
        demoIntent.putExtra("estimated_duration", getEstimatedTotalDuration());

        // Pass exercise data
        demoIntent.putExtra("exercise_name", nextExercise.getName());
        demoIntent.putExtra("exercise_description", nextExercise.getDescription());
        demoIntent.putExtra("exercise_instructions", nextExercise.getInstructions());
        demoIntent.putExtra("exercise_duration", nextExercise.getEstimatedDurationMinutes());
        demoIntent.putExtra("exercise_calories", nextExercise.getEstimatedCalories());
        demoIntent.putExtra("exercise_category", nextExercise.getCategory().getDisplayName());
        demoIntent.putExtra("exercise_difficulty", nextExercise.getDifficulty().getDisplayName());

        // NEW: Pass GIF name
        String gifName = nextExercise.getImageUrl(); // Already set in exercise creation
        demoIntent.putExtra("exercise_gif", gifName);

        startActivityForResult(demoIntent, 1001);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * Complete the workout session and save data
     */
    private void completeWorkoutSession(Intent data) {
        if (currentSession == null) return;

        try {
            // End the session
            currentSession.endWorkout();

            // Get actual duration from timer if available
            if (data != null && data.hasExtra("actual_duration")) {
                int actualDuration = data.getIntExtra("actual_duration", 0);
                currentSession.setDurationMinutes(actualDuration);
            }

            // Save workout session
            dataManager.recordWorkoutCompletion(currentSession);

            // Show completion message
            showCompletionMessage();

        } catch (Exception e) {
            android.util.Log.e(TAG, "Error completing workout session", e);
        }
    }

    /**
     * Show workout completion message
     */
    private void showCompletionMessage() {
        String message = "Great job! You've completed your " +
                selectedMood.getDisplayName().toLowerCase() +
                " mood workout! 🎉";
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_LONG).show();
    }

    /**
     * Handle back button press
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * Clean up resources
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up any ongoing sessions if activity is destroyed unexpectedly
        if (currentSession != null && currentSession.isActive()) {
            currentSession = null;
        }
    }
}