package com.example.tugasappmobile1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tugasappmobile1.Adapter.MainAdapter;
import com.example.tugasappmobile1.Model.Catatan;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView title = findViewById(R.id.title);
        title.setText("Home");


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MainAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        String CATATAN = "catatan";
        DatabaseReference notesRef = FirebaseDatabase.getInstance().getReference(CATATAN);
        notesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Catatan> notes = new ArrayList<>();
                for (DataSnapshot noteSnapshot : snapshot.getChildren()) {
                    Catatan note = noteSnapshot.getValue(Catatan.class);
                    if (note != null) {
                        notes.add(note);
                    }
                }
                adapter.setNotes(notes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Error fetching notes", Toast.LENGTH_SHORT).show();
            }
        });

        // Setup Bottom Navigation
        BottomNavigationView botNavBar = findViewById(R.id.bottomNavigation);
        botNavBar.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            } else if (id == R.id.nav_add) {
                startActivity(new Intent(this, TambahDataActivity.class));
                return true;
            } else {
                return false;
            }
        });

        botNavBar.setSelectedItemId(R.id.nav_home); // default active tab
    }
}
