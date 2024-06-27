package com.example.littlelingo.ui.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthRepository {
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private String authError ;

    //Constructor
    public AuthRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        userLiveData = new MutableLiveData<>();
        loggedOutLiveData = new MutableLiveData<>();
        authError = "";

        if (firebaseAuth.getCurrentUser() != null) {
            userLiveData.postValue(firebaseAuth.getCurrentUser());
            loggedOutLiveData.postValue(false);
        }
    }

    public  void login(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener( task->{
           if(task.isSuccessful()) {
               userLiveData.postValue(firebaseAuth.getCurrentUser());
           } else {
               userLiveData.postValue(null);
           }

        });
    }

    public  void register(String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener( task->{
            if(task.isSuccessful()) {
                userLiveData.postValue(firebaseAuth.getCurrentUser());
            } else {
                authError = task.getException().getMessage();
                userLiveData.postValue(null);
            }

        });
    }

    public void signOut(){
        firebaseAuth.signOut();
        loggedOutLiveData.postValue(true);
    }

    //Getters
    public LiveData<FirebaseUser> getUserLiveData(){
        return userLiveData;
    }

    public LiveData<Boolean> getLoggedOutLiveData(){
        return loggedOutLiveData;
    }
    public String getAuthError() {
        return authError;
    }
}
