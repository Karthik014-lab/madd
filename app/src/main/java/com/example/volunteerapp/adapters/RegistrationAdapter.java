package com.example.volunteerapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.volunteerapp.R;
import com.example.volunteerapp.models.Registration;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RegistrationAdapter extends RecyclerView.Adapter<RegistrationAdapter.RegistrationViewHolder> {

    private Context context;
    private List<Registration> registrationList;
    private OnRegistrationClickListener listener;

    public interface OnRegistrationClickListener {
        void onRegistrationClick(Registration registration);
    }

    public RegistrationAdapter(Context context, List<Registration> registrationList,
                               OnRegistrationClickListener listener) {
        this.context = context;
        this.registrationList = registrationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RegistrationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_registration, parent, false);
        return new RegistrationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RegistrationViewHolder holder, int position) {
        Registration registration = registrationList.get(position);

        holder.volunteerNameTextView.setText(registration.getVolunteerName());
        holder.statusTextView.setText(registration.getStatus().toUpperCase());

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        holder.dateTextView.setText(dateFormat.format(new Date(registration.getRegistrationDate())));

        if (registration.getAssignedRole() != null) {
            holder.roleTextView.setText("Role: " + registration.getAssignedRole());
            holder.roleTextView.setVisibility(View.VISIBLE);
        } else {
            holder.roleTextView.setVisibility(View.GONE);
        }

        // Set status color
        int statusColor;
        switch (registration.getStatus()) {
            case "approved":
                statusColor = context.getResources().getColor(android.R.color.holo_green_dark);
                break;
            case "rejected":
                statusColor = context.getResources().getColor(android.R.color.holo_red_dark);
                break;
            default:
                statusColor = context.getResources().getColor(android.R.color.holo_orange_dark);
                break;
        }
        holder.statusTextView.setTextColor(statusColor);

        holder.cardView.setOnClickListener(v -> listener.onRegistrationClick(registration));
    }

    @Override
    public int getItemCount() {
        return registrationList.size();
    }

    static class RegistrationViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView volunteerNameTextView, statusTextView, dateTextView, roleTextView;

        public RegistrationViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            volunteerNameTextView = itemView.findViewById(R.id.volunteerNameTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            roleTextView = itemView.findViewById(R.id.roleTextView);
        }
    }
}