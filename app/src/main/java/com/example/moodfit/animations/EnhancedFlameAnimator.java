package com.example.moodfit.animations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Enhanced Flame Animation System - FIXED VERSION
 * Creates realistic, dynamic flame animations that respond to streak levels
 * with organic movement patterns and visual effects
 */
public class EnhancedFlameAnimator {

    private static final String TAG = "EnhancedFlameAnimator";

    private Context context;
    private ImageView flameIcon;
    private View glowBackground;
    private Handler animationHandler;
    private Random random;

    // Animation state tracking
    private List<Animator> activeAnimators;
    private boolean isAnimating = false;
    private int currentStreakLevel = 0;

    // Animation parameters (updated for bigger flame)
    private static final int BASE_DURATION = 800;
    private static final float MAX_SCALE = 1.25f;  // Slightly less scale since flame is bigger
    private static final float MIN_SCALE = 0.95f;  // Less dramatic scaling
    private static final float MAX_ROTATION = 12f;  // Slightly reduced rotation
    private static final float GLOW_INTENSITY = 0.6f;

    // FIXED: Better color progression for different streak levels
    private int[] flameColors = {
            Color.parseColor("#94A3B8"), // Gray for no streak (0)
            Color.parseColor("#F59E0B"), // Orange for 1-2 days
            Color.parseColor("#F97316"), // Deeper orange for 3-5 days
            Color.parseColor("#EF4444"), // Red-orange for 6-10 days
            Color.parseColor("#DC2626"), // Red for 11-20 days
            Color.parseColor("#B91C1C"), // Deep red for 21-30 days
            Color.parseColor("#7C2D12")  // Epic dark red for 30+ days
    };

    public EnhancedFlameAnimator(Context context, ImageView flameIcon, View glowBackground) {
        this.context = context;
        this.flameIcon = flameIcon;
        this.glowBackground = glowBackground;
        this.animationHandler = new Handler(Looper.getMainLooper());
        this.random = new Random();
        this.activeAnimators = new ArrayList<>();

        // FIXED: Don't reset to default immediately - let the calling code set initial state
        initializeFlameState();
    }

    /**
     * FIXED: Initialize flame without clearing colors
     */
    private void initializeFlameState() {
        if (flameIcon != null) {
            flameIcon.setScaleX(1.0f);
            flameIcon.setScaleY(1.0f);
            flameIcon.setRotation(0f);
            flameIcon.setTranslationX(0f);
            flameIcon.setTranslationY(0f);
            // Don't clear color filter or set alpha here - let animateForStreak handle it
        }

        if (glowBackground != null) {
            glowBackground.setScaleX(1.0f);
            glowBackground.setScaleY(1.0f);
        }
    }

    /**
     * Start animations based on streak level
     * @param streakCount Current user streak count
     */
    public void animateForStreak(int streakCount) {
        // Stop any existing animations
        stopAllAnimations();

        currentStreakLevel = streakCount;

        // Apply streak-appropriate styling FIRST
        applyStreakStyling(streakCount);

        // Start appropriate animation pattern
        if (streakCount == 0) {
            animateNoStreak();
        } else if (streakCount == 1) {
            animateNewbieFlame();
        } else if (streakCount <= 3) {
            animateGrowingFlame();
        } else if (streakCount <= 7) {
            animateStrongFlame();
        } else if (streakCount <= 14) {
            animateRagingFlame();
        } else if (streakCount <= 30) {
            animateInfernoFlame();
        } else {
            animateLegendaryFlame();
        }

        isAnimating = true;
    }

    /**
     * FIXED: Better color selection logic
     */
    private void applyStreakStyling(int streakCount) {
        // Better color progression
        int colorIndex;
        if (streakCount == 0) {
            colorIndex = 0; // Gray
        } else if (streakCount <= 2) {
            colorIndex = 1; // Orange
        } else if (streakCount <= 5) {
            colorIndex = 2; // Deeper orange
        } else if (streakCount <= 10) {
            colorIndex = 3; // Red-orange
        } else if (streakCount <= 20) {
            colorIndex = 4; // Red
        } else if (streakCount <= 30) {
            colorIndex = 5; // Deep red
        } else {
            colorIndex = 6; // Epic dark red
        }

        int flameColor = flameColors[colorIndex];

        // Apply color filter to flame
        flameIcon.setColorFilter(flameColor);

        // FIXED: Better alpha progression
        float baseAlpha;
        if (streakCount == 0) {
            baseAlpha = 0.4f; // More visible than 0.3f
        } else if (streakCount == 1) {
            baseAlpha = 0.7f; // Good visibility for first day
        } else {
            baseAlpha = Math.min(0.8f + (streakCount * 0.02f), 1.0f);
        }
        flameIcon.setAlpha(baseAlpha);

        // Configure glow background if available
        if (glowBackground != null) {
            glowBackground.setBackground(createGlowDrawable(flameColor, streakCount));
            float glowAlpha = streakCount == 0 ? 0.2f : Math.min(0.3f + (streakCount * 0.03f), GLOW_INTENSITY);
            glowBackground.setAlpha(glowAlpha);
        }
    }

    /**
     * No streak - gentle dormant animation
     */
    private void animateNoStreak() {
        ObjectAnimator breathe = ObjectAnimator.ofFloat(flameIcon, "alpha", 0.3f, 0.5f);
        breathe.setDuration(3000);
        breathe.setRepeatCount(ValueAnimator.INFINITE);
        breathe.setRepeatMode(ValueAnimator.REVERSE);
        breathe.setInterpolator(new AccelerateDecelerateInterpolator());

        activeAnimators.add(breathe);
        breathe.start();
    }

    /**
     * FIXED: Day 1 - gentle awakening flame (uncommented and improved)
     */
    private void animateNewbieFlame() {
        // Gentle scale breathing
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(flameIcon, "scaleX", 1.0f, 1.08f);
        scaleX.setDuration(2000);
        scaleX.setRepeatCount(ValueAnimator.INFINITE);
        scaleX.setRepeatMode(ValueAnimator.REVERSE);
        scaleX.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(flameIcon, "scaleY", 1.0f, 1.08f);
        scaleY.setDuration(2000);
        scaleY.setRepeatCount(ValueAnimator.INFINITE);
        scaleY.setRepeatMode(ValueAnimator.REVERSE);
        scaleY.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator alpha = ObjectAnimator.ofFloat(flameIcon, "alpha", 0.7f, 0.9f);
        alpha.setDuration(2000);
        alpha.setRepeatCount(ValueAnimator.INFINITE);
        alpha.setRepeatMode(ValueAnimator.REVERSE);
        alpha.setInterpolator(new AccelerateDecelerateInterpolator());

        // Start all animations together
        activeAnimators.add(scaleX);
        activeAnimators.add(scaleY);
        activeAnimators.add(alpha);

        scaleX.start();
        scaleY.start();
        alpha.start();

        // Add subtle glow pulsing
        if (glowBackground != null) {
            animateGlowPulse(2500, 0.2f, 0.4f);
        }
    }

    /**
     * Days 2-3 - Growing flame with more movement
     */
    private void animateGrowingFlame() {
        // Primary flame dance
        startFlameDance(1500, 1.0f, 1.12f, 3f);

        // Add flickering effect
        startFlameFlicker(800, 0.7f, 1.0f);

        // Glow animation
        if (glowBackground != null) {
            animateGlowPulse(2000, 0.3f, 0.5f);
        }

        // Random micro-movements
        startMicroMovements(500, 2f);
    }

    /**
     * Days 4-7 - Strong flame with confident movement
     */
    private void animateStrongFlame() {
        // More pronounced flame dance
        startFlameDance(1200, 1.0f, 1.18f, 5f);

        // Enhanced flickering
        startFlameFlicker(600, 0.8f, 1.0f);

        // Add rotation sway
        startFlameRotationSway(2000, 8f);

        // Stronger glow
        if (glowBackground != null) {
            animateGlowPulse(1800, 0.4f, 0.6f);
        }

        // More frequent micro-movements
        startMicroMovements(300, 3f);

        // Add occasional intensity bursts
        scheduleIntensityBursts(5000);
    }

    /**
     * Days 8-14 - Raging flame with dynamic effects
     */
    private void animateRagingFlame() {
        // Vigorous flame dance
        startFlameDance(1000, 0.95f, 1.25f, 8f);

        // Rapid flickering
        startFlameFlicker(400, 0.85f, 1.0f);

        // Complex rotation patterns
        startComplexRotation(1500, 12f);

        // Intense glow with variation
        if (glowBackground != null) {
            animateVariableGlow(1200);
        }

        // Continuous micro-movements
        startMicroMovements(200, 4f);

        // Frequent intensity bursts
        scheduleIntensityBursts(3000);

        // Add heat wave effect
        startHeatWaveEffect(2500);
    }

    /**
     * Days 15-30 - Inferno flame with spectacular effects
     */
    private void animateInfernoFlame() {
        // Extreme flame dance
        startFlameDance(800, 0.9f, 1.3f, 12f);

        // Very rapid flickering
        startFlameFlicker(300, 0.9f, 1.0f);

        // Multi-axis rotation
        startMultiAxisRotation(1200);

        // Dramatic glow effects
        if (glowBackground != null) {
            animateDramaticGlow(1000);
        }

        // Constant movement
        startMicroMovements(150, 5f);

        // Very frequent bursts
        scheduleIntensityBursts(2000);

        // Heat wave and shimmer
        startHeatWaveEffect(2000);
        startShimmerEffect(1800);
    }

    /**
     * 30+ days - Legendary flame with epic effects
     */
    private void animateLegendaryFlame() {
        // Epic flame dance
        startFlameDance(600, 0.85f, 1.35f, 15f);

        // Lightning-fast flickering
        startFlameFlicker(200, 0.95f, 1.0f);

        // Complex multi-directional rotation
        startLegendaryRotation(1000);

        // Epic glow with color shifting
        if (glowBackground != null) {
            animateEpicGlow(800);
        }

        // Constant fluid movement
        startMicroMovements(100, 6f);

        // Continuous intensity variations
        scheduleIntensityBursts(1500);

        // All effects combined
        startHeatWaveEffect(1500);
        startShimmerEffect(1200);
        startColorShiftEffect(3000);

        // Occasional explosive bursts
        scheduleExplosiveBursts(8000);
    }

    /**
     * Core flame dance animation - organic scaling movement
     */
    private void startFlameDance(int duration, float minScale, float maxScale, float intensity) {
        // Create organic scaling pattern with individual animators
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(flameIcon, "scaleX",
                minScale, maxScale * (0.95f + random.nextFloat() * 0.1f), minScale);
        scaleXAnimator.setDuration(duration + (int)(random.nextFloat() * 400));
        scaleXAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleXAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(flameIcon, "scaleY",
                minScale, maxScale * (0.98f + random.nextFloat() * 0.04f), minScale);
        scaleYAnimator.setDuration(duration + (int)(random.nextFloat() * 300));
        scaleYAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleYAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        activeAnimators.add(scaleXAnimator);
        activeAnimators.add(scaleYAnimator);

        scaleXAnimator.start();
        scaleYAnimator.start();
    }

    /**
     * Flame flickering - alpha variations
     */
    private void startFlameFlicker(int duration, float minAlpha, float maxAlpha) {
        ObjectAnimator flicker = ObjectAnimator.ofFloat(flameIcon, "alpha", minAlpha, maxAlpha);
        flicker.setDuration(duration);
        flicker.setRepeatCount(ValueAnimator.INFINITE);
        flicker.setRepeatMode(ValueAnimator.REVERSE);
        flicker.setInterpolator(new CycleInterpolator(1.5f));

        activeAnimators.add(flicker);
        flicker.start();
    }

    /**
     * Simple rotation sway
     */
    private void startFlameRotationSway(int duration, float maxRotation) {
        ObjectAnimator rotationSway = ObjectAnimator.ofFloat(flameIcon, "rotation",
                -maxRotation/2, maxRotation/2);
        rotationSway.setDuration(duration);
        rotationSway.setRepeatCount(ValueAnimator.INFINITE);
        rotationSway.setRepeatMode(ValueAnimator.REVERSE);
        rotationSway.setInterpolator(new AccelerateDecelerateInterpolator());

        activeAnimators.add(rotationSway);
        rotationSway.start();
    }

    /**
     * Complex rotation patterns
     */
    private void startComplexRotation(int duration, float maxRotation) {
        ObjectAnimator rotation = ObjectAnimator.ofFloat(flameIcon, "rotation",
                -maxRotation, maxRotation/2, -maxRotation/3, maxRotation);
        rotation.setDuration(duration);
        rotation.setRepeatCount(ValueAnimator.INFINITE);
        rotation.setInterpolator(new AccelerateDecelerateInterpolator());

        activeAnimators.add(rotation);
        rotation.start();
    }

    /**
     * Multi-axis rotation for advanced flames
     */
    private void startMultiAxisRotation(int duration) {
        // Primary rotation
        ObjectAnimator mainRotation = ObjectAnimator.ofFloat(flameIcon, "rotation", 0f, 360f);
        mainRotation.setDuration(duration * 3);
        mainRotation.setRepeatCount(ValueAnimator.INFINITE);
        mainRotation.setInterpolator(new AccelerateDecelerateInterpolator());

        // Secondary sway
        ObjectAnimator sway = ObjectAnimator.ofFloat(flameIcon, "rotationY", -10f, 10f);
        sway.setDuration(duration);
        sway.setRepeatCount(ValueAnimator.INFINITE);
        sway.setRepeatMode(ValueAnimator.REVERSE);

        activeAnimators.add(mainRotation);
        activeAnimators.add(sway);
        mainRotation.start();
        sway.start();
    }

    /**
     * Legendary rotation with complex patterns
     */
    private void startLegendaryRotation(int duration) {
        // Create a complex rotation sequence without using AnimatorSet for repeating
        ObjectAnimator rotation1 = ObjectAnimator.ofFloat(flameIcon, "rotation", 0f, 120f);
        rotation1.setDuration(duration);
        rotation1.setInterpolator(new OvershootInterpolator());

        rotation1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isAnimating) return;

                ObjectAnimator rotation2 = ObjectAnimator.ofFloat(flameIcon, "rotation", 120f, -60f);
                rotation2.setDuration(duration);
                rotation2.setInterpolator(new OvershootInterpolator());

                rotation2.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (!isAnimating) return;

                        ObjectAnimator rotation3 = ObjectAnimator.ofFloat(flameIcon, "rotation", -60f, 360f);
                        rotation3.setDuration(duration * 2);
                        rotation3.setInterpolator(new OvershootInterpolator());

                        rotation3.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                if (isAnimating) {
                                    // Reset rotation and restart sequence
                                    flameIcon.setRotation(0f);
                                    startLegendaryRotation(duration);
                                }
                            }
                        });

                        activeAnimators.add(rotation3);
                        rotation3.start();
                    }
                });

                activeAnimators.add(rotation2);
                rotation2.start();
            }
        });

        activeAnimators.add(rotation1);
        rotation1.start();
    }

    /**
     * Micro-movements for realistic flame behavior
     */
    private void startMicroMovements(int interval, float maxOffset) {
        Runnable microMovementRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isAnimating) return;

                float offsetX = (random.nextFloat() - 0.5f) * maxOffset;
                float offsetY = (random.nextFloat() - 0.5f) * maxOffset;

                flameIcon.animate()
                        .translationX(offsetX)
                        .translationY(offsetY)
                        .setDuration(interval)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .start();

                animationHandler.postDelayed(this, interval);
            }
        };

        animationHandler.postDelayed(microMovementRunnable, interval);
    }

    /**
     * Glow background pulsing
     */
    private void animateGlowPulse(int duration, float minAlpha, float maxAlpha) {
        if (glowBackground == null) return;

        ObjectAnimator glowPulse = ObjectAnimator.ofFloat(glowBackground, "alpha", minAlpha, maxAlpha);
        glowPulse.setDuration(duration);
        glowPulse.setRepeatCount(ValueAnimator.INFINITE);
        glowPulse.setRepeatMode(ValueAnimator.REVERSE);
        glowPulse.setInterpolator(new AccelerateDecelerateInterpolator());

        activeAnimators.add(glowPulse);
        glowPulse.start();
    }

    /**
     * Variable glow animation
     */
    private void animateVariableGlow(int duration) {
        if (glowBackground == null) return;

        ObjectAnimator variableGlow = ObjectAnimator.ofFloat(glowBackground, "alpha",
                0.3f, 0.7f, 0.4f, 0.8f, 0.3f);
        variableGlow.setDuration(duration);
        variableGlow.setRepeatCount(ValueAnimator.INFINITE);
        variableGlow.setInterpolator(new AccelerateDecelerateInterpolator());

        activeAnimators.add(variableGlow);
        variableGlow.start();
    }

    /**
     * Dramatic glow effects
     */
    private void animateDramaticGlow(int duration) {
        if (glowBackground == null) return;

        // Create individual animators for dramatic glow
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(glowBackground, "alpha", 0.2f, 0.9f, 0.1f, 0.8f);
        alphaAnimator.setDuration(duration);
        alphaAnimator.setRepeatCount(ValueAnimator.INFINITE);
        alphaAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(glowBackground, "scaleX", 1.0f, 1.3f, 0.9f, 1.2f);
        scaleXAnimator.setDuration(duration);
        scaleXAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleXAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(glowBackground, "scaleY", 1.0f, 1.3f, 0.9f, 1.2f);
        scaleYAnimator.setDuration(duration);
        scaleYAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleYAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        activeAnimators.add(alphaAnimator);
        activeAnimators.add(scaleXAnimator);
        activeAnimators.add(scaleYAnimator);

        alphaAnimator.start();
        scaleXAnimator.start();
        scaleYAnimator.start();
    }

    /**
     * Epic glow with complex patterns
     */
    private void animateEpicGlow(int duration) {
        if (glowBackground == null) return;

        // Multi-stage epic glow using sequential animations
        ObjectAnimator phase1 = ObjectAnimator.ofFloat(glowBackground, "alpha", 0.2f, 0.9f);
        phase1.setDuration(duration);

        phase1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isAnimating) return;

                ObjectAnimator phase2 = ObjectAnimator.ofFloat(glowBackground, "alpha", 0.9f, 0.3f);
                phase2.setDuration(duration / 2);

                phase2.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (!isAnimating) return;

                        ObjectAnimator phase3 = ObjectAnimator.ofFloat(glowBackground, "alpha", 0.3f, 1.0f);
                        phase3.setDuration(duration);

                        phase3.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                if (isAnimating) {
                                    animateEpicGlow(duration);
                                }
                            }
                        });

                        activeAnimators.add(phase3);
                        phase3.start();
                    }
                });

                activeAnimators.add(phase2);
                phase2.start();
            }
        });

        activeAnimators.add(phase1);
        phase1.start();
    }

    /**
     * Schedule intensity bursts
     */
    private void scheduleIntensityBursts(int interval) {
        Runnable burstRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isAnimating) return;

                triggerIntensityBurst();
                animationHandler.postDelayed(this, interval + random.nextInt(2000));
            }
        };

        animationHandler.postDelayed(burstRunnable, interval);
    }

    /**
     * Trigger single intensity burst (updated for bigger flame)
     */
    private void triggerIntensityBurst() {
        flameIcon.animate()
                .scaleX(1.3f)  // Reduced from 1.4f since flame is bigger
                .scaleY(1.3f)
                .alpha(1.0f)
                .setDuration(300)
                .setInterpolator(new OvershootInterpolator())
                .withEndAction(() -> {
                    flameIcon.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(400)
                            .setInterpolator(new AccelerateDecelerateInterpolator())
                            .start();
                })
                .start();
    }

    /**
     * Heat wave effect
     */
    private void startHeatWaveEffect(int duration) {
        ObjectAnimator heatWave = ObjectAnimator.ofFloat(flameIcon, "scaleY", 1.0f, 1.1f, 0.95f, 1.05f);
        heatWave.setDuration(duration);
        heatWave.setRepeatCount(ValueAnimator.INFINITE);
        heatWave.setInterpolator(new CycleInterpolator(2f));

        activeAnimators.add(heatWave);
        heatWave.start();
    }

    /**
     * Shimmer effect
     */
    private void startShimmerEffect(int duration) {
        ObjectAnimator shimmer = ObjectAnimator.ofFloat(flameIcon, "alpha", 0.8f, 1.0f, 0.9f, 1.0f);
        shimmer.setDuration(duration);
        shimmer.setRepeatCount(ValueAnimator.INFINITE);
        shimmer.setInterpolator(new CycleInterpolator(3f));

        activeAnimators.add(shimmer);
        shimmer.start();
    }

    /**
     * Color shift effect for legendary flames
     */
    private void startColorShiftEffect(int duration) {
        // Cycle through different flame colors
        ValueAnimator colorShift = ValueAnimator.ofFloat(0f, 1f);
        colorShift.setDuration(duration);
        colorShift.setRepeatCount(ValueAnimator.INFINITE);
        colorShift.addUpdateListener(animator -> {
            float fraction = animator.getAnimatedFraction();
            int colorIndex = (int) (fraction * (flameColors.length - 1));
            flameIcon.setColorFilter(flameColors[Math.min(colorIndex, flameColors.length - 1)]);
        });

        activeAnimators.add(colorShift);
        colorShift.start();
    }

    /**
     * Explosive bursts for legendary flames
     */
    private void scheduleExplosiveBursts(int interval) {
        Runnable explosiveBurstRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isAnimating) return;

                triggerExplosiveBurst();
                animationHandler.postDelayed(this, interval + random.nextInt(5000));
            }
        };

        animationHandler.postDelayed(explosiveBurstRunnable, interval);
    }

    /**
     * Trigger explosive burst animation (updated for bigger flame)
     */
    private void triggerExplosiveBurst() {
        // Main flame explosion (scaled for bigger flame)
        flameIcon.animate()
                .scaleX(1.6f)  // Reduced from 1.8f
                .scaleY(1.6f)
                .alpha(1.0f)
                .rotation(flameIcon.getRotation() + 180f)
                .setDuration(200)
                .setInterpolator(new OvershootInterpolator(2f))
                .withEndAction(() -> {
                    flameIcon.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(800)
                            .setInterpolator(new BounceInterpolator())
                            .start();
                })
                .start();

        // Glow explosion (scaled for bigger flame)
        if (glowBackground != null) {
            glowBackground.animate()
                    .scaleX(1.7f)  // Reduced from 2.0f
                    .scaleY(1.7f)
                    .alpha(1.0f)
                    .setDuration(200)
                    .withEndAction(() -> {
                        glowBackground.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .alpha(0.5f)
                                .setDuration(600)
                                .start();
                    })
                    .start();
        }
    }

    /**
     * Create glow drawable for background
     */
    private android.graphics.drawable.Drawable createGlowDrawable(int color, int intensity) {
        android.graphics.drawable.GradientDrawable glowDrawable = new android.graphics.drawable.GradientDrawable();
        glowDrawable.setShape(android.graphics.drawable.GradientDrawable.OVAL);

        // Create glow effect with alpha
        int glowColor = Color.argb(Math.min(100 + intensity * 10, 200),
                Color.red(color), Color.green(color), Color.blue(color));
        glowDrawable.setColor(glowColor);

        return glowDrawable;
    }

    /**
     * FIXED: Reset flame to visible state (don't clear color filter)
     */
    private void resetFlameToDefault() {
        if (flameIcon != null) {
            flameIcon.setScaleX(1.0f);
            flameIcon.setScaleY(1.0f);
            flameIcon.setRotation(0f);
            flameIcon.setTranslationX(0f);
            flameIcon.setTranslationY(0f);
            // FIXED: Don't clear color filter or set very low alpha
            // flameIcon.setAlpha(0.3f);      // REMOVED - let applyStreakStyling handle this
            // flameIcon.clearColorFilter();  // REMOVED - let applyStreakStyling handle this
        }

        if (glowBackground != null) {
            glowBackground.setScaleX(1.0f);
            glowBackground.setScaleY(1.0f);
            // Don't set alpha here either
        }
    }

    /**
     * Stop all animations and reset state
     */
    public void stopAllAnimations() {
        isAnimating = false;

        // Cancel all active animators
        for (Animator animator : activeAnimators) {
            if (animator != null && animator.isRunning()) {
                animator.cancel();
            }
        }
        activeAnimators.clear();

        // Clear all pending callbacks
        animationHandler.removeCallbacksAndMessages(null);

        // Cancel view animations
        if (flameIcon != null) {
            flameIcon.animate().cancel();
        }
        if (glowBackground != null) {
            glowBackground.animate().cancel();
        }
    }

    /**
     * Pause animations
     */
    public void pauseAnimations() {
        isAnimating = false;
        animationHandler.removeCallbacksAndMessages(null);
    }

    /**
     * Resume animations
     */
    public void resumeAnimations() {
        if (currentStreakLevel >= 0) {
            animateForStreak(currentStreakLevel);
        }
    }

    /**
     * Check if currently animating
     */
    public boolean isAnimating() {
        return isAnimating;
    }

    /**
     * Get current streak level
     */
    public int getCurrentStreakLevel() {
        return currentStreakLevel;
    }

    /**
     * Cleanup resources
     */
    public void cleanup() {
        stopAllAnimations();
        context = null;
        flameIcon = null;
        glowBackground = null;
        animationHandler = null;
        activeAnimators = null;
    }
}