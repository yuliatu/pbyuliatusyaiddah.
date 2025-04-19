package com.example.tugasappmobile1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class TambahDataActivity extends AppCompatActivity {

    private TextInputEditText catatanEditText;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_data);

        catatanEditText = findViewById(R.id.catatan);
        btnSubmit = findViewById(R.id.btnSubmit);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_add);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            } else if (id == R.id.nav_add) {
                return true;
            }
            return false;
        });

        btnSubmit.setOnClickListener(view -> {
            String note = catatanEditText.getText().toString().trim();

            if (note.isEmpty()) {
                Toast.makeText(TambahDataActivity.this, "Silakan isi catatan terlebih dahulu", Toast.LENGTH_SHORT).show();
                return;
            }

            String CATATAN = "catatan";
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(CATATAN);
            String noteId = databaseReference.push().getKey();

            if (noteId != null) {
                Map<String, Object> noteData = new HashMap<>();
                noteData.put("id", noteId);
                noteData.put("note", note);

                databaseReference.child(noteId).setValue(noteData)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(TambahDataActivity.this, "Catatan berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                                catatanEditText.setText("");
                            } else {
                                Toast.makeText(TambahDataActivity.this, "Gagal menambahkan catatan", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(TambahDataActivity.this, "Terjadi kesalahan saat membuat ID", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
