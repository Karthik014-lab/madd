package com.example.volunteerapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.volunteerapp.R;
import com.example.volunteerapp.adapters.EventAdapter;
import com.example.volunteerapp.models.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class OrganizerDashboardActivity extends AppCompatActivity {

    private RecyclerView eventsRecyclerView;
    private TabLayout tabLayout;
    private FloatingActionButton createEventFab;

    private EventAdapter adapter;
    private List<Event> eventList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_dashboard);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        initializeViews();
        setupRecyclerView();
        setupTabs();
        loadEvents("all");
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Organizer Dashboard");

        eventsRecyclerView = findViewById(R.id.eventsRecyclerView);
        tabLayout = findViewById(R.id.tabLayout);
        createEventFab = findViewById(R.id.createEventFab);

        createEventFab.setOnClickListener(v -> {
            startActivity(new Intent(this, CreateEventActivity.class));
        });
    }

    private void setupRecyclerView() {
        eventList = new ArrayList<>();
        adapter = new EventAdapter(this, eventList, event -> {
            Intent intent = new Intent(this, ManageVolunteersActivity.class);
            intent.putExtra("eventId", event.getEventId());
            startActivity(intent);
        });

        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventsRecyclerView.setAdapter(adapter);
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("All"));
        tabLayout.addTab(tabLayout.newTab().setText("Upcoming"));
        tabLayout.addTab(tabLayout.newTab().setText("Completed"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        loadEvents("all");
                        break;
                    case 1:
                        loadEvents("upcoming");
                        break;
                    case 2:
                        loadEvents("completed");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadEvents(String filter) {
        String organizerId = mAuth.getCurrentUser().getUid();

        db.collection("events")
                .whereEqualTo("organizerId", organizerId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    eventList.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Event event = document.toObject(Event.class);

                        if ("all".equals(filter) || filter.equals(event.getStatus())) {
                            eventList.add(event);
                        }
                    }

                    adapter.notifyDataSetChanged();
                });
    }
}
