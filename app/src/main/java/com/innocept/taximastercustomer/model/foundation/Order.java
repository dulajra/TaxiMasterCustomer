package com.innocept.taximastercustomer.model.foundation;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dulaj on 5/24/16.
 */
public class Order implements Serializable{

    private String origin;
    private String destination;
    private Location originCoordinates;
    private Location destinationCoordinates;
    private Date time;
    private String note;
    private Driver driver;
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

    public enum OrderState {

        ACCEPTED(1),
        PENDING(2),
        FINISHED(3);

        int value;

        OrderState(int v) {
            value = v;
        }

        public int getValue() {
            return value;
        }

    }
}