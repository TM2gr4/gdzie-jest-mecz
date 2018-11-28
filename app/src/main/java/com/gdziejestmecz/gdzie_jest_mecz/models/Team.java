package com.gdziejestmecz.gdzie_jest_mecz.models;

public class Team {
    private int teamId;
    private String name;
    private String logoURL;
    private String countryOfOrigin;

    public Team(int teamId, String name, String logoURL, String countryOfOrigin) {
        this.teamId = teamId;
        this.name = name;
        this.logoURL = logoURL;
        this.countryOfOrigin = countryOfOrigin;
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

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }
}
