package com.innocept.taximastercustomer.model.foundation;

import java.io.Serializable;

/**
 * Created by Dulaj on 16-Apr-16.
 */
public class Driver implements Serializable{

    private int id;
    private String firstName;
    private String lastName;
    private String phone;

    public Driver() {
    }

    public Driver(int id, String firstName, String lastName, String phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
