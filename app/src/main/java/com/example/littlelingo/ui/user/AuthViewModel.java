package com.example.littlelingo.ui.user;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends ViewModel {
    private  AuthRepository authRepository; // user auth model
    private LiveData<FirebaseUser> userLiveData;
    private LiveData<Boolean> loggedoutLiveData;
    public String authError;


    //Constructor
    public AuthViewModel(){
        authRepository = new AuthRepository();
        userLiveData = authRepository.getUserLiveData();
        loggedoutLiveData = authRepository.getLoggedOutLiveData();
        authError = authRepository.getAuthError();
    }

    public void login(String email, String password){
        authRepository.login(email, password);
    }

    public void register(String email, String password){
        authRepository.register(email, password);
        //Log.d(TAG, String.format("register: %s, %s", email, password));
    }

    public void signOut(){
        authRepository.signOut();
    }

    public boolean validateInput(String userName, String password, TextInputLayout userNameInputLayout, TextInputLayout passwordInputLayout) {
        boolean isValid = true;

        if (userName.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(userName).matches()) {
            userNameInputLayout.setError("Please enter a valid email address");
            isValid = false;
        } else {
            userNameInputLayout.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || !password.matches("^[a-zA-Z0-9.$]+$")) {
            passwordInputLayout.setError("Password must be at least 4 characters long and contain only letters, numbers, dots, and dollar signs");
            isValid = false;
        } else {
            passwordInputLayout.setError(null);
        }

        return isValid;
    }

    //Getters
    public LiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<Boolean> getLoggedoutLiveData() {
        return loggedoutLiveData;
    }
}
