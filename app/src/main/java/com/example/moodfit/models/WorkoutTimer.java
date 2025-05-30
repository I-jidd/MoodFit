package com.example.moodfit.models;

public class WorkoutTimer {
    private int totalSeconds;
    private int remainingSeconds;
    private boolean isRunning;
    private boolean isPaused;
    private long startTime;
    private long pausedTime;
    private TimerListener listener;

    public interface TimerListener {
        void onTick(int remainingSeconds);
        void onFinish();
        void onTimerStart();
        void onTimerPause();
        void onTimerResume();
    }

    // Constructors
    public WorkoutTimer(int seconds) {
        this.totalSeconds = seconds;
        this.remainingSeconds = seconds;
        this.isRunning = false;
        this.isPaused = false;
    }

    // Timer Control Methods
    public void start() {
        if (!isRunning && !isPaused) {
            isRunning = true;
            startTime = System.currentTimeMillis();
            if (listener != null) listener.onTimerStart();
        }
    }

    public void pause() {
        if (isRunning && !isPaused) {
            isPaused = true;
            pausedTime = System.currentTimeMillis();
            if (listener != null) listener.onTimerPause();
        }
    }

    public void resume() {
        if (isPaused) {
            isPaused = false;
            // Adjust start time to account for pause duration
            long pauseDuration = System.currentTimeMillis() - pausedTime;
            startTime += pauseDuration;
            if (listener != null) listener.onTimerResume();
        }
    }

    public void reset() {
        isRunning = false;
        isPaused = false;
        remainingSeconds = totalSeconds;
        startTime = 0;
        pausedTime = 0;
    }

    public void stop() {
        isRunning = false;
        isPaused = false;
        if (listener != null) listener.onFinish();
    }

    // Utility Methods
    public void tick() {
        if (isRunning && !isPaused) {
            long elapsed = (System.currentTimeMillis() - startTime) / 1000;
            remainingSeconds = Math.max(0, totalSeconds - (int) elapsed);

            if (listener != null) listener.onTick(remainingSeconds);

            if (remainingSeconds <= 0) {
                stop();
            }
        }
    }

    public String getFormattedTime() {
        int minutes = remainingSeconds / 60;
        int seconds = remainingSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public float getProgress() {
        return (float) (totalSeconds - remainingSeconds) / totalSeconds;
    }

    // Getters and Setters
    public int getTotalSeconds() { return totalSeconds; }
    public void setTotalSeconds(int totalSeconds) {
        this.totalSeconds = totalSeconds;
        this.remainingSeconds = totalSeconds;
    }

    public int getRemainingSeconds() { return remainingSeconds; }
    public boolean isRunning() { return isRunning; }
    public boolean isPaused() { return isPaused; }

    public TimerListener getListener() { return listener; }
    public void setListener(TimerListener listener) { this.listener = listener; }
}