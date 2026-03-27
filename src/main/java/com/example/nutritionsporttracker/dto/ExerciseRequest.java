package com.example.nutritionsporttracker.dto;

import com.example.nutritionsporttracker.model.ExerciseType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ExerciseRequest {

    @NotNull
    private ExerciseType exerciseType;

    private String exerciseName;

    @NotNull
    @Min(1)
    private Integer durationMinutes;

    public ExerciseType getExerciseType() { return exerciseType; }
    public void setExerciseType(ExerciseType exerciseType) { this.exerciseType = exerciseType; }

    public String getExerciseName() { return exerciseName; }
    public void setExerciseName(String exerciseName) { this.exerciseName = exerciseName; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
}
