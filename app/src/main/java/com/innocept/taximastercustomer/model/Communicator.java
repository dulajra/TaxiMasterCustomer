package com.innocept.taximastercustomer.model;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.innocept.taximastercustomer.ApplicationContext;
import com.innocept.taximastercustomer.model.foundation.Location;
import com.innocept.taximastercustomer.model.foundation.Order;
import com.innocept.taximastercustomer.model.foundation.Taxi;
import com.innocept.taximastercustomer.model.foundation.TaxiType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dulaj on 14-Apr-16.
 */

public class Communicator{

    private final String DEBUG_TAG = Communicator.class.getSimpleName();

    private final String URL_ROOT = "http://183a9906.ngrok.io";
    private final String URL_GET_AVAILABLE_TAXIS = URL_ROOT + "/customer/taxis";
    private final String URL_PLACE_ORDER = URL_ROOT + "/customer/order/new";


    public Communicator() {
    }

    public List<Taxi> getAvailableTaxis(Location location, TaxiType taxiType) {
        ContentValues values = new ContentValues();
        values.put("taxiTypeId", taxiType.getValue());
        values.put("latitude", location.getLatitude());
        values.put("longitude", location.getLongitude());
        String response = HTTPHandler.sendGET(URL_GET_AVAILABLE_TAXIS, values);
        List<Taxi> taxiList = new ArrayList<Taxi>();

        try {
            JSONArray jsonArray = new JSONArray(response);

            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Taxi taxi = new Gson().fromJson(jsonObject.toString(), Taxi.class);
                taxiList.add(taxi);
            }
        } catch (JSONException e) {
            Log.e(DEBUG_TAG, "Error converting to json array " + e.toString());
        }

        return taxiList;
    }

    public boolean placeOrder(Order order){
        ContentValues values = new ContentValues();
        values.put("origin", order.getOrigin());
        values.put("destination", order.getDestination());
        values.put("originLatitude", order.getOriginCoordinates().getLatitude());
        values.put("originLongitude", order.getOriginCoordinates().getLongitude());
        values.put("destinationLongitude", order.getDestinationCoordinates().getLatitude());
        values.put("destinationLongitude", order.getDestinationCoordinates().getLatitude());
        values.put("note", order.getNote());
        values.put("driverId", order.getDriverId());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm");
        String time = sdf.format(order.getTime());
        values.put("time", time);

        String response = HTTPHandler.sendPOST(URL_PLACE_ORDER, values);

        try {
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.getBoolean("success");
        } catch (JSONException e) {
            Log.e(DEBUG_TAG, "Error converting to json object " + e.toString());
        }
        return false;
    }

}

