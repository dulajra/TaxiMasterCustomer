package com.innocept.taximastercustomer.model.foundation;

import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author dulaj
 * @version 1.0.0
 */
public class Order implements Serializable{

    private int id;
    private String origin;
    private String destination;
    private Location originCoordinates;
    private Location destinationCoordinates;
    private Time time;
    private String note;
    private String contact;
    private Driver driver;
    private TaxiType taxiType;
    private int fare;
    private double distance;    // in Km
    private Order.OrderState orderState;

    public Order() {
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

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
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

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
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
