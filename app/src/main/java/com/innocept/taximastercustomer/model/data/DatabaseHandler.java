package com.innocept.taximastercustomer.model.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.innocept.taximastercustomer.model.foundation.Driver;
import com.innocept.taximastercustomer.model.foundation.Location;
import com.innocept.taximastercustomer.model.foundation.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by dulaj on 5/25/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private final String DEBUG_TAG = DatabaseHandler.class.getSimpleName();

    // Database details
    private static final int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "taxiMaster.db";

    // Table names
    private static String TABLE_MY_ORDERS = "myOrders";

    // Table Columns name
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_ORIGIN = "origin";
    private static final String COLUMN_DESTINATION = "destination";
    private static final String COLUMN_ORIGIN_LATITUDE = "originLatitude";
    private static final String COLUMN_ORIGIN_LONGITUDE = "originLongitude";
    private static final String COLUMN_DESTINATION_LATITUDE = "destinationLatitude";
    private static final String COLUMN_DESTINATION_LONGITUDE = "destinationLongitude";
    private static final String COLUMN_DRIVER_ID = "driverId";
    private static final String COLUMN_DRIVER_NAME = "driverName";
    private static final String COLUMN_DRIVER_PHONE = "driverPhone";
    private static final String COLUMN_STATE = "state";

    // Create queries
    private static final String CREATE_TABLE_MY_ORDERS = "CREATE TABLE " + TABLE_MY_ORDERS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_TIME + " TEXT NOT NULL, " + COLUMN_ORIGIN + " TEXT NOT NULL, " + COLUMN_DESTINATION + " TEXT NOT NULL, " + COLUMN_ORIGIN_LATITUDE + " REAL NOT NULL, " + COLUMN_ORIGIN_LONGITUDE + " REAL NOT NULL, " + COLUMN_DESTINATION_LATITUDE + " REAL NOT NULL, " + COLUMN_DESTINATION_LONGITUDE + " REAL NOT NULL, " + COLUMN_DRIVER_ID + " INTEGER NOT NULL, " + COLUMN_DRIVER_NAME + " TEXT NOT NULL, " + COLUMN_DRIVER_PHONE + " TEXT NOT NULL, " + COLUMN_STATE + " TEXT NOT NULL)";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MY_ORDERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_MY_ORDERS);
    }


    public List<Order> getAllOrders(Order.OrderState[] orderStates) {
        List<Order> orderList = new ArrayList<Order>();
        SQLiteDatabase rdb;
        rdb = this.getReadableDatabase();

        String[] selectionArgs =  new String[orderStates.length];
        StringBuilder inQuery = new StringBuilder();

        selectionArgs[0] = orderStates[0].toString();
        inQuery.append("?");

        for(int i=1;i<orderStates.length;i++){
            selectionArgs[i] = orderStates[i].toString();
            inQuery.append(", ?");
        }
        String selectQuery = "SELECT * FROM " + TABLE_MY_ORDERS + " WHERE " + COLUMN_STATE + " IN(" + inQuery.toString() + ")";
        Cursor cursor = rdb.rawQuery(selectQuery, selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm");
                Date time = null;
                try {
                    time = sdf.parse(cursor.getString(1));
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e(DEBUG_TAG, "Error when formatting date." + e.toString());
                }

                Order order = new Order();
                order.setTime(time);
                order.setOrigin(cursor.getString(2));
                order.setDestination(cursor.getString(3));
                order.setOriginCoordinates(new Location(cursor.getDouble(4), cursor.getDouble(5)));
                order.setDestinationCoordinates(new Location(cursor.getDouble(6), cursor.getDouble(7)));
                Driver driver = new Driver();
                driver.setId(cursor.getInt(8));
                driver.setFirstName(cursor.getString(9));
                driver.setPhone(cursor.getString(10));
                order.setDriver(driver);
                order.setOrderState(Order.OrderState.valueOf(cursor.getString(11)));
                orderList.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        rdb.close();

        return orderList;
    }

    public void saveOrder(Order order) {
        SQLiteDatabase wdb;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm");
        String time = sdf.format(order.getTime());

        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, order.getId());
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_ORIGIN, order.getOrigin());
        values.put(COLUMN_DESTINATION, order.getDestination());
        values.put(COLUMN_ORIGIN_LATITUDE, order.getOriginCoordinates().getLatitude());
        values.put(COLUMN_ORIGIN_LONGITUDE, order.getOriginCoordinates().getLongitude());
        values.put(COLUMN_DESTINATION_LATITUDE, order.getDestinationCoordinates().getLatitude());
        values.put(COLUMN_DESTINATION_LONGITUDE, order.getDestinationCoordinates().getLongitude());
        values.put(COLUMN_STATE, order.getOrderState().toString());
        values.put(COLUMN_DRIVER_ID, order.getDriver().getId());
        values.put(COLUMN_DRIVER_NAME, order.getDriver().getFirstName());
        values.put(COLUMN_DRIVER_PHONE, order.getDriver().getPhone());
        wdb = this.getWritableDatabase();
        wdb.insert(TABLE_MY_ORDERS, null, values);
        wdb.close();
    }

    public void updateOrderState(int id, Order.OrderState orderState) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STATE, orderState.toString());
        db.update(TABLE_MY_ORDERS, values, COLUMN_ID + "='" + id + "'", null);
        db.close();
    }
}
