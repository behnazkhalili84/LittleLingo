package com.example.littlelingo.ui.vocabularyquiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import com.example.littlelingo.R;
import com.example.littlelingo.ui.VocabularyResult.ResultVocabulary;
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
    private int currentScore = 0; // Initialize currentScore

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
        String correctAnswer = currentQuestion.getCorrectAnswer();
        int correctAnswerPosition = getCorrectAnswerPosition(currentQuestion, correctAnswer);

        if (selectedOptionPosition == correctAnswerPosition) {
            showAnswerFeedback(correctAnswerPosition, R.drawable.correct_option_border_bg);
            Toast.makeText(getContext(), "Correct!", Toast.LENGTH_SHORT).show();
            currentScore++; // Increment the score if the answer is correct
            Log.d("VocabularyQuiz", "Score incremented: " + currentScore); // Debug log
        } else {
            showAnswerFeedback(correctAnswerPosition, R.drawable.correct_option_border_bg);
            showAnswerFeedback(selectedOptionPosition, R.drawable.wrong_option_border_bg);
            Toast.makeText(getContext(), "Wrong!", Toast.LENGTH_SHORT).show();
        }

        currentQuestionIndex++;
    }

    private int getCorrectAnswerPosition(VocabularyQuestion question, String correctAnswer) {
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
        VocabularyQuestion nextQuestion = questionsList.get(currentQuestionIndex);
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

    private void shuffleOptions(VocabularyQuestion question) {
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
                List<VocabularyQuestion> allQuestions = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    VocabularyQuestion question = snapshot.getValue(VocabularyQuestion.class);
                    allQuestions.add(question);
                    // Log the data fetched
                    Log.d("VocabularyQuiz", "Question fetched: " + question);
                }

                if (allQuestions.isEmpty()) {
                    Toast.makeText(getContext(), "No questions available from database.", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<VocabularyQuestion> selectedQuestions = getRandomQuestions(allQuestions, 8);

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
        if (questions.size() < count) {
            count = questions.size();
        }

        Set<Integer> selectedIndices = new HashSet<>();
        Random random = new Random();

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
        Intent intent = new Intent(getActivity(), ResultVocabulary.class);
        mDatabase = FirebaseDatabase.getInstance().getReference("Results");
        intent.putExtra("SCORE", currentScore); // Note the key "SCORE"
//        intent.putExtra("USERNAME", username);// Pass the score to the result activity
        Log.d("VocabularyQuiz", "Navigating to result with score: " + currentScore); // Debug log
        startActivity(intent);
        getActivity().finish();
    }


private void addQuestions() {
//
///Question1
        VocabularyQuestion vocabularyQuestion1 = new VocabularyQuestion(1, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/apple.jpeg?alt=media&token=6e2a3e1b-ccd6-4997-b2bb-2fa849e48d4a",
                "drink", "water",
                "apple", "nose", "3","vocabulary");

        // Add the word to the database
        mDatabase.child("1").setValue(vocabularyQuestion1);

        ///Question2

        VocabularyQuestion vocabularyQuestion2 = new VocabularyQuestion(2, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/water.jpeg?alt=media&token=2b543b26-4443-44fa-adf8-1dc2697d1128",
                "juice", "water",
                "apple", "nose", "2","vocabulary");

        // Add the word to the database
        mDatabase.child("2").setValue(vocabularyQuestion2);

        ///Question3
        VocabularyQuestion vocabularyQuestion3 = new VocabularyQuestion(3, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/juice.jpeg?alt=media&token=8bd7359a-1318-4b40-b1e6-3385e1e2efa3",
                "juice", "water",
                "apple", "nose", "1","vocabulary");

        // Add the word to the database
        mDatabase.child("3").setValue(vocabularyQuestion3);

        ///Question4

        VocabularyQuestion vocabularyQuestion4 = new VocabularyQuestion(4, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/drink.jpeg?alt=media&token=9c739bc7-d501-4cbf-894c-c9bf77fc5ca8",
                "juice", "water",
                "drink", "nose", "3","vocabulary");

        // Add the word to the database
        mDatabase.child("4").setValue(vocabularyQuestion4);

        ///Question5
        VocabularyQuestion vocabularyQuestion5 = new VocabularyQuestion(5, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/toes.jpeg?alt=media&token=336ab8d1-3f5a-4007-8135-2563fabbb834",
                "Toes", "water",
                "drink", "nose", "1","vocabulary");

        // Add the word to the database
        mDatabase.child("5").setValue(vocabularyQuestion5);

        ///Question6

        VocabularyQuestion vocabularyQuestion6 = new VocabularyQuestion(6, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/nose.ogg?alt=media&token=1ea4d054-b872-4f7a-8749-afdf552ee021",
                "Noes", "Toes",
                "water", "drink", "1","vocabulary");

        // Add the word to the database
        mDatabase.child("6").setValue(vocabularyQuestion6);

        ///Question7

        VocabularyQuestion vocabularyQuestion7 = new VocabularyQuestion(7, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/biscuit.jpeg?alt=media&token=76f5c488-665b-4045-8c12-7b2eaaee2a6c",
                "biscuit", "milk",
                "water", "apple", "1","vocabulary");

        // Add the word to the database
        mDatabase.child("7").setValue(vocabularyQuestion7);

        ///Question8

        VocabularyQuestion vocabularyQuestion8 = new VocabularyQuestion(8, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/chair.jpeg?alt=media&token=78c4ba05-6b13-4cfe-83df-bb7c6ef2ea6e",
                "cup", "drink",
                "chair", "plate", "3","vocabulary");

        // Add the word to the database
        mDatabase.child("8").setValue(vocabularyQuestion8);

        ///Question9
        VocabularyQuestion vocabularyQuestion9 = new VocabularyQuestion(9, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/cup.jpeg?alt=media&token=c4142c8b-14e0-4619-b597-03a25f72e23d",
                "cup", "plate",
                "chair", "nose", "1","vocabulary");

        // Add the word to the database
        mDatabase.child("9").setValue(vocabularyQuestion9);

        ///Question10
        VocabularyQuestion vocabularyQuestion10 = new VocabularyQuestion(10, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/dady.jpeg?alt=media&token=06f91ba9-d230-4aa7-9cf6-8348b086d1b4",
                "daddy", "mommy",
                "cup", "plate", "1","vocabulary");

        // Add the word to the database
        mDatabase.child("10").setValue(vocabularyQuestion10);

        ///Question11
        VocabularyQuestion vocabularyQuestion11 = new VocabularyQuestion(11, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/milk.jpeg?alt=media&token=2eb78a24-4efb-4505-8d92-59ccfa9fb4f8",
                "cup", "nose",
                "water", "milk", "4","vocabulary");

        // Add the word to the database
        mDatabase.child("11").setValue(vocabularyQuestion11);

        ///Question12
        VocabularyQuestion vocabularyQuestion12 = new VocabularyQuestion(12, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/mommy.jpeg?alt=media&token=77ebd833-56db-4c37-8db3-2b8243aa8890",
                "daddy", "mommy",
                "cup", "plate", "2","vocabulary");

        // Add the word to the database
        mDatabase.child("12").setValue(vocabularyQuestion12);

        ///Question13
        VocabularyQuestion vocabularyQuestion13 = new VocabularyQuestion(13, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/mouth.jpeg?alt=media&token=8501e6bb-1943-4d47-9d1d-337d4823668f",
                "Noes", "Toes",
                "mouth", "sweet", "3","vocabulary");

        // Add the word to the database
        mDatabase.child("13").setValue(vocabularyQuestion13);

        ///Question14
        VocabularyQuestion vocabularyQuestion14 = new VocabularyQuestion(14, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/plate.png?alt=media&token=503c43cb-95d8-4413-9e19-7c85e2285be5",
                "noes", "toes",
                "plate", "cup", "3","vocabulary");

        // Add the word to the database
        mDatabase.child("14").setValue(vocabularyQuestion14);

        ///Question15
        VocabularyQuestion vocabularyQuestion15 = new VocabularyQuestion(15, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/sweets.jpeg?alt=media&token=f44bc33e-4108-488d-abf8-6a52125e29c8",
                "Noes", "Toes",
                "water", "drink", "1","vocabulary");

        // Add the word to the database
        mDatabase.child("15").setValue(vocabularyQuestion15);

        ///Question16
        VocabularyQuestion vocabularyQuestion16 = new VocabularyQuestion(16, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/table.jpeg?alt=media&token=c8169476-cdf1-447c-a51b-82200f69b85b",
                "Noes", "Toes",
                "water", "drink", "1","vocabulary");

        // Add the word to the database
        mDatabase.child("16").setValue(vocabularyQuestion16);

        ///Question16
        VocabularyQuestion vocabularyQuestion17 = new VocabularyQuestion(17, "https://firebasestorage.googleapis.com/v0/b/littlelingo-6bcce.appspot.com/o/videoplayer.jpg?alt=media&token=fb86ba2a-cd49-4ed7-9383-8793e52aff49",
                "Media", "Toes",
                "water", "drink", "1","vocabulary");

        // Add the word to the database
        mDatabase.child("17").setValue(vocabularyQuestion17);
    }
}
//553b8c2a31b52d4e0c69d9c185d007e3-623e10c8-9148d441
//sandbox8ca82183ec064960bac4f2195f9eedf4.mailgun.org
