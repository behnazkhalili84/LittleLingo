package com.example.littlelingo.ui.user;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthRepository {
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private MutableLiveData<Users> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private String authError ;

    //Constructor
    public AuthRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        userLiveData = new MutableLiveData<>();
        loggedOutLiveData = new MutableLiveData<>();
        authError = "";

        if (firebaseAuth.getCurrentUser() != null) {
            fetchUserData(firebaseAuth.getCurrentUser().getUid());
            loggedOutLiveData.postValue(false);
        }
    }

    public  void login(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener( task->{
           if(task.isSuccessful()) {
               fetchUserData(firebaseAuth.getCurrentUser().getUid());
           } else {
               userLiveData.postValue(null);
           }

        });
    }

    public  void register(String email, String password, String name){
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener( task->{
            if(task.isSuccessful()) {
                //userLiveData.postValue(firebaseAuth.getCurrentUser());
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    String userId = firebaseUser.getUid();
                    //Users user = new Users(userId, name, age, nativeLanguage, dateOfBirth);
                    Users user = new Users(userId, name,firebaseUser.getEmail(), 0, "", "");
                    databaseReference.child("users").child(userId).setValue(user).addOnCompleteListener(dbTask -> {
                        if (dbTask.isSuccessful()) {
                            userLiveData.postValue(user);
                        } else {
                            authError = dbTask.getException().getMessage();
                            userLiveData.postValue(null);
                        }
                    });
                }
            } else {
                authError = task.getException().getMessage();
                userLiveData.postValue(null);
            }

        });
    }

    private void fetchUserData(String userId) {
        databaseReference.child("users").child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Users user = task.getResult().getValue(Users.class);
                if (user != null) {
                    userLiveData.postValue(user);
                } else {
                    Log.e(TAG, "User data not found");
                    userLiveData.postValue(null); // Add this line to handle error case
                }
            } else {
                Log.e(TAG, "Failed to fetch user data: ", task.getException());
                userLiveData.postValue(null); // Add this line to handle error case
            }
        });
    }

    public void signOut(){
        firebaseAuth.signOut();
        loggedOutLiveData.postValue(true);
    }

    //Getters
    public LiveData<Users> getUserLiveData(){
        return userLiveData;
    }

    public LiveData<Boolean> getLoggedOutLiveData(){
        return loggedOutLiveData;
    }
    public String getAuthError() {
        return authError;
    }
}
