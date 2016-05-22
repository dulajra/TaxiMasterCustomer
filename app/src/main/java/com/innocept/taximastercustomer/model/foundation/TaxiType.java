package com.innocept.taximastercustomer.model.foundation;

/**
 * Created by Dulaj on 14-Apr-16.
 */
public enum TaxiType {

    NANO(1),
    CAR(2),
    VAN(3);

    int value;

    TaxiType(int v) {
        value = v;
    }

    public int getValue() {
        return value;
    }

}
