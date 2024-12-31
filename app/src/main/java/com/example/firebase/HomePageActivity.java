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
import android.widget.*;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.os.*;


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
class Event implements Parcelable {
    private String title, location, date, owner, desc, imageUrl;
    private int distance;
    private boolean vaccinatedOnly;
    private double price, discount;
    private String contact;

    // Constructor, Getters, Setters, and Parcelable implementation
    protected Event(Parcel in) {
        title = in.readString();
        location = in.readString();
        date = in.readString();
        owner = in.readString();
        desc = in.readString();
        imageUrl = in.readString();
        distance = in.readInt();
        vaccinatedOnly = in.readByte() != 0;
        price = in.readDouble();
        discount = in.readDouble();
        contact = in.readString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return desc;
    }

    public void setDescription(String description) {
        this.desc = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(desc);
        dest.writeString(date);
        dest.writeString(location);
        dest.writeString(imageUrl);
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };
}

// Adapter class for RecyclerView
class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {
    private List<Event> eventList = new ArrayList<>();
    private OnEventClickListener onEventClickListener;

    // Constructor
    public EventAdapter(List<Event> eventList, OnEventClickListener listener) {
        this.eventList = eventList;
        this.onEventClickListener = listener;
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

        // Handle click events
        holder.itemView.setOnClickListener(v -> {
            if (onEventClickListener != null) {
                onEventClickListener.onEventClick(event);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    // Method to update the event list dynamically
    public void updateEventList(List<Event> events) {
        this.eventList.clear();
        this.eventList.addAll(events);
        notifyDataSetChanged();
    }

    // Interface for handling item clicks
    public interface OnEventClickListener {
        void onEventClick(Event event);
    }
}

// ViewHolder class for RecyclerView
class EventViewHolder extends RecyclerView.ViewHolder {
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView dateTextView;
    private TextView locationTextView;
    private ImageView eventImageView;

    public EventViewHolder(@NonNull View itemView) {
        super(itemView);

        // Initialize views
        titleTextView = itemView.findViewById(R.id.event_title);
        descriptionTextView = itemView.findViewById(R.id.event_description);
        dateTextView = itemView.findViewById(R.id.event_date);
        locationTextView = itemView.findViewById(R.id.event_location);
        eventImageView = itemView.findViewById(R.id.event_image);
    }

    // Bind data from the Event model to the UI
    public void bind(Event event) {
        titleTextView.setText(event.getTitle());
        descriptionTextView.setText(event.getDescription());
        dateTextView.setText(event.getDate());
        locationTextView.setText(event.getLocation());

        // Load image using Picasso/Glide (make sure to add the library)
        if (event.getImageUrl() != null && !event.getImageUrl().isEmpty()) {
            // Load image from URL using Picasso
            Picasso.get()
                    .load(event.getImageUrl())
                    .placeholder(R.drawable.loading_placeholder) // Optional: Show while loading
                    .error(R.drawable.error_placeholder)         // Optional: Show if loading fails
                    .into(eventImageView);
        } else {
            // Set a default placeholder image if URL is null or empty
            eventImageView.setImageResource(R.drawable.placeholder_image);
        }
    }
}

