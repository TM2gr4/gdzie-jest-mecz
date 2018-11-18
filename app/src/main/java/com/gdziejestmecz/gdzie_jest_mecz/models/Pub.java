package com.gdziejestmecz.gdzie_jest_mecz.models;

public class Pub {

    private int id;
    private double latitude;
    private double longitude;
    private String name;
    private String street;
    private String number;

    public Pub(int id, double latitude, double longitude, String name, String street, String number) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.street = street;
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}


/*"id": 1,
"latitude": 51.7519858,
"longitude": 19.452498,
"street": "Profesora Bohdana Stefanowskiego",
"number": "17",
"name": "Indeks"*/