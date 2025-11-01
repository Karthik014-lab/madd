package com.example.volunteerapp.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.volunteerapp.R;
import com.example.volunteerapp.adapters.VolunteerAdapter;
import com.example.volunteerapp.models.Registration;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ManageVolunteersActivity extends AppCompatActivity {

    private RecyclerView volunteersRecyclerView;
    private TabLayout tabLayout;

    private VolunteerAdapter adapter;
    private List<Registration> registrationList;
    private FirebaseFirestore db;
    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_volunteers);

        db = FirebaseFirestore.getInstance();
        eventId = getIntent().getStringExtra("eventId");

        initializeViews();
        setupRecyclerView();
        setupTabs();
        loadVolunteers("all");
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Manage Volunteers");

        volunteersRecyclerView = findViewById(R.id.volunteersRecyclerView);
        tabLayout = findViewById(R.id.tabLayout);
    }

    private void setupRecyclerView() {
        registrationList = new ArrayList<>();
        adapter = new VolunteerAdapter(this, registrationList,
                new VolunteerAdapter.OnActionClickListener() {
                    @Override
                    public void onApprove(Registration registration) {
                        updateRegistrationStatus(registration.getRegistrationId(), "approved");
                    }

                    @Override
                    public void onReject(Registration registration) {
                        updateRegistrationStatus(registration.getRegistrationId(), "rejected");
                    }

                    @Override
                    public void onViewDetails(Registration registration) {
                        // Show volunteer details dialog
                        Toast.makeText(ManageVolunteersActivity.this,
                                "Volunteer: " + registration.getVolunteerName(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

        volunteersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        volunteersRecyclerView.setAdapter(adapter);
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("All"));
        tabLayout.addTab(tabLayout.newTab().setText("Pending"));
        tabLayout.addTab(tabLayout.newTab().setText("Approved"));
        tabLayout.addTab(tabLayout.newTab().setText("Rejected"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        loadVolunteers("all");
                        break;
                    case 1:
                        loadVolunteers("pending");
                        break;
                    case 2:
                        loadVolunteers("approved");
                        break;
                    case 3:
                        loadVolunteers("rejected");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadVolunteers(String filter) {
        db.collection("registrations")
                .whereEqualTo("eventId", eventId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    registrationList.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Registration registration;
                        registration = document.toObject(Registration.class);

                        if ("all".equals(filter) || filter.equals(registration.getStatus())) {
                            registrationList.add(registration);
                        }
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading volunteers: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private void updateRegistrationStatus(String registrationId, String status) {
        db.collection("registrations")
                .document(registrationId)
                .update("status", status)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Status updated to: " + status, Toast.LENGTH_SHORT).show();
                    loadVolunteers("all");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}