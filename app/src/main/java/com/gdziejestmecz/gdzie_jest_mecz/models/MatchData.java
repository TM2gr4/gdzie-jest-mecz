package com.gdziejestmecz.gdzie_jest_mecz.models;

import java.time.LocalDateTime;

public class MatchData {
    public int id;
    public Team homeTeam; //raczej bedzie to kiedys tylko id druzyny
    public Team awayTeam; //raczej bedzie to kiedys tylko id druzyny
//    public Pub pub;      //raczej bedzie to kiedys tylko id pubu
    public String date;
    public LocalDateTime time;

    public MatchData(int id, Team homeTeam, Team awayTeam, String date, LocalDateTime time) {
        this.id = id;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
//        this.pub = pub;
        this.date = date;
        this.time = time;
    }
}
