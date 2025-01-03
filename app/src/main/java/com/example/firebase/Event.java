package com.example.firebase;

public class Event {
    private String id;
    private String name;
    private String date;
    private String startTime;
    private String endTime;
    private String location;
    private String category;
    private String photoUrl;;
    private String description;

    // Default constructor required for Firebase
    public Event() {}

    // Parameterized constructor
    public Event(String id, String name, String date, String startTime, String endTime, String location, String category, String photoUrl, String description) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.category = category;
        this.photoUrl = photoUrl;
        this.description = description;
    }

    // Getters
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDate() {
        return date;
    }
    public String getStartTime() {
        return startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public String getLocation() {
        return location;
    }
    public String getCategory() {
        return category;
    }
    public String getDescription() { return description; }

        // Setters (required for Firebase deserialization)
    public void setId(String id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    private String imageUrl;
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getPhotoUrl() {
        return photoUrl;
    }
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
