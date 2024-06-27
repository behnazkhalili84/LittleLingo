package com.example.littlelingo.ui.vocabularyquiz;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class QuestionDao {

    public void  addQuestions(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        ///Question1
        VocabularyQuestion vocabularyQuestion1 = new VocabularyQuestion(1,"gs://littlelingo-6bcce.appspot.com/apple.jpeg",
                "drink", "water",
                "apple", "nose", "3","vocabulary");

        // Add the word to the database
        mDatabase.child("word").child("1").setValue(vocabularyQuestion1);

        ///Question2

        VocabularyQuestion vocabularyQuestion2 = new VocabularyQuestion(2,"gs://littlelingo-6bcce.appspot.com/water.jpeg",
                "juice", "water",
                "apple", "nose", "2","vocabulary");

        // Add the word to the database
        mDatabase.child("word").child("2").setValue(vocabularyQuestion2);

        ///Question3
        VocabularyQuestion vocabularyQuestion3 = new VocabularyQuestion(3,"gs://littlelingo-6bcce.appspot.com/juice.jpeg",
                "juice", "water",
                "apple", "nose", "1","vocabulary");

        // Add the word to the database
        mDatabase.child("word").child("3").setValue(vocabularyQuestion3);

        ///Question3

        VocabularyQuestion vocabularyQuestion4 = new VocabularyQuestion(4,"gs://littlelingo-6bcce.appspot.com/drink.ogg",
                "juice", "water",
                "drink", "nose", "3","vocabulary");

        // Add the word to the database
        mDatabase.child("word").child("4").setValue(vocabularyQuestion4);

        ///Question5
        VocabularyQuestion vocabularyQuestion5 = new VocabularyQuestion(5,"gs://littlelingo-6bcce.appspot.com/toes.jpeg",
                "Toes", "water",
                "drink", "nose", "1","vocabulary");

        // Add the word to the database
        mDatabase.child("word").child("5").setValue(vocabularyQuestion5);

        ///Question6

        VocabularyQuestion vocabularyQuestion6 = new VocabularyQuestion(6,"gs://littlelingo-6bcce.appspot.com/nose.jpg",
                "Noes", "Toes",
                "water", "drink", "1","vocabulary");

        // Add the word to the database
        mDatabase.child("word").child("6").setValue(vocabularyQuestion6);

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
