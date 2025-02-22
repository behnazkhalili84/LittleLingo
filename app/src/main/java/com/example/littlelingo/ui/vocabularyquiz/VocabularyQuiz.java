package com.example.littlelingo.ui.vocabularyquiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import com.example.littlelingo.R;
import com.example.littlelingo.SignInActivity;
import com.example.littlelingo.ui.SharedViewModel;
import com.example.littlelingo.ui.VocabularyResult.ResultVocabulary;
import com.example.littlelingo.ui.quiz.Questions;
import com.example.littlelingo.ui.user.AuthViewModel;
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
    private AuthViewModel authViewModel;
    private List<Questions> questionsList = new ArrayList<>();
    private Set<String> askedQuestionIds = new HashSet<>();
    private int currentQuestionIndex = 0;
    private int selectedOptionPosition = 0;
    private boolean isAnswerSubmitted = false;
    private int currentScore = 0; // Initialize currentScore
    private String userId;
    private String username;



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
        // Access AuthViewModel
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        // Observe data from AuthViewModel
        authViewModel.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            userId = user.getUserId();
            username = user.getName();
            Toast.makeText(getContext(), "Name: " + user.getName(), Toast.LENGTH_SHORT).show();
            // you can user the variable name here
        });
//        sharedViewModel.getUserID().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(String id) {
//                userId = id;
//                Log.d("VocabularyQuiz", "UserID from ViewModel: " + userId);
//                if (userId == null) {
//                    Toast.makeText(getContext(), "Please login again", Toast.LENGTH_SHORT).show();
//                    navigateToLogin();
//                }
//            }
//        });
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
        Questions currentQuestion = questionsList.get(currentQuestionIndex);
        String correctAnswer = currentQuestion.getCorrectAnswer();
        int correctAnswerPosition = getCorrectAnswerPosition(currentQuestion, correctAnswer);

        if (selectedOptionPosition == correctAnswerPosition) {
            showAnswerFeedback(correctAnswerPosition, R.drawable.correct_option_border_bg);
//            Toast.makeText(getContext(), "Correct!", Toast.LENGTH_SHORT).show();
            currentScore++; // Increment the score if the answer is correct
            Log.d("VocabularyQuiz", "Score incremented: " + currentScore); // Debug log
        } else {
            showAnswerFeedback(correctAnswerPosition, R.drawable.correct_option_border_bg);
            showAnswerFeedback(selectedOptionPosition, R.drawable.wrong_option_border_bg);
//            Toast.makeText(getContext(), "Wrong!", Toast.LENGTH_SHORT).show();
        }

        currentQuestionIndex++;
    }

    private int getCorrectAnswerPosition(Questions question, String correctAnswer) {
        if (correctAnswer.equals(question.getOptionOne())) return 1;
        if (correctAnswer.equals(question.getOptionTwo())) return 2;
        if (correctAnswer.equals(question.getOptionThree())) return 3;
        if (correctAnswer.equals(question.getOptionFour())) return 4;
        return -1; // Invalid answer
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
        Questions nextQuestion = questionsList.get(currentQuestionIndex);
        shuffleOptions(nextQuestion);
        tvQuestion.setText("What is it?");
        Picasso.get().load(nextQuestion.getImage()).into(ivImage);
        tvOptionOne.setText(nextQuestion.getOptionOne());
        tvOptionTwo.setText(nextQuestion.getOptionTwo());
        tvOptionThree.setText(nextQuestion.getOptionThree());
        tvOptionFour.setText(nextQuestion.getOptionFour());
        tvProgress.setText((currentQuestionIndex + 1) + "/" + questionsList.size());
        progressBar.setProgress((currentQuestionIndex + 1) * 100 / questionsList.size());
    }

    private void shuffleOptions(Questions question) {
        List<String> options = new ArrayList<>();
        options.add(question.getOptionOne());
        options.add(question.getOptionTwo());
        options.add(question.getOptionThree());
        options.add(question.getOptionFour());
        Collections.shuffle(options);
        question.setOptionOne(options.get(0));
        question.setOptionTwo(options.get(1));
        question.setOptionThree(options.get(2));
        question.setOptionFour(options.get(3));
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
                List<Questions> allQuestions = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Questions question = snapshot.getValue(Questions.class);
                    allQuestions.add(question);
                    // Log the data fetched
                    Log.d("VocabularyQuiz", "Question fetched: " + question);
                }

                if (allQuestions.isEmpty()) {
                    Toast.makeText(getContext(), "No questions available from database.", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Questions> selectedQuestions = getRandomQuestions(allQuestions, 8);

                questionsList.addAll(selectedQuestions);

                if (!questionsList.isEmpty()) {
                    loadNextQuestion();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load questions from database.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<Questions> getRandomQuestions(List<Questions> allQuestions, int count) {
        List<Questions> selectedQuestions = new ArrayList<>();
        Random random = new Random();

        while (selectedQuestions.size() < count && selectedQuestions.size() < allQuestions.size()) {
            int index = random.nextInt(allQuestions.size());
            Questions randomQuestion = allQuestions.get(index);

            if (!askedQuestionIds.contains(String.valueOf(randomQuestion.getId()))) {
                selectedQuestions.add(randomQuestion);
                askedQuestionIds.add(String.valueOf(randomQuestion.getId()));
            }
        }

        return selectedQuestions;
    }

    private void navigateToResult() {
        storeScoreToDatabase(userId, currentScore);
        Intent intent = new Intent(getActivity(), ResultVocabulary.class);
        mDatabase = FirebaseDatabase.getInstance().getReference("Results");
        intent.putExtra("SCORE", currentScore); // Note the key "SCORE"
        intent.putExtra("USERNAME", username);// Pass the score to the result activity
        Log.d("VocabularyQuiz", "Navigating to result with score: " + currentScore); // Debug log
        startActivity(intent);
        getActivity().finish();
    }

    private void storeScoreToDatabase(String userId, int currentScore) {
        // Get the current date
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Get a reference to the current user's scores node under "users" table
        DatabaseReference userScoresRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("scores");

        // Generate a new unique key for the score entry
        String scoreId = userScoresRef.push().getKey();

        // Create a map to store score details
        Map<String, Object> scoreDetails = new HashMap<>();
        scoreDetails.put("date", currentDate);
        scoreDetails.put("quizType", "Vocabulary Quiz");
        scoreDetails.put("score", currentScore);

        // Save the score details to the database under the generated key
        userScoresRef.child(scoreId).setValue(scoreDetails);
    }

    private void navigateToLogin() {
        Intent intent = new Intent(getActivity(), SignInActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

}
