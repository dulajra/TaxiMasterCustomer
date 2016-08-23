package com.innocept.taximastercustomer.model.network;

import android.content.ContentValues;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.innocept.taximastercustomer.ApplicationPreferences;
import com.innocept.taximastercustomer.model.foundation.Driver;
import com.innocept.taximastercustomer.model.foundation.User;
import com.innocept.taximastercustomer.model.foundation.Location;
import com.innocept.taximastercustomer.model.foundation.Order;
import com.innocept.taximastercustomer.model.foundation.Taxi;
import com.innocept.taximastercustomer.model.foundation.TaxiType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dulaj on 14-Apr-16.
 */

public class Communicator {

    private final String DEBUG_TAG = Communicator.class.getSimpleName();

    private final String URL_ROOT = "http://62643790.ngrok.io";
    private final String URL_LOGIN = URL_ROOT + "/customer/login";
    private final String URL_LOGOUT = URL_ROOT + "/customer/logout";
    private final String URL_SIGN_UP = URL_ROOT + "/customer/signUp";
    private final String URL_GET_AVAILABLE_TAXIS = URL_ROOT + "/customer/taxis";
    private final String URL_PLACE_ORDER = URL_ROOT + "/customer/order/new";
    private final String URL_GET_DRIVER_UPDATE = URL_ROOT + "/customer/get/driverUpdate";
    private final String URL_GET_MY_ORDERS = URL_ROOT + "/customer/orders";

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

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Taxi taxi = new Gson().fromJson(jsonObject.toString(), Taxi.class);
                taxiList.add(taxi);
            }
        } catch (JSONException e) {
            Log.e(DEBUG_TAG, "Error converting to json array " + e.toString());
        } catch (NullPointerException e) {
            Log.e(DEBUG_TAG, "Server error occurred " + e.toString());
        }

        return taxiList;
    }

    public int placeOrder(Order order) {
        ContentValues values = new ContentValues();
        values.put("origin", order.getOrigin());
        values.put("customerId",ApplicationPreferences.getUser().getId());
        values.put("destination", order.getDestination());
        values.put("originLatitude", order.getOriginCoordinates().getLatitude());
        values.put("originLongitude", order.getOriginCoordinates().getLongitude());
        values.put("destinationLatitude", order.getDestinationCoordinates().getLatitude());
        values.put("destinationLongitude", order.getDestinationCoordinates().getLongitude());
        values.put("note", order.getNote());
        values.put("contact", order.getContact());
        values.put("driverId", order.getDriver().getId());
//        values.put("oneSignalUserId", ApplicationPreferences.getOneSignalUserId());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = sdf.format(order.getTime());
        values.put("time", time);

        String response = HTTPHandler.sendGET(URL_PLACE_ORDER, values);

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean("success")) {
                return jsonObject.getInt("id");
            }
        } catch (JSONException e) {
            Log.e(DEBUG_TAG, "Error converting to json object " + e.toString());
        } catch (NullPointerException e) {
            Log.e(DEBUG_TAG, "Server error occurred " + e.toString());
        }
        return -1;
    }

    /**
     * @param driverId
     * @param latLng
     * @return new String[]{latitude, longitude, distance, duration}
     */
    public String[] getDriverUpdates(int driverId, LatLng latLng) {
        ContentValues values = new ContentValues();
        values.put("driverId", driverId);
        values.put("latitude", latLng.latitude);
        values.put("longitude", latLng.longitude);

        String response = HTTPHandler.sendGET(URL_GET_DRIVER_UPDATE, values);

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean("success")) {
                return new String[]{String.valueOf(jsonObject.getDouble("latitude")), String.valueOf(jsonObject.getDouble("longitude")), jsonObject.getString("distance"), jsonObject.getString("duration")};
            }
        } catch (JSONException e) {
            Log.e(DEBUG_TAG, "Error converting to json object " + e.toString());
        } catch (NullPointerException e) {
            Log.e(DEBUG_TAG, "Server error occurred " + e.toString());
        }
        return null;
    }

    public int login(String phone, String password) {
        User user = null;
        int resultCode = -1;
        ContentValues values = new ContentValues();
        values.put("phone", phone);
        values.put("password", password);
        values.put("oneSignalUserId", ApplicationPreferences.getOneSignalUserId());
        String response = HTTPHandler.sendPOST(URL_LOGIN, values);

        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int result = jsonObject.getInt("success");
                switch (result) {
                    case 0:
                        user = new Gson().fromJson(jsonObject.getJSONObject("customer").toString(), User.class);
                        ApplicationPreferences.saveUser(user);
                        resultCode = 0;
                        Log.i(DEBUG_TAG, "Login success");
                        break;
                    case 1:
                        resultCode = 1;
                        Log.i(DEBUG_TAG, "Username or password is incorrect");
                        break;
                }
            } catch (JSONException e) {
                Log.e(DEBUG_TAG, e.toString());
            } catch (NullPointerException e) {
                Log.e(DEBUG_TAG, "Server error occurred " + e.toString());
            }
        }
        return resultCode;
    }

    public boolean logout() {
        boolean result = false;
        ContentValues values = new ContentValues();
        values.put("phone", ApplicationPreferences.getUser().getPhone());
        String response = HTTPHandler.sendPOST(URL_LOGOUT, values);

        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                result = jsonObject.getBoolean("success");
                if (result) {
                    ApplicationPreferences.saveUser(null);
                    Log.i(DEBUG_TAG, "Logout success");
                } else {
                    Log.i(DEBUG_TAG, "Logout failed");
                }
            } catch (JSONException e) {
                Log.e(DEBUG_TAG, e.toString());
            } catch (NullPointerException e) {
                Log.e(DEBUG_TAG, "Server error occurred " + e.toString());
            }
        }
        return result;
    }

    public int signUp(User user, String password) {
        int resultCode = -1;
        ContentValues values = new ContentValues();
        values.put("firstName", user.getFirstName());
        values.put("lastName", user.getLastName());
        values.put("phone", user.getPhone());
        values.put("password", password);
        values.put("oneSignalUserId", ApplicationPreferences.getOneSignalUserId());
        String response = HTTPHandler.sendPOST(URL_SIGN_UP, values);

        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int result = jsonObject.getInt("success");
                switch (result) {
                    case 0:
                        resultCode = 0;
                        Log.i(DEBUG_TAG, "Sign up success");
                        break;
                    case 1:
                        resultCode = 1;
                        Log.i(DEBUG_TAG, "Phone number has been taken already");
                        break;
                }
            } catch (JSONException e) {
                Log.e(DEBUG_TAG, e.toString());
            } catch (NullPointerException e) {
                Log.e(DEBUG_TAG, "Server error occurred " + e.toString());
            }
        }
        return resultCode;
    }

    public List<Order> getMyOrders(int userId, String state) {
        List<Order> orderList = null;
        ContentValues values = new ContentValues();
        values.put("customerId", userId);
        values.put("state", state);
        String response = HTTPHandler.sendGET(URL_GET_MY_ORDERS, values);

        if (response != null) {
            try {
                orderList = new ArrayList<Order>();
                JSONArray jsonArray = new JSONArray(response);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Order order = new Order();
                    order.setId(jsonObject.getInt("id"));
                    order.setOrigin(jsonObject.getString("origin"));
                    order.setOriginCoordinates(new Location(jsonObject.getDouble("originLatitude"), jsonObject.getDouble("originLongitude")));
                    order.setDestination(jsonObject.getString("destination"));
                    order.setDestinationCoordinates(new Location(jsonObject.getDouble("destinationLatitude"), jsonObject.getDouble("destinationLongitude")));
                    order.setContact(jsonObject.getString("contact"));
                    order.setDriver(new Gson().fromJson(jsonObject.getJSONObject("taxi_driver").toString(), Driver.class));

                    if(state.equals(Order.OrderState.FINISHED.toString())){
                        order.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(jsonObject.getString("startTime")));
                        order.setOrderState(Order.OrderState.FINISHED);
                    }
                    else{
                        order.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(jsonObject.getString("time")));
                        order.setOrderState(Order.OrderState.valueOf(jsonObject.getString("state")));
                        order.setNote(jsonObject.getString("note"));
                    }
                    order.setContact(jsonObject.getString("contact"));
                    order.setDriver(new Gson().fromJson(jsonObject.getJSONObject("taxi_driver").toString(), Driver.class));
                    orderList.add(order);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            Log.v(DEBUG_TAG, "Response is null");
        }
        return orderList;
    }
}

