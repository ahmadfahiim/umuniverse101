package com.example.firebase;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {

    private Context context;
    private List<Profile> profileList;

    public ProfileAdapter(Context context, List<Profile> profileList) {
        this.context = context;
        this.profileList = profileList;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_profile, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Profile profile = profileList.get(position);

        if(profile.getUsername() == null) {
            holder.usernameTextView.setText("Username Not Available");
        } else {
            holder.usernameTextView.setText(profile.getUsername());
        }

        Glide.with(context)
                .load(profile.getProfilePictureUrl())
                .placeholder(R.drawable.logo) // Fallback image
                .into(holder.profileImageView);

        // Set click listener to open the profile page
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OtherProfilePage.class);
            intent.putExtra("userId", profile.getUserId()); // Pass userId to the ProfilePageActivity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        ImageView profileImageView;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.tvUsername);
            profileImageView = itemView.findViewById(R.id.ivProfilePicture);
        }
    }

}
