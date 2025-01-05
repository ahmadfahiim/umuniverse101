package com.example.firebase;

public class Profile {

    private String userId;
    private String username;
    private String profilePictureUrl;

    public Profile() {
        // Default constructor for Firebase
    }

    public Profile(String userId, String username, String profilePictureUrl) {
        this.userId = userId;
        this.username = username;
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}
