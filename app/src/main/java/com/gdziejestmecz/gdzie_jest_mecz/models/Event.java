package com.gdziejestmecz.gdzie_jest_mecz.models;

public class Event {
    private int id;
    private Match match;
    private Pub pub;
    private int numberOfAttendees;
    private double longitude;
    private double latiitude;
    private String description;

    public Event(int id, Match match, Pub pub, int numberOfAttendees, double longitude, double latiitude, String description) {
        this.id = id;
        this.match = match;
        this.pub = pub;
        this.numberOfAttendees = numberOfAttendees;
        this.longitude = longitude;
        this.latiitude = latiitude;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public Match getMatch() {
        return match;
    }

    public Pub getPub(){
     return pub;
    }

    public int getNumberOfAttendees() {
        return numberOfAttendees;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatiitude() {
        return latiitude;
    }

    public String getDescription() {
        return description;
    }
}
