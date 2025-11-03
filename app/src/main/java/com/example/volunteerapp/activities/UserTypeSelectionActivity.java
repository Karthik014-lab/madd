package com.example.volunteerapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.volunteerapp.R;
import com.example.volunteerapp.models.User;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserTypeSelectionActivity extends AppCompatActivity {

    private CardView volunteerCard, organizerCard;
    private FirebaseFirestore db;

    private String userId, name, email, phone;

    @SuppressLint("MissingInflatedId")
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

        // Match CardView IDs from XML
        volunteerCard = findViewById(R.id.volunteerCard);
        organizerCard = findViewById(R.id.organizerCard);

        volunteerCard.setOnClickListener(v -> saveUserType("volunteer"));
        organizerCard.setOnClickListener(v -> saveUserType("organizer"));
        Log.d("UserTypeSelection", "userId = " + userId);

    }
    private void saveUserType(String userType) {
        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "User ID is missing. Cannot save user type.", Toast.LENGTH_LONG).show();
            return;
        }

        User user = new User(userId, name, email, phone, userType);

        db.collection("users")
                .document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Intent intent = "organizer".equals(userType)
                            ? new Intent(this, OrganizerDashboardActivity.class)
                            : new Intent(this, VolunteerHomeActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }



}
