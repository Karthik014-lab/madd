package com.example.volunteerapp.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.volunteerapp.R;
import com.example.volunteerapp.models.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class CreateEventActivity extends AppCompatActivity {

    private EditText eventNameEditText, descriptionEditText, venueEditText, cityEditText;
    private EditText volunteersRequiredEditText, rolesEditText, contactPhoneEditText, contactEmailEditText;
    private Button selectDateButton, selectTimeButton, createEventButton;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Calendar selectedDate;
    private String selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        selectedDate = Calendar.getInstance();

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Event");

        eventNameEditText = findViewById(R.id.eventNameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        venueEditText = findViewById(R.id.venueEditText);
        cityEditText = findViewById(R.id.cityEditText);
        volunteersRequiredEditText = findViewById(R.id.volunteersRequiredEditText);
        rolesEditText = findViewById(R.id.rolesEditText);
        contactPhoneEditText = findViewById(R.id.contactPhoneEditText);
        contactEmailEditText = findViewById(R.id.contactEmailEditText);
        selectDateButton = findViewById(R.id.selectDateButton);
        selectTimeButton = findViewById(R.id.selectTimeButton);
        createEventButton = findViewById(R.id.createEventButton);
    }

    private void setupClickListeners() {
        selectDateButton.setOnClickListener(v -> showDatePicker());
        selectTimeButton.setOnClickListener(v -> showTimePicker());
        createEventButton.setOnClickListener(v -> createEvent());
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedDate.set(year, month, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                    selectDateButton.setText(sdf.format(selectedDate.getTime()));
                },
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                    selectTimeButton.setText(selectedTime);
                },
                12, 0, true
        );
        timePickerDialog.show();
    }

    private void createEvent() {
        String eventName = eventNameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String venue = venueEditText.getText().toString().trim();
        String city = cityEditText.getText().toString().trim();
        String volunteersReq = volunteersRequiredEditText.getText().toString().trim();
        String roles = rolesEditText.getText().toString().trim();
        String phone = contactPhoneEditText.getText().toString().trim();
        String email = contactEmailEditText.getText().toString().trim();

        if (eventName.isEmpty() || description.isEmpty() || venue.isEmpty() ||
                city.isEmpty() || volunteersReq.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();
        String eventId = db.collection("events").document().getId();

        Event event = new Event(eventId, userId, "", eventName);
        event.setDescription(description);
        event.setVenue(venue);
        event.setCity(city);
        event.setEventDate(selectedDate.getTimeInMillis());
        event.setEventTime(selectedTime);
        event.setVolunteersRequired(Integer.parseInt(volunteersReq));
        event.setRolesRequired(Arrays.asList(roles.split(",")));
        event.setContactPhone(phone);
        event.setContactEmail(email);

        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        event.setOrganizerName(documentSnapshot.getString("name"));

                        db.collection("events")
                                .document(eventId)
                                .set(event)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Event created successfully!", Toast.LENGTH_SHORT).show();
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}