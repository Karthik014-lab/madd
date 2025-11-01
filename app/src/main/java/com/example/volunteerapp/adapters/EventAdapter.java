package com.example.volunteerapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.volunteerapp.R;
import com.example.volunteerapp.models.Event;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private Context context;
    private List<Event> eventList;
    private OnEventClickListener listener;

    public interface OnEventClickListener {
        void onEventClick(Event event);
    }

    public EventAdapter(Context context, List<Event> eventList, OnEventClickListener listener) {
        this.context = context;
        this.eventList = eventList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);

        holder.eventNameTextView.setText(event.getEventName());
        holder.organizerTextView.setText(event.getOrganizerName());
        holder.cityTextView.setText(event.getCity());

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        holder.dateTextView.setText(dateFormat.format(new Date(event.getEventDate())));

        holder.volunteersTextView.setText(event.getRegisteredVolunteers() + "/" +
                event.getVolunteersRequired() + " volunteers");

        if (event.getImageUrl() != null && !event.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(event.getImageUrl())
                    .placeholder(R.drawable.placeholder_event)
                    .into(holder.eventImageView);
        } else {
            holder.eventImageView.setImageResource(R.drawable.placeholder_event);
        }

        holder.cardView.setOnClickListener(v -> listener.onEventClick(event));
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView eventImageView;
        TextView eventNameTextView, organizerTextView, cityTextView, dateTextView, volunteersTextView;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            eventImageView = itemView.findViewById(R.id.eventImageView);
            eventNameTextView = itemView.findViewById(R.id.eventNameTextView);
            organizerTextView = itemView.findViewById(R.id.organizerTextView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            volunteersTextView = itemView.findViewById(R.id.volunteersTextView);
        }
    }
}