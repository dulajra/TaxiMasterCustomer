package com.innocept.taximastercustomer.model.foundation;

import java.util.Date;

/**
 * Created by dulaj on 5/24/16.
 */
public class Order {

    private String origin;
    private String destination;
    private Location originCoordinates;
    private Location destinationCoordinates;
    private Date time;
    private String note;
    private int driverId;

    public Order() {
    }

    public Order(String origin, String destination, Location originCoordinates, Location destinationCoordinates, Date time, String note, int driverId) {
        this.origin = origin;
        this.destination = destination;
        this.originCoordinates = originCoordinates;
        this.destinationCoordinates = destinationCoordinates;
        this.time = time;
        this.note = note;
        this.driverId = driverId;
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Location getOriginCoordinates() {
        return originCoordinates;
    }

    public void setOriginCoordinates(Location originCoordinates) {
        this.originCoordinates = originCoordinates;
    }

    public Location getDestinationCoordinates() {
        return destinationCoordinates;
    }

    public void setDestinationCoordinates(Location destinationCoordinates) {
        this.destinationCoordinates = destinationCoordinates;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
