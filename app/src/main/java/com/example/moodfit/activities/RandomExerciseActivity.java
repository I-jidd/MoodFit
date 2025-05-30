package com.example.moodfit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

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
 * RandomExerciseActivity - Surprise workout generator with diverse exercise database
 * Provides random challenges across all fitness categories and difficulty levels
 */
public class RandomExerciseActivity extends AppCompatActivity {

    private static final String TAG = "RandomExerciseActivity";

    // Data Management
    private DataManager dataManager;
    private User currentUser;

    // UI Components
    private Button btnSurpriseMe;
    private TextView tvRandomExercise;
    private Button btnTryAgain;

    // Exercise Database
    private List<Exercise> exerciseDatabase;
    private Random random;

    // Current State
    private Exercise currentExercise;
    private boolean isGenerating = false;

    // Animation Handler
    private Handler animationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_exercise);

        // Initialize data management
        initializeDataManager();

        // Initialize UI components
        initializeViews();

        // Build exercise database
        buildExerciseDatabase();

        // Setup event listeners
        setupEventListeners();

        // Initialize random generator
        random = new Random();
        animationHandler = new Handler(Looper.getMainLooper());
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
        btnSurpriseMe = findViewById(R.id.btn_surprise_me);
        tvRandomExercise = findViewById(R.id.tv_random_exercise);
        btnTryAgain = findViewById(R.id.btn_try_again);

        // Initially hide try again button
        btnTryAgain.setVisibility(View.GONE);
    }

    /**
     * Setup event listeners for buttons
     */
    private void setupEventListeners() {
        btnSurpriseMe.setOnClickListener(v -> generateRandomExercise());
        btnTryAgain.setOnClickListener(v -> generateRandomExercise());
    }

    /**
     * Build comprehensive exercise database with variety across all categories
     */
    private void buildExerciseDatabase() {
        exerciseDatabase = new ArrayList<>();

        // Add exercises for each category and difficulty level
        addCardioExercises();
        addStrengthExercises();
        addFlexibilityExercises();
        addYogaExercises();
        addHiitExercises();
        addBreathingExercises();
    }

    /**
     * Add cardio exercises to database
     */
    private void addCardioExercises() {
        // Beginner Cardio
        addExercise("Marching in Place", "Simple stationary march to get your heart pumping",
                DifficultyLevel.BEGINNER, WorkoutCategory.CARDIO, 5, 50,
                "March with high knees for 30 seconds, rest 10 seconds, repeat 5 times");

        addExercise("Wall Push-Ups", "Modified push-ups against a wall for beginners",
                DifficultyLevel.BEGINNER, WorkoutCategory.CARDIO, 8, 60,
                "Stand arm's length from wall, push and return. 3 sets of 10 reps");

        addExercise("Seated Leg Lifts", "Cardio workout you can do from a chair",
                DifficultyLevel.BEGINNER, WorkoutCategory.CARDIO, 6, 40,
                "Lift alternating legs while seated. 2 sets of 20 per leg");

        // Intermediate Cardio
        addExercise("Jumping Jacks", "Classic full-body cardio movement",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.CARDIO, 10, 100,
                "Jump feet apart while raising arms overhead. 4 sets of 25 reps");

        addExercise("Step-Ups", "Use stairs or a sturdy platform for cardio",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.CARDIO, 12, 120,
                "Step up and down on platform. 3 sets of 15 per leg");

        addExercise("Dancing", "Put on your favorite song and dance!",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.CARDIO, 15, 140,
                "Dance freely to 3-4 songs. Let the music move you!");

        // Advanced Cardio
        addExercise("Burpees", "Ultimate full-body cardio challenge",
                DifficultyLevel.ADVANCED, WorkoutCategory.CARDIO, 15, 200,
                "Squat, jump back to plank, push-up, jump forward, jump up. 3 sets of 10");

        addExercise("Mountain Climbers", "High-intensity core and cardio combo",
                DifficultyLevel.ADVANCED, WorkoutCategory.CARDIO, 12, 150,
                "Plank position, alternate bringing knees to chest rapidly. 4 sets of 30 seconds");
    }

    /**
     * Add strength exercises to database
     */
    private void addStrengthExercises() {
        // Beginner Strength
        addExercise("Chair Squats", "Build leg strength using a chair for support",
                DifficultyLevel.BEGINNER, WorkoutCategory.STRENGTH, 8, 70,
                "Lower down to chair, hover briefly, stand up. 2 sets of 12");

        addExercise("Wall Sits", "Static leg strengthening exercise",
                DifficultyLevel.BEGINNER, WorkoutCategory.STRENGTH, 5, 50,
                "Back against wall, slide down to sitting position. Hold for 30 seconds");

        addExercise("Modified Planks", "Core strengthening on knees",
                DifficultyLevel.BEGINNER, WorkoutCategory.STRENGTH, 6, 40,
                "Plank position on knees. Hold for 20 seconds, repeat 3 times");

        // Intermediate Strength
        addExercise("Push-Ups", "Classic upper body strength builder",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.STRENGTH, 10, 90,
                "Full push-ups maintaining straight line. 3 sets of 10-15 reps");

        addExercise("Bodyweight Squats", "Fundamental lower body exercise",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.STRENGTH, 12, 100,
                "Deep squats with proper form. 3 sets of 15 reps");

        addExercise("Lunges", "Single-leg strength and balance",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.STRENGTH, 10, 80,
                "Alternating forward lunges. 2 sets of 12 per leg");

        // Advanced Strength
        addExercise("Single-Arm Push-Ups", "Ultimate upper body challenge",
                DifficultyLevel.ADVANCED, WorkoutCategory.STRENGTH, 15, 180,
                "Push-ups with one arm behind back. Work up to 5 per arm");

        addExercise("Pistol Squats", "Single-leg squat mastery",
                DifficultyLevel.ADVANCED, WorkoutCategory.STRENGTH, 18, 160,
                "Single-leg squat to full depth. Assisted or full. 3 sets of 5 per leg");
    }

    /**
     * Add flexibility exercises to database
     */
    private void addFlexibilityExercises() {
        // Beginner Flexibility
        addExercise("Neck Rolls", "Gentle neck and shoulder mobility",
                DifficultyLevel.BEGINNER, WorkoutCategory.FLEXIBILITY, 5, 30,
                "Slow, controlled neck circles. 5 each direction");

        addExercise("Shoulder Shrugs", "Release shoulder tension",
                DifficultyLevel.BEGINNER, WorkoutCategory.FLEXIBILITY, 4, 25,
                "Lift shoulders to ears, hold 5 seconds, release. Repeat 10 times");

        addExercise("Ankle Circles", "Improve ankle mobility",
                DifficultyLevel.BEGINNER, WorkoutCategory.FLEXIBILITY, 6, 20,
                "Rotate ankles in circles. 10 each direction, both feet");

        // Intermediate Flexibility
        addExercise("Cat-Cow Stretches", "Spinal mobility and flexibility",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.FLEXIBILITY, 8, 40,
                "On hands and knees, arch and round spine slowly. 15 reps");

        addExercise("Hip Flexor Stretch", "Open tight hip flexors",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.FLEXIBILITY, 10, 35,
                "Kneeling lunge position, lean forward gently. Hold 30 seconds each side");

        addExercise("Hamstring Stretch", "Lengthen tight hamstrings",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.FLEXIBILITY, 8, 30,
                "Seated or standing forward fold. Hold 30-45 seconds");

        // Advanced Flexibility
        addExercise("Full Splits", "Advanced hip and leg flexibility",
                DifficultyLevel.ADVANCED, WorkoutCategory.FLEXIBILITY, 15, 50,
                "Work toward front or side splits. Hold comfortable edge for 1-2 minutes");

        addExercise("Backbend Flow", "Spinal extension and chest opening",
                DifficultyLevel.ADVANCED, WorkoutCategory.FLEXIBILITY, 12, 60,
                "Bridge to wheel pose progression. Hold for 30 seconds each");
    }

    /**
     * Add yoga exercises to database
     */
    private void addYogaExercises() {
        // Beginner Yoga
        addExercise("Child's Pose", "Restorative rest and gentle stretch",
                DifficultyLevel.BEGINNER, WorkoutCategory.YOGA, 5, 25,
                "Kneel, sit back on heels, fold forward. Rest and breathe for 1-2 minutes");

        addExercise("Mountain Pose", "Foundation of all standing poses",
                DifficultyLevel.BEGINNER, WorkoutCategory.YOGA, 4, 20,
                "Stand tall, feet together, arms at sides. Focus on alignment for 1 minute");

        addExercise("Easy Seated Twist", "Gentle spinal rotation",
                DifficultyLevel.BEGINNER, WorkoutCategory.YOGA, 6, 30,
                "Seated cross-legged, gentle twist both directions. Hold 30 seconds each");

        // Intermediate Yoga
        addExercise("Sun Salutation A", "Dynamic flowing sequence",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.YOGA, 12, 80,
                "Complete sun salutation sequence. Repeat 5 rounds with breath");

        addExercise("Warrior II Flow", "Standing strength and focus",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.YOGA, 10, 60,
                "Warrior II to extended side angle. Hold 45 seconds each side");

        addExercise("Tree Pose", "Standing balance and concentration",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.YOGA, 8, 40,
                "Single-leg balance with foot on inner thigh. Hold 30 seconds each side");

        // Advanced Yoga
        addExercise("Crow Pose", "Arm balance and core strength",
                DifficultyLevel.ADVANCED, WorkoutCategory.YOGA, 15, 100,
                "Balance on hands with knees on upper arms. Work up to 30 seconds");

        addExercise("Headstand", "Inversion and full-body strength",
                DifficultyLevel.ADVANCED, WorkoutCategory.YOGA, 18, 120,
                "Supported headstand against wall. Build up to 2-3 minutes");
    }

    /**
     * Add HIIT exercises to database
     */
    private void addHiitExercises() {
        // Beginner HIIT
        addExercise("Gentle HIIT Circuit", "Low-impact high-intensity intervals",
                DifficultyLevel.BEGINNER, WorkoutCategory.HIIT, 10, 80,
                "30 seconds work, 30 seconds rest. Marching, arm circles, modified squats");

        addExercise("Tabata Walking", "Interval walking routine",
                DifficultyLevel.BEGINNER, WorkoutCategory.HIIT, 8, 60,
                "20 seconds fast walk, 10 seconds slow. Repeat 8 rounds");

        // Intermediate HIIT
        addExercise("Classic Tabata", "High-intensity 4-minute protocol",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.HIIT, 15, 150,
                "20 seconds max effort, 10 seconds rest. 8 rounds of chosen exercise");

        addExercise("EMOM Challenge", "Every minute on the minute",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.HIIT, 18, 180,
                "Set number of reps each minute for 10 minutes. Rest remaining time");

        // Advanced HIIT
        addExercise("Death by Burpees", "Progressive intensity challenge",
                DifficultyLevel.ADVANCED, WorkoutCategory.HIIT, 20, 250,
                "Minute 1: 1 burpee, Minute 2: 2 burpees, etc. Go until failure");

        addExercise("Fight Gone Bad", "Mixed modal high intensity",
                DifficultyLevel.ADVANCED, WorkoutCategory.HIIT, 25, 300,
                "5 exercises, 1 minute each, 1 minute rest. Repeat 3 rounds");
    }

    /**
     * Add breathing exercises to database
     */
    private void addBreathingExercises() {
        // Beginner Breathing
        addExercise("Box Breathing", "Simple 4-count breathing pattern",
                DifficultyLevel.BEGINNER, WorkoutCategory.BREATHING, 5, 15,
                "Inhale 4, hold 4, exhale 4, hold 4. Repeat for 5 minutes");

        addExercise("Belly Breathing", "Deep diaphragmatic breathing",
                DifficultyLevel.BEGINNER, WorkoutCategory.BREATHING, 6, 20,
                "Hand on chest, hand on belly. Breathe so only belly hand moves");

        // Intermediate Breathing
        addExercise("4-7-8 Breathing", "Calming breath ratio",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.BREATHING, 8, 25,
                "Inhale 4, hold 7, exhale 8. Repeat 4-8 cycles");

        addExercise("Alternate Nostril", "Balancing pranayama technique",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.BREATHING, 10, 30,
                "Use thumb and finger to alternate breathing through each nostril");

        // Advanced Breathing
        addExercise("Breath of Fire", "Energizing rapid breathing",
                DifficultyLevel.ADVANCED, WorkoutCategory.BREATHING, 12, 50,
                "Rapid, shallow breathing through nose. 30 breaths, 3 rounds");

        addExercise("Wim Hof Method", "Power breathing technique",
                DifficultyLevel.ADVANCED, WorkoutCategory.BREATHING, 15, 60,
                "30 deep breaths, hold breath after exhale, repeat 3 rounds");
    }

    /**
     * Helper method to add exercise to database
     */
    private void addExercise(String name, String description, DifficultyLevel difficulty,
                             WorkoutCategory category, int duration, int calories, String instructions) {
        Exercise exercise = new Exercise(name, description, difficulty, category);
        exercise.setEstimatedDurationMinutes(duration);
        exercise.setEstimatedCalories(calories);
        exercise.setInstructions(instructions);
        exerciseDatabase.add(exercise);
    }

    /**
     * Generate and display a random exercise
     */
    private void generateRandomExercise() {
        if (isGenerating) return;

        isGenerating = true;

        // Disable buttons during generation
        btnSurpriseMe.setEnabled(false);
        btnTryAgain.setEnabled(false);

        // Start surprise animation
        startSurpriseAnimation();

        // Generate exercise after animation delay
        animationHandler.postDelayed(() -> {
            currentExercise = selectRandomExercise();
            displayExercise(currentExercise);
            showTryAgainButton();
            isGenerating = false;

            // Re-enable buttons
            btnSurpriseMe.setEnabled(true);
            btnTryAgain.setEnabled(true);

        }, 1500); // 1.5 second animation
    }

    /**
     * Start surprise generation animation
     */
    private void startSurpriseAnimation() {
        // Animate the surprise button
        btnSurpriseMe.animate()
                .scaleX(1.2f)
                .scaleY(1.2f)
                .rotation(360f)
                .setDuration(800)
                .withEndAction(() -> {
                    btnSurpriseMe.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .rotation(0f)
                            .setDuration(300)
                            .start();
                })
                .start();

        // Show generating text with typing effect
        showGeneratingAnimation();
    }

    /**
     * Show generating animation with text changes
     */
    private void showGeneratingAnimation() {
        String[] generatingTexts = {
                "🎲 Rolling the dice...",
                "🔮 Consulting the fitness oracle...",
                "⚡ Generating your challenge...",
                "🎯 Perfect! Here's your exercise:"
        };

        int[] delays = {0, 500, 1000, 1400};

        for (int i = 0; i < generatingTexts.length; i++) {
            final int index = i;
            animationHandler.postDelayed(() -> {
                if (index < generatingTexts.length - 1) {
                    tvRandomExercise.setText(generatingTexts[index]);
                    addTextBounceEffect();
                }
            }, delays[i]);
        }
    }

    /**
     * Add bounce effect to text
     */
    private void addTextBounceEffect() {
        tvRandomExercise.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(200)
                .withEndAction(() -> {
                    tvRandomExercise.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(200)
                            .start();
                })
                .start();
    }

    /**
     * Select a random exercise based on user's difficulty preference
     */
    private Exercise selectRandomExercise() {
        if (exerciseDatabase.isEmpty()) {
            // Fallback exercise
            return new Exercise("Jumping Jacks", "Classic cardio exercise",
                    DifficultyLevel.BEGINNER, WorkoutCategory.CARDIO);
        }

        // Get user's preferred difficulty
        DifficultyLevel userDifficulty = currentUser != null ?
                currentUser.getPreferredDifficulty() : DifficultyLevel.BEGINNER;

        // Filter exercises by difficulty (include current level and one level below/above)
        List<Exercise> suitableExercises = filterExercisesByDifficulty(userDifficulty);

        // Select random exercise from suitable list
        return suitableExercises.get(random.nextInt(suitableExercises.size()));
    }

    /**
     * Filter exercises by difficulty level with some variety
     */
    private List<Exercise> filterExercisesByDifficulty(DifficultyLevel targetDifficulty) {
        List<Exercise> filtered = new ArrayList<>();

        for (Exercise exercise : exerciseDatabase) {
            DifficultyLevel exerciseDifficulty = exercise.getDifficulty();

            // Include target difficulty (70% chance)
            if (exerciseDifficulty == targetDifficulty) {
                filtered.add(exercise);
                if (random.nextFloat() < 0.7f) {
                    filtered.add(exercise); // Add again for higher probability
                }
            }
            // Include one level easier (20% chance)
            else if (isOneLevelEasier(exerciseDifficulty, targetDifficulty) && random.nextFloat() < 0.2f) {
                filtered.add(exercise);
            }
            // Include one level harder (10% chance)
            else if (isOneLevelHarder(exerciseDifficulty, targetDifficulty) && random.nextFloat() < 0.1f) {
                filtered.add(exercise);
            }
        }

        // If no suitable exercises found, return all exercises of target difficulty
        if (filtered.isEmpty()) {
            for (Exercise exercise : exerciseDatabase) {
                if (exercise.getDifficulty() == targetDifficulty) {
                    filtered.add(exercise);
                }
            }
        }

        // If still empty, return all exercises
        if (filtered.isEmpty()) {
            filtered.addAll(exerciseDatabase);
        }

        return filtered;
    }

    /**
     * Check if exercise difficulty is one level easier than target
     */
    private boolean isOneLevelEasier(DifficultyLevel exercise, DifficultyLevel target) {
        return (target == DifficultyLevel.INTERMEDIATE && exercise == DifficultyLevel.BEGINNER) ||
                (target == DifficultyLevel.ADVANCED && exercise == DifficultyLevel.INTERMEDIATE);
    }

    /**
     * Check if exercise difficulty is one level harder than target
     */
    private boolean isOneLevelHarder(DifficultyLevel exercise, DifficultyLevel target) {
        return (target == DifficultyLevel.BEGINNER && exercise == DifficultyLevel.INTERMEDIATE) ||
                (target == DifficultyLevel.INTERMEDIATE && exercise == DifficultyLevel.ADVANCED);
    }

    /**
     * Display the selected exercise with animation
     */
    private void displayExercise(Exercise exercise) {
        // Format exercise information
        String displayText = "🏃‍♂️ " + exercise.getName() + "\n\n" +
                "📝 " + exercise.getDescription() + "\n\n" +
                "⏱️ Duration: " + exercise.getEstimatedDurationMinutes() + " minutes\n" +
                "🔥 Calories: ~" + exercise.getEstimatedCalories() + "\n" +
                "💪 Level: " + exercise.getDifficulty().getDisplayName() + "\n" +
                "📂 Category: " + exercise.getCategory().getDisplayName() + "\n\n" +
                "🎯 Instructions:\n" + exercise.getInstructions();

        // Animate text appearance
        tvRandomExercise.setAlpha(0f);
        tvRandomExercise.setText(displayText);
        tvRandomExercise.animate()
                .alpha(1f)
                .scaleX(1.05f)
                .scaleY(1.05f)
                .setDuration(500)
                .withEndAction(() -> {
                    tvRandomExercise.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(200)
                            .start();
                })
                .start();

        // Add haptic feedback
        performHapticFeedback();
    }

    /**
     * Show the try again button with animation
     */
    private void showTryAgainButton() {
        btnTryAgain.setVisibility(View.VISIBLE);
        btnTryAgain.setAlpha(0f);
        btnTryAgain.setScaleX(0.8f);
        btnTryAgain.setScaleY(0.8f);

        btnTryAgain.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(300)
                .start();
    }

    /**
     * Provide haptic feedback for exercise generation
     */
    private void performHapticFeedback() {
        try {
            android.os.Vibrator vibrator = (android.os.Vibrator) getSystemService(VIBRATOR_SERVICE);
            if (vibrator != null && vibrator.hasVibrator()) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    vibrator.vibrate(android.os.VibrationEffect.createOneShot(100, android.os.VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(100);
                }
            }
        } catch (Exception e) {
            android.util.Log.w(TAG, "Haptic feedback failed", e);
        }
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
        if (animationHandler != null) {
            animationHandler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * Create a quick workout session with current exercise (optional feature)
     */
    private void createQuickWorkoutSession() {
        if (currentExercise != null && currentUser != null) {
            try {
                WorkoutSession quickSession = new WorkoutSession(currentUser.getUserId(), MoodType.NEUTRAL);
                quickSession.addExercise(currentExercise);

                // This could be extended to track random exercise completions
                // dataManager.recordWorkoutCompletion(quickSession);

            } catch (Exception e) {
                android.util.Log.w(TAG, "Could not create quick workout session", e);
            }
        }
    }
}