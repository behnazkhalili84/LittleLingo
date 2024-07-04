package com.example.littlelingo.ui.Learningrammar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.littlelingo.R;
import com.example.littlelingo.ui.learningvocabulary.ViewModelFactory;
import com.example.littlelingo.ui.learningvocabulary.VocabularyViewModel;
import com.example.littlelingo.ui.learningvocabulary.Word;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.concurrent.atomic.AtomicInteger;


public class learningGrammar extends Fragment {

    private static final String TAG = "LearningGrammar";
    private VocabularyViewModel gViewModel;
    private ViewModelProvider.Factory viewModelFactory;

    private DatabaseReference mDatabase;

    ImageView ivImage ;
    ProgressBar progressBar;
    TextView tvProgress,tvWordname,tvexample1,tvexample2,tvexample3;
    MaterialButton btnPlayAudio;
    private Button btnNext,btnBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        viewModelFactory = new ViewModelFactory(databaseReference);
        gViewModel = new ViewModelProvider(this, viewModelFactory).get(VocabularyViewModel.class);

        gViewModel.loadWord("Grammar");

        // Initialize Firebase Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Add words to the database
      // addWords();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_learning_grammar, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupUI(view);
    }

    private void setupUI(View view) {
        ivImage = view.findViewById(R.id.iv_image);
        progressBar = view.findViewById(R.id.progressBar);
        tvProgress = view.findViewById(R.id.tv_progress);
        tvWordname = view.findViewById(R.id.tv_wordname);
        tvexample1 = view.findViewById(R.id.tv_example1);
        tvexample2 = view.findViewById(R.id.tv_example2);
        tvexample3 = view.findViewById(R.id.tv_example3);
        btnPlayAudio = view.findViewById(R.id.btn_play_audio);
        btnNext = view.findViewById(R.id.btn_next);
        Button btnBack = view.findViewById(R.id.btn_back);

        // Observe vocabularyList LiveData from ViewModel
        gViewModel.getGrammarList().observe(getViewLifecycleOwner(), grammarList -> {
            Log.d(TAG, "Received grammar list with size: " + grammarList.size());
            if (grammarList != null && !grammarList.isEmpty()) {
                // Initialize with the first word
                AtomicInteger currentWordIndex = new AtomicInteger(0);
                Word currentWord = grammarList.get(currentWordIndex.get());

                // Update UI with initial word
                updateUI(currentWord);

                // Setup Next button click listener
                btnNext.setOnClickListener(v -> {
                    currentWordIndex.incrementAndGet();

                    if (currentWordIndex.get() < grammarList.size()) {
                        // Load next word
                        Word nextWord = grammarList.get(currentWordIndex.get());
                        updateUI(nextWord);

                        // Update progress
                        progressBar.setProgress(currentWordIndex.get() + 1);
                        tvProgress.setText((currentWordIndex.get() + 1) + "/" + grammarList.size());
                    } else {
                        // Handle end of list
                        Toast.makeText(getContext(), "End of grammar list", Toast.LENGTH_SHORT).show();
                    }
                });

                btnBack.setOnClickListener(v -> {
                    currentWordIndex.decrementAndGet();
                    if (currentWordIndex.get() >= 0) {
                        // Load previous word
                        Word previousWord = grammarList.get(currentWordIndex.get());
                        updateUI(previousWord);
                        // Update progress
                        progressBar.setProgress(currentWordIndex.get() +1);
                        tvProgress.setText((currentWordIndex.get() + 1) + "/" + grammarList.size());
                    } else {
                        // Handle end of list
                        Toast.makeText(getContext(), "Beginning of grammar list", Toast.LENGTH_SHORT).show();
                    }
                });

                // Set progress bar max and initial progress
                progressBar.setMax(grammarList.size());
                progressBar.setProgress(currentWordIndex.get() + 1);
                tvProgress.setText((currentWordIndex.get() + 1) + "/" + grammarList.size());

            }
        });
    }
    private void updateUI(Word word) {
        // Check if the image URL is valid
        if (word.getImage() != null && !word.getImage().isEmpty()) {
            Picasso.get().load(word.getImage()).into(ivImage);
        } else {
            // Handle the case where the image URL is empty or null
            // For example, load a placeholder image
            Picasso.get().load(R.drawable.apple).into(ivImage);
        }
        // Update UI components with data from Word object
        tvWordname.setText(word.getWordName());
        tvexample1.setText(word.getExample1());
        tvexample2.setText(word.getExample2());
        tvexample3.setText(word.getExample3());
        // Play audio
        btnPlayAudio.setOnClickListener(v -> {
            Log.d(TAG, "Playing audio for word: " + word.getWordName());
            gViewModel.playAudio(word.getAudio());
        });


    }

    private void addWords() {
        {

            Word word17 = new Word("17", "A", "Grammar", "",
                    "", "", "Alligator",
                    "Apple", "Ant");

            mDatabase.child("word").child("17").setValue(word17);


            Word word18 = new Word("18", "B", "Grammar", "",
                    "", "", "Balloon",
                    "bee", "Bear");

            mDatabase.child("word").child("18").setValue(word18);


            Word word19 = new Word("19", "C", "Grammar", "",
                    "", "", "Cat",
                    "Carrot", "Cow");

            mDatabase.child("word").child("19").setValue(word19);


            Word word20 = new Word("20", "D", "Grammar", "",
                    "", "", "Deer",
                    "Donut", "Discus throw");

            mDatabase.child("word").child("20").setValue(word20);


            Word word21 = new Word("21", "E", "Grammar", "",
                    "", "", "Elephant",
                    "Earth", "Eye");

            mDatabase.child("word").child("21").setValue(word21);


            Word word22 = new Word("22", "F", "Grammar", "",
                    "", "", "Fish",
                    "Flower", "Frog");

            mDatabase.child("word").child("22").setValue(word22);


            Word word23 = new Word("23", "G", "Grammar", "",
                    "", "", "Grapes",
                    "Guitar", "Gift box");

            mDatabase.child("word").child("23").setValue(word23);


            Word word24 = new Word("24", "H", "Grammar", "",
                    "", "", "Horse",
                    "House", "Hambuirger");

            mDatabase.child("word").child("24").setValue(word24);


        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clean up listeners or resources here if needed
    }
}


