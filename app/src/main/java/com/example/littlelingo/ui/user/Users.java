package com.example.littlelingo.ui.user;

import java.util.HashMap;
import java.util.Map;

public class Users {
    private String userId;
    private String name;
    private int age;
    private String nativeLanguage;
    private String dateOfBirth; // Format: YYYY-MM-DD
    private Map<String, Integer> scores; // Map to store scores with unique IDs

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public Users() {
        // Initialize scores map to avoid null pointer exceptions
        scores = new HashMap<>();
    }

    // Constructor with parameters
    public Users(String userId, String name, int age, String nativeLanguage, String dateOfBirth) {
        this.userId = userId;
        this.name = name;
        this.age = age;
        this.nativeLanguage = nativeLanguage;
        this.dateOfBirth = dateOfBirth;
        this.scores = new HashMap<>();
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNativeLanguage() {
        return nativeLanguage;
    }

    public void setNativeLanguage(String nativeLanguage) {
        this.nativeLanguage = nativeLanguage;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Map<String, Integer> getScores() {
        return scores;
    }

    public void setScores(Map<String, Integer> scores) {
        this.scores = scores;
    }

    // Method to add a score
    public void addScore(String scoreId, int score) {
        this.scores.put(scoreId, score);
    }
}

