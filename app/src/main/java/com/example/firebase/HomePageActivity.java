package com.example.firebase;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;



public class HomePageActivity extends AppCompatActivity {

    private static final String TAG = "HomePageActivity";
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }
    }
}

// Event model class
class Event {
    private String title;
    private String description;

    public Event() {}

    public Event(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

// Adapter class for RecyclerView
class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {
    private List<Event> eventList = new ArrayList<>();
    public EventAdapter(List<Event> eventList) {
        this.eventList = eventList;
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
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void updateEventList(List<Event> events) {
        this.eventList.clear();
        this.eventList.addAll(events);
        notifyDataSetChanged();
    }
}

// ViewHolder class for RecyclerView
class EventViewHolder extends RecyclerView.ViewHolder {
    private TextView titleTextView;
    private TextView descriptionTextView;

    public EventViewHolder(@NonNull View itemView) {
        super(itemView);
        titleTextView = itemView.findViewById(R.id.event_title);
        descriptionTextView = itemView.findViewById(R.id.event_description);
    }

    public void bind(Event event) {
        titleTextView.setText(event.getTitle());
        descriptionTextView.setText(event.getDescription());
    }
}
