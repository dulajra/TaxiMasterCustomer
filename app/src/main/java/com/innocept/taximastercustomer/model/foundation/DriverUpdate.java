package com.innocept.taximastercustomer.model.foundation;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Dulaj on 27-Aug-16.
 */
public class DriverUpdate {

    private String location;
    private double latitude;
    private double longitude;
    private String distance;
    private String duration;

    public DriverUpdate() {

    }

    public DriverUpdate(String location, double latitude, double longitude, String distance, String duration) {
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.duration = duration;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    /* public String getDistanceAsString(){
        if(distance<1000){
            return distance + " m";
        }
        else {
            return String.format("%.2f", distance/1000.0) + " Km";
        }
    }

    public String getDurationAsString(){
        if(duration<60){
            return duration + "mins";
        }
        else {
            return duration%60 + " h " + duration/60 + " mins";
        }
    }*/
}
