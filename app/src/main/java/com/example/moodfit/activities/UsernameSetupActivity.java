package com.example.moodfit.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.moodfit.R;
import com.example.moodfit.models.OnboardingData;
import com.example.moodfit.models.User;
import com.example.moodfit.models.enums.DifficultyLevel;
import com.example.moodfit.utils.SharedPreferencesHelper;
import com.example.moodfit.utils.ValidationUtils;

/**
 * UsernameSetupActivity - Handles the complete onboarding flow
 * Steps: 1=Username, 2=Difficulty, 3=Preferences, 4=Categories
 */
public class UsernameSetupActivity extends AppCompatActivity {

    // Constants
    private static final String TAG = "UsernameSetupActivity";

    // Models
    private OnboardingData onboardingData;
    private SharedPreferencesHelper prefsHelper;

    // UI Components - Progress Indicators
    private View stepIndicator1, stepIndicator2, stepIndicator3, stepIndicator4;

    // UI Components - Step Containers
    private FrameLayout stepContentContainer;
    private LinearLayout stepUsername, stepDifficulty;

    // UI Components - Navigation
    private Button btnSkip, btnBack, btnNext;

    // UI Components - Username Step
    private EditText etUsername;
    private TextView tvCharacterCount;

    // UI Components - Difficulty Step
    private CardView cardBeginner, cardIntermediate, cardAdvanced;
    private ImageView checkBeginner, checkIntermediate, checkAdvanced;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username_setup);

        // Initialize models and helpers
        initializeModels();

        // Initialize UI components
        initializeViews();

        // Setup event listeners
        setupEventListeners();

        //setup modern back press handling
        setupBackPressHandler();

        // Initialize first step
        updateUIForCurrentStep();
    }

    /**
     * Initialize models and helper classes
     */
    private void initializeModels() {
        onboardingData = new OnboardingData();
        prefsHelper = new SharedPreferencesHelper(this);
    }

    /**
     * Initialize all UI components
     */
    private void initializeViews() {
        // Progress Indicators
        stepIndicator1 = findViewById(R.id.step_indicator_1);
        stepIndicator2 = findViewById(R.id.step_indicator_2);
        stepIndicator3 = findViewById(R.id.step_indicator_3);
        stepIndicator4 = findViewById(R.id.step_indicator_4);

        // Step Containers
        stepContentContainer = findViewById(R.id.step_content_container);
        stepUsername = findViewById(R.id.step_username);
        stepDifficulty = findViewById(R.id.step_difficulty);

        // Navigation Buttons
        btnSkip = findViewById(R.id.btn_skip);
        btnBack = findViewById(R.id.btn_back);
        btnNext = findViewById(R.id.btn_next);

        // Username Step Components
        etUsername = findViewById(R.id.et_username);
        tvCharacterCount = findViewById(R.id.tv_character_count);

        // Difficulty Step Components
        cardBeginner = findViewById(R.id.card_beginner);
        cardIntermediate = findViewById(R.id.card_intermediate);
        cardAdvanced = findViewById(R.id.card_advanced);
        checkBeginner = findViewById(R.id.check_beginner);
        checkIntermediate = findViewById(R.id.check_intermediate);
        checkAdvanced = findViewById(R.id.check_advanced);
    }

    /**
     * Setup all event listeners
     */
    private void setupEventListeners() {
        // Navigation button listeners
        btnNext.setOnClickListener(v -> handleNextStep());
        btnBack.setOnClickListener(v -> handleBackStep());
        btnSkip.setOnClickListener(v -> handleSkipStep());

        // Username input listeners
        setupUsernameInputListeners();

        // Difficulty selection listeners
        setupDifficultySelectionListeners();
    }

    /**
     * Setup username input validation and character counter
     */
    private void setupUsernameInputListeners() {
        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String username = s.toString().trim();

                // Update character counter
                tvCharacterCount.setText(username.length() + "/20");

                // Update onboarding data
                onboardingData.setUsername(username);

                // Validate and update UI
                updateNextButtonState();

                // Visual feedback for validation
                if (username.length() > 0) {
                    if (onboardingData.isUsernameValid()) {
                        etUsername.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_circle, 0);
                    } else {
                        etUsername.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_error, 0);
                    }
                } else {
                    etUsername.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Handle enter key
        etUsername.setOnEditorActionListener((v, actionId, event) -> {
            if (onboardingData.canProceedFromCurrentStep()) {
                handleNextStep();
                return true;
            }
            return false;
        });
    }

    /**
     * Setup difficulty selection listeners
     */
    private void setupDifficultySelectionListeners() {
        cardBeginner.setOnClickListener(v -> selectDifficulty(DifficultyLevel.BEGINNER));
        cardIntermediate.setOnClickListener(v -> selectDifficulty(DifficultyLevel.INTERMEDIATE));
        cardAdvanced.setOnClickListener(v -> selectDifficulty(DifficultyLevel.ADVANCED));
    }

    /**
     * Handle difficulty level selection
     */
    private void selectDifficulty(DifficultyLevel difficulty) {
        onboardingData.setSelectedDifficulty(difficulty);

        // Update visual feedback
        checkBeginner.setVisibility(difficulty == DifficultyLevel.BEGINNER ? View.VISIBLE : View.GONE);
        checkIntermediate.setVisibility(difficulty == DifficultyLevel.INTERMEDIATE ? View.VISIBLE : View.GONE);
        checkAdvanced.setVisibility(difficulty == DifficultyLevel.ADVANCED ? View.VISIBLE : View.GONE);

        // Update card backgrounds to show selection
        updateDifficultyCardStyles(difficulty);

        // Update next button state
        updateNextButtonState();
    }

    /**
     * Update difficulty card visual styles based on selection
     */
    private void updateDifficultyCardStyles(DifficultyLevel selectedDifficulty) {
        // Reset all cards to default style
        cardBeginner.setCardBackgroundColor(getResources().getColor(R.color.card_background));
        cardIntermediate.setCardBackgroundColor(getResources().getColor(R.color.card_background));
        cardAdvanced.setCardBackgroundColor(getResources().getColor(R.color.card_background));

        // Highlight selected card
        switch (selectedDifficulty) {
            case BEGINNER:
                cardBeginner.setCardBackgroundColor(getResources().getColor(R.color.primary_light));
                break;
            case INTERMEDIATE:
                cardIntermediate.setCardBackgroundColor(getResources().getColor(R.color.primary_light));
                break;
            case ADVANCED:
                cardAdvanced.setCardBackgroundColor(getResources().getColor(R.color.primary_light));
                break;
        }
    }

    /**
     * Handle next step navigation
     */
    private void handleNextStep() {
        if (!onboardingData.canProceedFromCurrentStep()) {
            showValidationError();
            return;
        }

        int currentStep = onboardingData.getCurrentStep();

        if (currentStep == OnboardingData.TOTAL_STEPS) {
            // Complete onboarding
            completeOnboarding();
        } else {
            // Move to next step
            onboardingData.nextStep();
            updateUIForCurrentStep();
        }
    }

    /**
     * Handle back step navigation
     */
    private void handleBackStep() {
        onboardingData.previousStep();
        updateUIForCurrentStep();
    }

    /**
     * Handle skip step (for optional steps)
     */
    private void handleSkipStep() {
        // For now, treat skip as next
        // In full implementation, you might want different logic per step
        handleNextStep();
    }

    /**
     * Complete the onboarding process
     */
    private void completeOnboarding() {
        try {
            // Mark onboarding as completed
            onboardingData.completeOnboarding();

            // Create user from onboarding data
            User user = onboardingData.createUser();

            // Save user data
            prefsHelper.saveUser(user);

            // Mark onboarding as completed in preferences
            prefsHelper.setOnboardingCompleted(true);

            // Navigate to HomeActivity
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            // Add transition animation
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            finish();

        } catch (Exception e) {
            // Handle any errors during user creation
            showErrorMessage("Error completing setup. Please try again.");
        }
    }

    /**
     * Update UI for the current step
     */
    private void updateUIForCurrentStep() {
        int currentStep = onboardingData.getCurrentStep();

        // Update progress indicators
        updateProgressIndicators(currentStep);

        // Update step visibility
        updateStepVisibility(currentStep);

        // Update navigation buttons
        updateNavigationButtons(currentStep);

        // Update next button state
        updateNextButtonState();
    }

    /**
     * Update progress indicator visual state
     */
    private void updateProgressIndicators(int currentStep) {
        // Reset all indicators
        stepIndicator1.setBackgroundColor(getResources().getColor(R.color.text_tertiary));
        stepIndicator2.setBackgroundColor(getResources().getColor(R.color.text_tertiary));
        stepIndicator3.setBackgroundColor(getResources().getColor(R.color.text_tertiary));
        stepIndicator4.setBackgroundColor(getResources().getColor(R.color.text_tertiary));

        // Highlight completed and current steps
        int primaryColor = getResources().getColor(R.color.primary_color);

        if (currentStep >= 1) stepIndicator1.setBackgroundColor(primaryColor);
        if (currentStep >= 2) stepIndicator2.setBackgroundColor(primaryColor);
        if (currentStep >= 3) stepIndicator3.setBackgroundColor(primaryColor);
        if (currentStep >= 4) stepIndicator4.setBackgroundColor(primaryColor);
    }

    /**
     * Update step container visibility
     */
    private void updateStepVisibility(int currentStep) {
        // Hide all steps
        stepUsername.setVisibility(View.GONE);
        stepDifficulty.setVisibility(View.GONE);
        // Add more steps here as they're implemented

        // Show current step
        switch (currentStep) {
            case OnboardingData.STEP_USERNAME:
                stepUsername.setVisibility(View.VISIBLE);
                // Focus on username input
                etUsername.requestFocus();
                break;
            case OnboardingData.STEP_DIFFICULTY:
                stepDifficulty.setVisibility(View.VISIBLE);
                break;
            // Add cases for steps 3 and 4 when implemented
            default:
                stepUsername.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * Update navigation button visibility and text
     */
    private void updateNavigationButtons(int currentStep) {
        // Back button visibility
        btnBack.setVisibility(currentStep > 1 ? View.VISIBLE : View.GONE);

        // Skip button visibility (hide for required steps)
        btnSkip.setVisibility(View.GONE); // Hide for now, can be enabled for optional steps

        // Next button text
        if (currentStep == OnboardingData.TOTAL_STEPS) {
            btnNext.setText("Get Started!");
        } else {
            btnNext.setText("Next");
        }
    }

    /**
     * Update next button enabled state based on current step validation
     */
    private void updateNextButtonState() {
        boolean canProceed = onboardingData.canProceedFromCurrentStep();
        btnNext.setEnabled(canProceed);

        // Visual feedback
        btnNext.setAlpha(canProceed ? 1.0f : 0.5f);
    }

    /**
     * Show validation error for current step
     */
    private void showValidationError() {
        int currentStep = onboardingData.getCurrentStep();
        String errorMessage = "";

        switch (currentStep) {
            case OnboardingData.STEP_USERNAME:
                errorMessage = ValidationUtils.getUsernameValidationError(onboardingData.getUsername());
                break;
            case OnboardingData.STEP_DIFFICULTY:
                errorMessage = "Please select your fitness level";
                break;
            default:
                errorMessage = "Please complete this step";
                break;
        }

        showErrorMessage(errorMessage);
    }

    /**
     * Show error message to user (you can customize this with Snackbar, Toast, etc.)
     */
    private void showErrorMessage(String message) {
        // For now, using Toast. In production, consider using Snackbar or custom dialog
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show();
    }

    /**
     * Handle back button press - Modern approach using OnBackPressedCallback
     */
    private void setupBackPressHandler() {
        getOnBackPressedDispatcher().addCallback(this, new androidx.activity.OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (onboardingData.getCurrentStep() > 1) {
                    handleBackStep();
                } else {
                    // Show confirmation dialog before exiting onboarding
                    showExitConfirmationDialog();
                }
            }
        });
    }
    /**
     * Handle back button press - Legacy approach (for older Android versions)
     * Remove this method if using only the modern approach above
     */
    @Override
    public void onBackPressed() {
        if (onboardingData.getCurrentStep() > 1) {
            handleBackStep();
        } else {
            // Show confirmation dialog before exiting onboarding
            showExitConfirmationDialog();
        }
        // Note: We intentionally don't call super.onBackPressed() here
        // because we're handling the back press ourselves
    }

    /**
     * Show confirmation dialog when user tries to exit onboarding
     */
    private void showExitConfirmationDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Exit Setup?")
                .setMessage("You'll need to complete this setup to use MoodFit.")
                .setPositiveButton("Continue Setup", (dialog, which) -> dialog.dismiss())
                .setNegativeButton("Exit", (dialog, which) -> {
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                })
                .show();
    }

    /**
     * Save current progress (in case user leaves and comes back)
     */
    @Override
    protected void onPause() {
        super.onPause();
        // Save current onboarding progress
        prefsHelper.saveOnboardingProgress(onboardingData);
    }

    /**
     * Restore progress if user returns
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Restore onboarding progress if it exists
        OnboardingData savedProgress = prefsHelper.getOnboardingProgress();
        if (savedProgress != null && !savedProgress.isOnboardingCompleted()) {
            onboardingData = savedProgress;
            updateUIForCurrentStep();

            // Restore UI state
            if (onboardingData.getUsername() != null) {
                etUsername.setText(onboardingData.getUsername());
            }
            if (onboardingData.getSelectedDifficulty() != null) {
                selectDifficulty(onboardingData.getSelectedDifficulty());
            }
        }
    }
}