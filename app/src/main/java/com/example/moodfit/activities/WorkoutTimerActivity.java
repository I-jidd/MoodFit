package com.example.moodfit.activities;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.moodfit.R;
import com.example.moodfit.models.WorkoutTimer;
import com.example.moodfit.models.WorkoutSession;
import com.example.moodfit.models.enums.MoodType;
import com.example.moodfit.utils.DataManager;

/**
 * WorkoutTimerActivity - Comprehensive workout timer with customizable durations
 * Integrates with workout sessions and provides audio/visual feedback
 */
public class WorkoutTimerActivity extends AppCompatActivity implements WorkoutTimer.TimerListener {

    private static final String TAG = "WorkoutTimerActivity";

    // Timer durations in seconds
    private static final int TIMER_30_SEC = 30;
    private static final int TIMER_60_SEC = 60;

    // Data Management
    private DataManager dataManager;
    private WorkoutTimer workoutTimer;
    private WorkoutSession currentSession;

    // UI Components - Timer Selection
    private Button btn30Sec;
    private Button btn60Sec;

    // UI Components - Timer Display
    private CardView timerCard;
    private TextView tvTimerDisplay;

    // UI Components - Controls
    private Button btnStart;
    private Button btnPause;
    private Button btnReset;
    private Switch switchSound;

    // Current State
    private int selectedDuration = TIMER_30_SEC;
    private boolean soundEnabled = true;
    private boolean isTimerRunning = false;
    private boolean isTimerPaused = false;
    private long sessionStartTime;
    private int completedCycles = 0;

    // Intent Data
    private MoodType workoutMood;
    private String sessionId;
    private int estimatedDuration;

    // Audio
    private MediaPlayer completionSound;
    private Handler uiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_timer);

        // Initialize data management
        initializeDataManager();

        // Get intent data
        extractIntentData();

        // Initialize UI components
        initializeViews();

        // Setup event listeners
        setupEventListeners();

        // Initialize timer
        initializeTimer();

        // Setup audio
        setupAudio();

        // Initialize UI state
        updateTimerDisplay();
        updateControlButtonStates();
    }

    /**
     * Initialize data management components
     */
    private void initializeDataManager() {
        dataManager = new DataManager(this);
        uiHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * Extract data passed from previous activity
     */
    private void extractIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            // Get mood type if passed from MoodWorkoutActivity
            String moodName = intent.getStringExtra("mood_type");
            if (moodName != null) {
                try {
                    workoutMood = MoodType.valueOf(moodName);
                } catch (IllegalArgumentException e) {
                    workoutMood = null;
                }
            }

            // Get session ID and estimated duration
            sessionId = intent.getStringExtra("session_id");
            estimatedDuration = intent.getIntExtra("estimated_duration", 30);
        }

        // Record session start time
        sessionStartTime = System.currentTimeMillis();
    }

    /**
     * Initialize all UI components
     */
    private void initializeViews() {
        // Timer selection buttons
        btn30Sec = findViewById(R.id.btn_30_sec);
        btn60Sec = findViewById(R.id.btn_60_sec);

        // Timer display
        timerCard = findViewById(R.id.timer_card); // Assuming the CardView has this ID
        tvTimerDisplay = findViewById(R.id.tv_timer_display);

        // Control buttons
        btnStart = findViewById(R.id.btn_start);
        btnPause = findViewById(R.id.btn_pause);
        btnReset = findViewById(R.id.btn_reset);

        // Sound switch
        switchSound = findViewById(R.id.switch_sound);
        switchSound.setChecked(soundEnabled);

        // Initial button selection
        updateTimerSelectionButtons();
    }

    /**
     * Setup all event listeners
     */
    private void setupEventListeners() {
        // Timer duration selection
        btn30Sec.setOnClickListener(v -> selectTimerDuration(TIMER_30_SEC));
        btn60Sec.setOnClickListener(v -> selectTimerDuration(TIMER_60_SEC));

        // Timer controls
        btnStart.setOnClickListener(v -> startTimer());
        btnPause.setOnClickListener(v -> pauseResumeTimer());
        btnReset.setOnClickListener(v -> resetTimer());

        // Sound toggle
        switchSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            soundEnabled = isChecked;
        });
    }

    /**
     * Initialize the workout timer
     */
    private void initializeTimer() {
        workoutTimer = new WorkoutTimer(selectedDuration);
        workoutTimer.setListener(this);
    }

    /**
     * Setup audio components
     */
    private void setupAudio() {
        try {
            // Use system notification sound for completion
            completionSound = MediaPlayer.create(this, android.provider.Settings.System.DEFAULT_NOTIFICATION_URI);
            if (completionSound != null) {
                completionSound.setLooping(false);
            }
        } catch (Exception e) {
            android.util.Log.w(TAG, "Could not initialize completion sound", e);
        }
    }

    /**
     * Select timer duration and update UI
     */
    private void selectTimerDuration(int duration) {
        if (isTimerRunning) {
            // Don't allow duration change while timer is running
            return;
        }

        selectedDuration = duration;

        // Reset and reinitialize timer with new duration
        resetTimer();
        initializeTimer();

        // Update UI
        updateTimerSelectionButtons();
        updateTimerDisplay();

        // Add visual feedback
        animateTimerSelection();
    }

    /**
     * Update timer selection button states
     */
    private void updateTimerSelectionButtons() {
        // Reset both buttons
        btn30Sec.setSelected(false);
        btn60Sec.setSelected(false);

        // Highlight selected button
        if (selectedDuration == TIMER_30_SEC) {
            btn30Sec.setSelected(true);
            highlightSelectedButton(btn30Sec);
        } else {
            btn60Sec.setSelected(true);
            highlightSelectedButton(btn60Sec);
        }
    }

    /**
     * Highlight the selected timer duration button
     */
    private void highlightSelectedButton(Button selectedButton) {
        // Add visual feedback for selected button
        selectedButton.setBackgroundColor(getResources().getColor(R.color.primary_color));
        selectedButton.setTextColor(getResources().getColor(R.color.white));

        // Reset non-selected button
        Button otherButton = selectedButton == btn30Sec ? btn60Sec : btn30Sec;
        otherButton.setBackgroundColor(getResources().getColor(R.color.button_secondary));
        otherButton.setTextColor(getResources().getColor(R.color.text_primary));
    }

    /**
     * Animate timer selection change
     */
    private void animateTimerSelection() {
        timerCard.animate()
                .scaleX(1.05f)
                .scaleY(1.05f)
                .setDuration(150)
                .withEndAction(() -> {
                    timerCard.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(150)
                            .start();
                })
                .start();
    }

    /**
     * Start the workout timer
     */
    private void startTimer() {
        if (workoutTimer != null) {
            workoutTimer.start();
            isTimerRunning = true;
            isTimerPaused = false;

            // Start UI update loop
            startTimerLoop();

            // Update button states
            updateControlButtonStates();

            // Visual feedback
            animateTimerStart();
        }
    }

    /**
     * Pause or resume the timer
     */
    private void pauseResumeTimer() {
        if (workoutTimer != null) {
            if (isTimerPaused) {
                workoutTimer.resume();
                isTimerPaused = false;
                btnPause.setText("Pause");
            } else {
                workoutTimer.pause();
                isTimerPaused = true;
                btnPause.setText("Resume");
            }

            updateControlButtonStates();
        }
    }

    /**
     * Reset the timer to initial state
     */
    private void resetTimer() {
        if (workoutTimer != null) {
            workoutTimer.reset();
        }

        isTimerRunning = false;
        isTimerPaused = false;
        btnPause.setText("Pause");

        // Update UI
        updateTimerDisplay();
        updateControlButtonStates();
        resetTimerCardAppearance();
    }

    /**
     * Start the timer update loop
     */
    private void startTimerLoop() {
        if (isTimerRunning && !isTimerPaused) {
            workoutTimer.tick();

            // Schedule next update
            uiHandler.postDelayed(this::startTimerLoop, 100); // Update every 100ms for smooth display
        }
    }

    /**
     * Update timer display
     */
    private void updateTimerDisplay() {
        if (workoutTimer != null) {
            tvTimerDisplay.setText(workoutTimer.getFormattedTime());

            // Update timer card color based on progress
            updateTimerCardProgress();
        }
    }

    /**
     * Update timer card appearance based on progress
     */
    private void updateTimerCardProgress() {
        if (workoutTimer == null) return;

        float progress = workoutTimer.getProgress();
        int remainingSeconds = workoutTimer.getRemainingSeconds();

        // Change color as timer progresses
        int cardColor;
        if (remainingSeconds <= 5 && isTimerRunning) {
            // Final countdown - red
            cardColor = getResources().getColor(R.color.error);
        } else if (remainingSeconds <= 10 && isTimerRunning) {
            // Warning phase - orange
            cardColor = getResources().getColor(R.color.warning);
        } else if (isTimerRunning) {
            // Active phase - primary color
            cardColor = getResources().getColor(R.color.timer_background);
        } else {
            // Inactive phase - default
            cardColor = getResources().getColor(R.color.timer_background);
        }

        timerCard.setCardBackgroundColor(cardColor);

        // Pulse effect for final countdown
        if (remainingSeconds <= 3 && remainingSeconds > 0 && isTimerRunning) {
            addPulseEffect();
        }
    }

    /**
     * Add pulse effect for final countdown
     */
    private void addPulseEffect() {
        timerCard.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(300)
                .withEndAction(() -> {
                    timerCard.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(300)
                            .start();
                })
                .start();
    }

    /**
     * Update control button states based on timer status
     */
    private void updateControlButtonStates() {
        if (isTimerRunning) {
            btnStart.setEnabled(false);
            btnStart.setAlpha(0.5f);
            btnPause.setEnabled(true);
            btnPause.setAlpha(1.0f);
        } else {
            btnStart.setEnabled(true);
            btnStart.setAlpha(1.0f);
            btnPause.setEnabled(false);
            btnPause.setAlpha(0.5f);
        }

        btnReset.setEnabled(true);
        btnReset.setAlpha(1.0f);

        // Disable timer selection during active timer
        btn30Sec.setEnabled(!isTimerRunning);
        btn60Sec.setEnabled(!isTimerRunning);
        btn30Sec.setAlpha(isTimerRunning ? 0.5f : 1.0f);
        btn60Sec.setAlpha(isTimerRunning ? 0.5f : 1.0f);
    }

    /**
     * Animate timer start
     */
    private void animateTimerStart() {
        timerCard.animate()
                .scaleX(1.1f)
                .scaleY(1.1f)
                .setDuration(200)
                .withEndAction(() -> {
                    timerCard.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(200)
                            .start();
                })
                .start();
    }

    /**
     * Reset timer card appearance
     */
    private void resetTimerCardAppearance() {
        timerCard.setCardBackgroundColor(getResources().getColor(R.color.timer_background));
        timerCard.setScaleX(1.0f);
        timerCard.setScaleY(1.0f);
    }

    // WorkoutTimer.TimerListener implementation

    @Override
    public void onTick(int remainingSeconds) {
        uiHandler.post(this::updateTimerDisplay);
    }

    @Override
    public void onFinish() {
        uiHandler.post(() -> {
            // Timer completed
            isTimerRunning = false;
            isTimerPaused = false;
            completedCycles++;

            // Play completion sound
            playCompletionSound();

            // Show completion feedback
            showCompletionFeedback();

            // Update UI
            updateControlButtonStates();
            resetTimerCardAppearance();

            // Auto-reset for next cycle
            resetTimer();
        });
    }

    @Override
    public void onTimerStart() {
        // Timer started - already handled in startTimer()
    }

    @Override
    public void onTimerPause() {
        // Timer paused - already handled in pauseResumeTimer()
    }

    @Override
    public void onTimerResume() {
        // Timer resumed - already handled in pauseResumeTimer()
        uiHandler.post(() -> startTimerLoop());
    }

    /**
     * Play completion sound if enabled
     */
    private void playCompletionSound() {
        if (soundEnabled && completionSound != null) {
            try {
                if (completionSound.isPlaying()) {
                    completionSound.stop();
                    completionSound.prepare();
                }
                completionSound.start();
            } catch (Exception e) {
                android.util.Log.w(TAG, "Could not play completion sound", e);
            }
        }

        // Haptic feedback
        performHapticFeedback();
    }

    /**
     * Show completion feedback
     */
    private void showCompletionFeedback() {
        // Celebration animation
        timerCard.animate()
                .scaleX(1.2f)
                .scaleY(1.2f)
                .setDuration(300)
                .withEndAction(() -> {
                    timerCard.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(300)
                            .start();
                })
                .start();

        // Show completion message
        String message = "Timer completed! Cycle " + completedCycles + " ✅";
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show();
    }

    /**
     * Provide haptic feedback
     */
    private void performHapticFeedback() {
        try {
            android.os.Vibrator vibrator = (android.os.Vibrator) getSystemService(VIBRATOR_SERVICE);
            if (vibrator != null && vibrator.hasVibrator()) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    // Create a pattern: short-long-short vibration
                    long[] pattern = {0, 100, 100, 200, 100, 100};
                    int[] amplitudes = {0, 255, 0, 255, 0, 255};
                    vibrator.vibrate(android.os.VibrationEffect.createWaveform(pattern, amplitudes, -1));
                } else {
                    long[] pattern = {0, 100, 100, 200, 100, 100};
                    vibrator.vibrate(pattern, -1);
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
        if (isTimerRunning) {
            // Show confirmation dialog if timer is running
            showExitConfirmationDialog();
        } else {
            finishWorkout();
        }
    }

    /**
     * Show confirmation dialog when trying to exit during active timer
     */
    private void showExitConfirmationDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Exit Timer?")
                .setMessage("Your timer is still running. Do you want to stop and exit?")
                .setPositiveButton("Continue Timer", (dialog, which) -> dialog.dismiss())
                .setNegativeButton("Stop & Exit", (dialog, which) -> {
                    resetTimer();
                    finishWorkout();
                })
                .show();
    }

    /**
     * Finish workout and return to previous activity
     */
    private void finishWorkout() {
        // Calculate total workout time
        long totalTime = System.currentTimeMillis() - sessionStartTime;
        int actualDurationMinutes = (int) (totalTime / (1000 * 60));

        // Prepare result intent
        Intent resultIntent = new Intent();
        resultIntent.putExtra("actual_duration", actualDurationMinutes);
        resultIntent.putExtra("completed_cycles", completedCycles);
        resultIntent.putExtra("session_id", sessionId);

        if (completedCycles > 0) {
            // Workout was completed
            setResult(RESULT_OK, resultIntent);
        } else {
            // Workout was cancelled
            setResult(RESULT_CANCELED, resultIntent);
        }

        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    /**
     * Clean up resources
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Clean up timer
        if (workoutTimer != null) {
            workoutTimer.stop();
        }

        // Clean up audio
        if (completionSound != null) {
            completionSound.release();
            completionSound = null;
        }

        // Clean up handler
        if (uiHandler != null) {
            uiHandler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * Handle activity pause
     */
    @Override
    protected void onPause() {
        super.onPause();
        // Timer continues running in background
    }

    /**
     * Handle activity resume
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Resume UI updates if timer is running
        if (isTimerRunning && !isTimerPaused) {
            startTimerLoop();
        }

        // Update display
        updateTimerDisplay();
    }
}