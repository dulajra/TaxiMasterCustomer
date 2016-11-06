package com.innocept.taximastercustomer.model.foundation;

import java.io.Serializable;

/**
 * @author dulaj
 * @version 1.0.0
 */
public class FinishedOrder implements Serializable{

    private int id;
    private String origin;
    private String destination;
    private Location originCoordinates;
    private Location destinationCoordinates;
    private Time startTime;
    private Time endTime;
    private String note;
    private String contact;
    private Driver driver;
    private TaxiType taxiType;
    private int fare;
    private double distance;    // in Km
    private int rating;
    private String comment;

    public FinishedOrder() {
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

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaxiType getTaxiType() {
        return taxiType;
    }

    public void setTaxiType(TaxiType taxiType) {
        this.taxiType = taxiType;
    }

    public int getFare() {
        return fare;
    }

    public void setFare(int fare) {
        this.fare = fare;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public enum OrderState {

        ACCEPTED(1),
        PENDING(2),
        FINISHED(3),
        REJECTED(4),
        NOW(5);

        int value;

        OrderState(int v) {
            value = v;
        }

        public int getValue() {
            return value;
        }

    }
}
