package com.innocept.taximastercustomer.model.foundation;

/**
 * Created by Dulaj on 14-Aug-16.
 */
public class User {

    private String firstName;
    private String lastName;
    private String phone;

    public User() {
    }

    public User(String firstName, String lastName, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
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

    public String getFullName(){
        return this.firstName + " " + this.lastName;
    }
}
