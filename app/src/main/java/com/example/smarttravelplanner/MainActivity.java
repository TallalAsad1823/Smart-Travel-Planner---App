package com.example.smarttravelplanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Button loginBtn;
    private TextView goToRegister;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // --- THEME MANAGEMENT (Requirement 1.4) ---
        // SharedPreferences se saved theme uthana aur apply karna activity load hone se pehle [cite: 9]
        SharedPreferences themePref = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        int themeMode = themePref.getInt("ThemeMode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(themeMode);

        super.onCreate(savedInstanceState);

        // --- USER STATE CHECK (Requirement 2.3) ---
        // Check karna ke kya user pehle se logged in hai
        SharedPreferences userPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isLoggedIn = userPref.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // Agar logged in hai, toh direct CountryActivity par bhej dain
            startActivity(new Intent(MainActivity.this, CountryActivity.class));
            finish();
            return; // Agla code execute na ho
        }

        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        emailField = findViewById(R.id.emailInput);
        passwordField = findViewById(R.id.passwordInput);
        loginBtn = findViewById(R.id.loginBtn);
        goToRegister = findViewById(R.id.goToRegister);

        loginBtn.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user != null && user.isEmailVerified()) {
                                // --- PERSISTENT USER STATE (Requirement 2.2) ---
                                // SharedPreferences mein login flag save karna
                                SharedPreferences.Editor editor = userPref.edit();
                                editor.putBoolean("isLoggedIn", true);
                                editor.apply();

                                Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(MainActivity.this, CountryActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(MainActivity.this, "Please verify your email first!", Toast.LENGTH_LONG).show();
                                mAuth.signOut();
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        goToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}