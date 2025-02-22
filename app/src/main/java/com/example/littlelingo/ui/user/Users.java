package com.example.littlelingo.ui.user;

import java.util.HashMap;
import java.util.Map;

public class Users {
    private String userId;
    private String name;
    private String email;
    private int age;
    private String nativeLanguage;
    private String imageLink;
    private String dateOfBirth; // Format: YYYY-MM-DD
    private Map<String, Map<String, Object>> scores; // Change to Map<String, Map<String, Object>>
    private String userRoll;
    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public Users() {
        // Initialize scores map to avoid null pointer exceptions
        scores = new HashMap<>();
    }

    public void setUserRoll(String userRoll) {
        this.userRoll = userRoll;
    }

    public String getUserRoll() {
        return userRoll;
    }

    // Constructor with parameters
    public Users(String userId, String name, String email, int age, String nativeLanguage, String dateOfBirth) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.age = age;
        this.nativeLanguage = nativeLanguage;
        this.dateOfBirth = dateOfBirth;
        this.scores = new HashMap<>();
        this.imageLink = "";
        this.userRoll = "user";
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

    public Map<String, Map<String, Object>> getScores() {
        return scores;
    }

    public void setScores(Map<String, Map<String, Object>> scores) {
        this.scores = scores;
    }

    public void addScoreDetail(String key, Map<String, Object> scoreDetail) {
        this.scores.put(key, scoreDetail);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", nativeLanguage='" + nativeLanguage + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", scores=" + scores +
                '}';
    }
}
