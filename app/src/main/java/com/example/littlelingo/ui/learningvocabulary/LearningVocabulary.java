package com.example.littlelingo.ui.learningvocabulary;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.littlelingo.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class LearningVocabulary extends Fragment {

    private DatabaseReference mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // Add words to the database
        addWords();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_learning_vocabulary, container, false);

    }


    private void addWords() {
        for (int i = 1; i <= 100; i++) {
            String wordId = String.valueOf(i);
            Word word = new Word(wordId, "Apple" + i, "Vocab" + i, "Apple is a round fruit" + i,
                    "apple.jpeg" + i, "apple.ogg" + i, "" + i,
                    "" + i, "" + i);

            // Add the word to the database
            mDatabase.child("word").child(wordId).setValue(word);
        }
    }

}