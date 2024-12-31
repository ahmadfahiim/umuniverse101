package com.example.firebase;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

// Android libraries
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// Firebase Firestore
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

// Your adapter and model classes (replace with your actual package names)
import com.example.app.adapters.EventAdapter; // Your RecyclerView adapter
import com.example.app.models.Event;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {
    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<Event> eventList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        eventList = new ArrayList<>();
        adapter = new EventAdapter(getContext(), eventList);
        recyclerView.setAdapter(adapter);

        fetchEventsFromFirebase();
    }

    private void fetchEventsFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshots = task.getResult();
                        if (snapshots != null) {
                            eventList.clear();
                            eventList.addAll(snapshots.toObjects(Event.class));
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }
}
