package com.example.volunteerapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.volunteerapp.R;
import com.example.volunteerapp.models.Registration;
import java.util.List;

public class VolunteerAdapter extends RecyclerView.Adapter<VolunteerAdapter.VolunteerViewHolder> {

    private Context context;
    private List<Registration> registrationList;
    private OnActionClickListener listener;

    public interface OnActionClickListener {
        void onApprove(Registration registration);
        void onReject(Registration registration);
        void onViewDetails(Registration registration);
    }

    public VolunteerAdapter(Context context, List<Registration> registrationList,
                            OnActionClickListener listener) {
        this.context = context;
        this.registrationList = registrationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VolunteerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_volunteer, parent, false);
        return new VolunteerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VolunteerViewHolder holder, int position) {
        Registration registration = registrationList.get(position);

        holder.volunteerNameTextView.setText(registration.getVolunteerName());
        holder.emailTextView.setText(registration.getVolunteerEmail());
        holder.phoneTextView.setText(registration.getVolunteerPhone());
        holder.statusTextView.setText("Status: " + registration.getStatus());

        if ("pending".equals(registration.getStatus())) {
            holder.approveButton.setVisibility(View.VISIBLE);
            holder.rejectButton.setVisibility(View.VISIBLE);
        } else {
            holder.approveButton.setVisibility(View.GONE);
            holder.rejectButton.setVisibility(View.GONE);
        }

        holder.approveButton.setOnClickListener(v -> listener.onApprove(registration));
        holder.rejectButton.setOnClickListener(v -> listener.onReject(registration));
        holder.cardView.setOnClickListener(v -> listener.onViewDetails(registration));
    }

    @Override
    public int getItemCount() {
        return registrationList.size();
    }

    static class VolunteerViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView volunteerNameTextView, emailTextView, phoneTextView, statusTextView;
        Button approveButton, rejectButton;

        public VolunteerViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            volunteerNameTextView = itemView.findViewById(R.id.volunteerNameTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            approveButton = itemView.findViewById(R.id.approveButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
        }
    }
}