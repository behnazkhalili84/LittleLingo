package com.example.littlelingo.ui.VocabularyResult;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.littlelingo.API.MailgunService;
import com.example.littlelingo.MainActivity;
import com.example.littlelingo.R;

import java.io.IOException;

public class ResultVocabulary extends AppCompatActivity {

    private static final String TAG = "ResultVocabulary";
    private MailgunService mailgunService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_vocabulary);

        TextView tvScore = findViewById(R.id.tv_score);
        TextView tvUsername = findViewById(R.id.tv_name);
        Button btnFinish = findViewById(R.id.btn_finish);

        // Get the score and username from the Intent
        Intent intent = getIntent();
        int score = intent.getIntExtra("SCORE", 0); // Note the key "SCORE"
        String username = intent.getStringExtra("USERNAME");

        // Set the score and username to the TextViews
        tvScore.setText("Score: " + score);
        tvUsername.setText("Username: " + username);

        // Initialize MailgunService
        mailgunService = new MailgunService(username);

        // Send the quiz result via email using Mailgun
        sendEmail(username, score);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the home_nav destination
                Intent intent = new Intent(ResultVocabulary.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void sendEmail(String username, int score) {
        String recipientEmail = "nutriscan2024@gmail.com"; // Replace with actual recipient email address
        String subject = "Quiz Result for " + username;
        String message = "Dear " + username + ",\n\nYour quiz result is: " + score;

        new Thread(() -> {
            try {
                Log.d(TAG, "Sending email...");
                mailgunService.sendSimpleMessage(recipientEmail, subject, message);
                runOnUiThread(() -> Toast.makeText(ResultVocabulary.this, "Email sent successfully", Toast.LENGTH_SHORT).show());
                Log.d(TAG, "Email sent successfully");
            } catch (IOException e) {
                runOnUiThread(() -> Toast.makeText(ResultVocabulary.this, "Failed to send email: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                Log.e(TAG, "Failed to send email", e);
            }
        }).start();
    }
}
