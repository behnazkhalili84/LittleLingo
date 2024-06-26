package com.example.littlelingo.ui.vocabularyquiz;

import android.annotation.SuppressLint;
import android.os.Bundle;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import java.util.ArrayList;

import com.example.littlelingo.R;
import com.example.littlelingo.ui.learningvocabulary.Word;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class VocabularyQuiz extends Fragment {

    TextView tvQuestion, tvProgress, tvOptionOne, tvOptionTwo, tvOptionThree, tvOptionFour;
    ImageView ivImage;
    ProgressBar progressBar;

    MaterialButton btnSubmit;

    private DatabaseReference mDatabase;
    private List<VocabularyQuestion> questionsList = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int selectedOptionPosition = 0;
    private boolean isAnswerSubmitted = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference("questions");
        loadQuestions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vocabulary_quiz, container, false);
        setupUI(view);
        setupListeners();
        return view;
    }

    @SuppressLint("WrongViewCast")
    private void setupUI(View view) {
        tvQuestion = view.findViewById(R.id.tv_question);
        ivImage = view.findViewById(R.id.iv_image);
        progressBar = view.findViewById(R.id.progressBar);
        tvProgress = view.findViewById(R.id.tv_progress);
        tvOptionOne = view.findViewById(R.id.tv_option_one);
        tvOptionTwo = view.findViewById(R.id.tv_option_two);
        tvOptionThree = view.findViewById(R.id.tv_option_three);
        tvOptionFour = view.findViewById(R.id.tv_option_four);
        btnSubmit = view.findViewById(R.id.btn_submit);
    }

    private void setupListeners() {
        tvOptionOne.setOnClickListener(view -> onOptionSelected(1));
        tvOptionTwo.setOnClickListener(view -> onOptionSelected(2));
        tvOptionThree.setOnClickListener(view -> onOptionSelected(3));
        tvOptionFour.setOnClickListener(view -> onOptionSelected(4));
        btnSubmit.setOnClickListener(view -> onSubmitClicked());
    }

    private void onOptionSelected(int optionPosition) {
        selectedOptionPosition = optionPosition;
        tvOptionOne.setBackgroundResource(optionPosition == 1 ? R.drawable.selected_option_border_bg : R.drawable.default_option_border_bg);
        tvOptionTwo.setBackgroundResource(optionPosition == 2 ? R.drawable.selected_option_border_bg : R.drawable.default_option_border_bg);
        tvOptionThree.setBackgroundResource(optionPosition == 3 ? R.drawable.selected_option_border_bg : R.drawable.default_option_border_bg);
        tvOptionFour.setBackgroundResource(optionPosition == 4 ? R.drawable.selected_option_border_bg : R.drawable.default_option_border_bg);
    }

    private void onSubmitClicked() {
        if (isAnswerSubmitted) {
            if (currentQuestionIndex < questionsList.size()) {
                loadNextQuestion();
                btnSubmit.setText("SUBMIT");
                isAnswerSubmitted = false;
            } else {
                navigateToResult();
            }
        } else {
            if (selectedOptionPosition == 0) {
                Toast.makeText(getContext(), "Please select an option", Toast.LENGTH_SHORT).show();
            } else {
                checkAnswer();
                isAnswerSubmitted = true;
                btnSubmit.setText(currentQuestionIndex == questionsList.size() - 1 ? "FINISH" : "NEXT");
            }
        }
    }

    private void checkAnswer() {
        VocabularyQuestion currentQuestion = questionsList.get(currentQuestionIndex);
        int correctAnswerPosition = Integer.parseInt(currentQuestion.getCorrectAnswer());

        if (selectedOptionPosition == correctAnswerPosition) {
            showAnswerFeedback(correctAnswerPosition, R.drawable.correct_option_border_bg);
            Toast.makeText(getContext(), "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            showAnswerFeedback(correctAnswerPosition, R.drawable.correct_option_border_bg);
            showAnswerFeedback(selectedOptionPosition, R.drawable.wrong_option_border_bg);
            Toast.makeText(getContext(), "Wrong!", Toast.LENGTH_SHORT).show();
        }

        currentQuestionIndex++;
    }

    private void showAnswerFeedback(int optionPosition, int drawableId) {
        switch (optionPosition) {
            case 1:
                tvOptionOne.setBackgroundResource(drawableId);
                break;
            case 2:
                tvOptionTwo.setBackgroundResource(drawableId);
                break;
            case 3:
                tvOptionThree.setBackgroundResource(drawableId);
                break;
            case 4:
                tvOptionFour.setBackgroundResource(drawableId);
                break;
        }
    }

    private void loadNextQuestion() {
        resetSelectedOption();
        VocabularyQuestion nextQuestion = questionsList.get(currentQuestionIndex);
        tvQuestion.setText("What is it?");
        Picasso.get().load(nextQuestion.getImage()).into(ivImage);
        tvOptionOne.setText(nextQuestion.getOptionOne());
        tvOptionTwo.setText(nextQuestion.getOptionTwo());
        tvOptionThree.setText(nextQuestion.getOptionThree());
        tvOptionFour.setText(nextQuestion.getOptionFour());
        tvProgress.setText((currentQuestionIndex + 1) + "/" + questionsList.size());
        progressBar.setProgress((currentQuestionIndex + 1) * 100 / questionsList.size());
    }

    private void resetSelectedOption() {
        selectedOptionPosition = 0;
        tvOptionOne.setBackgroundResource(R.drawable.default_option_border_bg);
        tvOptionTwo.setBackgroundResource(R.drawable.default_option_border_bg);
        tvOptionThree.setBackgroundResource(R.drawable.default_option_border_bg);
        tvOptionFour.setBackgroundResource(R.drawable.default_option_border_bg);
    }

    private void loadQuestions() {
        mDatabase.orderByChild("questionType").equalTo("vocabulary").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<VocabularyQuestion> allQuestions = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    VocabularyQuestion question = snapshot.getValue(VocabularyQuestion.class);
                    allQuestions.add(question);
                }

                List<VocabularyQuestion> selectedQuestions = getRandomQuestions(allQuestions, 4);

                questionsList.addAll(selectedQuestions);

                if (!questionsList.isEmpty()) {
                    loadNextQuestion();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load questions", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<VocabularyQuestion> getRandomQuestions(List<VocabularyQuestion> questions, int count) {
        List<VocabularyQuestion> selectedQuestions = new ArrayList<>();
        Random random = new Random();
        Set<Integer> selectedIndices = new HashSet<>();

        while (selectedIndices.size() < count) {
            int randomIndex = random.nextInt(questions.size());
            if (!selectedIndices.contains(randomIndex)) {
                selectedIndices.add(randomIndex);
                selectedQuestions.add(questions.get(randomIndex));
            }
        }

        return selectedQuestions;
    }


    private void navigateToResult() {
        // Implement navigation to the result screen
    }


    private void addQuestions() {
//
///Question1
        VocabularyQuestion vocabularyQuestion1 = new VocabularyQuestion(1, "gs://littlelingo-6bcce.appspot.com/apple.jpeg",
                "drink", "water",
                "apple", "nose", "3");

        // Add the word to the database
        mDatabase.child("questions").child("1").setValue(vocabularyQuestion1);

        ///Question2

        VocabularyQuestion vocabularyQuestion2 = new VocabularyQuestion(2, "gs://littlelingo-6bcce.appspot.com/water.jpeg",
                "juice", "water",
                "apple", "nose", "2");

        // Add the word to the database
        mDatabase.child("questions").child("2").setValue(vocabularyQuestion2);

        ///Question3
        VocabularyQuestion vocabularyQuestion3 = new VocabularyQuestion(3, "gs://littlelingo-6bcce.appspot.com/juice.jpeg",
                "juice", "water",
                "apple", "nose", "1");

        // Add the word to the database
        mDatabase.child("questions").child("3").setValue(vocabularyQuestion3);

        ///Question3

        VocabularyQuestion vocabularyQuestion4 = new VocabularyQuestion(4, "gs://littlelingo-6bcce.appspot.com/drink.ogg",
                "juice", "water",
                "drink", "nose", "3");

        // Add the word to the database
        mDatabase.child("questions").child("4").setValue(vocabularyQuestion4);

        ///Question5
        VocabularyQuestion vocabularyQuestion5 = new VocabularyQuestion(5, "gs://littlelingo-6bcce.appspot.com/toes.jpeg",
                "Toes", "water",
                "drink", "nose", "1");

        // Add the word to the database
        mDatabase.child("questions").child("5").setValue(vocabularyQuestion5);

        ///Question6

        VocabularyQuestion vocabularyQuestion6 = new VocabularyQuestion(6, "gs://littlelingo-6bcce.appspot.com/nose.jpg",
                "Noes", "Toes",
                "water", "drink", "1");

        // Add the word to the database
        mDatabase.child("questions").child("6").setValue(vocabularyQuestion6);

        ///Question7

        VocabularyQuestion vocabularyQuestion7 = new VocabularyQuestion(7, "nose",
                "Noes", "Toes",
                "water", "drink", "1");

        // Add the word to the database
        mDatabase.child("word").child("7").setValue(vocabularyQuestion7);

        ///Question6

        VocabularyQuestion vocabularyQuestion8 = new VocabularyQuestion(8, "nose",
                "Noes", "Toes",
                "water", "drink", "1");

        // Add the word to the database
        mDatabase.child("word").child("8").setValue(vocabularyQuestion8);
    }
}
