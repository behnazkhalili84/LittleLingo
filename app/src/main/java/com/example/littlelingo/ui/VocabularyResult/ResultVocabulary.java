package com.example.littlelingo.ui.VocabularyResult;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.littlelingo.R;

public class ResultVocabulary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_vocabulary);

        TextView tvScore = findViewById(R.id.tv_score);
        TextView tvUsername = findViewById(R.id.tv_name);

        // Get the score and username from the Intent
        Intent intent = getIntent();
        int score = intent.getIntExtra("SCORE", 0); // Note the key "SCORE"
        String username = intent.getStringExtra("USERNAME");

        // Set the score and username to the TextViews
        tvScore.setText("Score: " + score);
        tvUsername.setText("Username: " + username);
    }
}