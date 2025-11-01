package com.example.volunteerapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.volunteerapp.R;
import com.example.volunteerapp.adapters.RegistrationAdapter;
import com.example.volunteerapp.models.Registration;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class VolunteerDashboardActivity extends AppCompatActivity {

    private RecyclerView registrationsRecyclerView;
    private TabLayout tabLayout;
    private ImageView qrCodeImageView;
    private TextView noDataTextView;

    private RegistrationAdapter adapter;
    private List<Registration> registrationList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_dashboard);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        initializeViews();
        setupRecyclerView();
        setupTabs();
        loadRegistrations("all");
        setupBottomNavigation();
    }

    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Dashboard");

        registrationsRecyclerView = findViewById(R.id.registrationsRecyclerView);
        tabLayout = findViewById(R.id.tabLayout);
        qrCodeImageView = findViewById(R.id.qrCodeImageView);
        noDataTextView = findViewById(R.id.noDataTextView);
    }

    private void setupRecyclerView() {
        registrationList = new ArrayList<>();
        adapter = new RegistrationAdapter(this, registrationList, registration -> {
            // Handle click - show event details
        });

        registrationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        registrationsRecyclerView.setAdapter(adapter);
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("All"));
        tabLayout.addTab(tabLayout.newTab().setText("Upcoming"));
        tabLayout.addTab(tabLayout.newTab().setText("Past"));
        tabLayout.addTab(tabLayout.newTab().setText("Pending"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        loadRegistrations("all");
                        break;
                    case 1:
                        loadRegistrations("upcoming");
                        break;
                    case 2:
                        loadRegistrations("past");
                        break;
                    case 3:
                        loadRegistrations("pending");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadRegistrations(String filter) {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("registrations")
                .whereEqualTo("volunteerId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    registrationList.clear();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Registration registration = document.toObject(Registration.class);

                        boolean shouldAdd = false;
                        switch (filter) {
                            case "all":
                                shouldAdd = true;
                                break;
                            case "pending":
                                shouldAdd = "pending".equals(registration.getStatus());
                                break;
                            case "upcoming":
                                shouldAdd = "approved".equals(registration.getStatus());
                                break;
                        }

                        if (shouldAdd) {
                            registrationList.add(registration);
                        }
                    }

                    adapter.notifyDataSetChanged();
                });
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_dashboard);

        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, VolunteerHomeActivity.class));
                return true;
            } else if (itemId == R.id.nav_dashboard) {
                return true;
            }
            return false;
        });
    }
}