package com.gdziejestmecz.gdzie_jest_mecz.models;

public class Team {
    private int teamId;
    private String name;
    private String logoURL;

    public Team(int teamId, String name, String logoURL) {
        this.teamId = teamId;
        this.name = name;
        this.logoURL = logoURL;
    }

    public int getTeamId() {
        return teamId;
    }

    public String getName() {
        return name;
    }

    public String getLogoURL() {
        return logoURL;
    }
}
