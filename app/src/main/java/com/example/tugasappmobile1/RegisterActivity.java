package com.example.tugasappmobile1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tugasappmobile1.Model.UserDetails;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    Button signUpBtn;
    TextInputEditText usernameSignUp, passwordSignUp, nimPengguna, emailPengguna;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi komponen
        signUpBtn = findViewById(R.id.signUpBtn);
        usernameSignUp = findViewById(R.id.usernameSignUp);
        emailPengguna = findViewById(R.id.emailPengguna);
        passwordSignUp = findViewById(R.id.passwordSignUp);
        nimPengguna = findViewById(R.id.nimPengguna);

        // Listener tombol Sign Up
        signUpBtn.setOnClickListener(view -> {
            String username = Objects.requireNonNull(usernameSignUp.getText()).toString().trim();
            String email = Objects.requireNonNull(emailPengguna.getText()).toString().trim();
            String password = Objects.requireNonNull(passwordSignUp.getText()).toString().trim();
            String NIM = Objects.requireNonNull(nimPengguna.getText()).toString().trim();

            // Validasi input
            if (username.isEmpty()) {
                usernameSignUp.setError("Masukkan nama pengguna");
                usernameSignUp.requestFocus();
            } else if (email.isEmpty()) {
                emailPengguna.setError("Masukkan email");
                emailPengguna.requestFocus();
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailPengguna.setError("Format email tidak valid");
                emailPengguna.requestFocus();
            } else if (password.isEmpty()) {
                passwordSignUp.setError("Masukkan password");
                passwordSignUp.requestFocus();
            } else if (password.length() < 6) {
                passwordSignUp.setError("Password minimal 6 karakter");
                passwordSignUp.requestFocus();
            } else if (NIM.isEmpty()) {
                nimPengguna.setError("Masukkan NIM");
                nimPengguna.requestFocus();
            } else {
                registerUser(username, email, password, NIM);
            }
        });
    }

    // Fungsi untuk mendaftarkan pengguna
    private void registerUser(String username, String email, String password, String NIM) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser fUser = auth.getCurrentUser();
                        if (fUser != null) {
                            fUser.sendEmailVerification().addOnCompleteListener(verificationTask -> {
                                if (verificationTask.isSuccessful()) {
                                    String uid = fUser.getUid();
                                    UserDetails userDetails = new UserDetails(uid, username, email, password, NIM);

                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                                    reference.child(uid).setValue(userDetails)
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    Toast.makeText(this, "Akun berhasil dibuat. Verifikasi email Anda.", Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(this, HomeActivity.class));
                                                    finish();
                                                }
                                            }).addOnFailureListener(e ->
                                                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
                                } else {
                                    Toast.makeText(this, "Gagal mengirim verifikasi email", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            Toast.makeText(this, "Gagal membuat pengguna", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
                            Toast.makeText(this, "Password terlalu lemah!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(e ->
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
