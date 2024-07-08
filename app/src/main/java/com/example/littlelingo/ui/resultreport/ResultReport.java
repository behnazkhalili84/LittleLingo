package com.example.littlelingo.ui.resultreport;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.littlelingo.R;
import com.example.littlelingo.ui.SharedViewModel;
import com.example.littlelingo.ui.user.AuthRepository;
import com.example.littlelingo.ui.user.Users;

import java.util.Map;

public class ResultReport extends Fragment {

    private LinearLayout resultContainer;
    private AuthRepository authRepository;
    private SharedViewModel sharedViewModel;
    private String userId;
    private String username;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result_report, container, false);
        resultContainer = view.findViewById(R.id.resultContainer);

        // Initialize SharedViewModel
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Observe SharedViewModel data
        sharedViewModel.getName().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String name) {
                username = name;
                Log.d("VocabularyQuiz", "Username from ViewModel: " + username);
            }
        });
        sharedViewModel.getUserID().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String id) {
                userId = id;
                Log.d("VocabularyQuiz", "UserID from ViewModel: " + userId);

                // Fetch user data when userId is updated
                if (userId != null && !userId.isEmpty()) {
                    fetchUserData(userId);
                } else {
                    Log.e("ResultReportFragment", "UserID is null or empty");
                }
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize AuthRepository
        authRepository = new AuthRepository();

        // Observe changes in user data
        authRepository.getUserLiveData().observe(getViewLifecycleOwner(), new Observer<Users>() {
            @Override
            public void onChanged(Users user) {
                if (user != null) {
                    Log.d("ResultReportFragment", "User data: " + user);
                    displayQuizResults(user.getScores());
                } else {
                    Log.e("ResultReportFragment", "User data is null");
                }
            }
        });
    }

    private void fetchUserData(String userId) {
        // Fetch user data
        authRepository.fetchUserData(userId);
    }

    private void displayQuizResults(Map<String, Map<String, Object>> scores) {
        resultContainer.removeAllViews();
        if (scores != null && !scores.isEmpty()) {
            for (Map.Entry<String, Map<String, Object>> entry : scores.entrySet()) {
                Map<String, Object> quizResult = entry.getValue();
                View quizResultView = createQuizResultView(quizResult);
                resultContainer.addView(quizResultView);
            }
        } else {
            Log.e("ResultReportFragment", "Scores data is null or empty");
        }
    }

    private View createQuizResultView(Map<String, Object> quizResult) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_quiz_result, resultContainer, false);
        TextView quizTypeTextView = view.findViewById(R.id.quizTypeTextView);
        TextView totalQuestionsTextView = view.findViewById(R.id.totalQuestionsTextView);
        TextView correctAnswersTextView = view.findViewById(R.id.correctAnswersTextView);
        TextView dateOfQuizTextView = view.findViewById(R.id.dateOfQuizTextView);

        quizTypeTextView.setText("Quiz Type: " + quizResult.get("quizType"));
        totalQuestionsTextView.setText("Total Questions: " + quizResult.get("totalQuestions"));
        correctAnswersTextView.setText("Correct Answers: " + quizResult.get("score")); // Changed key to "score"
        dateOfQuizTextView.setText("Date: " + quizResult.get("date"));
        return view;
    }
}
