package com.example.firebase;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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
        holder.title.setText(event.getTitle());
        holder.location.setText(event.getLocation());
        holder.distance.setText(event.getDistance());
        holder.date.setText(event.getDate());
        holder.imageView.setImageResource(event.getImageResId());
        holder.vaccinatedOnly.setVisibility(event.isVaccinatedOnly() ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> listener.onEventClick(event));
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView title, location, distance, date;
        ImageView imageView, vaccinatedOnly;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.eventTitle);
            location = itemView.findViewById(R.id.eventLocation);
            distance = itemView.findViewById(R.id.eventDistance);
            date = itemView.findViewById(R.id.eventDate);
            imageView = itemView.findViewById(R.id.eventImage);
            vaccinatedOnly = itemView.findViewById(R.id.vaccinatedOnlyIcon);
        }
    }

    public interface OnEventClickListener {
        void onEventClick(Event event);
    }
}
