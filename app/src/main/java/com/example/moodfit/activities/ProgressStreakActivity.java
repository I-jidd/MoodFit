package com.example.moodfit.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.moodfit.R;
import com.example.moodfit.models.User;
import com.example.moodfit.models.UserProgress;
import com.example.moodfit.models.WorkoutSession;
import com.example.moodfit.utils.DataManager;

import java.util.List;
import java.util.Calendar;

/**
 * ProgressStreakActivity - Displays comprehensive user progress and streak information
 * Shows current streak, best streak, motivational messages, and workout statistics
 * Integrates with DataManager for real-time progress tracking
 */
public class ProgressStreakActivity extends AppCompatActivity {

    private static final String TAG = "ProgressStreakActivity";

    // Data Management
    private DataManager dataManager;
    private User currentUser;
    private DataManager.UserStats userStats;
    private UserProgress userProgress;

    // UI Components
    private ImageView ivFlameIcon;
    private TextView tvStreakCount;
    private TextView tvMotivationalMessage;
    private TextView tvWeekCount;
    private TextView tvBestStreak;
    private CardView motivationalCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_streak);

        // Initialize data management
        initializeDataManager();

        // Initialize UI components
        initializeViews();

        // Load and display progress data
        loadProgressData();

        // Setup animations
        startProgressAnimations();
    }

    /**
     * Initialize data management components
     */
    private void initializeDataManager() {
        try {
            dataManager = new DataManager(this);
            currentUser = dataManager.getCurrentUser();
            userStats = dataManager.getUserStats();
            userProgress = dataManager.getUserProgress();

            android.util.Log.d(TAG, "DataManager initialized successfully");

        } catch (Exception e) {
            android.util.Log.e(TAG, "Error initializing data manager", e);
            showErrorMessage("Unable to load progress data.");
        }
    }

    /**
     * Initialize all UI components
     */
    private void initializeViews() {
        ivFlameIcon = findViewById(R.id.iv_flame_icon);
        tvStreakCount = findViewById(R.id.tv_streak_count);
        tvMotivationalMessage = findViewById(R.id.tv_motivational_message);
        tvWeekCount = findViewById(R.id.tv_week_count);
        tvBestStreak = findViewById(R.id.tv_best_streak);

        // Find the motivational message card
        motivationalCard = (CardView) tvMotivationalMessage.getParent().getParent();
    }

    /**
     * Load and display comprehensive progress data
     */
    private void loadProgressData() {
        try {
            // Update streak display
            updateStreakDisplay();

            // Update weekly progress
            updateWeeklyProgress();

            // Update best streak
            updateBestStreakDisplay();

            // Update motivational message
            updateMotivationalMessage();

            // Update flame icon based on streak
            updateFlameIcon();

            android.util.Log.d(TAG, "Progress data loaded successfully");

        } catch (Exception e) {
            android.util.Log.e(TAG, "Error loading progress data", e);
            showDefaultProgress();
            showErrorMessage("Some progress data may not be accurate.");
        }
    }

    /**
     * Update current streak display with dynamic formatting
     */
    private void updateStreakDisplay() {
        try {
            int currentStreak = 0;

            if (userStats != null) {
                currentStreak = userStats.currentStreak;
            } else if (currentUser != null) {
                currentStreak = currentUser.getCurrentStreak();
            }

            // Format streak text dynamically
            String streakText = formatStreakText(currentStreak);
            tvStreakCount.setText(streakText);

            // Set text color based on streak level
            setStreakTextColor(currentStreak);

            android.util.Log.d(TAG, "Current streak: " + currentStreak);

        } catch (Exception e) {
            android.util.Log.e(TAG, "Error updating streak display", e);
            tvStreakCount.setText("0 Days");
            tvStreakCount.setTextColor(getResources().getColor(R.color.text_secondary));
        }
    }

    /**
     * Format streak text based on count
     */
    private String formatStreakText(int streakCount) {
        if (streakCount == 0) {
            return "Start Your Streak!";
        } else if (streakCount == 1) {
            return "1 Day Streak";
        } else {
            return streakCount + " Days Streak";
        }
    }

    /**
     * Set streak text color based on achievement level
     */
    private void setStreakTextColor(int streakCount) {
        int color;

        if (streakCount == 0) {
            color = getResources().getColor(R.color.text_secondary);
        } else if (streakCount < 3) {
            color = getResources().getColor(R.color.accent_color);
        } else if (streakCount < 7) {
            color = getResources().getColor(R.color.success);
        } else if (streakCount < 30) {
            color = getResources().getColor(R.color.primary_color);
        } else {
            // Epic streak!
            color = Color.parseColor("#FF6B35"); // Orange for legendary streaks
        }

        tvStreakCount.setTextColor(color);
    }

    /**
     * Update weekly progress display
     */
    private void updateWeeklyProgress() {
        try {
            int workoutsThisWeek = 0;

            if (userStats != null) {
                workoutsThisWeek = userStats.workoutsThisWeek;
            } else {
                workoutsThisWeek = dataManager.getWorkoutsThisWeek();
            }

            // Format weekly progress text
            String weekText = formatWeeklyText(workoutsThisWeek);
            tvWeekCount.setText(weekText);

            android.util.Log.d(TAG, "Workouts this week: " + workoutsThisWeek);

        } catch (Exception e) {
            android.util.Log.e(TAG, "Error updating weekly progress", e);
            tvWeekCount.setText("0 days");
        }
    }

    /**
     * Format weekly progress text
     */
    private String formatWeeklyText(int workoutCount) {
        if (workoutCount == 0) {
            return "Start this week!";
        } else if (workoutCount == 1) {
            return "1 day";
        } else {
            return workoutCount + " days";
        }
    }

    /**
     * Update best streak display
     */
    private void updateBestStreakDisplay() {
        try {
            int bestStreak = 0;

            if (userStats != null) {
                bestStreak = userStats.bestStreak;
            } else if (currentUser != null) {
                bestStreak = currentUser.getBestStreak();
            }

            // Format best streak text
            String bestStreakText = formatBestStreakText(bestStreak);
            tvBestStreak.setText(bestStreakText);

            android.util.Log.d(TAG, "Best streak: " + bestStreak);

        } catch (Exception e) {
            android.util.Log.e(TAG, "Error updating best streak", e);
            tvBestStreak.setText("0 days");
        }
    }

    /**
     * Format best streak text
     */
    private String formatBestStreakText(int bestStreak) {
        if (bestStreak == 0) {
            return "No streak yet";
        } else if (bestStreak == 1) {
            return "1 day";
        } else {
            return bestStreak + " days";
        }
    }

    /**
     * Update motivational message based on current progress
     */
    private void updateMotivationalMessage() {
        try {
            int currentStreak = (userStats != null) ? userStats.currentStreak :
                    (currentUser != null) ? currentUser.getCurrentStreak() : 0;

            int workoutsThisWeek = (userStats != null) ? userStats.workoutsThisWeek :
                    dataManager.getWorkoutsThisWeek();

            String message = generateMotivationalMessage(currentStreak, workoutsThisWeek);
            tvMotivationalMessage.setText(message);

            // Update card background based on achievement level
            updateMotivationalCardStyle(currentStreak);

        } catch (Exception e) {
            android.util.Log.e(TAG, "Error updating motivational message", e);
            tvMotivationalMessage.setText("Every workout brings you closer to your goals! 💪");
        }
    }

    /**
     * Generate context-aware motivational message
     */
    private String generateMotivationalMessage(int currentStreak, int workoutsThisWeek) {
        // Check what day of the week it is
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        boolean isWeekend = (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY);

        // Generate message based on various factors
        if (currentStreak == 0) {
            if (workoutsThisWeek == 0) {
                return isWeekend ?
                        "Perfect weekend to start fresh! Begin your streak today! 🌟" :
                        "Today is a perfect day to begin your fitness journey! 🚀";
            } else {
                return "You've been active this week! Let's build a streak! 🔥";
            }
        } else if (currentStreak == 1) {
            return "Great start! One day down, let's keep the momentum going! 💪";
        } else if (currentStreak < 3) {
            return "You're building something amazing! Keep it up! ⭐";
        } else if (currentStreak < 7) {
            return "Incredible consistency! You're on fire! 🔥🔥";
        } else if (currentStreak < 14) {
            return "You're absolutely crushing it! This is what dedication looks like! 🏆";
        } else if (currentStreak < 30) {
            return "LEGEND status! Your commitment is truly inspiring! 👑";
        } else {
            return "UNSTOPPABLE! You're a true fitness champion! 🎖️✨";
        }
    }

    /**
     * Update motivational card styling based on achievement level
     */
    private void updateMotivationalCardStyle(int streakCount) {
        try {
            if (motivationalCard == null) return;

            // Create dynamic background color based on streak
            int backgroundColor;
            if (streakCount == 0) {
                backgroundColor = getResources().getColor(R.color.card_background);
            } else if (streakCount < 3) {
                backgroundColor = Color.parseColor("#FFF3E0"); // Light orange
            } else if (streakCount < 7) {
                backgroundColor = Color.parseColor("#E8F5E8"); // Light green
            } else if (streakCount < 30) {
                backgroundColor = Color.parseColor("#E3F2FD"); // Light blue
            } else {
                backgroundColor = Color.parseColor("#F3E5F5"); // Light purple for legends
            }

            motivationalCard.setCardBackgroundColor(backgroundColor);

            // Add subtle elevation for higher streaks
            float elevation = Math.min(8f, 2f + (streakCount * 0.2f));
            motivationalCard.setCardElevation(elevation);

        } catch (Exception e) {
            android.util.Log.e(TAG, "Error updating card style", e);
        }
    }

    /**
     * Update flame icon based on streak level
     */
    private void updateFlameIcon() {
        try {
            int currentStreak = (userStats != null) ? userStats.currentStreak :
                    (currentUser != null) ? currentUser.getCurrentStreak() : 0;

            // Set flame icon color/intensity based on streak
            if (currentStreak == 0) {
                ivFlameIcon.setAlpha(0.3f);
                ivFlameIcon.setColorFilter(getResources().getColor(R.color.text_tertiary));
            } else if (currentStreak < 3) {
                ivFlameIcon.setAlpha(0.6f);
                ivFlameIcon.setColorFilter(getResources().getColor(R.color.accent_color));
            } else if (currentStreak < 7) {
                ivFlameIcon.setAlpha(0.8f);
                ivFlameIcon.setColorFilter(getResources().getColor(R.color.success));
            } else {
                ivFlameIcon.setAlpha(1.0f);
                ivFlameIcon.setColorFilter(Color.parseColor("#FF5722")); // Bright orange for high streaks

                // Add pulsing animation for high streaks
                addFlameAnimation();
            }

        } catch (Exception e) {
            android.util.Log.e(TAG, "Error updating flame icon", e);
        }
    }

    /**
     * Add pulsing animation to flame for high streaks
     */
    private void addFlameAnimation() {
        ivFlameIcon.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(1000)
                .withEndAction(() -> {
                    ivFlameIcon.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(1000)
                            .withEndAction(this::addFlameAnimation) // Loop the animation
                            .start();
                })
                .start();
    }

    /**
     * Start progress reveal animations
     */
    private void startProgressAnimations() {
        // Animate flame icon entrance
        ivFlameIcon.setAlpha(0f);
        ivFlameIcon.setScaleX(0.5f);
        ivFlameIcon.setScaleY(0.5f);
        ivFlameIcon.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(800)
                .start();

        // Animate streak count
        tvStreakCount.setAlpha(0f);
        tvStreakCount.setTranslationY(30f);
        tvStreakCount.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(600)
                .setStartDelay(400)
                .start();

        // Animate motivational card
        motivationalCard.setAlpha(0f);
        motivationalCard.setTranslationY(50f);
        motivationalCard.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(700)
                .setStartDelay(600)
                .start();

        // Animate stats section
        View statsSection = (View) findViewById(R.id.tv_week_count).getParent().getParent();
        if (statsSection != null) {
            statsSection.setAlpha(0f);
            statsSection.setTranslationY(30f);
            statsSection.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(600)
                    .setStartDelay(800)
                    .start();
        }
    }

    /**
     * Show default progress for error cases
     */
    private void showDefaultProgress() {
        tvStreakCount.setText("0 Days");
        tvStreakCount.setTextColor(getResources().getColor(R.color.text_secondary));
        tvWeekCount.setText("0 days");
        tvBestStreak.setText("0 days");
        tvMotivationalMessage.setText("Ready to start your fitness journey? 🌟");

        if (ivFlameIcon != null) {
            ivFlameIcon.setAlpha(0.3f);
            ivFlameIcon.setColorFilter(getResources().getColor(R.color.text_tertiary));
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
            // Refresh data from DataManager
            if (dataManager != null) {
                dataManager.refreshData();
                currentUser = dataManager.getCurrentUser();
                userStats = dataManager.getUserStats();
                userProgress = dataManager.getUserProgress();

                // Reload and update display
                loadProgressData();
            }
        } catch (Exception e) {
            android.util.Log.e(TAG, "Error refreshing data on resume", e);
        }
    }

    /**
     * Handle back button press with smooth transition
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

        // Stop any ongoing animations
        if (ivFlameIcon != null) {
            ivFlameIcon.clearAnimation();
        }

        // Clear DataManager caches if needed
        if (dataManager != null) {
            dataManager.clearCaches();
        }
    }

    /**
     * Utility method to get detailed progress information (for future features)
     */
    private String getDetailedProgressInfo() {
        try {
            if (userStats == null) return "No progress data available";

            StringBuilder info = new StringBuilder();
            info.append("Total Workouts: ").append(userStats.totalWorkouts).append("\n");
            info.append("Total Minutes: ").append(userStats.totalMinutes).append("\n");
            info.append("Total Calories: ").append(userStats.totalCalories).append("\n");
            info.append("Current Streak: ").append(userStats.currentStreak).append("\n");
            info.append("Best Streak: ").append(userStats.bestStreak).append("\n");
            info.append("This Week: ").append(userStats.workoutsThisWeek).append(" workouts");

            return info.toString();

        } catch (Exception e) {
            android.util.Log.e(TAG, "Error generating detailed progress info", e);
            return "Unable to generate progress summary";
        }
    }

    /**
     * ENHANCEMENT 1: Streak Milestone Celebrations
     * Add special celebrations for milestone achievements
     */
    private void checkAndCelebrateMilestones() {
        try {
            int currentStreak = (userStats != null) ? userStats.currentStreak :
                    (currentUser != null) ? currentUser.getCurrentStreak() : 0;

            // Check for milestone achievements
            if (isMilestoneStreak(currentStreak)) {
                showMilestoneCelebration(currentStreak);
            }

        } catch (Exception e) {
            android.util.Log.e(TAG, "Error checking milestones", e);
        }
    }

    /**
     * Check if current streak is a milestone
     */
    private boolean isMilestoneStreak(int streak) {
        int[] milestones = {3, 7, 14, 21, 30, 50, 75, 100};
        for (int milestone : milestones) {
            if (streak == milestone) {
                return true;
            }
        }
        return false;
    }

    /**
     * Show milestone celebration
     */
    private void showMilestoneCelebration(int streak) {
        String celebrationMessage = getMilestoneMessage(streak);

        // Create and show milestone dialog
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("🎉 Milestone Achieved!")
                .setMessage(celebrationMessage)
                .setPositiveButton("Keep Going!", (dialog, which) -> dialog.dismiss())
                .setCancelable(false)
                .show();

        // Add special milestone animation
        addMilestoneAnimation();
    }

    /**
     * Get milestone-specific celebration message
     */
    private String getMilestoneMessage(int streak) {
        switch (streak) {
            case 3: return "3 days in a row! You're building a habit! 🌱";
            case 7: return "One week strong! You're on fire! 🔥";
            case 14: return "Two weeks of dedication! Incredible! ⭐";
            case 21: return "21 days! You've officially built a habit! 🏆";
            case 30: return "ONE MONTH! You're absolutely amazing! 👑";
            case 50: return "50 DAYS! You're a fitness legend! 🎖️";
            case 75: return "75 DAYS! Unstoppable dedication! ⚡";
            case 100: return "100 DAYS! Welcome to the Hall of Fame! 🏛️";
            default: return "Amazing milestone reached! Keep it up! 🎊";
        }
    }

    /**
     * Add special animation for milestones
     */
    private void addMilestoneAnimation() {
        // Create rainbow effect on flame
        ivFlameIcon.animate()
                .scaleX(1.5f)
                .scaleY(1.5f)
                .rotation(360f)
                .setDuration(1000)
                .withEndAction(() -> {
                    ivFlameIcon.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .rotation(0f)
                            .setDuration(500)
                            .start();
                })
                .start();

        // Add confetti-like effect to the card
        motivationalCard.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(300)
                .withEndAction(() -> {
                    motivationalCard.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(300)
                            .start();
                })
                .start();
    }

    /**
     * ENHANCEMENT 2: Weekly Progress Insights
     * Add detailed weekly analysis
     */
    private void analyzeWeeklyProgress() {
        try {
            List<WorkoutSession> recentSessions = dataManager.getRecentWorkoutSessions();
            WeeklyInsights insights = calculateWeeklyInsights(recentSessions);

            android.util.Log.d(TAG, "Weekly insights: " + insights.toString());

            // You could display these insights in additional UI elements
            // or use them to enhance motivational messages

        } catch (Exception e) {
            android.util.Log.e(TAG, "Error analyzing weekly progress", e);
        }
    }

    /**
     * Calculate comprehensive weekly insights
     */
    private WeeklyInsights calculateWeeklyInsights(List<WorkoutSession> sessions) {
        WeeklyInsights insights = new WeeklyInsights();

        Calendar weekStart = Calendar.getInstance();
        weekStart.add(Calendar.DAY_OF_YEAR, -7);
        long weekStartTime = weekStart.getTimeInMillis();

        for (WorkoutSession session : sessions) {
            if (session.getEndTime() >= weekStartTime) {
                insights.totalWorkouts++;
                insights.totalMinutes += session.getDurationMinutes();
                insights.totalCalories += session.getCaloriesBurned();

                // Track daily activity
                Calendar sessionCal = Calendar.getInstance();
                sessionCal.setTimeInMillis(session.getEndTime());
                int dayOfWeek = sessionCal.get(Calendar.DAY_OF_WEEK);
                insights.activeDays[dayOfWeek - 1] = true;
            }
        }

        // Calculate active days count
        for (boolean active : insights.activeDays) {
            if (active) insights.activeDaysCount++;
        }

        return insights;
    }

    /**
     * Weekly insights data class
     */
    private static class WeeklyInsights {
        int totalWorkouts = 0;
        int totalMinutes = 0;
        int totalCalories = 0;
        int activeDaysCount = 0;
        boolean[] activeDays = new boolean[7]; // Sunday = 0, Monday = 1, etc.

        @Override
        public String toString() {
            return String.format("Workouts: %d, Minutes: %d, Calories: %d, Active Days: %d/7",
                    totalWorkouts, totalMinutes, totalCalories, activeDaysCount);
        }
    }

    /**
     * ENHANCEMENT 3: Smart Motivational Messages
     * Generate more intelligent motivational messages based on patterns
     */
    private String generateSmartMotivationalMessage() {
        try {
            List<WorkoutSession> recentSessions = dataManager.getRecentWorkoutSessions();

            // Analyze workout patterns
            boolean hasWorkedOutToday = hasWorkedOutToday(recentSessions);
            boolean workedOutYesterday = hasWorkedOutYesterday(recentSessions);
            int daysThisWeek = countWorkoutDaysThisWeek(recentSessions);

            Calendar cal = Calendar.getInstance();
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);

            // Generate context-aware message
            if (hasWorkedOutToday) {
                return getAlreadyWorkedOutMessage(daysThisWeek);
            } else if (dayOfWeek == Calendar.MONDAY) {
                return getMondayMotivationMessage(workedOutYesterday);
            } else if (dayOfWeek == Calendar.FRIDAY) {
                return getFridayMotivationMessage(daysThisWeek);
            } else if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                return getWeekendMotivationMessage(daysThisWeek);
            } else if (hourOfDay < 12) {
                return getMorningMotivationMessage(workedOutYesterday);
            } else if (hourOfDay > 18) {
                return getEveningMotivationMessage(daysThisWeek);
            } else {
                return getAfternoonMotivationMessage();
            }

        } catch (Exception e) {
            android.util.Log.e(TAG, "Error generating smart message", e);
            return "You're doing great! Keep up the awesome work! 💪";
        }
    }

    private boolean hasWorkedOutToday(List<WorkoutSession> sessions) {
        Calendar today = Calendar.getInstance();
        for (WorkoutSession session : sessions) {
            Calendar sessionCal = Calendar.getInstance();
            sessionCal.setTimeInMillis(session.getEndTime());
            if (isSameDay(today, sessionCal)) {
                return true;
            }
        }
        return false;
    }

    private boolean hasWorkedOutYesterday(List<WorkoutSession> sessions) {
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);

        for (WorkoutSession session : sessions) {
            Calendar sessionCal = Calendar.getInstance();
            sessionCal.setTimeInMillis(session.getEndTime());
            if (isSameDay(yesterday, sessionCal)) {
                return true;
            }
        }
        return false;
    }

    private int countWorkoutDaysThisWeek(List<WorkoutSession> sessions) {
        Calendar weekStart = Calendar.getInstance();
        weekStart.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        weekStart.set(Calendar.HOUR_OF_DAY, 0);
        weekStart.set(Calendar.MINUTE, 0);
        weekStart.set(Calendar.SECOND, 0);

        boolean[] daysWorkedOut = new boolean[7];

        for (WorkoutSession session : sessions) {
            if (session.getEndTime() >= weekStart.getTimeInMillis()) {
                Calendar sessionCal = Calendar.getInstance();
                sessionCal.setTimeInMillis(session.getEndTime());
                int dayOfWeek = sessionCal.get(Calendar.DAY_OF_WEEK) - 1; // 0-based
                daysWorkedOut[dayOfWeek] = true;
            }
        }

        int count = 0;
        for (boolean worked : daysWorkedOut) {
            if (worked) count++;
        }
        return count;
    }

    private boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    // Specific motivational messages for different contexts
    private String getAlreadyWorkedOutMessage(int daysThisWeek) {
        String[] messages = {
                "Already crushed today's workout! You're unstoppable! 🔥",
                "Look at you being consistent! Today's workout: ✅",
                "Another day, another victory! You're building something amazing! ⭐"
        };
        return messages[(int) (Math.random() * messages.length)];
    }

    private String getMondayMotivationMessage(boolean workedOutYesterday) {
        if (workedOutYesterday) {
            return "Perfect Sunday workout! Let's keep the momentum going this Monday! 💪";
        } else {
            return "New week, fresh start! Monday motivation is the best motivation! 🚀";
        }
    }

    private String getFridayMotivationMessage(int daysThisWeek) {
        if (daysThisWeek >= 4) {
            return "What a week! One more workout to finish strong! 🏆";
        } else if (daysThisWeek >= 2) {
            return "Friday motivation! Let's make this week count! 💫";
        } else {
            return "It's Friday! Perfect day to energize for the weekend! ⚡";
        }
    }

    private String getWeekendMotivationMessage(int daysThisWeek) {
        return "Weekend vibes! You've got time to focus on yourself! 🌟";
    }

    private String getMorningMotivationMessage(boolean workedOutYesterday) {
        return "Good morning, champion! Start your day with energy! ☀️";
    }

    private String getEveningMotivationMessage(int daysThisWeek) {
        return "Evening energy! Perfect time to release the day's stress! 🌙";
    }

    private String getAfternoonMotivationMessage() {
        return "Afternoon power! Beat the midday slump with movement! ⚡";
    }

    /**
     * ENHANCEMENT 4: Add these calls to onCreate() after loadProgressData()
     */
    private void applyEnhancements() {
        try {
            // Check for milestone celebrations
            checkAndCelebrateMilestones();

            // Analyze weekly progress for insights
            analyzeWeeklyProgress();

            // Update motivational message with smart algorithm
            String smartMessage = generateSmartMotivationalMessage();
            tvMotivationalMessage.setText(smartMessage);

        } catch (Exception e) {
            android.util.Log.e(TAG, "Error applying enhancements", e);
        }
    }
}