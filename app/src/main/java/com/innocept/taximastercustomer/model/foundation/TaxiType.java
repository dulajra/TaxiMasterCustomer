package com.innocept.taximastercustomer.model.foundation;

/**
 * Created by Dulaj on 14-Apr-16.
 */
public enum TaxiType {

    NANO,
    CAR,
    VAN;

    private final int value;

    TaxiType() {
        this.value = ordinal() + 1;
    }

    public int getValue() {
        return value;
    }

    public static TaxiType getEnum(int value){
        return TaxiType.values()[value-1];
    }

}
