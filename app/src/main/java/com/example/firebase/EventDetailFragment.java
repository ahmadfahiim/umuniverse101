package com.example.firebase;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventDetailFragment extends Fragment {
    private TextView title, location, date, owner, about, price, contact;
    private ImageView image;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        title = view.findViewById(R.id.title);
        location = view.findViewById(R.id.location);
        date = view.findViewById(R.id.date);
        owner = view.findViewById(R.id.owner);
        about = view.findViewById(R.id.about);
        price = view.findViewById(R.id.price);
        contact = view.findViewById(R.id.contact);
        image = view.findViewById(R.id.image);

        // Get event data
        Event event = EventDetailFragmentArgs.fromBundle(getArguments()).getEvent();

        // Set data
        title.setText(event.getTitle());
        location.setText(event.getLocation());
        date.setText(event.getDate());
        owner.setText("Owner: " + event.getOwner());
        about.setText(event.getAbout());
        price.setText(String.format("Price: %.2f MYR (%.0f%% Off)", event.getPrice(), event.getDiscount()));
        contact.setText("Contact: " + event.getContact());

        Picasso.get().load(event.getImageUrl()).into(image);
    }
}


