package com.example.firebase;

public class AppUser {

    String username, email, bio, faculty;
    int eventsJoined;

    public AppUser() {
    }

    public AppUser(String username, String email, String bio, String faculty, int eventsJoined) {
        this.username = username;
        this.email = email;
        this.bio = bio;
        this.faculty = faculty;
        this.eventsJoined = eventsJoined;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public int getEventsJoined() {
        return eventsJoined;
    }

    public void setEventsJoined(int eventsJoined) {
        this.eventsJoined = eventsJoined;
    }

}
