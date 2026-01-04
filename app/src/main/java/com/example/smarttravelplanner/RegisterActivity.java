package com.example.smarttravelplanner;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Button registerBtn;
    private ImageView btnBack; // Back arrow ke liye variable
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        // XML se views connect karna
        emailField = findViewById(R.id.regEmail);
        passwordField = findViewById(R.id.regPassword);
        registerBtn = findViewById(R.id.registerBtn);
        btnBack = findViewById(R.id.btnBack); // Back arrow link kiya

        // Back Arrow click logic
        btnBack.setOnClickListener(v -> {
            finish(); // Ye current screen band karke wapis Login (MainActivity) par le jayega
        });

        registerBtn.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (email.isEmpty() || password.length() < 6) {
                Toast.makeText(RegisterActivity.this, "Invalid Email or Password (min 6 chars)", Toast.LENGTH_SHORT).show();
                return;
            }

            // Firebase User Creation
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // User ban gaya, ab verification email bhejein
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                user.sendEmailVerification()
                                        .addOnCompleteListener(verificationTask -> {
                                            if (verificationTask.isSuccessful()) {
                                                Toast.makeText(RegisterActivity.this, "Registration Successful! Please check your email for verification.", Toast.LENGTH_LONG).show();
                                                mAuth.signOut();
                                                finish();
                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}