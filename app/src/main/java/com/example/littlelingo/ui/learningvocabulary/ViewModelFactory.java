package com.example.littlelingo.ui.learningvocabulary;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DatabaseReference;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private DatabaseReference mDatabase;

    public ViewModelFactory(DatabaseReference databaseReference) {
        this.mDatabase = databaseReference;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(VocabularyViewModel.class)) {
            // Pass mDatabase to the VocabularyViewModel constructor
            return (T) new VocabularyViewModel(mDatabase);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
