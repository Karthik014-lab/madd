package com.example.volunteerapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.volunteerapp.R;
import com.example.volunteerapp.models.User;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserTypeSelectionActivity extends AppCompatActivity {

    private Button volunteerButton, organizerButton;
    private FirebaseFirestore db;

    private String userId, name, email, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type_selection);

        db = FirebaseFirestore.getInstance();

        // Get data from intent
        userId = getIntent().getStringExtra("userId");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        phone = getIntent().getStringExtra("phone");

        volunteerButton = findViewById(R.id.volunteerButton);
        organizerButton = findViewById(R.id.organizerButton);

        volunteerButton.setOnClickListener(v -> {
            saveUserType("volunteer");
        });

        organizerButton.setOnClickListener(v -> {
            saveUserType("organizer");
        });
    }

    private void saveUserType(String userType) {
        User user = new User(userId, name, email, phone, userType);

        db.collection("users")
                .document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Intent intent;
                    if ("organizer".equals(userType)) {
                        intent = new Intent(UserTypeSelectionActivity.this, OrganizerDashboardActivity.class);
                    } else {
                        intent = new Intent(UserTypeSelectionActivity.this, VolunteerHomeActivity.class);
                    }
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}