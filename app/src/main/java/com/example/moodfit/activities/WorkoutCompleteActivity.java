package com.example.moodfit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.moodfit.R;
import com.example.moodfit.models.User;
import com.example.moodfit.models.WorkoutSession;
import com.example.moodfit.models.enums.MoodType;
import com.example.moodfit.utils.DataManager;

/**
 * WorkoutCompleteActivity - Celebrates workout completion and shows summary
 * Displays workout stats, motivational messages, and navigation options
 */
public class WorkoutCompleteActivity extends AppCompatActivity {

    private static final String TAG = "WorkoutCompleteActivity";

    // Data Management
    private DataManager dataManager;
    private User currentUser;

    // UI Components
    private ImageView ivCelebrationIcon;
    private TextView tvCongratulationsTitle;
    private TextView tvWorkoutSummary;
    private TextView tvStreakUpdate;
    private TextView tvMotivationalMessage;
    private CardView summaryCard;
    private Button btnBackToHome;
    private Button btnShareAchievement;

    // Workout Data
    private String sessionId;
    private MoodType workoutMood;
    private int totalExercises;
    private int estimatedDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_complete);

        // Initialize data management
        initializeDataManager();

        // Extract intent data
        extractIntentData();

        // Initialize UI components
        initializeViews();

        // Setup event listeners
        setupEventListeners();

        // Display completion information
        displayCompletionInfo();

        // Celebrate with animations
        startCelebrationAnimations();
    }

    /**
     * Initialize data management components
     */
    private void initializeDataManager() {
        dataManager = new DataManager(this);
        currentUser = dataManager.getCurrentUser();
    }

    /**
     * Extract data from intent
     */
    private void extractIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            sessionId = intent.getStringExtra("session_id");
            String moodName = intent.getStringExtra("mood_type");
            if (moodName != null) {
                try {
                    workoutMood = MoodType.valueOf(moodName);
                } catch (IllegalArgumentException e) {
                    workoutMood = MoodType.NEUTRAL;
                }
            }
            totalExercises = intent.getIntExtra("total_exercises", 1);
            estimatedDuration = intent.getIntExtra("estimated_duration", 30);
        }
    }

    /**
     * Initialize all UI components
     */
    private void initializeViews() {
        ivCelebrationIcon = findViewById(R.id.iv_celebration_icon);
        tvCongratulationsTitle = findViewById(R.id.tv_congratulations_title);
        tvWorkoutSummary = findViewById(R.id.tv_workout_summary);
        tvStreakUpdate = findViewById(R.id.tv_streak_update);
        tvMotivationalMessage = findViewById(R.id.tv_motivational_message);
        summaryCard = findViewById(R.id.summary_card);
        btnBackToHome = findViewById(R.id.btn_back_to_home);
        btnShareAchievement = findViewById(R.id.btn_share_achievement);
    }

    /**
     * Setup event listeners
     */
    private void setupEventListeners() {
        btnBackToHome.setOnClickListener(v -> returnToHome());
        btnShareAchievement.setOnClickListener(v -> shareAchievement());
    }

    /**
     * Display workout completion information
     */
    private void displayCompletionInfo() {
        // Set celebration icon based on mood
        setCelebrationIcon();

        // Set congratulations title
        tvCongratulationsTitle.setText("Workout Complete! 🎉");

        // Set workout summary
        String summary = createWorkoutSummary();
        tvWorkoutSummary.setText(summary);

        // Update and display streak information
        updateStreakDisplay();

        // Set motivational message
        setMotivationalMessage();
    }

    /**
     * Set appropriate celebration icon based on workout mood
     */
    private void setCelebrationIcon() {
        if (workoutMood != null) {
            switch (workoutMood) {
                case HAPPY:
                    ivCelebrationIcon.setImageResource(R.drawable.ic_celebration_happy);
                    break;
                case STRESSED:
                    ivCelebrationIcon.setImageResource(R.drawable.ic_celebration_calm);
                    break;
                case FRUSTRATED:
                    ivCelebrationIcon.setImageResource(R.drawable.ic_celebration_strong);
                    break;
                default:
                    ivCelebrationIcon.setImageResource(R.drawable.ic_celebration_trophy);
                    break;
            }
        } else {
            ivCelebrationIcon.setImageResource(R.drawable.ic_celebration_trophy);
        }
    }

    /**
     * Create workout summary text
     */
    private String createWorkoutSummary() {
        StringBuilder summary = new StringBuilder();

        // Workout type
        if (workoutMood != null) {
            summary.append("You completed a ").append(workoutMood.getDisplayName().toLowerCase())
                    .append(" mood workout!\n\n");
        } else {
            summary.append("You completed your workout!\n\n");
        }

        // Exercise count
        summary.append("✅ ").append(totalExercises).append(" exercise")
                .append(totalExercises > 1 ? "s" : "").append(" completed\n");

        // Estimated time
        summary.append("⏱️ ~").append(estimatedDuration).append(" minutes of activity\n");

        // Mood benefit
        if (workoutMood != null) {
            summary.append("😊 ").append(getMoodBenefit(workoutMood));
        }

        return summary.toString();
    }

    /**
     * Get mood-specific benefit message
     */
    private String getMoodBenefit(MoodType mood) {
        switch (mood) {
            case HAPPY:
                return "Boosted your positive energy!";
            case STRESSED:
                return "Reduced stress and tension!";
            case FRUSTRATED:
                return "Released frustration constructively!";
            case NEUTRAL:
                return "Maintained your fitness routine!";
            default:
                return "Improved your overall wellbeing!";
        }
    }

    /**
     * Update and display streak information
     */
    private void updateStreakDisplay() {
        if (currentUser != null) {
            // Update user streak
            dataManager.updateUserStreak();

            // Refresh user data to get updated streak
            currentUser = dataManager.getCurrentUser();

            int currentStreak = currentUser.getCurrentStreak();
            int bestStreak = currentUser.getBestStreak();

            String streakText;
            if (currentStreak == 1) {
                streakText = "🔥 Great start! You've started a new streak!";
            } else if (currentStreak == bestStreak && currentStreak > 1) {
                streakText = "🔥 New record! " + currentStreak + " day streak - your best yet!";
            } else if (currentStreak > 1) {
                streakText = "🔥 Amazing! " + currentStreak + " day streak going strong!";
            } else {
                streakText = "🔥 Keep building your fitness habit!";
            }

            tvStreakUpdate.setText(streakText);
        } else {
            tvStreakUpdate.setText("🔥 Great job completing your workout!");
        }
    }

    /**
     * Set appropriate motivational message
     */
    private void setMotivationalMessage() {
        String[] messages = {
                "Every workout counts towards a healthier you!",
                "You're building strength inside and out!",
                "Consistency is the key to lasting change!",
                "Your future self will thank you for this!",
                "You've invested in your health today!",
                "Progress, not perfection - and you're progressing!",
                "You showed up for yourself today!"
        };

        // Select message based on user's streak or random
        int messageIndex = 0;
        if (currentUser != null) {
            messageIndex = currentUser.getCurrentStreak() % messages.length;
        } else {
            messageIndex = (int) (Math.random() * messages.length);
        }

        tvMotivationalMessage.setText(messages[messageIndex]);
    }

    /**
     * Start celebration animations
     */
    private void startCelebrationAnimations() {
        // Animate celebration icon
        ivCelebrationIcon.setAlpha(0f);
        ivCelebrationIcon.setScaleX(0.5f);
        ivCelebrationIcon.setScaleY(0.5f);
        ivCelebrationIcon.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(800)
                .start();

        // Animate title
        tvCongratulationsTitle.setAlpha(0f);
        tvCongratulationsTitle.setTranslationY(-30f);
        tvCongratulationsTitle.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(600)
                .setStartDelay(300)
                .start();

        // Animate summary card
        summaryCard.setAlpha(0f);
        summaryCard.setTranslationY(50f);
        summaryCard.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(700)
                .setStartDelay(600)
                .start();

        // Animate buttons
        btnBackToHome.setAlpha(0f);
        btnBackToHome.setTranslationY(30f);
        btnBackToHome.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(500)
                .setStartDelay(1000)
                .start();

        btnShareAchievement.setAlpha(0f);
        btnShareAchievement.setTranslationY(30f);
        btnShareAchievement.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(500)
                .setStartDelay(1100)
                .start();
    }

    /**
     * Return to home activity
     */
    private void returnToHome() {
        Intent homeIntent = new Intent(this, HomeActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(homeIntent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    /**
     * Share workout achievement
     */
    private void shareAchievement() {
        try {
            String shareText = createShareText();

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My MoodFit Workout Achievement!");

            Intent chooser = Intent.createChooser(shareIntent, "Share Your Achievement");
            startActivity(chooser);
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error sharing achievement", e);
            android.widget.Toast.makeText(this, "Unable to share at this time", android.widget.Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Create share text for social media
     */
    private String createShareText() {
        StringBuilder shareText = new StringBuilder();

        shareText.append("🎉 Just completed my workout with MoodFit! ");

        if (workoutMood != null) {
            shareText.append("Turned my ").append(workoutMood.getDisplayName().toLowerCase())
                    .append(" mood into motivation! ");
        }

        shareText.append("💪 ").append(totalExercises).append(" exercises done ");

        if (currentUser != null && currentUser.getCurrentStreak() > 1) {
            shareText.append("🔥 ").append(currentUser.getCurrentStreak()).append(" day streak! ");
        }

        shareText.append("\n\n#MoodFit #Fitness #Wellness #HealthyLifestyle");

        return shareText.toString();
    }

    /**
     * Handle back button press
     */
    @Override
    public void onBackPressed() {
        returnToHome();
    }

    /**
     * Handle activity results
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle any results if needed
    }
}