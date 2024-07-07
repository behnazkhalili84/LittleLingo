package com.example.littlelingo.ui.user;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void fetchUserData(String userId) {
        databaseReference.child("users").child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot snapshot = task.getResult();
                if (snapshot.exists()) {
                    Users user = new Users();
                    user.setUserId(snapshot.child("userId").getValue(String.class));
                    user.setName(snapshot.child("name").getValue(String.class));
                    user.setEmail(snapshot.child("email").getValue(String.class));
                    user.setAge(snapshot.child("age").getValue(Integer.class));
                    user.setNativeLanguage(snapshot.child("nativeLanguage").getValue(String.class));
                    user.setDateOfBirth(snapshot.child("dateOfBirth").getValue(String.class));
                    user.setUserRoll(snapshot.child("userRoll").getValue(String.class));

                    Map<String, Map<String, Object>> scoresMap = new HashMap<>();
                    for (DataSnapshot scoreSnapshot : snapshot.child("scores").getChildren()) {
                        Map<String, Object> scoreDetails = (Map<String, Object>) scoreSnapshot.getValue();
                        scoresMap.put(scoreSnapshot.getKey(), scoreDetails);
                    }
                    user.setScores(scoresMap);

                    userLiveData.postValue(user);
                } else {
                    Log.e(TAG, "User data not found");
                    userLiveData.postValue(null);
                }
            } else {
                Log.e(TAG, "Failed to fetch user data: ", task.getException());
                userLiveData.postValue(null);
            }
        });
    }





    public void updateUser(Users user) {
        databaseReference.child("users").child(user.getUserId()).setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userLiveData.postValue(user);
                    } else {
                        Log.e(TAG, "Failed to update user data: ", task.getException());
                    }
                });
    }

    public void signOut(){
        firebaseAuth.signOut();
        loggedOutLiveData.postValue(true);
    }

    public void deleteAccount(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            user.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Account deleted from Firebase Authentication
                    deleteUserData(userId);
                } else {
                    // Handle failure
                    authError = task.getException().getMessage();
                }
            });
        }
    }

    private void deleteUserData(String userId) {
        databaseReference.child("users").child(userId).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Data deleted from Realtime Database
                userLiveData.postValue(null);
                loggedOutLiveData.postValue(true);
            } else {
                // Handle failure
                authError = task.getException().getMessage();
            }
        });
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
