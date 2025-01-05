package com.example.firebase;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import com.bumptech.glide.Glide;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;
    private OnEventClickListener listener;

    public EventAdapter(List<Event> eventList, OnEventClickListener listener) {
        this.eventList = eventList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.title.setText(event.getName());
        holder.location.setText(event.getLocation());
        holder.date.setText(event.getDate());
        holder.startTime.setText(event.getStartTime());
        holder.endTime.setText(event.getEndTime());
        holder.category.setText(event.getCategory());
        holder.description.setText(event.getDescription());

        if (event.getPhotoUrl() != null && !event.getPhotoUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(event.getPhotoUrl())
                    .placeholder(R.drawable.edit_pen) // Temporary placeholder during loading
                    .error(R.drawable.logo) // Fallback image if loading fails
                    .into(holder.photo);
        } else {
            Log.d("EventAdapter", "Invalid photoUrl for event: " + event.getName());
            holder.photo.setImageResource(R.drawable.edit_pen);  // Set a placeholder if no image is available
        }

        // Handle click events
        holder.itemView.setOnClickListener(v -> listener.onEventClick(event));
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView title, location, date, startTime, endTime, category, description;
        ImageView photo;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.eventTitle);
            location = itemView.findViewById(R.id.eventLocation);
            date = itemView.findViewById(R.id.eventDate);
            startTime = itemView.findViewById(R.id.eventStartTime);
            endTime = itemView.findViewById(R.id.eventEndTime);
            category = itemView.findViewById(R.id.eventCategory);
            photo = itemView.findViewById(R.id.eventPhoto);
            description = itemView.findViewById(R.id.eventDescription);
        }
    }

    public void filterList(List<Event> filteredList) {
        this.eventList = filteredList;
        notifyDataSetChanged();
    }

    public interface OnEventClickListener {
        void onEventClick(Event event);
    }
}
