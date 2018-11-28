package com.gdziejestmecz.gdzie_jest_mecz.models;

import java.util.ArrayList;

public class Match {
    private int id;
    private Team homeTeam;
    private Team awayTeam;
    private String date;
    private String time;
    private ArrayList<Pub> pubs;

    public Match(int id, Team homeTeam, Team awayTeam, String date, String time, ArrayList<Pub> pubs) {
        this.id = id;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.date = date;
        this.time = time;
        this.pubs = pubs;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Pub> getPubs() {
        return pubs;
    }
}
