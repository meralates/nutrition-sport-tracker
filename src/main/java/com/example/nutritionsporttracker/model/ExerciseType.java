package com.example.nutritionsporttracker.model;

public enum ExerciseType {
    CARDIO(8.0),
    STRENGTH(6.0),
    FLEXIBILITY(2.5),
    BALANCE(3.0),
    OTHER(3.5);

    private final double met;

    ExerciseType(double met) {
        this.met = met;
    }

    public double getMet() {
        return met;
    }
}
