package com.example.firebase;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize adapter
        eventAdapter = new EventAdapter(new ArrayList<>());
        recyclerView.setAdapter(eventAdapter);

        // Load events from Firebase
        loadEventsFromFirebase();

        return view;
    }

    private void loadEventsFromFirebase() {
        db.collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            List<Event> events = querySnapshot.toObjects(Event.class);
                            eventAdapter.updateEventList(events);
                        }
                    } else {
                        Log.e(TAG, "Error getting events: ", task.getException());
                    }
                });
    }
}
