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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * MoodWorkoutActivity - Enhanced to use Tutorial Exercise Database
 * Generates large random workouts (6-10 exercises) using existing tutorial exercises
 * UPDATED: Now uses the same exercise database as TutorialsActivity for consistency
 */
public class MoodWorkoutActivity extends AppCompatActivity {

    private static final String TAG = "MoodWorkoutActivity";

    // ENHANCED: Workout configuration
    private static final int MIN_EXERCISES_PER_WORKOUT = 6;
    private static final int MAX_EXERCISES_PER_WORKOUT = 10;

    // Data Management
    private DataManager dataManager;
    private User currentUser;
    private Random random;

    // Exercise Database (same as TutorialsActivity)
    private Map<DifficultyLevel, List<Exercise>> exercisesByDifficulty;
    private Map<String, String> exerciseGifMap;

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

    private int totalExercisesCompleted = 0;
    private int totalCaloriesBurned = 0;
    private boolean anyExercisesSkipped = false;
    private long workoutStartTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_workout_generator);

        // Initialize data management
        initializeDataManager();

        // Build exercise database (same as TutorialsActivity)
        buildExerciseDatabase();

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
        random = new Random();
    }

    /**
     * Build comprehensive exercise database (same as TutorialsActivity)
     */
    private void buildExerciseDatabase() {
        exercisesByDifficulty = new HashMap<>();

        // Initialize gif mapping first
        initializeExerciseGifMapping();

        // Initialize lists for each difficulty
        exercisesByDifficulty.put(DifficultyLevel.BEGINNER, new ArrayList<>());
        exercisesByDifficulty.put(DifficultyLevel.INTERMEDIATE, new ArrayList<>());
        exercisesByDifficulty.put(DifficultyLevel.ADVANCED, new ArrayList<>());

        // Add exercises for each difficulty level (same as TutorialsActivity)
        addBeginnerExercises();
        addIntermediateExercises();
        addAdvancedExercises();
    }

    /**
     * Initialize proper mapping between exercise names and GIF files (same as TutorialsActivity)
     */
    private void initializeExerciseGifMapping() {
        exerciseGifMap = new HashMap<>();

        // BEGINNER EXERCISES
        exerciseGifMap.put("Marching in Place", "gif_marching_in_place");
        exerciseGifMap.put("Wall Push-Ups", "gif_wall_pushups");
        exerciseGifMap.put("Seated Leg Lifts", "gif_seated_leg_lifts");
        exerciseGifMap.put("Chair Squats", "gif_chair_squats");
        exerciseGifMap.put("Wall Sits", "gif_wall_sits");
        exerciseGifMap.put("Modified Planks", "gif_modified_planks");
        exerciseGifMap.put("Neck Rolls", "gif_neck_rolls");
        exerciseGifMap.put("Shoulder Shrugs", "gif_shoulder_shrugs");
        exerciseGifMap.put("Box Breathing", "gif_box_breathing");
        exerciseGifMap.put("Belly Breathing", "gif_belly_breathing");
        exerciseGifMap.put("Child's Pose", "gif_childs_pose");
        exerciseGifMap.put("Mountain Pose", "gif_mountain_pose");

        // INTERMEDIATE EXERCISES
        exerciseGifMap.put("Jumping Jacks", "gif_jumping_jacks");
        exerciseGifMap.put("Step-Ups", "gif_stepups");
        exerciseGifMap.put("Dancing", "gif_dancing");
        exerciseGifMap.put("Push-Ups", "gif_pushups");
        exerciseGifMap.put("Bodyweight Squats", "gif_bodyweight_squats");
        exerciseGifMap.put("Lunges", "gif_lunges");
        exerciseGifMap.put("Classic Tabata", "gif_classic_tabata");
        exerciseGifMap.put("EMOM Challenge", "gif_emom_challenge");
        exerciseGifMap.put("Sun Salutation A", "gif_sun_salutation_a");
        exerciseGifMap.put("Warrior II Flow", "gif_warrior_ii_flow");
        exerciseGifMap.put("Cat-Cow Stretches", "gif_catcow_stretches");
        exerciseGifMap.put("Hip Flexor Stretch", "gif_hip_flexor_stretch");

        // ADVANCED EXERCISES
        exerciseGifMap.put("Burpees", "gif_burpees");
        exerciseGifMap.put("Mountain Climbers", "gif_mountain_climbers");
        exerciseGifMap.put("Single-Arm Push-Ups", "gif_singlearm_pushups");
        exerciseGifMap.put("Pistol Squats", "gif_pistol_squats");
        exerciseGifMap.put("Death by Burpees", "gif_death_by_burpees");
        exerciseGifMap.put("Fight Gone Bad", "gif_fight_gone_bad");
        exerciseGifMap.put("Crow Pose", "gif_crow_pose");
        exerciseGifMap.put("Headstand", "gif_headstand");
        exerciseGifMap.put("Full Splits", "gif_full_splits");
        exerciseGifMap.put("Backbend Flow", "gif_backbend_flow");
        exerciseGifMap.put("Breath of Fire", "gif_breath_of_fire");
        exerciseGifMap.put("Wim Hof Method", "gif_wim_hof_method");
    }

    /**
     * Add beginner level exercises (same as TutorialsActivity)
     */
    private void addBeginnerExercises() {
        List<Exercise> beginnerExercises = exercisesByDifficulty.get(DifficultyLevel.BEGINNER);

        // Cardio exercises
        beginnerExercises.add(createExercise("Marching in Place",
                "Simple stationary march to get your heart pumping",
                "March with high knees for 30 seconds, rest 10 seconds, repeat 5 times",
                DifficultyLevel.BEGINNER, WorkoutCategory.CARDIO, 5, 50));

        beginnerExercises.add(createExercise("Wall Push-Ups",
                "Modified push-ups against a wall for beginners",
                "Stand arm's length from wall, push and return. 3 sets of 10 reps",
                DifficultyLevel.BEGINNER, WorkoutCategory.STRENGTH, 8, 60));

        beginnerExercises.add(createExercise("Seated Leg Lifts",
                "Cardio workout you can do from a chair",
                "Lift alternating legs while seated. 2 sets of 20 per leg",
                DifficultyLevel.BEGINNER, WorkoutCategory.FLEXIBILITY, 6, 40));

        // Strength exercises
        beginnerExercises.add(createExercise("Chair Squats",
                "Build leg strength using a chair for support",
                "Lower down to chair, hover briefly, stand up. 2 sets of 12",
                DifficultyLevel.BEGINNER, WorkoutCategory.STRENGTH, 8, 70));

        beginnerExercises.add(createExercise("Wall Sits",
                "Static leg strengthening exercise",
                "Back against wall, slide down to sitting position. Hold for 30 seconds",
                DifficultyLevel.BEGINNER, WorkoutCategory.STRENGTH, 5, 50));

        beginnerExercises.add(createExercise("Modified Planks",
                "Core strengthening on knees",
                "Plank position on knees. Hold for 20 seconds, repeat 3 times",
                DifficultyLevel.BEGINNER, WorkoutCategory.STRENGTH, 6, 40));

        // Flexibility exercises
        beginnerExercises.add(createExercise("Neck Rolls",
                "Gentle neck and shoulder mobility",
                "Slow, controlled neck circles. 5 each direction",
                DifficultyLevel.BEGINNER, WorkoutCategory.FLEXIBILITY, 5, 30));

        beginnerExercises.add(createExercise("Shoulder Shrugs",
                "Release shoulder tension",
                "Lift shoulders to ears, hold 5 seconds, release. Repeat 10 times",
                DifficultyLevel.BEGINNER, WorkoutCategory.FLEXIBILITY, 4, 25));

        // Breathing exercises
        beginnerExercises.add(createExercise("Box Breathing",
                "Simple 4-count breathing pattern",
                "Inhale 4, hold 4, exhale 4, hold 4. Repeat for 5 minutes",
                DifficultyLevel.BEGINNER, WorkoutCategory.BREATHING, 5, 15));

        beginnerExercises.add(createExercise("Belly Breathing",
                "Deep diaphragmatic breathing",
                "Hand on chest, hand on belly. Breathe so only belly hand moves",
                DifficultyLevel.BEGINNER, WorkoutCategory.BREATHING, 6, 20));

        // Yoga exercises
        beginnerExercises.add(createExercise("Child's Pose",
                "Restorative rest and gentle stretch",
                "Kneel, sit back on heels, fold forward. Rest and breathe for 1-2 minutes",
                DifficultyLevel.BEGINNER, WorkoutCategory.YOGA, 5, 25));

        beginnerExercises.add(createExercise("Mountain Pose",
                "Foundation of all standing poses",
                "Stand tall, feet together, arms at sides. Focus on alignment for 1 minute",
                DifficultyLevel.BEGINNER, WorkoutCategory.YOGA, 4, 20));
    }

    /**
     * Add intermediate level exercises (same as TutorialsActivity)
     */
    private void addIntermediateExercises() {
        List<Exercise> intermediateExercises = exercisesByDifficulty.get(DifficultyLevel.INTERMEDIATE);

        // Cardio exercises
        intermediateExercises.add(createExercise("Jumping Jacks",
                "Classic full-body cardio movement",
                "Jump feet apart while raising arms overhead. 4 sets of 25 reps",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.CARDIO, 10, 100));

        intermediateExercises.add(createExercise("Step-Ups",
                "Use stairs or a sturdy platform for cardio",
                "Step up and down on platform. 3 sets of 15 per leg",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.CARDIO, 12, 120));

        intermediateExercises.add(createExercise("Dancing",
                "Put on your favorite song and dance!",
                "Dance freely to 3-4 songs. Let the music move you!",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.CARDIO, 15, 140));

        // Strength exercises
        intermediateExercises.add(createExercise("Push-Ups",
                "Classic upper body strength builder",
                "Full push-ups maintaining straight line. 3 sets of 10-15 reps",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.STRENGTH, 10, 90));

        intermediateExercises.add(createExercise("Bodyweight Squats",
                "Fundamental lower body exercise",
                "Deep squats with proper form. 3 sets of 15 reps",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.STRENGTH, 12, 100));

        intermediateExercises.add(createExercise("Lunges",
                "Single-leg strength and balance",
                "Alternating forward lunges. 2 sets of 12 per leg",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.STRENGTH, 10, 80));

        // HIIT exercises
        intermediateExercises.add(createExercise("Classic Tabata",
                "High-intensity 4-minute protocol",
                "20 seconds max effort, 10 seconds rest. 8 rounds of chosen exercise",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.HIIT, 15, 150));

        intermediateExercises.add(createExercise("EMOM Challenge",
                "Every minute on the minute",
                "Set number of reps each minute for 10 minutes. Rest remaining time",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.HIIT, 18, 180));

        // Yoga exercises
        intermediateExercises.add(createExercise("Sun Salutation A",
                "Dynamic flowing sequence",
                "Complete sun salutation sequence. Repeat 5 rounds with breath",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.YOGA, 12, 80));

        intermediateExercises.add(createExercise("Warrior II Flow",
                "Standing strength and focus",
                "Warrior II to extended side angle. Hold 45 seconds each side",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.YOGA, 10, 60));

        // Flexibility exercises
        intermediateExercises.add(createExercise("Cat-Cow Stretches",
                "Spinal mobility and flexibility",
                "On hands and knees, arch and round spine slowly. 15 reps",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.FLEXIBILITY, 8, 40));

        intermediateExercises.add(createExercise("Hip Flexor Stretch",
                "Open tight hip flexors",
                "Kneeling lunge position, lean forward gently. Hold 30 seconds each side",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.FLEXIBILITY, 10, 35));
    }

    /**
     * Add advanced level exercises (same as TutorialsActivity)
     */
    private void addAdvancedExercises() {
        List<Exercise> advancedExercises = exercisesByDifficulty.get(DifficultyLevel.ADVANCED);

        // Cardio exercises
        advancedExercises.add(createExercise("Burpees",
                "Ultimate full-body cardio challenge",
                "Squat, jump back to plank, push-up, jump forward, jump up. 3 sets of 10",
                DifficultyLevel.ADVANCED, WorkoutCategory.CARDIO, 15, 200));

        advancedExercises.add(createExercise("Mountain Climbers",
                "High-intensity core and cardio combo",
                "Plank position, alternate bringing knees to chest rapidly. 4 sets of 30 seconds",
                DifficultyLevel.ADVANCED, WorkoutCategory.CARDIO, 12, 150));

        // Strength exercises
        advancedExercises.add(createExercise("Single-Arm Push-Ups",
                "Ultimate upper body challenge",
                "Push-ups with one arm behind back. Work up to 5 per arm",
                DifficultyLevel.ADVANCED, WorkoutCategory.STRENGTH, 15, 180));

        advancedExercises.add(createExercise("Pistol Squats",
                "Single-leg squat mastery",
                "Single-leg squat to full depth. Assisted or full. 3 sets of 5 per leg",
                DifficultyLevel.ADVANCED, WorkoutCategory.STRENGTH, 18, 160));

        // HIIT exercises
        advancedExercises.add(createExercise("Death by Burpees",
                "Progressive intensity challenge",
                "Minute 1: 1 burpee, Minute 2: 2 burpees, etc. Go until failure",
                DifficultyLevel.ADVANCED, WorkoutCategory.HIIT, 20, 250));

        advancedExercises.add(createExercise("Fight Gone Bad",
                "Mixed modal high intensity",
                "5 exercises, 1 minute each, 1 minute rest. Repeat 3 rounds",
                DifficultyLevel.ADVANCED, WorkoutCategory.HIIT, 25, 300));

        // Yoga exercises
        advancedExercises.add(createExercise("Crow Pose",
                "Arm balance and core strength",
                "Balance on hands with knees on upper arms. Work up to 30 seconds",
                DifficultyLevel.ADVANCED, WorkoutCategory.YOGA, 15, 100));

        advancedExercises.add(createExercise("Headstand",
                "Inversion and full-body strength",
                "Supported headstand against wall. Build up to 2-3 minutes",
                DifficultyLevel.ADVANCED, WorkoutCategory.YOGA, 18, 120));

        // Flexibility exercises
        advancedExercises.add(createExercise("Full Splits",
                "Advanced hip and leg flexibility",
                "Work toward front or side splits. Hold comfortable edge for 1-2 minutes",
                DifficultyLevel.ADVANCED, WorkoutCategory.FLEXIBILITY, 15, 50));

        advancedExercises.add(createExercise("Backbend Flow",
                "Spinal extension and chest opening",
                "Bridge to wheel pose progression. Hold for 30 seconds each",
                DifficultyLevel.ADVANCED, WorkoutCategory.FLEXIBILITY, 12, 60));

        // Breathing exercises
        advancedExercises.add(createExercise("Breath of Fire",
                "Energizing rapid breathing",
                "Rapid, shallow breathing through nose. 30 breaths, 3 rounds",
                DifficultyLevel.ADVANCED, WorkoutCategory.BREATHING, 12, 50));

        advancedExercises.add(createExercise("Wim Hof Method",
                "Power breathing technique",
                "30 deep breaths, hold breath after exhale, repeat 3 rounds",
                DifficultyLevel.ADVANCED, WorkoutCategory.BREATHING, 15, 60));
    }

    /**
     * Helper method to create exercise objects with proper GIF mapping (same as TutorialsActivity)
     */
    private Exercise createExercise(String name, String description, String instructions,
                                    DifficultyLevel difficulty, WorkoutCategory category,
                                    int duration, int calories) {
        Exercise exercise = new Exercise(name, description, difficulty, category);
        exercise.setInstructions(instructions);
        exercise.setEstimatedDurationMinutes(duration);
        exercise.setEstimatedCalories(calories);

        // Use the mapped GIF name
        String gifName = getCorrectGifName(name);
        exercise.setImageUrl(gifName);

        return exercise;
    }

    /**
     * Get the correct GIF name for an exercise (same as TutorialsActivity)
     */
    private String getCorrectGifName(String exerciseName) {
        String mappedGif = exerciseGifMap.get(exerciseName);

        if (mappedGif != null) {
            android.util.Log.d("GIF_MAPPING", "✅ Found mapped GIF for '" + exerciseName + "': " + mappedGif);
            return mappedGif;
        } else {
            String generatedGif = generateGifName(exerciseName);
            android.util.Log.w("GIF_MAPPING", "⚠️ No mapping found for '" + exerciseName + "', using generated: " + generatedGif);
            return generatedGif;
        }
    }

    /**
     * Generate GIF filename from exercise name (fallback method)
     */
    private String generateGifName(String exerciseName) {
        return "gif_" + exerciseName.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "_")
                .replaceAll("_+", "_");
    }

    // ==================== MOOD-SPECIFIC EXERCISE SELECTION ====================

    /**
     * Generate workout recommendations based on selected mood using tutorial exercises
     */
    private void generateWorkoutRecommendations() {
        if (selectedMood == null) {
            recommendedExercises.clear();
            return;
        }

        DifficultyLevel userDifficulty = currentUser != null ?
                currentUser.getPreferredDifficulty() : DifficultyLevel.BEGINNER;

        // Get all available exercises for user's difficulty and adjacent levels
        List<Exercise> availableExercises = getAvailableExercisesForDifficulty(userDifficulty);

        // Filter exercises based on mood preferences
        List<Exercise> moodFilteredExercises = filterExercisesByMood(availableExercises, selectedMood);

        // Randomly select exercises for the workout
        recommendedExercises = selectRandomExercisesForWorkout(moodFilteredExercises, userDifficulty);

        android.util.Log.d(TAG, "Generated " + recommendedExercises.size() + " exercises for " + selectedMood.getDisplayName() + " mood");
    }

    /**
     * Get available exercises for user's difficulty level (including adjacent levels for variety)
     */
    private List<Exercise> getAvailableExercisesForDifficulty(DifficultyLevel userDifficulty) {
        List<Exercise> availableExercises = new ArrayList<>();

        // Primary difficulty level (70% weight)
        List<Exercise> primaryExercises = exercisesByDifficulty.get(userDifficulty);
        if (primaryExercises != null) {
            availableExercises.addAll(primaryExercises);
            availableExercises.addAll(primaryExercises); // Add twice for higher probability
        }

        // Add some exercises from adjacent difficulty levels for variety
        switch (userDifficulty) {
            case BEGINNER:
                // Add some intermediate exercises
                List<Exercise> intermediateExercises = exercisesByDifficulty.get(DifficultyLevel.INTERMEDIATE);
                if (intermediateExercises != null) {
                    // Add only first few intermediate exercises (easier ones)
                    int count = Math.min(4, intermediateExercises.size());
                    availableExercises.addAll(intermediateExercises.subList(0, count));
                }
                break;
            case INTERMEDIATE:
                // Add some beginner and advanced exercises
                List<Exercise> beginnerExercises = exercisesByDifficulty.get(DifficultyLevel.BEGINNER);
                List<Exercise> advancedExercises = exercisesByDifficulty.get(DifficultyLevel.ADVANCED);
                if (beginnerExercises != null) {
                    availableExercises.addAll(beginnerExercises.subList(0, Math.min(3, beginnerExercises.size())));
                }
                if (advancedExercises != null) {
                    availableExercises.addAll(advancedExercises.subList(0, Math.min(3, advancedExercises.size())));
                }
                break;
            case ADVANCED:
                // Add some intermediate exercises
                List<Exercise> intExercises = exercisesByDifficulty.get(DifficultyLevel.INTERMEDIATE);
                if (intExercises != null) {
                    availableExercises.addAll(intExercises);
                }
                break;
        }

        return availableExercises;
    }

    /**
     * Filter exercises based on mood preferences
     */
    private List<Exercise> filterExercisesByMood(List<Exercise> exercises, MoodType mood) {
        List<Exercise> filteredExercises = new ArrayList<>();

        for (Exercise exercise : exercises) {
            if (isExerciseSuitableForMood(exercise, mood)) {
                filteredExercises.add(exercise);
            }
        }

        // If filtered list is too small, add some general exercises
        if (filteredExercises.size() < MIN_EXERCISES_PER_WORKOUT) {
            for (Exercise exercise : exercises) {
                if (!filteredExercises.contains(exercise) && isGenerallyGoodExercise(exercise)) {
                    filteredExercises.add(exercise);
                    if (filteredExercises.size() >= MIN_EXERCISES_PER_WORKOUT * 2) break;
                }
            }
        }

        return filteredExercises;
    }

    /**
     * Check if exercise is suitable for specific mood
     */
    private boolean isExerciseSuitableForMood(Exercise exercise, MoodType mood) {
        String exerciseName = exercise.getName().toLowerCase();
        WorkoutCategory category = exercise.getCategory();

        switch (mood) {
            case HAPPY:
                // High-energy, fun exercises
                return category == WorkoutCategory.CARDIO ||
                        category == WorkoutCategory.HIIT ||
                        exerciseName.contains("jumping") ||
                        exerciseName.contains("dance") ||
                        exerciseName.contains("tabata") ||
                        exerciseName.contains("emom");

            case NEUTRAL:
                // Balanced exercises from all categories
                return category == WorkoutCategory.STRENGTH ||
                        category == WorkoutCategory.CARDIO ||
                        category == WorkoutCategory.FLEXIBILITY ||
                        exerciseName.contains("squat") ||
                        exerciseName.contains("push") ||
                        exerciseName.contains("stretch");

            case FRUSTRATED:
                // High-intensity, powerful exercises
                return category == WorkoutCategory.HIIT ||
                        category == WorkoutCategory.STRENGTH ||
                        exerciseName.contains("burpee") ||
                        exerciseName.contains("mountain") ||
                        exerciseName.contains("fight") ||
                        exerciseName.contains("death") ||
                        exerciseName.contains("push-up");

            case STRESSED:
                // Calming, restorative exercises
                return category == WorkoutCategory.YOGA ||
                        category == WorkoutCategory.BREATHING ||
                        category == WorkoutCategory.FLEXIBILITY ||
                        exerciseName.contains("breathing") ||
                        exerciseName.contains("child") ||
                        exerciseName.contains("stretch") ||
                        exerciseName.contains("yoga");

            default:
                return true;
        }
    }

    /**
     * Check if exercise is generally good (fallback for mood filtering)
     */
    private boolean isGenerallyGoodExercise(Exercise exercise) {
        // Avoid very advanced or very specialized exercises for general inclusion
        String name = exercise.getName().toLowerCase();
        return !name.contains("single-arm") &&
                !name.contains("pistol") &&
                !name.contains("death") &&
                !name.contains("fight") &&
                exercise.getDifficulty() != DifficultyLevel.ADVANCED;
    }

    /**
     * Randomly select exercises for a complete workout with smart variety
     */
    private List<Exercise> selectRandomExercisesForWorkout(List<Exercise> availableExercises, DifficultyLevel difficulty) {
        if (availableExercises.isEmpty()) {
            return new ArrayList<>();
        }

        // Determine workout size
        int exerciseCount = determineWorkoutSize(difficulty);

        // Shuffle available exercises
        Collections.shuffle(availableExercises, random);

        // Ensure variety by selecting from different categories
        List<Exercise> selectedExercises = ensureExerciseVariety(availableExercises, exerciseCount);

        android.util.Log.d(TAG, "Selected " + selectedExercises.size() + " exercises from " + availableExercises.size() + " available");

        return selectedExercises;
    }

    /**
     * Determine workout size based on difficulty and mood
     */
    private int determineWorkoutSize(DifficultyLevel difficulty) {
        int baseSize = random.nextInt(MAX_EXERCISES_PER_WORKOUT - MIN_EXERCISES_PER_WORKOUT + 1) + MIN_EXERCISES_PER_WORKOUT;

        // Adjust based on difficulty
        switch (difficulty) {
            case BEGINNER:
                return Math.max(MIN_EXERCISES_PER_WORKOUT, baseSize - 1);
            case INTERMEDIATE:
                return baseSize;
            case ADVANCED:
                return Math.min(MAX_EXERCISES_PER_WORKOUT, baseSize + 1);
            default:
                return baseSize;
        }
    }

    /**
     * Ensure variety in exercise selection by category
     */
    private List<Exercise> ensureExerciseVariety(List<Exercise> availableExercises, int targetCount) {
        List<Exercise> selectedExercises = new ArrayList<>();
        List<WorkoutCategory> usedCategories = new ArrayList<>();

        // First pass: select one from each category when possible
        for (Exercise exercise : availableExercises) {
            if (selectedExercises.size() >= targetCount) break;

            WorkoutCategory category = exercise.getCategory();
            if (!usedCategories.contains(category)) {
                selectedExercises.add(exercise);
                usedCategories.add(category);
            }
        }

        // Second pass: fill remaining slots
        for (Exercise exercise : availableExercises) {
            if (selectedExercises.size() >= targetCount) break;

            if (!selectedExercises.contains(exercise)) {
                selectedExercises.add(exercise);
            }
        }

        // Final shuffle for random order
        Collections.shuffle(selectedExercises, random);

        return selectedExercises;
    }

    // ==================== UI COMPONENTS (same as original) ====================

    /**
     * Initialize all UI components
     */
    private void initializeViews() {
        // Mood selection buttons
        btnMoodHappy = findViewById(R.id.btn_mood_happy);
        btnMoodNeutral = findViewById(R.id.btn_mood_neutral);
        btnMoodFrustrated = findViewById(R.id.btn_mood_frustrated);
        btnMoodStressed = findViewById(R.id.btn_mood_stressed);

        // Mood labels
        findMoodLabels();

        // Workout suggestion display
        tvWorkoutSuggestion = findViewById(R.id.tv_workout_suggestion);
        btnStartWorkout = findViewById(R.id.btn_start_workout);

        // Initially disable start button
        btnStartWorkout.setEnabled(false);
        btnStartWorkout.setAlpha(0.5f);
    }

    private void findMoodLabels() {
        android.view.ViewGroup moodContainer = (android.view.ViewGroup) btnMoodHappy.getParent().getParent();
        android.view.ViewGroup happyContainer = (android.view.ViewGroup) btnMoodHappy.getParent();
        android.view.ViewGroup neutralContainer = (android.view.ViewGroup) btnMoodNeutral.getParent();
        android.view.ViewGroup frustratedContainer = (android.view.ViewGroup) btnMoodFrustrated.getParent();
        android.view.ViewGroup stressedContainer = (android.view.ViewGroup) btnMoodStressed.getParent();

        tvMoodHappy = findTextViewInContainer(happyContainer);
        tvMoodNeutral = findTextViewInContainer(neutralContainer);
        tvMoodFrustrated = findTextViewInContainer(frustratedContainer);
        tvMoodStressed = findTextViewInContainer(stressedContainer);
    }

    private TextView findTextViewInContainer(android.view.ViewGroup container) {
        for (int i = 0; i < container.getChildCount(); i++) {
            android.view.View child = container.getChildAt(i);
            if (child instanceof TextView) {
                return (TextView) child;
            }
        }
        return null;
    }

    private void setupMoodSelectionListeners() {
        btnMoodHappy.setOnClickListener(v -> selectMood(MoodType.HAPPY));
        btnMoodNeutral.setOnClickListener(v -> selectMood(MoodType.NEUTRAL));
        btnMoodFrustrated.setOnClickListener(v -> selectMood(MoodType.FRUSTRATED));
        btnMoodStressed.setOnClickListener(v -> selectMood(MoodType.STRESSED));
    }

    private void setupWorkoutButtonListener() {
        btnStartWorkout.setOnClickListener(v -> startWorkout());
    }

    private void selectMood(MoodType mood) {
        selectedMood = mood;
        updateMoodButtonStates();
        generateWorkoutRecommendations();
        updateWorkoutSuggestion();
        enableStartWorkoutButton();
        performHapticFeedback();
        addSelectionBounceEffect();
    }

    private void addSelectionBounceEffect() {
        android.view.View moodContainer = (View) btnMoodHappy.getParent().getParent();
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

    private void updateMoodButtonStates() {
        resetMoodButtonStates();
        if (selectedMood != null) {
            ImageButton selectedButton = getMoodButton(selectedMood);
            if (selectedButton != null) {
                highlightMoodButton(selectedButton, selectedMood);
            }
        }
    }

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

        for (TextView label : labels) {
            if (label != null) {
                label.setTextColor(getResources().getColor(R.color.text_secondary));
                label.setAlpha(0.7f);
                label.setScaleX(1.0f);
                label.setScaleY(1.0f);
            }
        }
    }

    private ImageButton getMoodButton(MoodType mood) {
        switch (mood) {
            case HAPPY: return btnMoodHappy;
            case NEUTRAL: return btnMoodNeutral;
            case FRUSTRATED: return btnMoodFrustrated;
            case STRESSED: return btnMoodStressed;
            default: return null;
        }
    }

    private TextView getMoodLabel(MoodType mood) {
        switch (mood) {
            case HAPPY: return tvMoodHappy;
            case NEUTRAL: return tvMoodNeutral;
            case FRUSTRATED: return tvMoodFrustrated;
            case STRESSED: return tvMoodStressed;
            default: return null;
        }
    }

    private void highlightMoodButton(ImageButton button, MoodType mood) {
        if (button == null) return;

        int color = Color.parseColor(mood.getColorHex());
        android.graphics.drawable.GradientDrawable background = new android.graphics.drawable.GradientDrawable();
        background.setShape(android.graphics.drawable.GradientDrawable.OVAL);
        background.setColor(color);
        background.setAlpha(30);
        background.setStroke(4, color);

        button.setBackground(background);
        button.animate()
                .scaleX(1.15f)
                .scaleY(1.15f)
                .alpha(1.0f)
                .setDuration(200)
                .start();
        button.setElevation(8f);

        TextView label = getMoodLabel(mood);
        if (label != null) {
            highlightMoodLabel(label, mood);
        }
    }

    private void highlightMoodLabel(TextView label, MoodType mood) {
        if (label == null) return;

        int color = Color.parseColor(mood.getColorHex());
        label.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .alpha(1.0f)
                .setDuration(200)
                .start();

        label.setTextColor(color);
        android.graphics.drawable.GradientDrawable labelBackground = new android.graphics.drawable.GradientDrawable();
        labelBackground.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
        labelBackground.setColor(color);
        labelBackground.setAlpha(20);
        labelBackground.setCornerRadius(12f);

        label.setBackground(labelBackground);
        label.setPadding(12, 4, 12, 4);
        addPulseEffect(label);
    }

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
     * Update workout suggestion display based on selected mood and tutorial exercises
     */
    private void updateWorkoutSuggestion() {
        if (selectedMood == null || recommendedExercises.isEmpty()) {
            tvWorkoutSuggestion.setText("Select your mood to get a personalized workout with " + MIN_EXERCISES_PER_WORKOUT + "-" + MAX_EXERCISES_PER_WORKOUT + " exercises from our tutorial library!");
            resetWorkoutSuggestionCard();
            return;
        }

        String moodMessage = getMoodSpecificMessage(selectedMood);
        int totalDuration = calculateTotalWorkoutDuration();
        int totalCalories = calculateTotalCalories();

        String suggestionText = moodMessage + "\n\n" +
                "🎯 Your Custom Workout:\n" +
                "📚 " + recommendedExercises.size() + " exercises from tutorial library\n" +
                "⏱️ " + totalDuration + " minutes total\n" +
                "🔥 ~" + totalCalories + " calories\n\n" +
                "✨ Categories: " + getExerciseCategorySummary();

        tvWorkoutSuggestion.setText(suggestionText);
        animateWorkoutSuggestionCard();
    }

    private int calculateTotalWorkoutDuration() {
        int total = 0;
        for (Exercise exercise : recommendedExercises) {
            total += exercise.getEstimatedDurationMinutes();
        }
        return total;
    }

    private int calculateTotalCalories() {
        int total = 0;
        for (Exercise exercise : recommendedExercises) {
            total += exercise.getEstimatedCalories();
        }
        return total;
    }

    private String getExerciseCategorySummary() {
        List<String> categories = new ArrayList<>();
        for (Exercise exercise : recommendedExercises) {
            String category = exercise.getCategory().getDisplayName();
            if (!categories.contains(category)) {
                categories.add(category);
            }
        }

        if (categories.size() <= 2) {
            return String.join(" & ", categories);
        } else {
            return categories.get(0) + ", " + categories.get(1) + " & more";
        }
    }

    private void animateWorkoutSuggestionCard() {
        android.view.View cardView = (View) tvWorkoutSuggestion.getParent().getParent();
        int color = Color.parseColor(selectedMood.getColorHex());

        if (cardView instanceof androidx.cardview.widget.CardView) {
            androidx.cardview.widget.CardView card = (androidx.cardview.widget.CardView) cardView;

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

            card.setCardBackgroundColor(mixColors(getResources().getColor(R.color.card_background), color, 0.05f));
        }
    }

    private void resetWorkoutSuggestionCard() {
        android.view.View cardView = (View) tvWorkoutSuggestion.getParent().getParent();
        if (cardView instanceof androidx.cardview.widget.CardView) {
            androidx.cardview.widget.CardView card = (androidx.cardview.widget.CardView) cardView;
            card.setCardBackgroundColor(getResources().getColor(R.color.card_background));
        }
    }

    private int mixColors(int color1, int color2, float ratio) {
        final float inverseRatio = 1f - ratio;
        int r = (int) ((Color.red(color1) * inverseRatio) + (Color.red(color2) * ratio));
        int g = (int) ((Color.green(color1) * inverseRatio) + (Color.green(color2) * ratio));
        int b = (int) ((Color.blue(color1) * inverseRatio) + (Color.blue(color2) * ratio));
        return Color.rgb(r, g, b);
    }

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

    private void enableStartWorkoutButton() {
        if (selectedMood != null && !recommendedExercises.isEmpty()) {
            btnStartWorkout.setEnabled(true);
            btnStartWorkout.setAlpha(1.0f);

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

    private void startWorkout() {
        if (selectedMood == null || recommendedExercises.isEmpty()) {
            return;
        }

        workoutStartTime = System.currentTimeMillis();

        try {
            String userId = currentUser != null ? currentUser.getUserId() : "anonymous";
            currentSession = new WorkoutSession(userId, selectedMood);

            for (Exercise exercise : recommendedExercises) {
                currentSession.addExercise(exercise);
            }

            currentSession.startWorkout();

            Intent demoIntent = new Intent(this, ExerciseDemoActivity.class);
            demoIntent.putExtra("mood_type", selectedMood.name());
            demoIntent.putExtra("session_id", currentSession.getSessionId());
            demoIntent.putExtra("current_exercise_index", 0);
            demoIntent.putExtra("total_exercises", recommendedExercises.size());
            demoIntent.putExtra("estimated_duration", calculateTotalWorkoutDuration());

            Exercise firstExercise = recommendedExercises.get(0);
            demoIntent.putExtra("exercise_name", firstExercise.getName());
            demoIntent.putExtra("exercise_description", firstExercise.getDescription());
            demoIntent.putExtra("exercise_instructions", firstExercise.getInstructions());
            demoIntent.putExtra("exercise_duration", firstExercise.getEstimatedDurationMinutes());
            demoIntent.putExtra("exercise_calories", firstExercise.getEstimatedCalories());
            demoIntent.putExtra("exercise_category", firstExercise.getCategory().getDisplayName());
            demoIntent.putExtra("exercise_difficulty", firstExercise.getDifficulty().getDisplayName());
            demoIntent.putExtra("exercise_gif", firstExercise.getImageUrl());

            startActivityForResult(demoIntent, 1001);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        } catch (Exception e) {
            android.util.Log.e(TAG, "Error starting workout", e);
            showError("Unable to start workout. Please try again.");
        }
    }

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
            android.util.Log.w(TAG, "Haptic feedback failed", e);
        }
    }

    private void showError(String message) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show();
    }

    // ==================== Activity Results and Navigation (same as original) ====================

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001) {
            if (resultCode == RESULT_OK && data != null) {
                String action = data.getStringExtra("action");
                totalExercisesCompleted = data.getIntExtra("exercises_completed", totalExercisesCompleted);
                totalCaloriesBurned = data.getIntExtra("calories_burned", totalCaloriesBurned);
                anyExercisesSkipped = data.getBooleanExtra("exercises_skipped", anyExercisesSkipped);

                if ("next_exercise".equals(action)) {
                    int nextExerciseIndex = data.getIntExtra("current_exercise_index", 0);
                    navigateToNextExercise(nextExerciseIndex);
                } else if ("workout_complete".equals(action)) {
                    completeWorkoutSession(data);
                    setResult(RESULT_OK);
                    showCompletionMessage();
                    finish();
                }
            } else if (resultCode == RESULT_CANCELED) {
                handleExerciseDemoReturn(data);
            }
        }
    }

    private void handleExerciseDemoReturn(Intent data) {
        if (data != null) {
            String action = data.getStringExtra("action");
            if ("exercise_cancelled".equals(action)) {
                showExerciseCancelledFeedback();
            } else {
                showReturnFromDemoFeedback();
            }
        } else {
            showReturnFromDemoFeedback();
        }
        enableStartWorkoutButton();
    }

    private void showExerciseCancelledFeedback() {
        android.widget.Toast.makeText(this,
                "No worries! You can try the exercise again or choose a different mood.",
                android.widget.Toast.LENGTH_LONG).show();

        if (selectedMood != null) {
            ImageButton selectedButton = getMoodButton(selectedMood);
            if (selectedButton != null) {
                selectedButton.animate()
                        .scaleX(1.1f)
                        .scaleY(1.1f)
                        .setDuration(300)
                        .withEndAction(() -> {
                            selectedButton.animate()
                                    .scaleX(1.15f)
                                    .scaleY(1.15f)
                                    .setDuration(300)
                                    .start();
                        })
                        .start();
            }
        }
    }

    private void showReturnFromDemoFeedback() {
        android.widget.Toast.makeText(this,
                "Ready to start your workout?",
                android.widget.Toast.LENGTH_SHORT).show();
    }

    private void navigateToNextExercise(int exerciseIndex) {
        if (exerciseIndex >= recommendedExercises.size()) {
            completeWorkoutSession(null);
            setResult(RESULT_OK);
            showCompletionMessage();
            finish();
            return;
        }

        Exercise nextExercise = recommendedExercises.get(exerciseIndex);

        Intent demoIntent = new Intent(this, ExerciseDemoActivity.class);
        demoIntent.putExtra("mood_type", selectedMood.name());
        demoIntent.putExtra("session_id", currentSession.getSessionId());
        demoIntent.putExtra("current_exercise_index", exerciseIndex);
        demoIntent.putExtra("total_exercises", recommendedExercises.size());
        demoIntent.putExtra("estimated_duration", calculateTotalWorkoutDuration());

        demoIntent.putExtra("exercise_name", nextExercise.getName());
        demoIntent.putExtra("exercise_description", nextExercise.getDescription());
        demoIntent.putExtra("exercise_instructions", nextExercise.getInstructions());
        demoIntent.putExtra("exercise_duration", nextExercise.getEstimatedDurationMinutes());
        demoIntent.putExtra("exercise_calories", nextExercise.getEstimatedCalories());
        demoIntent.putExtra("exercise_category", nextExercise.getCategory().getDisplayName());
        demoIntent.putExtra("exercise_difficulty", nextExercise.getDifficulty().getDisplayName());
        demoIntent.putExtra("exercise_gif", nextExercise.getImageUrl());

        startActivityForResult(demoIntent, 1001);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void completeWorkoutSession(Intent data) {
        if (currentSession == null) return;

        try {
            currentSession.endWorkout();

            long workoutEndTime = System.currentTimeMillis();
            int actualDurationMinutes = (int) ((workoutEndTime - workoutStartTime) / (1000 * 60));
            if (actualDurationMinutes < 1) {
                actualDurationMinutes = 1;
            }

            if (data != null && data.hasExtra("actual_duration")) {
                actualDurationMinutes = data.getIntExtra("actual_duration", actualDurationMinutes);
            }

            currentSession.setDurationMinutes(actualDurationMinutes);
            dataManager.recordWorkoutCompletion(currentSession);

            Intent completionIntent = new Intent(this, WorkoutCompleteActivity.class);
            completionIntent.putExtra("session_id", currentSession.getSessionId());
            completionIntent.putExtra("mood_type", selectedMood.name());
            completionIntent.putExtra("total_exercises", recommendedExercises.size());
            completionIntent.putExtra("estimated_duration", calculateTotalWorkoutDuration());
            completionIntent.putExtra("actual_duration", actualDurationMinutes);
            completionIntent.putExtra("exercises_completed", totalExercisesCompleted);
            completionIntent.putExtra("calories_burned", totalCaloriesBurned);
            completionIntent.putExtra("exercises_skipped", anyExercisesSkipped);

            startActivity(completionIntent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();

        } catch (Exception e) {
            android.util.Log.e(TAG, "Error completing workout session", e);
        }
    }

    private void showCompletionMessage() {
        String message = "Amazing! You've completed your " +
                selectedMood.getDisplayName().toLowerCase() +
                " mood workout with " + recommendedExercises.size() + " tutorial exercises! 🎉";
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (currentSession != null && currentSession.isActive()) {
            currentSession = null;
        }
    }
}