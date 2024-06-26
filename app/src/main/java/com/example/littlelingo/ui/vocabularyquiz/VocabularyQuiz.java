package com.example.littlelingo.ui.vocabularyquiz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.littlelingo.R;
import com.example.littlelingo.ui.learningvocabulary.Word;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VocabularyQuiz extends Fragment {



    private DatabaseReference mDatabase;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
//         addQuestions();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vocabulary_quiz, container, false);
    }

    private void addQuestions() {
//
///Question1
        VocabularyQuestion vocabularyQuestion1 = new VocabularyQuestion(1,"gs://littlelingo-6bcce.appspot.com/apple.jpeg",
                "drink", "water",
                "apple", "nose", "3");

        // Add the word to the database
        mDatabase.child("questions").child("1").setValue(vocabularyQuestion1);

        ///Question2

        VocabularyQuestion vocabularyQuestion2 = new VocabularyQuestion(2,"gs://littlelingo-6bcce.appspot.com/water.jpeg",
                "juice", "water",
                "apple", "nose", "2");

        // Add the word to the database
        mDatabase.child("questions").child("2").setValue(vocabularyQuestion2);

        ///Question3
        VocabularyQuestion vocabularyQuestion3 = new VocabularyQuestion(3,"gs://littlelingo-6bcce.appspot.com/juice.jpeg",
                "juice", "water",
                "apple", "nose", "1");

        // Add the word to the database
        mDatabase.child("questions").child("3").setValue(vocabularyQuestion3);

        ///Question3

        VocabularyQuestion vocabularyQuestion4 = new VocabularyQuestion(4,"gs://littlelingo-6bcce.appspot.com/drink.ogg",
                "juice", "water",
                "drink", "nose", "3");

        // Add the word to the database
        mDatabase.child("questions").child("4").setValue(vocabularyQuestion4);

        ///Question5
        VocabularyQuestion vocabularyQuestion5 = new VocabularyQuestion(5,"gs://littlelingo-6bcce.appspot.com/toes.jpeg",
                "Toes", "water",
                "drink", "nose", "1");

        // Add the word to the database
        mDatabase.child("questions").child("5").setValue(vocabularyQuestion5);

        ///Question6

        VocabularyQuestion vocabularyQuestion6 = new VocabularyQuestion(6,"gs://littlelingo-6bcce.appspot.com/nose.jpg",
                "Noes", "Toes",
                "water", "drink", "1");

        // Add the word to the database
        mDatabase.child("questions").child("6").setValue(vocabularyQuestion6);

        ///Question7
//
//        VocabularyQuestion vocabularyQuestion7 = new VocabularyQuestion(7,"nose",
//                "Noes", "Toes",
//                "water", "drink", "1");
//
//        // Add the word to the database
//        mDatabase.child("word").child("7").setValue(vocabularyQuestion7);
//
//        ///Question6
//
//        VocabularyQuestion vocabularyQuestion8 = new VocabularyQuestion(8,"nose",
//                "Noes", "Toes",
//                "water", "drink", "1");
//
//        // Add the word to the database
//        mDatabase.child("word").child("8").setValue(vocabularyQuestion8);
    }
}