package com.gdziejestmecz.gdzie_jest_mecz.models;

import java.time.LocalDateTime;
import java.util.Date;

public class EventData {
    public String teamOneName; //raczej bedzie to kiedys tylko id druzyny
    public String teamTwoName; //raczej bedzie to kiedys tylko id druzyny
    public String pubName;      //raczej bedzie to kiedys tylko id pubu
    public LocalDateTime dateTime;

    public EventData(String teamOneName, String teamTwoName, String pubName, LocalDateTime dateTime) {
        this.teamOneName = teamOneName;
        this.teamTwoName = teamTwoName;
        this.pubName = pubName;
        this.dateTime = dateTime;
    }
}
