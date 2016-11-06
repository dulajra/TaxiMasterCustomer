package com.innocept.taximastercustomer.model.foundation;

/**
 * @author dulaj
 * @version 1.0.0
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
