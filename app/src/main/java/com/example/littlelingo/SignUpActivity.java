package com.example.littlelingo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.littlelingo.ui.user.AuthViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private EditText userNameEditText, passwordEditText;
    private MaterialButton textButtonSignIn;
    private Button signUpButton;

    //for error handling
    private TextInputLayout userNameInputLayout, passwordInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
//authViewModel.signOut();

        userNameEditText = findViewById(R.id.UserName);
        passwordEditText = findViewById(R.id.password);
        signUpButton = findViewById(R.id.signUpButton);
        // from material design for better error handling
        userNameInputLayout = findViewById(R.id.userNameInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);

        // Add the TextWatcher to the EditTexts
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userName = userNameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                signUpButton.setEnabled(authViewModel.validateInput(userName, password, userNameInputLayout, passwordInputLayout));
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed here
            }
        };

// Add the TextWatcher to the EditText fields
        userNameEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userNameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                authViewModel.register(email, password);
            }
        });

        textButtonSignIn = findViewById(R.id.textButtonSignIn);

        textButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        authViewModel.getUserLiveData().observe(this, firebaseUser -> {
            if(firebaseUser != null){
                Toast.makeText(SignUpActivity.this,"Sign Up Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                intent.putExtra("email",firebaseUser.getEmail());
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(SignUpActivity.this,"Sign Up Fails: "+ authViewModel.authError, Toast.LENGTH_SHORT).show();
            }
        });


                ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}