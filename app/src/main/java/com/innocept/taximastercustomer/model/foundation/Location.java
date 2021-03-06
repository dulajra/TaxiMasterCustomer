package com.innocept.taximastercustomer.model.foundation;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * @author dulaj
 * @version 1.0.0
 */
public class Location implements Serializable{

    private double longitude;
    private double latitude;

    public Location() {
        this.longitude=0;
        this.latitude=0;
    }

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public LatLng getLatLng(){
        return new LatLng(this.latitude, this.longitude);
    }
}
