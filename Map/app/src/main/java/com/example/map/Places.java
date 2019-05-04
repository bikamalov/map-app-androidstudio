package com.example.map;

public class Places {
    private int id=-1;
    private String title;
    private String description;
    private double latitude;
    private double longitude;
    public Places(String title,String description,double latitude,double longitude) {
        this.title = title;
        this.description = description;
        this.longitude = longitude;
        this.latitude = latitude;
    }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public void setID(int ID) { this.id = ID; }
    public Places(){}
    public int getId() { return id; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getTitle(){ return title; }
    public void setTitle(String title) { this.title = title; }
}