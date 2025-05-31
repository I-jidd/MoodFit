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
        addFunAndCreativeExercises();
    }

    /**
     * EXPANDED EXERCISE DATABASE - Replace the existing exercise methods in RandomExerciseActivity.java
     * This adds 50+ new exercises across all categories and difficulty levels
     */

    /**
     * Add cardio exercises to database - EXPANDED with 15+ new exercises
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

        // NEW BEGINNER CARDIO
        addExercise("Arm Circles", "Gentle shoulder warm-up and light cardio",
                DifficultyLevel.BEGINNER, WorkoutCategory.CARDIO, 4, 30,
                "Extend arms to sides, make small circles forward 30 seconds, backward 30 seconds");

        addExercise("Heel-Toe Walk", "Balance and coordination cardio",
                DifficultyLevel.BEGINNER, WorkoutCategory.CARDIO, 6, 35,
                "Walk in straight line placing heel directly in front of toe. 10 steps forward, 10 back, repeat");

        addExercise("Gentle Bouncing", "Low-impact rhythm cardio",
                DifficultyLevel.BEGINNER, WorkoutCategory.CARDIO, 5, 45,
                "Bounce gently on balls of feet to music. Keep it light and fun for 3 minutes");

        addExercise("Side Steps", "Lateral movement cardio",
                DifficultyLevel.BEGINNER, WorkoutCategory.CARDIO, 6, 50,
                "Step side to side, adding arm swings. 30 seconds right, 30 seconds left, repeat 5 times");

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

        // NEW INTERMEDIATE CARDIO
        addExercise("Butt Kickers", "Dynamic hamstring and cardio exercise",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.CARDIO, 8, 90,
                "Jog in place bringing heels up to glutes. 30 seconds on, 15 seconds rest, repeat 6 times");

        addExercise("High Knees", "Explosive lower body cardio",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.CARDIO, 8, 95,
                "Run in place bringing knees up to chest level. 30 seconds on, 15 seconds rest, 6 rounds");

        addExercise("Skipping in Place", "Fun cardio without the rope",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.CARDIO, 10, 110,
                "Mimic jump rope motion without rope. Mix in single bounces and double hops for 8 minutes");

        addExercise("Lateral Shuffles", "Side-to-side agility cardio",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.CARDIO, 9, 105,
                "Shuffle left 10 steps, right 10 steps. Stay low and quick. 3 sets of 5 shuttles");

        addExercise("Shadow Boxing", "Boxing movements for cardio",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.CARDIO, 12, 130,
                "Throw punches at imaginary opponent. Mix jabs, crosses, hooks. 3 rounds of 3 minutes");

        addExercise("Stair Climbing", "Use real stairs for intense cardio",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.CARDIO, 15, 150,
                "Walk/jog up and down stairs for 12 minutes. Take breaks as needed");

        // Advanced Cardio
        addExercise("Burpees", "Ultimate full-body cardio challenge",
                DifficultyLevel.ADVANCED, WorkoutCategory.CARDIO, 15, 200,
                "Squat, jump back to plank, push-up, jump forward, jump up. 3 sets of 10");

        addExercise("Mountain Climbers", "High-intensity core and cardio combo",
                DifficultyLevel.ADVANCED, WorkoutCategory.CARDIO, 12, 150,
                "Plank position, alternate bringing knees to chest rapidly. 4 sets of 30 seconds");

        // NEW ADVANCED CARDIO
        addExercise("Burpee Box Jumps", "Explosive full-body movement",
                DifficultyLevel.ADVANCED, WorkoutCategory.CARDIO, 18, 220,
                "Burpee + jump onto sturdy surface. 5 sets of 8 reps with 90 second rest");

        addExercise("Sprint Intervals", "Maximum intensity running",
                DifficultyLevel.ADVANCED, WorkoutCategory.CARDIO, 20, 250,
                "Sprint 30 seconds, walk 90 seconds. Repeat 8 times for total intensity");

        addExercise("Plyometric Circuit", "Explosive jumping exercises",
                DifficultyLevel.ADVANCED, WorkoutCategory.CARDIO, 16, 190,
                "Jump squats, tuck jumps, broad jumps, lateral bounds. 45 seconds each, 4 rounds");

        addExercise("Bear Crawls", "Primal movement cardio",
                DifficultyLevel.ADVANCED, WorkoutCategory.CARDIO, 14, 170,
                "Crawl forward on hands and feet, knees off ground. 20 feet forward, 20 back, 6 rounds");
    }

    /**
     * Add strength exercises to database - EXPANDED with 20+ new exercises
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

        // NEW BEGINNER STRENGTH
        addExercise("Standing Calf Raises", "Strengthen your calves anywhere",
                DifficultyLevel.BEGINNER, WorkoutCategory.STRENGTH, 5, 35,
                "Rise up on toes, hold 2 seconds, lower slowly. 3 sets of 15 reps");

        addExercise("Assisted Squats", "Learn proper squat form safely",
                DifficultyLevel.BEGINNER, WorkoutCategory.STRENGTH, 7, 55,
                "Hold onto stable surface, squat down and up. Focus on form. 2 sets of 10");

        addExercise("Dead Bug", "Gentle core strengthening exercise",
                DifficultyLevel.BEGINNER, WorkoutCategory.STRENGTH, 6, 40,
                "Lie on back, extend opposite arm and leg slowly. 10 reps each side, 2 sets");

        addExercise("Bird Dog", "Core and back strengthening",
                DifficultyLevel.BEGINNER, WorkoutCategory.STRENGTH, 7, 45,
                "On hands and knees, extend opposite arm and leg. Hold 10 seconds, 5 reps each side");

        addExercise("Seated Rows", "Upper back strength without weights",
                DifficultyLevel.BEGINNER, WorkoutCategory.STRENGTH, 6, 40,
                "Sit tall, pull shoulder blades together, squeeze 5 seconds. 15 reps, 2 sets");

        addExercise("Standing Bridges", "Glute activation exercise",
                DifficultyLevel.BEGINNER, WorkoutCategory.STRENGTH, 5, 35,
                "Stand, squeeze glutes tight, tilt pelvis slightly. Hold 10 seconds, 10 reps");

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

        // NEW INTERMEDIATE STRENGTH
        addExercise("Pike Push-Ups", "Shoulder and upper body strengthener",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.STRENGTH, 8, 85,
                "Downward dog position, lower head toward hands. 3 sets of 8-12 reps");

        addExercise("Single-Leg Glute Bridges", "Unilateral glute and hamstring strength",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.STRENGTH, 9, 75,
                "Bridge on one leg, squeeze glutes at top. 12 reps each leg, 3 sets");

        addExercise("Tricep Dips", "Target triceps using chair or couch",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.STRENGTH, 8, 70,
                "Hands on edge, lower body down and press up. 3 sets of 10-15 reps");

        addExercise("Reverse Lunges", "Backward stepping lunge variation",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.STRENGTH, 10, 85,
                "Step backward into lunge, return to start. 12 reps each leg, 3 sets");

        addExercise("Superman", "Lower back and posterior chain",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.STRENGTH, 7, 55,
                "Lie prone, lift chest and legs simultaneously. Hold 5 seconds, 12 reps, 3 sets");

        addExercise("Side Plank", "Lateral core strengthening",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.STRENGTH, 8, 60,
                "Hold side plank position. 30 seconds each side, 3 sets");

        addExercise("Bulgarian Split Squats", "Elevated rear foot squat",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.STRENGTH, 12, 95,
                "Rear foot elevated, squat on front leg. 10 reps each leg, 3 sets");

        // Advanced Strength
        addExercise("Single-Arm Push-Ups", "Ultimate upper body challenge",
                DifficultyLevel.ADVANCED, WorkoutCategory.STRENGTH, 15, 180,
                "Push-ups with one arm behind back. Work up to 5 per arm");

        addExercise("Pistol Squats", "Single-leg squat mastery",
                DifficultyLevel.ADVANCED, WorkoutCategory.STRENGTH, 18, 160,
                "Single-leg squat to full depth. Assisted or full. 3 sets of 5 per leg");

        // NEW ADVANCED STRENGTH
        addExercise("Archer Push-Ups", "Unilateral push-up progression",
                DifficultyLevel.ADVANCED, WorkoutCategory.STRENGTH, 14, 140,
                "Wide push-up shifting weight to one arm. 6 reps each side, 3 sets");

        addExercise("Shrimp Squats", "Advanced single-leg squat variation",
                DifficultyLevel.ADVANCED, WorkoutCategory.STRENGTH, 16, 150,
                "Single-leg squat with other leg extended behind. 3-5 reps each leg");

        addExercise("Human Flag Progression", "Ultimate core and strength challenge",
                DifficultyLevel.ADVANCED, WorkoutCategory.STRENGTH, 20, 200,
                "Work toward horizontal body hold on vertical pole. Practice holds and negatives");

        addExercise("One-Arm Handstand Push-Up", "Elite upper body strength",
                DifficultyLevel.ADVANCED, WorkoutCategory.STRENGTH, 25, 250,
                "Handstand push-up on one arm. Work progressions and skill development");

        addExercise("Dragon Squats", "Extreme single-leg squat",
                DifficultyLevel.ADVANCED, WorkoutCategory.STRENGTH, 18, 170,
                "Single-leg squat with other leg extended straight in front. 3-5 reps per leg");
    }

    /**
     * Add flexibility exercises to database - EXPANDED with 15+ new exercises
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

        // NEW BEGINNER FLEXIBILITY
        addExercise("Seated Spinal Twist", "Gentle back mobility",
                DifficultyLevel.BEGINNER, WorkoutCategory.FLEXIBILITY, 6, 25,
                "Sit tall, rotate torso left and right slowly. Hold 15 seconds each side, 3 sets");

        addExercise("Forward Fold Progression", "Gradual hamstring stretch",
                DifficultyLevel.BEGINNER, WorkoutCategory.FLEXIBILITY, 8, 30,
                "Standing or seated, fold forward gently. Hold comfortable stretch 30 seconds, 3 sets");

        addExercise("Chest Doorway Stretch", "Open tight chest and shoulders",
                DifficultyLevel.BEGINNER, WorkoutCategory.FLEXIBILITY, 5, 20,
                "Stand in doorway, press forearms against frame, step forward. Hold 30 seconds");

        addExercise("Gentle Side Bends", "Lateral spine flexibility",
                DifficultyLevel.BEGINNER, WorkoutCategory.FLEXIBILITY, 5, 25,
                "Standing, reach one arm overhead and bend to side. 30 seconds each side, 2 sets");

        addExercise("Knee to Chest", "Lower back and hip relief",
                DifficultyLevel.BEGINNER, WorkoutCategory.FLEXIBILITY, 6, 25,
                "Lying down, pull one knee to chest gently. Hold 30 seconds each leg, 2 sets");

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

        // NEW INTERMEDIATE FLEXIBILITY
        addExercise("Pigeon Pose Prep", "Deep hip opening stretch",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.FLEXIBILITY, 12, 40,
                "Figure-4 position, lean forward over front leg. Hold 60 seconds each side");

        addExercise("Thoracic Spine Rotation", "Upper back mobility",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.FLEXIBILITY, 8, 35,
                "Side-lying, rotate top arm in large circles. 10 rotations each direction, both sides");

        addExercise("Standing Quad Stretch", "Stretch front of thigh",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.FLEXIBILITY, 7, 30,
                "Hold foot behind you, pull heel to glute. Hold 45 seconds each leg");

        addExercise("Seated Figure-4", "Hip and glute stretch",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.FLEXIBILITY, 10, 35,
                "Sit, ankle on opposite knee, lean forward. Hold 45 seconds each side");

        addExercise("Wall Angels", "Shoulder blade mobility",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.FLEXIBILITY, 8, 40,
                "Back to wall, slide arms up and down like snow angel. 15 slow reps, 3 sets");

        // Advanced Flexibility
        addExercise("Full Splits", "Advanced hip and leg flexibility",
                DifficultyLevel.ADVANCED, WorkoutCategory.FLEXIBILITY, 15, 50,
                "Work toward front or side splits. Hold comfortable edge for 1-2 minutes");

        addExercise("Backbend Flow", "Spinal extension and chest opening",
                DifficultyLevel.ADVANCED, WorkoutCategory.FLEXIBILITY, 12, 60,
                "Bridge to wheel pose progression. Hold for 30 seconds each");

        // NEW ADVANCED FLEXIBILITY
        addExercise("King Pigeon Pose", "Extreme hip and back flexibility",
                DifficultyLevel.ADVANCED, WorkoutCategory.FLEXIBILITY, 18, 65,
                "Pigeon pose with back leg grabbed overhead. Hold 90 seconds each side");

        addExercise("Oversplits", "Beyond 180-degree splits",
                DifficultyLevel.ADVANCED, WorkoutCategory.FLEXIBILITY, 20, 70,
                "Elevate front or back foot for deeper split. Hold 2 minutes each side");

        addExercise("Scorpion Stretch", "Advanced back and shoulder flexibility",
                DifficultyLevel.ADVANCED, WorkoutCategory.FLEXIBILITY, 15, 60,
                "Prone position, bring foot to touch head. Hold 60 seconds each side");
    }

    /**
     * Add yoga exercises to database - EXPANDED with 20+ new exercises
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

        // NEW BEGINNER YOGA
        addExercise("Standing Forward Fold", "Gentle inversion and hamstring stretch",
                DifficultyLevel.BEGINNER, WorkoutCategory.YOGA, 6, 35,
                "Feet hip-width apart, fold forward with bent knees. Sway gently, hold 1 minute");

        addExercise("Cobra Pose", "Gentle backbend for spine health",
                DifficultyLevel.BEGINNER, WorkoutCategory.YOGA, 5, 30,
                "Lie prone, press palms down, lift chest. Hold 30 seconds, repeat 3 times");

        addExercise("Bridge Pose", "Gentle heart opener and glute strengthener",
                DifficultyLevel.BEGINNER, WorkoutCategory.YOGA, 7, 40,
                "Lie on back, feet flat, lift hips up. Hold 30 seconds, repeat 5 times");

        addExercise("Legs Up the Wall", "Restorative inversion",
                DifficultyLevel.BEGINNER, WorkoutCategory.YOGA, 10, 25,
                "Lie near wall, legs up wall. Relax and breathe deeply for 8-10 minutes");

        addExercise("Happy Baby Pose", "Hip opening and relaxation",
                DifficultyLevel.BEGINNER, WorkoutCategory.YOGA, 5, 25,
                "Lie on back, knees to chest, grab feet. Rock gently side to side for 2 minutes");

        addExercise("Seated Cat-Cow", "Spinal mobility while seated",
                DifficultyLevel.BEGINNER, WorkoutCategory.YOGA, 5, 30,
                "Sit cross-legged, arch and round spine slowly. 10 movements with breath");

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

        // NEW INTERMEDIATE YOGA
        addExercise("Warrior III", "Standing balance and strength challenge",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.YOGA, 10, 70,
                "Balance on one leg, other leg and torso parallel to floor. Hold 30 seconds each side");

        addExercise("Side Plank Pose", "Core strength and balance",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.YOGA, 8, 65,
                "Hold side plank with top arm reaching up. 45 seconds each side");

        addExercise("Camel Pose", "Heart-opening backbend",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.YOGA, 8, 55,
                "Kneeling, reach back to heels, open chest. Hold 30 seconds, repeat 2 times");

        addExercise("Revolved Triangle", "Twisting and balancing pose",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.YOGA, 10, 60,
                "Wide-legged fold with twist. Hold 45 seconds each side with modifications");

        addExercise("Dolphin Pose", "Forearm downward dog preparation",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.YOGA, 9, 70,
                "Forearms down, lift hips up. Hold 45 seconds, repeat 3 times");

        addExercise("Boat Pose", "Core strengthening balance",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.YOGA, 7, 60,
                "Sit, lift legs and lean back to V-shape. Hold 30 seconds, repeat 4 times");

        // Advanced Yoga
        addExercise("Crow Pose", "Arm balance and core strength",
                DifficultyLevel.ADVANCED, WorkoutCategory.YOGA, 15, 100,
                "Balance on hands with knees on upper arms. Work up to 30 seconds");

        addExercise("Headstand", "Inversion and full-body strength",
                DifficultyLevel.ADVANCED, WorkoutCategory.YOGA, 18, 120,
                "Supported headstand against wall. Build up to 2-3 minutes");

        // NEW ADVANCED YOGA
        addExercise("Firefly Pose", "Advanced arm balance",
                DifficultyLevel.ADVANCED, WorkoutCategory.YOGA, 20, 140,
                "Balance on hands with legs extended through arms. Hold 15-30 seconds");

        addExercise("Eight-Angle Pose", "Twisted arm balance",
                DifficultyLevel.ADVANCED, WorkoutCategory.YOGA, 18, 130,
                "Complex arm balance with twisted legs. Work progressions each side");

        addExercise("Scorpion Pose", "Advanced inversion with backbend",
                DifficultyLevel.ADVANCED, WorkoutCategory.YOGA, 22, 150,
                "Forearm stand with feet toward head. Extreme flexibility and strength required");

        addExercise("Flying Pigeon", "Advanced hip opening arm balance",
                DifficultyLevel.ADVANCED, WorkoutCategory.YOGA, 16, 120,
                "From pigeon pose, transition to arm balance. Hold 15 seconds each side");

        addExercise("Eka Pada Koundinyasana", "One-legged arm balance",
                DifficultyLevel.ADVANCED, WorkoutCategory.YOGA, 18, 130,
                "Side arm balance with one leg extended. Advanced strength and balance");
    }

    /**
     * Add HIIT exercises to database - EXPANDED with 12+ new exercises
     */
    private void addHiitExercises() {
        // Beginner HIIT
        addExercise("Gentle HIIT Circuit", "Low-impact high-intensity intervals",
                DifficultyLevel.BEGINNER, WorkoutCategory.HIIT, 10, 80,
                "30 seconds work, 30 seconds rest. Marching, arm circles, modified squats");

        addExercise("Tabata Walking", "Interval walking routine",
                DifficultyLevel.BEGINNER, WorkoutCategory.HIIT, 8, 60,
                "20 seconds fast walk, 10 seconds slow. Repeat 8 rounds");

        // NEW BEGINNER HIIT
        addExercise("Chair-Based HIIT", "High intensity while seated",
                DifficultyLevel.BEGINNER, WorkoutCategory.HIIT, 12, 70,
                "Seated punches, marching, arm raises. 20 seconds on, 20 seconds rest, 12 rounds");

        addExercise("Wall Push HIIT", "Upper body intervals against wall",
                DifficultyLevel.BEGINNER, WorkoutCategory.HIIT, 10, 65,
                "Wall pushes, arm circles, shoulder shrugs. 30 seconds each, 2 rounds");

        // Intermediate HIIT
        addExercise("Classic Tabata", "High-intensity 4-minute protocol",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.HIIT, 15, 150,
                "20 seconds max effort, 10 seconds rest. 8 rounds of chosen exercise");

        addExercise("EMOM Challenge", "Every minute on the minute",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.HIIT, 18, 180,
                "Set number of reps each minute for 10 minutes. Rest remaining time");

        // NEW INTERMEDIATE HIIT
        addExercise("Bodyweight Bootcamp", "Military-style conditioning",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.HIIT, 20, 200,
                "Squats, push-ups, planks, jumping jacks. 45 seconds on, 15 seconds rest, 5 rounds");

        addExercise("Animal Flow HIIT", "Primal movement patterns",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.HIIT, 16, 160,
                "Bear crawl, crab walk, frog jumps, lizard crawl. 40 seconds each, 4 rounds");

        addExercise("Cardio Blast Circuit", "High-energy cardio combinations",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.HIIT, 18, 190,
                "Mountain climbers, high knees, butt kickers, jacks. 30 seconds each, 6 rounds");

        addExercise("Power Yoga Flow", "Dynamic yoga-based HIIT",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.HIIT, 14, 140,
                "Fast-paced sun salutations and warrior flows. 30 seconds flow, 10 seconds rest");

        // Advanced HIIT
        addExercise("Death by Burpees", "Progressive intensity challenge",
                DifficultyLevel.ADVANCED, WorkoutCategory.HIIT, 20, 250,
                "Minute 1: 1 burpee, Minute 2: 2 burpees, etc. Go until failure");

        addExercise("Fight Gone Bad", "Mixed modal high intensity",
                DifficultyLevel.ADVANCED, WorkoutCategory.HIIT, 25, 300,
                "5 exercises, 1 minute each, 1 minute rest. Repeat 3 rounds");

        // NEW ADVANCED HIIT
        addExercise("Spartan Race Training", "Obstacle course simulation",
                DifficultyLevel.ADVANCED, WorkoutCategory.HIIT, 30, 350,
                "Burpees, bear crawls, jump overs, carries. 8 exercises, 2 minutes each");

        addExercise("Navy SEAL Workout", "Elite military conditioning",
                DifficultyLevel.ADVANCED, WorkoutCategory.HIIT, 35, 400,
                "Push-ups, sit-ups, squats, running. Pyramid sets with minimal rest");

        addExercise("CrossFit Hero WOD", "Honor workout simulation",
                DifficultyLevel.ADVANCED, WorkoutCategory.HIIT, 28, 320,
                "Complex movements for time. Pull-ups, push-ups, squats, runs in challenging format");
    }

    /**
     * Add breathing exercises to database - EXPANDED with 15+ new exercises
     */
    private void addBreathingExercises() {
        // Beginner Breathing
        addExercise("Box Breathing", "Simple 4-count breathing pattern",
                DifficultyLevel.BEGINNER, WorkoutCategory.BREATHING, 5, 15,
                "Inhale 4, hold 4, exhale 4, hold 4. Repeat for 5 minutes");

        addExercise("Belly Breathing", "Deep diaphragmatic breathing",
                DifficultyLevel.BEGINNER, WorkoutCategory.BREATHING, 6, 20,
                "Hand on chest, hand on belly. Breathe so only belly hand moves");

        // NEW BEGINNER BREATHING
        addExercise("Three-Part Breath", "Complete yogic breathing",
                DifficultyLevel.BEGINNER, WorkoutCategory.BREATHING, 8, 25,
                "Breathe into belly, ribs, then chest. Exhale in reverse order. 10 complete breaths");

        addExercise("Counting Breath", "Simple breath counting meditation",
                DifficultyLevel.BEGINNER, WorkoutCategory.BREATHING, 7, 20,
                "Count breaths from 1 to 10, start over. Focus only on counting for 5 minutes");

        addExercise("Extended Exhale", "Calming breath pattern",
                DifficultyLevel.BEGINNER, WorkoutCategory.BREATHING, 6, 18,
                "Inhale for 4, exhale for 6. Longer exhale activates relaxation response");

        addExercise("Natural Breathing", "Awareness of normal breath",
                DifficultyLevel.BEGINNER, WorkoutCategory.BREATHING, 10, 15,
                "Simply observe your natural breath without changing it. Notice sensations");

        // Intermediate Breathing
        addExercise("4-7-8 Breathing", "Calming breath ratio",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.BREATHING, 8, 25,
                "Inhale 4, hold 7, exhale 8. Repeat 4-8 cycles");

        addExercise("Alternate Nostril", "Balancing pranayama technique",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.BREATHING, 10, 30,
                "Use thumb and finger to alternate breathing through each nostril");

        // NEW INTERMEDIATE BREATHING
        addExercise("Coherent Breathing", "5-second rhythm breathing",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.BREATHING, 12, 35,
                "Inhale 5 seconds, exhale 5 seconds. Creates heart rate variability coherence");

        addExercise("Triangle Breathing", "Three-part breath pattern",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.BREATHING, 9, 28,
                "Inhale 4, hold 4, exhale 4. Equal parts create balance and focus");

        addExercise("Bumble Bee Breath", "Humming breath for relaxation",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.BREATHING, 8, 30,
                "Hum during exhale with fingers in ears. Creates internal vibration and calm");

        addExercise("Victory Breath", "Ujjayi pranayama breathing",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.BREATHING, 10, 32,
                "Breathe through nose with slight throat constriction. Creates ocean sound");

        addExercise("Retention Breathing", "Breath holding practice",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.BREATHING, 12, 35,
                "Inhale, hold comfortably, exhale. Gradually increase retention time");

        // Advanced Breathing
        addExercise("Breath of Fire", "Energizing rapid breathing",
                DifficultyLevel.ADVANCED, WorkoutCategory.BREATHING, 12, 50,
                "Rapid, shallow breathing through nose. 30 breaths, 3 rounds");

        addExercise("Wim Hof Method", "Power breathing technique",
                DifficultyLevel.ADVANCED, WorkoutCategory.BREATHING, 15, 60,
                "30 deep breaths, hold breath after exhale, repeat 3 rounds");

        // NEW ADVANCED BREATHING
        addExercise("Holotropic Breathing", "Consciousness-altering breath work",
                DifficultyLevel.ADVANCED, WorkoutCategory.BREATHING, 45, 100,
                "Continuous connected breathing for extended period. Deep transformative practice");

        addExercise("Breath Suspension", "Advanced retention practice",
                DifficultyLevel.ADVANCED, WorkoutCategory.BREATHING, 20, 70,
                "Long breath holds after inhale and exhale. Build tolerance gradually");

        addExercise("Power Breathing Circuit", "Athletic breathing patterns",
                DifficultyLevel.ADVANCED, WorkoutCategory.BREATHING, 18, 80,
                "Combine multiple advanced techniques in sequence. High-intensity breath work");

        addExercise("Psychedelic Breathing", "Altered state breathing",
                DifficultyLevel.ADVANCED, WorkoutCategory.BREATHING, 30, 90,
                "Rapid, deep breathing to induce natural altered states. Requires experience");
    }

    /**
     * NEW CATEGORY: Fun and Creative Exercises - Add this new method
     */
    private void addFunAndCreativeExercises() {
        // Fun Beginner Exercises
        addExercise("Dance Party", "Free-form dancing to favorite music",
                DifficultyLevel.BEGINNER, WorkoutCategory.CARDIO, 12, 85,
                "Put on 3-4 favorite songs and dance however feels good. No rules, just fun!");

        addExercise("Animal Walks", "Mimic different animal movements",
                DifficultyLevel.BEGINNER, WorkoutCategory.CARDIO, 8, 60,
                "Bear walk, crab walk, frog hops, duck walk. 30 seconds each animal, 2 rounds");

        addExercise("Invisible Jump Rope", "Jump rope without the rope",
                DifficultyLevel.BEGINNER, WorkoutCategory.CARDIO, 6, 55,
                "Mimic jump rope movements. Mix single bounces, side to side, front to back");

        // Fun Intermediate Exercises
        addExercise("Superhero Training", "Comic book hero-inspired moves",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.STRENGTH, 15, 120,
                "Superman flies, Spider-man crawls, Wonder Woman poses. Create your hero workout!");

        addExercise("Zombie Apocalypse", "Survival-themed exercise circuit",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.HIIT, 18, 150,
                "Run from zombies, climb over obstacles, fight them off. High-intensity survival!");

        addExercise("Martial Arts Flow", "Basic martial arts movements",
                DifficultyLevel.INTERMEDIATE, WorkoutCategory.CARDIO, 14, 110,
                "Punches, kicks, blocks in flowing sequence. Channel your inner warrior!");

        // Fun Advanced Exercises
        addExercise("Ninja Training", "Stealth and agility challenges",
                DifficultyLevel.ADVANCED, WorkoutCategory.HIIT, 25, 200,
                "Silent movements, precision jumps, balance challenges. Train like a ninja!");

        addExercise("Parkour Basics", "Urban movement fundamentals",
                DifficultyLevel.ADVANCED, WorkoutCategory.CARDIO, 20, 180,
                "Precision jumps, vaults, wall runs (safely adapted for home). Flow through space!");

        addExercise("Circus Training", "Acrobatic and performance moves",
                DifficultyLevel.ADVANCED, WorkoutCategory.STRENGTH, 22, 160,
                "Handstand progressions, basic tumbling, balance challenges. Join the circus!");
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