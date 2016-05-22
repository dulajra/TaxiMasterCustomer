package com.innocept.taximastercustomer.model.foundation;

/**
 * Created by dulaj on 5/22/16.
 */
public class Taxi {

    private Driver driver;
    private String model;
    private int noOfSeats;
    private String distance;
    private String duration;

    public Taxi(Driver driver, String model, int noOfSeats, String distance, String duration) {
        this.driver = driver;
        this.model = model;
        this.noOfSeats = noOfSeats;
        this.distance = distance;
        this.duration = duration;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getNoOfSeats() {
        return noOfSeats;
    }

    public void setNoOfSeats(int noOfSeats) {
        this.noOfSeats = noOfSeats;
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
}
