package com.example.moodfit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import com.example.moodfit.R;
import com.example.moodfit.models.Exercise;
import com.example.moodfit.models.enums.DifficultyLevel;
import com.example.moodfit.models.enums.WorkoutCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TutorialsActivity - Exercise library showing all exercises categorized by difficulty
 * Provides educational content and exercise demonstrations
 * UPDATED: Fixed GIF mapping issues and enhanced debugging
 */
public class TutorialsActivity extends AppCompatActivity {

    private static final String TAG = "TutorialsActivity";

    // UI Components
    private NestedScrollView scrollView;
    private LinearLayout contentContainer;

    // Difficulty Section Headers
    private CardView beginnerHeader;
    private CardView intermediateHeader;
    private CardView advancedHeader;

    // Exercise Containers
    private LinearLayout beginnerExercisesContainer;
    private LinearLayout intermediateExercisesContainer;
    private LinearLayout advancedExercisesContainer;

    // Expand/Collapse states
    private boolean beginnerExpanded = true;
    private boolean intermediateExpanded = false;
    private boolean advancedExpanded = false;

    // Exercise Database
    private Map<DifficultyLevel, List<Exercise>> exercisesByDifficulty;

    // ADDED: GIF mapping system
    private Map<String, String> exerciseGifMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorials);

        // Initialize UI components
        initializeViews();

        // Build exercise database
        buildExerciseDatabase();

        // Setup event listeners
        setupEventListeners();

        // Populate exercise lists
        populateExerciseLists();

        // Setup initial state
        updateSectionVisibility();

        // DEBUG: Enable this to debug GIF mapping issues (disable in production)
        debugGifMappingIssues();
    }

    /**
     * Initialize all UI components
     */
    private void initializeViews() {
        scrollView = findViewById(R.id.scroll_view);
        contentContainer = findViewById(R.id.content_container);

        // Difficulty headers
        beginnerHeader = findViewById(R.id.beginner_header);
        intermediateHeader = findViewById(R.id.intermediate_header);
        advancedHeader = findViewById(R.id.advanced_header);

        // Exercise containers
        beginnerExercisesContainer = findViewById(R.id.beginner_exercises_container);
        intermediateExercisesContainer = findViewById(R.id.intermediate_exercises_container);
        advancedExercisesContainer = findViewById(R.id.advanced_exercises_container);
    }

    /**
     * Build comprehensive exercise database with proper GIF mapping
     */
    private void buildExerciseDatabase() {
        exercisesByDifficulty = new HashMap<>();

        // Initialize gif mapping first
        initializeExerciseGifMapping();

        // Initialize lists for each difficulty
        exercisesByDifficulty.put(DifficultyLevel.BEGINNER, new ArrayList<>());
        exercisesByDifficulty.put(DifficultyLevel.INTERMEDIATE, new ArrayList<>());
        exercisesByDifficulty.put(DifficultyLevel.ADVANCED, new ArrayList<>());

        // Add exercises for each difficulty level
        addBeginnerExercises();
        addIntermediateExercises();
        addAdvancedExercises();
    }

    /**
     * Initialize proper mapping between exercise names and their actual GIF filenames
     * UPDATE THIS MAP TO MATCH YOUR ACTUAL GIF FILES
     */
    private void initializeExerciseGifMapping() {
        exerciseGifMap = new HashMap<>();

        // BEGINNER EXERCISES - Map exact exercise names to actual GIF files
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

        // TODO: Update these mappings to match your actual GIF file names
        // Run the app and check the logs to see which files exist
    }

    /**
     * Add beginner level exercises
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
     * Add intermediate level exercises
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
     * Add advanced level exercises
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
     * Helper method to create exercise objects with proper GIF mapping
     */
    private Exercise createExercise(String name, String description, String instructions,
                                    DifficultyLevel difficulty, WorkoutCategory category,
                                    int duration, int calories) {
        Exercise exercise = new Exercise(name, description, difficulty, category);
        exercise.setInstructions(instructions);
        exercise.setEstimatedDurationMinutes(duration);
        exercise.setEstimatedCalories(calories);

        // Use the mapped GIF name, or generate one if not found
        String gifName = getCorrectGifName(name);
        exercise.setImageUrl(gifName);

        return exercise;
    }

    /**
     * Get the correct GIF name for an exercise
     */
    private String getCorrectGifName(String exerciseName) {
        // First, try to get from our mapping
        String mappedGif = exerciseGifMap.get(exerciseName);

        if (mappedGif != null) {
            android.util.Log.d("GIF_MAPPING", "✅ Found mapped GIF for '" + exerciseName + "': " + mappedGif);
            return mappedGif;
        } else {
            // Fallback to generated name
            String generatedGif = generateGifName(exerciseName);
            android.util.Log.w("GIF_MAPPING", "⚠️ No mapping found for '" + exerciseName + "', using generated: " + generatedGif);
            return generatedGif;
        }
    }

    /**
     * Generate GIF filename from exercise name (fallback method)
     */
    private String generateGifName(String exerciseName) {
        // Convert exercise name to lowercase and replace spaces with underscores
        return "gif_" + exerciseName.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "") // Remove special characters
                .replaceAll("\\s+", "_") // Replace spaces with underscores
                .replaceAll("_+", "_"); // Replace multiple underscores with single
    }

    /**
     * Setup event listeners for expand/collapse functionality
     */
    private void setupEventListeners() {
        beginnerHeader.setOnClickListener(v -> toggleSection(DifficultyLevel.BEGINNER));
        intermediateHeader.setOnClickListener(v -> toggleSection(DifficultyLevel.INTERMEDIATE));
        advancedHeader.setOnClickListener(v -> toggleSection(DifficultyLevel.ADVANCED));
    }

    /**
     * Toggle section expand/collapse
     */
    private void toggleSection(DifficultyLevel difficulty) {
        switch (difficulty) {
            case BEGINNER:
                beginnerExpanded = !beginnerExpanded;
                break;
            case INTERMEDIATE:
                intermediateExpanded = !intermediateExpanded;
                break;
            case ADVANCED:
                advancedExpanded = !advancedExpanded;
                break;
        }
        updateSectionVisibility();
        animateSectionToggle(difficulty);
    }

    /**
     * Update section visibility based on expand/collapse state
     */
    private void updateSectionVisibility() {
        beginnerExercisesContainer.setVisibility(beginnerExpanded ? View.VISIBLE : View.GONE);
        intermediateExercisesContainer.setVisibility(intermediateExpanded ? View.VISIBLE : View.GONE);
        advancedExercisesContainer.setVisibility(advancedExpanded ? View.VISIBLE : View.GONE);

        // Update chevron icons
        updateChevronIcon(beginnerHeader, beginnerExpanded);
        updateChevronIcon(intermediateHeader, intermediateExpanded);
        updateChevronIcon(advancedHeader, advancedExpanded);
    }

    /**
     * Update chevron icon for expand/collapse state
     */
    private void updateChevronIcon(CardView header, boolean expanded) {
        ImageView chevron = null;

        // Find the correct chevron based on the header
        if (header == beginnerHeader) {
            chevron = header.findViewById(R.id.chevron_icon_beginner);
        } else if (header == intermediateHeader) {
            chevron = header.findViewById(R.id.chevron_icon_intermediate);
        } else if (header == advancedHeader) {
            chevron = header.findViewById(R.id.chevron_icon_advanced);
        }

        if (chevron != null) {
            chevron.setRotation(expanded ? 90f : 0f);
        }
    }

    /**
     * Animate section toggle
     */
    private void animateSectionToggle(DifficultyLevel difficulty) {
        CardView header = null;
        switch (difficulty) {
            case BEGINNER: header = beginnerHeader; break;
            case INTERMEDIATE: header = intermediateHeader; break;
            case ADVANCED: header = advancedHeader; break;
        }

        if (header != null) {
            CardView finalHeader = header;
            header.animate()
                    .scaleX(1.02f)
                    .scaleY(1.02f)
                    .setDuration(100)
                    .withEndAction(() -> {
                        finalHeader.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .setDuration(100)
                                .start();
                    })
                    .start();
        }
    }

    /**
     * Populate exercise lists in the UI
     */
    private void populateExerciseLists() {
        populateExerciseContainer(beginnerExercisesContainer, DifficultyLevel.BEGINNER);
        populateExerciseContainer(intermediateExercisesContainer, DifficultyLevel.INTERMEDIATE);
        populateExerciseContainer(advancedExercisesContainer, DifficultyLevel.ADVANCED);
    }

    /**
     * Populate specific exercise container
     */
    private void populateExerciseContainer(LinearLayout container, DifficultyLevel difficulty) {
        List<Exercise> exercises = exercisesByDifficulty.get(difficulty);
        if (exercises == null) return;

        container.removeAllViews();

        for (Exercise exercise : exercises) {
            View exerciseCard = createExerciseCard(exercise);
            container.addView(exerciseCard);
        }
    }

    /**
     * Create exercise card view - UPDATED with enhanced GIF loading
     */
    private View createExerciseCard(Exercise exercise) {
        View cardView = getLayoutInflater().inflate(R.layout.item_exercise_card, null);

        // Find views in the card
        TextView tvExerciseName = cardView.findViewById(R.id.tv_exercise_name);
        TextView tvExerciseDescription = cardView.findViewById(R.id.tv_exercise_description);
        TextView tvExerciseDuration = cardView.findViewById(R.id.tv_exercise_duration);
        TextView tvExerciseCalories = cardView.findViewById(R.id.tv_exercise_calories);
        TextView tvExerciseCategory = cardView.findViewById(R.id.tv_exercise_category);
        ImageView ivExerciseGif = cardView.findViewById(R.id.iv_exercise_gif);
        CardView exerciseCardContainer = cardView.findViewById(R.id.exercise_card_container);

        // Set exercise data with null checks
        if (tvExerciseName != null) {
            tvExerciseName.setText(exercise.getName());
        }
        if (tvExerciseDescription != null) {
            tvExerciseDescription.setText(exercise.getDescription());
        }
        if (tvExerciseDuration != null) {
            tvExerciseDuration.setText(exercise.getEstimatedDurationMinutes() + " min");
        }
        if (tvExerciseCalories != null) {
            tvExerciseCalories.setText("~" + exercise.getEstimatedCalories() + " cal");
        }
        if (tvExerciseCategory != null) {
            tvExerciseCategory.setText(exercise.getCategory().getDisplayName());
        }

        // Load exercise GIF with enhanced error handling
        if (ivExerciseGif != null) {
            loadExerciseGif(ivExerciseGif, exercise);
        }

        // Set click listener to view exercise details
        if (exerciseCardContainer != null) {
            exerciseCardContainer.setOnClickListener(v -> showExerciseDetails(exercise));
        }

        // Set margin
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 16);
        cardView.setLayoutParams(params);

        return cardView;
    }

    /**
     * Enhanced GIF loading with better error handling and mapping
     */
    private void loadExerciseGif(ImageView gifView, Exercise exercise) {
        String gifName = exercise.getImageUrl(); // GIF filename from mapping

        // DEBUG: Enhanced logging
        android.util.Log.d("GIF_DEBUG", "=== Loading GIF for Exercise ===");
        android.util.Log.d("GIF_DEBUG", "Exercise: " + exercise.getName());
        android.util.Log.d("GIF_DEBUG", "Expected GIF: " + gifName);

        if (gifName != null && !gifName.isEmpty()) {
            // Try to load GIF from drawable resources
            int gifResourceId = getResources().getIdentifier(gifName, "drawable", getPackageName());

            android.util.Log.d("GIF_DEBUG", "Resource ID: " + gifResourceId + " (0 means not found)");

            if (gifResourceId != 0) {
                // GIF exists, load it with Glide for animation
                Glide.with(this)
                        .asGif() // Explicitly load as GIF
                        .load(gifResourceId)
                        .apply(new RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.default_exercise_demo)
                                .error(R.drawable.default_exercise_demo)
                                .centerCrop()) // Fit nicely in the card
                        .into(gifView);

                gifView.setVisibility(View.VISIBLE);
                android.util.Log.d("GIF_DEBUG", "✅ Animated GIF loaded successfully!");
            } else {
                // GIF doesn't exist, try alternative approaches
                android.util.Log.w("GIF_DEBUG", "❌ GIF not found: " + gifName);

                // Try without "gif_" prefix
                String alternativeGifName = gifName.replace("gif_", "");
                int altResourceId = getResources().getIdentifier(alternativeGifName, "drawable", getPackageName());

                if (altResourceId != 0) {
                    android.util.Log.d("GIF_DEBUG", "✅ Found alternative GIF: " + alternativeGifName);
                    loadGifResource(gifView, altResourceId);
                } else {
                    // Try common alternative names
                    tryAlternativeGifNames(gifView, exercise.getName());
                }
            }
        } else {
            android.util.Log.d("GIF_DEBUG", "❌ No GIF name provided");
            showGifPlaceholder(gifView, exercise);
        }
    }

    /**
     * Try alternative GIF names for common variations
     */
    private void tryAlternativeGifNames(ImageView gifView, String exerciseName) {
        // Common alternative naming patterns
        String[] alternativePatterns = {
                exerciseName.toLowerCase().replaceAll("[^a-z0-9]", ""), // Remove all special chars
                exerciseName.toLowerCase().replaceAll("\\s+", "_"), // Simple underscore replacement
                exerciseName.toLowerCase().replaceAll("[^a-z0-9\\s]", "").replaceAll("\\s+", "_"), // Your current pattern
                "exercise_" + exerciseName.toLowerCase().replaceAll("\\s+", "_"),
                "demo_" + exerciseName.toLowerCase().replaceAll("\\s+", "_")
        };

        for (String pattern : alternativePatterns) {
            int resourceId = getResources().getIdentifier(pattern, "drawable", getPackageName());
            if (resourceId != 0) {
                android.util.Log.d("GIF_DEBUG", "✅ Found alternative pattern: " + pattern);
                loadGifResource(gifView, resourceId);
                return;
            }
        }

        // If no alternatives found, show placeholder
        android.util.Log.w("GIF_DEBUG", "❌ No alternative GIFs found for: " + exerciseName);
        showGifPlaceholder(gifView, exerciseName);
    }

    /**
     * Load GIF resource with Glide
     */
    private void loadGifResource(ImageView gifView, int resourceId) {
        Glide.with(this)
                .asGif()
                .load(resourceId)
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.default_exercise_demo)
                        .error(R.drawable.default_exercise_demo)
                        .centerCrop())
                .into(gifView);

        gifView.setVisibility(View.VISIBLE);
    }

    /**
     * Enhanced placeholder with exercise name for debugging
     */
    private void showGifPlaceholder(ImageView gifView, String exerciseName) {
        // Load placeholder with Glide for consistency
        Glide.with(this)
                .load(R.drawable.default_exercise_demo)
                .apply(new RequestOptions().centerCrop())
                .into(gifView);

        gifView.setVisibility(View.VISIBLE);
        gifView.setAlpha(0.7f);

        android.util.Log.i("GIF_PLACEHOLDER", "Showing placeholder for: " + exerciseName);
    }

    /**
     * Enhanced placeholder (overloaded method)
     */
    private void showGifPlaceholder(ImageView gifView, Exercise exercise) {
        showGifPlaceholder(gifView, exercise.getName());
    }

    /**
     * Show exercise details - UPDATED to pass GIF name correctly
     */
    private void showExerciseDetails(Exercise exercise) {
        Intent detailIntent = new Intent(this, ExerciseDemoActivity.class);

        // Pass exercise data
        detailIntent.putExtra("exercise_name", exercise.getName());
        detailIntent.putExtra("exercise_description", exercise.getDescription());
        detailIntent.putExtra("exercise_instructions", exercise.getInstructions());
        detailIntent.putExtra("exercise_duration", exercise.getEstimatedDurationMinutes());
        detailIntent.putExtra("exercise_calories", exercise.getEstimatedCalories());
        detailIntent.putExtra("exercise_category", exercise.getCategory().getDisplayName());
        detailIntent.putExtra("exercise_difficulty", exercise.getDifficulty().getDisplayName());

        // UPDATED: Pass GIF name - This is the key fix!
        detailIntent.putExtra("exercise_gif", exercise.getImageUrl()); // This contains the GIF filename

        // Set as tutorial mode (not part of workout session)
        detailIntent.putExtra("tutorial_mode", true);
        detailIntent.putExtra("current_exercise_index", 0);
        detailIntent.putExtra("total_exercises", 1);

        startActivity(detailIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    /**
     * Handle back button press
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    // ===============================
    // DEBUG METHODS (Remove in production)
    // ===============================

    /**
     * Debug method to identify GIF mapping issues
     * DISABLE THIS IN PRODUCTION by commenting out the call in onCreate()
     */
    private void debugGifMappingIssues() {
        android.util.Log.d("GIF_DEBUG", "=== DEBUGGING GIF MAPPING ISSUES ===");

        // Test the problematic exercises specifically
        String[] problematicExercises = {
                "Marching in Place",
                "Modified Planks",
                "Wall Push-Ups",
                "Chair Squats",
                "Jumping Jacks",
                "Burpees"
        };

        for (String exerciseName : problematicExercises) {
            android.util.Log.d("GIF_DEBUG", "\n--- Testing: " + exerciseName + " ---");

            // 1. Check what your current mapping generates
            String currentGifName = getCorrectGifName(exerciseName);
            android.util.Log.d("GIF_DEBUG", "Current mapping result: " + currentGifName);

            // 2. Check if that resource exists
            int resourceId = getResources().getIdentifier(currentGifName, "drawable", getPackageName());
            android.util.Log.d("GIF_DEBUG", "Resource exists: " + (resourceId != 0) + " (ID: " + resourceId + ")");

            // 3. Try alternative names
            String[] alternatives = {
                    exerciseName.toLowerCase().replaceAll("\\s+", "_"),
                    exerciseName.toLowerCase().replaceAll("[^a-z0-9]", ""),
                    "gif_" + exerciseName.toLowerCase().replaceAll("\\s+", "_"),
                    "exercise_" + exerciseName.toLowerCase().replaceAll("\\s+", "_"),
                    "demo_" + exerciseName.toLowerCase().replaceAll("\\s+", "_")
            };

            android.util.Log.d("GIF_DEBUG", "Trying alternatives:");
            for (String alt : alternatives) {
                int altId = getResources().getIdentifier(alt, "drawable", getPackageName());
                android.util.Log.d("GIF_DEBUG", "  " + alt + ": " + (altId != 0 ? "✅ EXISTS" : "❌ missing"));
            }
        }

        android.util.Log.d("GIF_DEBUG", "=== END DEBUG ===");
    }

    /**
     * Utility method to list all available GIF resources (for debugging)
     * Uncomment the call in onCreate() to use this
     */
    private void listAvailableGifs() {
        android.util.Log.d("AVAILABLE_GIFS", "=== Scanning for available GIF resources ===");

        try {
            java.lang.reflect.Field[] fields = R.drawable.class.getFields();

            for (java.lang.reflect.Field field : fields) {
                String resourceName = field.getName();

                // Look for files that might be GIFs
                if (resourceName.contains("gif") ||
                        resourceName.contains("anim") ||
                        resourceName.contains("exercise") ||
                        resourceName.contains("demo") ||
                        resourceName.contains("workout")) {

                    android.util.Log.d("AVAILABLE_GIFS", "Found potential GIF: " + resourceName);
                }
            }

        } catch (Exception e) {
            android.util.Log.e("AVAILABLE_GIFS", "Error scanning for GIF resources", e);
        }
    }
}