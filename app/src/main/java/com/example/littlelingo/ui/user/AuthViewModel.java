package com.example.littlelingo.ui.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;

public class AuthViewModel extends ViewModel {
    private  AuthRepository authRepository; // user auth model
    private LiveData<FirebaseUser> userLiveData;
    private LiveData<Boolean> loggedoutLiveData;

    //Constructor
    public AuthViewModel(){
        authRepository = new AuthRepository();
        userLiveData = authRepository.getUserLiveData();
        loggedoutLiveData = authRepository.getLoggedOutLiveData();
    }

    public void login(String email, String password){
        authRepository.login(email, password);
    }

    public void register(String email, String password){
        authRepository.register(email, password);
    }

    public void signOut(){
        authRepository.signOut();
    }

    //Getters
    public LiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<Boolean> getLoggedoutLiveData() {
        return loggedoutLiveData;
    }
}
