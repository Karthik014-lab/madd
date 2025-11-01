package com.example.volunteerapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.example.volunteerapp.R;
import com.example.volunteerapp.models.Event;
import com.example.volunteerapp.models.Registration;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventDetailsActivity extends AppCompatActivity {

    private ImageView eventImageView;
    private TextView eventNameTextView, organizerNameTextView, descriptionTextView;
    private TextView venueTextView, dateTextView, timeTextView, volunteersTextView, rolesTextView;
    private Button registerButton, viewMapButton, contactOrganizerButton;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String eventId;
    private Event currentEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        eventId = getIntent().getStringExtra("eventId");

        initializeViews();
        loadEventDetails();
        checkRegistrationStatus();
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Event Details");

        eventImageView = findViewById(R.id.eventImageView);
        eventNameTextView = findViewById(R.id.eventNameTextView);
        organizerNameTextView = findViewById(R.id.organizerNameTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        venueTextView = findViewById(R.id.venueTextView);
        dateTextView = findViewById(R.id.dateTextView);
        timeTextView = findViewById(R.id.timeTextView);
        volunteersTextView = findViewById(R.id.volunteersTextView);
        rolesTextView = findViewById(R.id.rolesTextView);
        registerButton = findViewById(R.id.registerButton);
        viewMapButton = findViewById(R.id.viewMapButton);
        contactOrganizerButton = findViewById(R.id.contactOrganizerButton);

        registerButton.setOnClickListener(v -> registerForEvent());
        viewMapButton.setOnClickListener(v -> openMap());
        contactOrganizerButton.setOnClickListener(v -> contactOrganizer());
    }

    private void loadEventDetails() {
        db.collection("events")
                .document(eventId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        currentEvent = documentSnapshot.toObject(Event.class);
                        displayEventDetails();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading event: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void displayEventDetails() {
        if (currentEvent == null) return;

        eventNameTextView.setText(currentEvent.getEventName());
        organizerNameTextView.setText("Organized by: " + currentEvent.getOrganizerName());
        descriptionTextView.setText(currentEvent.getDescription());
        venueTextView.setText(currentEvent.getVenue() + ", " + currentEvent.getCity());

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        dateTextView.setText(dateFormat.format(new Date(currentEvent.getEventDate())));

        timeTextView.setText(currentEvent.getEventTime());

        volunteersTextView.setText(currentEvent.getRegisteredVolunteers() + " / " +
                currentEvent.getVolunteersRequired() + " volunteers");

        if (currentEvent.getRolesRequired() != null && !currentEvent.getRolesRequired().isEmpty()) {
            rolesTextView.setText("Roles: " + String.join(", ", currentEvent.getRolesRequired()));
        }

        if (currentEvent.getImageUrl() != null && !currentEvent.getImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(currentEvent.getImageUrl())
                    .placeholder(R.drawable.placeholder_event)
                    .into(eventImageView);
        }
    }

    private void checkRegistrationStatus() {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("registrations")
                .whereEqualTo("eventId", eventId)
                .whereEqualTo("volunteerId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        registerButton.setText("Already Registered");
                        registerButton.setEnabled(false);
                    }
                });
    }

    private void registerForEvent() {
        String userId = mAuth.getCurrentUser().getUid();
        String registrationId = db.collection("registrations").document().getId();

        Registration registration = new Registration(registrationId, eventId, userId);

        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        registration.setVolunteerName(documentSnapshot.getString("name"));
                        registration.setVolunteerEmail(documentSnapshot.getString("email"));
                        registration.setVolunteerPhone(documentSnapshot.getString("phone"));

                        db.collection("registrations")
                                .document(registrationId)
                                .set(registration)
                                .addOnSuccessListener(aVoid -> {
                                    // Update event registered volunteers count
                                    db.collection("events").document(eventId)
                                            .update("registeredVolunteers",
                                                    currentEvent.getRegisteredVolunteers() + 1);

                                    Toast.makeText(this, "Successfully registered!", Toast.LENGTH_SHORT).show();
                                    registerButton.setText("Already Registered");
                                    registerButton.setEnabled(false);
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Registration failed: " + e.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                });
                    }
                });
    }

    private void openMap() {
        // Implement Google Maps integration
        Toast.makeText(this, "Opening map...", Toast.LENGTH_SHORT).show();
    }

    private void contactOrganizer() {
        // Implement contact organizer functionality
        Toast.makeText(this, "Contact: " + currentEvent.getContactPhone(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}