package com.innocept.taximastercustomer.model;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.innocept.taximastercustomer.ApplicationContext;
import com.innocept.taximastercustomer.model.foundation.Location;
import com.innocept.taximastercustomer.model.foundation.Taxi;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dulaj on 14-Apr-16.
 */

public class Communicator{

    private final String TAG = Communicator.class.getSimpleName();

    private final String URL_ROOT = "http://33f42da1.ngrok.io";
    private final String URL_GET_AVAILABLE_TAXIS = URL_ROOT + "/customer/taxis";

    public Communicator() {
    }

    public List<Taxi> getAvailableTaxis(Location location, int taxiTypeId) {
        ContentValues values = new ContentValues();
        values.put("taxiTypeId", taxiTypeId);
        values.put("latitude", location.getLatitude());
        values.put("longitude", location.getLatitude());
        JSONObject response = HTTPHandler.sendPOST(URL_GET_AVAILABLE_TAXIS, values);

        List<Taxi> taxiList = new ArrayList<Taxi>();
        if (response != null) {
            Log.i(TAG, response.toString());
        }
        return taxiList;
    }
}

