package com.innocept.taximastercustomer.model.foundation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author dulaj
 * @version 1.0.0
 */
public class Time extends Date {

    public Time(Date date) {
        super(date.getTime());
    }

    public Time(long milliseconds) {
        super(milliseconds);
    }

    public Time(String string) throws ParseException {
        super(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(string).getTime());
    }

    @Override
    public String toString() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(this);
    }
}
