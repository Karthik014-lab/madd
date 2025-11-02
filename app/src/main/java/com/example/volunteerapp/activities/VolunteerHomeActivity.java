package com.example.volunteerapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.volunteerapp.R;
import com.example.volunteerapp.adapters.EventAdapter;
import com.example.volunteerapp.models.Event;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class VolunteerHomeActivity extends AppCompatActivity {

    private RecyclerView eventsRecyclerView, recommendedRecyclerView;
    private EditText searchEditText;
    private EventAdapter eventAdapter, recommendedAdapter;
    private List<Event> eventList, recommendedList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_home);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        initializeViews();
        setupRecyclerViews();
        setupSearch();
        loadEvents();
        loadRecommendedEvents();
        setupBottomNavigation();
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Volunteer Events");

        searchEditText = findViewById(R.id.searchEditText);
        eventsRecyclerView = findViewById(R.id.eventsRecyclerView);
        recommendedRecyclerView = findViewById(R.id.recommendedRecyclerView);
    }

    private void setupRecyclerViews() {
        eventList = new ArrayList<>();
        recommendedList = new ArrayList<>();

        eventAdapter = new EventAdapter(this, eventList, event -> {
            Intent intent = new Intent(VolunteerHomeActivity.this, EventDetailsActivity.class);
            intent.putExtra("eventId", event.getEventId());
            startActivity(intent);
        });

        recommendedAdapter = new EventAdapter(this, recommendedList, event -> {
            Intent intent = new Intent(VolunteerHomeActivity.this, EventDetailsActivity.class);
            intent.putExtra("eventId", event.getEventId());
            startActivity(intent);
        });

        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventsRecyclerView.setAdapter(eventAdapter);

        recommendedRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recommendedRecyclerView.setAdapter(recommendedAdapter);
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchEvents(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadEvents() {
        db.collection("events")
                .whereEqualTo("status", "upcoming")
                .orderBy("eventDate", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    eventList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Event event = document.toObject(Event.class);
                        eventList.add(event);
                    }
                    eventAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading events: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void loadRecommendedEvents() {
        // Load recommended events based on user's city or preferences
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String userCity = documentSnapshot.getString("city");

                        if (userCity != null && !userCity.isEmpty()) {
                            db.collection("events")
                                    .whereEqualTo("city", userCity)
                                    .whereEqualTo("status", "upcoming")
                                    .limit(5)
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots -> {
                                        recommendedList.clear();
                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                            Event event = document.toObject(Event.class);
                                            recommendedList.add(event);
                                        }
                                        recommendedAdapter.notifyDataSetChanged();
                                    });
                        }
                    }
                });
    }

    private void searchEvents(String query) {
        if (query.isEmpty()) {
            loadEvents();
            return;
        }

        db.collection("events")
                .whereEqualTo("status", "upcoming")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    eventList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Event event = document.toObject(Event.class);

                        if (event.getEventName().toLowerCase().contains(query.toLowerCase()) ||
                                event.getCity().toLowerCase().contains(query.toLowerCase())) {
                            eventList.add(event);
                        }
                    }
                    eventAdapter.notifyDataSetChanged();
                });
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_home);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_dashboard) {
                startActivity(new Intent(this, VolunteerDashboardActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                // Open profile
                return true;
            }
            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,
                menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_filter) {
            // Open filter dialog
            return true;
        } else if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}