package com.gdziejestmecz.gdzie_jest_mecz.models;

public class Match {
    private int id;
    private Team homeTeam;
    private Team awayTeam;
    private String date;
    private String time;

    public Match(int id, Team homeTeam, Team awayTeam, String date, String time) {
        this.id = id;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.date = date;
        this.time = time;
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
}
