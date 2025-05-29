package com.example.moodfit.models;

import com.example.moodfit.models.enums.DifficultyLevel;
import com.example.moodfit.models.enums.MoodType;
import com.example.moodfit.models.enums.WorkoutCategory;

import java.util.ArrayList;
import java.util.List;

public class Exercise {
    private String exerciseId;
    private String name;
    private String description;
    private String instructions;
    private DifficultyLevel difficulty;
    private WorkoutCategory category;
    private int estimatedDurationMinutes;
    private int estimatedCalories;
    private String imageUrl;
    private String videoUrl;
    private boolean requiresEquipment;
    private String equipment;
    private int recommendedReps;
    private int recommendedSets;
    private List<String> targetMuscles;
    private List<MoodType> suitableForMoods;

    // Constructors
    public Exercise() {
        this.exerciseId = generateExerciseId();
        this.targetMuscles = new ArrayList<>();
        this.suitableForMoods = new ArrayList<>();
    }

    public Exercise(String name, String description, DifficultyLevel difficulty, WorkoutCategory category) {
        this();
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.category = category;
    }

    // Utility Methods
    private String generateExerciseId() {
        return "ex_" + System.currentTimeMillis();
    }

    public boolean isSuitableForMood(MoodType mood) {
        return suitableForMoods.contains(mood);
    }

    public void addTargetMuscle(String muscle) {
        if (!targetMuscles.contains(muscle)) {
            targetMuscles.add(muscle);
        }
    }

    public void addSuitableMood(MoodType mood) {
        if (!suitableForMoods.contains(mood)) {
            suitableForMoods.add(mood);
        }
    }

    // Getters and Setters
    public String getExerciseId() { return exerciseId; }
    public void setExerciseId(String exerciseId) { this.exerciseId = exerciseId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public DifficultyLevel getDifficulty() { return difficulty; }
    public void setDifficulty(DifficultyLevel difficulty) { this.difficulty = difficulty; }

    public WorkoutCategory getCategory() { return category; }
    public void setCategory(WorkoutCategory category) { this.category = category; }

    public int getEstimatedDurationMinutes() { return estimatedDurationMinutes; }
    public void setEstimatedDurationMinutes(int estimatedDurationMinutes) { this.estimatedDurationMinutes = estimatedDurationMinutes; }

    public int getEstimatedCalories() { return estimatedCalories; }
    public void setEstimatedCalories(int estimatedCalories) { this.estimatedCalories = estimatedCalories; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public boolean isRequiresEquipment() { return requiresEquipment; }
    public void setRequiresEquipment(boolean requiresEquipment) { this.requiresEquipment = requiresEquipment; }

    public String getEquipment() { return equipment; }
    public void setEquipment(String equipment) { this.equipment = equipment; }

    public int getRecommendedReps() { return recommendedReps; }
    public void setRecommendedReps(int recommendedReps) { this.recommendedReps = recommendedReps; }

    public int getRecommendedSets() { return recommendedSets; }
    public void setRecommendedSets(int recommendedSets) { this.recommendedSets = recommendedSets; }

    public List<String> getTargetMuscles() { return new ArrayList<>(targetMuscles); }
    public void setTargetMuscles(List<String> targetMuscles) { this.targetMuscles = new ArrayList<>(targetMuscles); }

    public List<MoodType> getSuitableForMoods() { return new ArrayList<>(suitableForMoods); }
    public void setSuitableForMoods(List<MoodType> suitableForMoods) { this.suitableForMoods = new ArrayList<>(suitableForMoods); }
}