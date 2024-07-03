package com.example.littlelingo.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> name = new MutableLiveData<>();
    private final MutableLiveData<String> userID = new MutableLiveData<>();

    public void setName(String name) {
        this.name.setValue(name);
    }

    public LiveData<String> getName() {
        return name;
    }

    public void setUserID(String userID) {
        this.userID.setValue(userID);
    }

    public LiveData<String> getUserID() {
        return userID;
    }
}

