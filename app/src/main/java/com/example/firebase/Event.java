package com.example.firebase;

public class Event {
    private String title;
    private String location;
    private String distance;
    private String date;
    private int imageResId;
    private boolean vaccinatedOnly;

    public Event(String title, String location, String distance, String date, int imageResId, boolean vaccinatedOnly) {
        this.title = title;
        this.location = location;
        this.distance = distance;
        this.date = date;
        this.imageResId = imageResId;
        this.vaccinatedOnly = vaccinatedOnly;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getDistance() {
        return distance;
    }

    public String getDate() {
        return date;
    }

    public int getImageResId() {
        return imageResId;
    }

    public boolean isVaccinatedOnly() {
        return vaccinatedOnly;
    }
}
