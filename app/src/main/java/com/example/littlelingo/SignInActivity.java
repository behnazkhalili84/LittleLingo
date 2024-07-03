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

public class SignInActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private EditText userNameEditText, passwordEditText;
    private MaterialButton textButtonSignUp;
    private Button signInButton;

    //for error handling
    private TextInputLayout userNameInputLayout, passwordInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
authViewModel.signOut();

        userNameEditText = findViewById(R.id.UserName);
        passwordEditText = findViewById(R.id.password);
        signInButton = findViewById(R.id.signInButton);
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
                signInButton.setEnabled(authViewModel.validateInput(userName, password, userNameInputLayout, passwordInputLayout));
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed here
            }
        };

// Add the TextWatcher to the EditText fields
        userNameEditText.addTextChangedListener(textWatcher);
        passwordEditText.addTextChangedListener(textWatcher);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userNameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                authViewModel.login(email, password);
            }
        });

        textButtonSignUp = findViewById(R.id.textButtonSignUp);

        textButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        authViewModel.getUserLiveData().observe(this, user -> {
            if(user != null){
                Toast.makeText(SignInActivity.this,"Sign In Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                intent.putExtra("name",user.getName());
                intent.putExtra("userID",user.getUserId());
                //Toast.makeText(SignInActivity.this,"Sign In Intent: "+ intent.getStringExtra("name"), Toast.LENGTH_SHORT).show();

                startActivity(intent);
                finish();
            } else {
                Toast.makeText(SignInActivity.this,"Sign In Fails: "+ authViewModel.authError, Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}