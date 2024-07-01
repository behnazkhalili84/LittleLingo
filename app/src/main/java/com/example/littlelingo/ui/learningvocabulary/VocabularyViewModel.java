package com.example.littlelingo.ui.learningvocabulary;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.littlelingo.ui.vocabularyquiz.VocabularyQuestion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class VocabularyViewModel extends ViewModel {
    private static final String TAG = "LearningVocabulary";
MediaPlayer mediaPlayer = new MediaPlayer();
    private Context context;
    private DatabaseReference mDatabase;
    private  MutableLiveData<List<Word>> vocabularyList = new MutableLiveData<>();
    private  MutableLiveData<List<Word>> grammarList = new MutableLiveData<>();
    private  MutableLiveData<String> error = new MutableLiveData<>();
    private MutableLiveData<Integer> currentPosition = new MutableLiveData<>(1);

    public VocabularyViewModel(DatabaseReference databaseReference) {

        this.mDatabase = databaseReference;
        loadWord("Vocab"); // Load vocabulary words
        loadWord("Grammar"); // Load grammar words
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

    public LiveData<Integer> getCurrentPosition() {return currentPosition; }

    void loadWord(String type) { // Load vocabulary and grammar words if wordType is "vocab" or "vocab")
        Log.d(TAG, "Loading words from Firebase for type: " + type);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("word");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<Word> vocabularyWords = new ArrayList<>();
                List<Word> grammarWords = new ArrayList<>();

                for (DataSnapshot wordSnapshot : snapshot.getChildren()) {
                    Word word = wordSnapshot.getValue(Word.class);
                    if (word != null) {
                        if ("Vocab".equals(word.getWordType())) {
                            vocabularyWords.add(word);
                        } else if ("Grammar".equals(word.getWordType())) {
                            grammarWords.add(word);
                        }
                    }
                }
                // Update LiveData with fetched data
                vocabularyList.setValue(vocabularyWords);
                grammarList.setValue(grammarWords);

                Log.d(TAG, "Words loaded successfully. Vocabulary Count: " + vocabularyWords.size()
                        + ", Grammar Count: " + grammarWords.size());
            }

            public void onCancelled(DatabaseError databaseError) {
                // Handle error
                error.setValue("Failed to load words: " + databaseError.getMessage());
                Log.e(TAG, "Failed to load words: " + databaseError.getMessage());
            }
        });
    }

    public void playAudio(String audio) {
        // Play audio
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("word");
        StorageReference audioRef = FirebaseStorage.getInstance().getReferenceFromUrl(audio);
        Log.e("AudioRef", "Failed to load audio");

        audioRef.getDownloadUrl().addOnSuccessListener(uri -> {
            mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(uri.toString());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("LearningVocabulary", "Failed to load audio", e);
        }
        }).addOnFailureListener(e -> {
            error.setValue("Failed to load audio: " + e.getMessage());
            Log.e(TAG, "Failed to load audio: " + e.getMessage());
        });
    }
    protected void onCleared() {
        super.onCleared();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

     }

