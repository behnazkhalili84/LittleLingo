package com.example.littlelingo.ui.resultVocabulary;
import com.example.littlelingo.MainActivity;
import com.example.littlelingo.R;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;


import java.time.LocalDate;

public class ResultVocabulary extends Fragment {

    private TextView tvScore;
    private TextView tvName;
    private Button btnFinish;

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result_vocabulary, container, false);

        tvName = view.findViewById(R.id.tv_name);
        tvScore = view.findViewById(R.id.tv_score);
        btnFinish = view.findViewById(R.id.btn_finish);

        Intent intent = getActivity().getIntent();
        String userId = intent.getStringExtra("userId");
        String userName = intent.getStringExtra("userName");
        int correctAnswers = intent.getIntExtra("correctAnswers", 0);
        int totalQuestions = intent.getIntExtra("totalQuestions", 0);

        tvName.setText(userName);
        tvScore.setText("Your Score is " + correctAnswers + " out of " + totalQuestions);

        btnFinish.setOnClickListener(v -> {
            String username = tvName.getText().toString();
            int score = correctAnswers;
            String currentDate = LocalDate.now().toString();

            // Using lifecycleScope to launch a coroutine
            ((LifecycleOwner) requireContext()).getLifecycle().addObserver(new LifecycleEventObserver() {
                @Override
                public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                    if (event == Lifecycle.Event.ON_RESUME) {
                        new Thread(() -> {
                            Log.d("VocabularyScore", "Score question: " + score);
                            try {
                               //inserer dans la base de donnée

                            } catch (Exception e) {
                                Log.e("VocabularyScore", "Error inserting result: " + e.getMessage());
                            }
                        }).start();
                    }
                }
            });

            startActivity(new Intent(getActivity(), MainActivity.class));
            if (getActivity() != null) {
                getActivity().finish();
            }
        });

        return view;
    }

}
