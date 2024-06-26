package com.example.littlelingo.ui.learningvocabulary;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.littlelingo.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class LearningVocabulary extends Fragment {
    private VocabularyViewModel viewModel;
    private ViewModelProvider.Factory viewModelFactory;

    private DatabaseReference mDatabase;

    private ImageView ivImage ;
            private ProgressBar progressBar;
            private TextView tvProgress;
            private TextView tvWordname;
            private TextView tvexampleSentence;
            private MaterialButton btnPlayAudio;
            private Button btnNext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        viewModelFactory = new ViewModelFactory(databaseReference);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(VocabularyViewModel.class);

        // Initialize Firebase Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Load vocabulary and grammar words
        viewModel.loadWord("vocab"); // Load vocabulary words
        viewModel.loadWord("grammar"); // Load grammar words

        // Add words to the database
//        addWords();
    }

    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_learning_vocabulary, container, false);

        // Initialize views here
        ivImage = view.findViewById(R.id.iv_image);
        progressBar = view.findViewById(R.id.progressBar);
        tvProgress = view.findViewById(R.id.tv_progress);
        tvWordname = view.findViewById(R.id.tv_wordname);
        tvexampleSentence = view.findViewById(R.id.tv_exampleSentence);
        btnPlayAudio = view.findViewById(R.id.btn_play_audio);
        btnNext = view.findViewById(R.id.btn_next);

return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup UI or start observing LiveData here
        setupUI();

    }

    private void setupUI() {
        viewModel.getVocabularyList().observe(getViewLifecycleOwner(), vocabularyList -> {

            if (vocabularyList != null && !vocabularyList.isEmpty()) {
                // Assuming wordID is zero-indexed in the list
                AtomicInteger currentWordIndex = new AtomicInteger(1);
                Word currentWord = vocabularyList.get(currentWordIndex.get());

                // Update UI components with the first word
                updateUI(currentWord);

                // Update progress indicators
                progressBar.setMax(vocabularyList.size());
                progressBar.setProgress(currentWordIndex.get() + 1);
                tvProgress.setText((currentWordIndex.get() + 1) + "/" + vocabularyList.size());

                // Handle button clicks or navigation to next word
                btnNext.setOnClickListener(v -> {
                    currentWordIndex.getAndIncrement();

                    // Check if index is within bounds
                    if (currentWordIndex.get() < vocabularyList.size()) {
                        Word nextWord = vocabularyList.get(currentWordIndex.get());// Move to next word
                        updateUI(nextWord);

                        // Update progress
                        progressBar.setProgress(currentWordIndex.get() + 1);
                        tvProgress.setText((currentWordIndex.get() + 1) + "/" + vocabularyList.size());
                    } else {
                        // Handle end of list, if needed
                        Toast.makeText(getContext(), "End of vocabulary list", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void updateUI(Word word) {
        // Update UI components with data from Word object
        ivImage.setImageURI(Uri.parse(word.getWordImage()));
        tvWordname.setText(word.getWordName());
        tvexampleSentence.setText(word.getExampleSentences());

        // You can update other UI components (image, audio, etc.) here as well
    }
    private void addWords () {
                {

                    Word word1 = new Word("1", "Apple", "Vocab", "Apple is a round fruit",
                            "gs://littlelingo-6bcce.appspot.com/apple.jpeg", "gs://littlelingo-6bcce.appspot.com/apple.ogg", "",
                            "", "");

                    // Add the word to the database
                    mDatabase.child("word").child("1").setValue(word1);

                    Word word2 = new Word("2", "Chair", "Vocab", "Sit on the chair",
                            "gs://littlelingo-6bcce.appspot.com/chair.jpeg", "gs://littlelingo-6bcce.appspot.com/chair.ogg", "",
                            "", "");

                    // Add the word to the database
                    mDatabase.child("word").child("2").setValue(word2);


                    Word word3 = new Word("3", "Daddy", "Vocab", "I am going to the park with my daddy",
                            "gs://littlelingo-6bcce.appspot.com/dady.jpeg", "gs://littlelingo-6bcce.appspot.com/daddy.ogg", "",
                            "", "");

                    // Add the word to the database
                    mDatabase.child("word").child("3").setValue(word3);

                    Word word4 = new Word("4", "Cup", "Vocab", "I drink tea in my cup",
                            "gs://littlelingo-6bcce.appspot.com/cup.jpeg", "gs://littlelingo-6bcce.appspot.com/cup.ogg", "",
                            "", "");

                    // Add the word to the database
                    mDatabase.child("word").child("4").setValue(word4);

                    Word word5 = new Word("5", "Juice", "Vocab", "I like orange juice",
                            "gs://littlelingo-6bcce.appspot.com/juice.jpeg", "gs://littlelingo-6bcce.appspot.com/juice.ogg", "",
                            "", "");

                    // Add the word to the database
                    mDatabase.child("word").child("5").setValue(word5);

                    Word word6 = new Word("6", "Toe", "Vocab", "I hurt my toe",
                            "gs://littlelingo-6bcce.appspot.com/toes.jpeg", "gs://littlelingo-6bcce.appspot.com/toe.ogg", "",
                            "", "");

                    // Add the word to the database
                    mDatabase.child("word").child("6").setValue(word6);

                    Word word7 = new Word("7", "Mouth", "Vocab", "A beautiful mouth",
                            "gs://littlelingo-6bcce.appspot.com/mouth.jpeg", "gs://littlelingo-6bcce.appspot.com/mouth.ogg", "",
                            "", "");

                    // Add the word to the database
                    mDatabase.child("word").child("7").setValue(word7);

                    Word word8 = new Word("8", "Nose", "Vocab", "A big nose",
                            "gs://littlelingo-6bcce.appspot.com/nose.jpg", "gs://littlelingo-6bcce.appspot.com/nose.ogg", "",
                            "", "");

                    // Add the word to the database
                    mDatabase.child("word").child("8").setValue(word8);

                    Word word9 = new Word("9", "Plate", "Vocab", "I eat in my plate",
                            "gs://littlelingo-6bcce.appspot.com/plate.png", "gs://littlelingo-6bcce.appspot.com/plate.ogg", "",
                            "", "");

                    // Add the word to the database
                    mDatabase.child("word").child("9").setValue(word9);

                    Word word10 = new Word("10", "Sweet", "Vocab", "Sweets can hurt your teeth",
                            "gs://littlelingo-6bcce.appspot.com/sweets.jpeg", "gs://littlelingo-6bcce.appspot.com/sweet.ogg", "",
                            "", "");

                    // Add the word to the database
                    mDatabase.child("word").child("10").setValue(word10);

                    Word word11 = new Word("11", "Table", "Vocab", "Please eat at the table",
                            "gs://littlelingo-6bcce.appspot.com/table.jpeg", "gs://littlelingo-6bcce.appspot.com/table.ogg", "",
                            "", "");

                    // Add the word to the database
                    mDatabase.child("word").child("11").setValue(word11);

                    Word word12 = new Word("12", "Water", "Vocab", "Drink water everyday for your health",
                            "gs://littlelingo-6bcce.appspot.com/water.jpeg", "gs://littlelingo-6bcce.appspot.com/water.ogg", "",
                            "", "");

                    // Add the word to the database
                    mDatabase.child("word").child("12").setValue(word12);

                    Word word13 = new Word("13", "Drink", "Vocab", "I like sweet drinks",
                            "gs://littlelingo-6bcce.appspot.com/drink.jpeg", "gs://littlelingo-6bcce.appspot.com/drink.ogg", "",
                            "", "");

                    // Add the word to the database
                    mDatabase.child("word").child("13").setValue(word13);

                    Word word14 = new Word("14", "Biscuit", "Vocab", "I eat biscuit with my milk",
                            "gs://littlelingo-6bcce.appspot.com/biscuit.jpeg", "gs://littlelingo-6bcce.appspot.com/biscuit.ogg", "",
                            "", "");

                    // Add the word to the database
                    mDatabase.child("word").child("14").setValue(word14);

                    Word word15 = new Word("15", "Milk", "Vocab", "Milk is my favorite drink",
                            "gs://littlelingo-6bcce.appspot.com/milk.jpeg", "gs://littlelingo-6bcce.appspot.com/milk.ogg", "",
                            "", "");

                    // Add the word to the database
                    mDatabase.child("word").child("15").setValue(word15);

                    Word word16 = new Word("16", "Mommy", "Vocab", "I love my mommy",
                            "gs://littlelingo-6bcce.appspot.com/mommy.jpeg", "gs://littlelingo-6bcce.appspot.com/mommy.ogg", "",
                            "", "");

                    // Add the word to the database
                    mDatabase.child("word").child("16").setValue(word16);

                }

            }
        }




