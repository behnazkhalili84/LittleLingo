package com.example.littlelingo.ui.learningvocabulary;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VocabularyViewModel extends ViewModel {
    private DatabaseReference mDatabase;
    private final MutableLiveData<List<Word>> vocabularyList = new MutableLiveData<>();
    private final MutableLiveData<List<Word>> grammarList = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public VocabularyViewModel(DatabaseReference databaseReference) {
        this.mDatabase = databaseReference;
    }

    public LiveData<List<Word>> getVocabularyList() {
        return vocabularyList;
    }
     public LiveData<List<Word>> getGrammarList() {
         return grammarList;
     }

     private LiveData<String> getError() {
         return error;
     }

    void loadWord(String vocab) {

        mDatabase.child("word").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Word>  vocabularyWords = new ArrayList<>();
                List<Word>  grammarWords = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                     Word word = snapshot.getValue(Word.class);
                    if(word != null) {
                        if(word.getWordType().equals("vocab")) {
                            vocabularyWords.add(word);
                        } else {
                            grammarWords.add(word);
                        }
                    }
                }
                // Set LiveData based on wordType
                if (!vocabularyWords.isEmpty()) {
                    vocabularyList.setValue(vocabularyWords);
                }

                if (!grammarWords.isEmpty()) {
                    grammarList.setValue(grammarWords);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
                error.setValue("Failed to load words");
            }

        });
    }


}
