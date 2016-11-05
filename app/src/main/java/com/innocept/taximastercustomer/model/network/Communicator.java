package com.innocept.taximastercustomer.model.network;

import android.content.ContentValues;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.innocept.taximastercustomer.ApplicationPreferences;
import com.innocept.taximastercustomer.model.foundation.Driver;
import com.innocept.taximastercustomer.model.foundation.DriverUpdate;
import com.innocept.taximastercustomer.model.foundation.FinishedOrder;
import com.innocept.taximastercustomer.model.foundation.Offer;
import com.innocept.taximastercustomer.model.foundation.Time;
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

    private static final String DEBUG_TAG = Communicator.class.getSimpleName();

    private static final String URL_ROOT = "http://taximaster.herokuapp.com";
//    private static final String URL_ROOT = "http://92893741.ngrok.io";

    private static final String URL_LOGIN = URL_ROOT + "/customer/login";
    private static final String URL_LOGOUT = URL_ROOT + "/customer/signOut";
    private static final String URL_SIGN_UP = URL_ROOT + "/customer/signUp";
    private static final String URL_GET_AVAILABLE_TAXIS = URL_ROOT + "/customer/taxis";
    private static final String URL_PLACE_ORDER = URL_ROOT + "/customer/order/new";
    private static final String URL_GET_DRIVER_UPDATE = URL_ROOT + "/customer/get/driverUpdate";
    private static final String URL_GET_DRIVER_LOCATION = URL_ROOT + "/customer/get/driverLocation";
    private static final String URL_GET_MY_ORDERS = URL_ROOT + "/customer/orders";
    private static final String URL_GET_OFFERS = URL_ROOT + "/offers";
    private static final String URL_RATE = URL_ROOT + "/order/rate";

    public Communicator() {
    }

    public static List<Taxi> getAvailableTaxis(Location location, TaxiType taxiType) {
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

    public static int placeOrder(Order order) {
        ContentValues values = new ContentValues();
        values.put("origin", order.getOrigin());
        values.put("customerId", ApplicationPreferences.getUser().getId());
        values.put("destination", order.getDestination());
        values.put("originLatitude", order.getOriginCoordinates().getLatitude());
        values.put("originLongitude", order.getOriginCoordinates().getLongitude());
        values.put("destinationLatitude", order.getDestinationCoordinates().getLatitude());
        values.put("destinationLongitude", order.getDestinationCoordinates().getLongitude());
        values.put("note", order.getNote());
        values.put("contact", order.getContact());
        values.put("driverId", order.getDriver().getId());
        values.put("taxiTypeId", order.getTaxiType().getValue());
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
    public static DriverUpdate getDriverUpdate(int driverId, LatLng latLng) {
        ContentValues values = new ContentValues();
        values.put("driverId", driverId);
        values.put("latitude", latLng.latitude);
        values.put("longitude", latLng.longitude);

        String response = HTTPHandler.sendGET(URL_GET_DRIVER_UPDATE, values);

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean("success")) {
                return new Gson().fromJson(jsonObject.getJSONObject("data").toString(), DriverUpdate.class);
            }
        } catch (JSONException e) {
            Log.e(DEBUG_TAG, "Error converting to json object " + e.toString());
        } catch (NullPointerException e) {
            Log.e(DEBUG_TAG, "Server error occurred " + e.toString());
        }
        return null;
    }

    public static DriverUpdate getDriverLocation(int driverId) {
        ContentValues values = new ContentValues();
        values.put("driverId", driverId);
        String response = HTTPHandler.sendGET(URL_GET_DRIVER_LOCATION, values);

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean("success")) {
                return new Gson().fromJson(jsonObject.getJSONObject("data").toString(), DriverUpdate.class);
            } else {
                Log.e(DEBUG_TAG, "Response is not success");
            }
        } catch (JSONException e) {
            Log.e(DEBUG_TAG, "Error converting to json object " + e.toString());
        } catch (NullPointerException e) {
            Log.e(DEBUG_TAG, "Server error occurred " + e.toString());
        }
        return null;
    }

    public static int login(String phone, String password) {
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
                        Log.v(DEBUG_TAG, "Login success");
                        break;
                    case 1:
                        resultCode = 1;
                        Log.w(DEBUG_TAG, "Username or password is incorrect");
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

    public static boolean signOut() {
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
                    Log.v(DEBUG_TAG, "Logout success");
                } else {
                    Log.w(DEBUG_TAG, "Logout failed");
                }
            } catch (JSONException e) {
                Log.e(DEBUG_TAG, e.toString());
            } catch (NullPointerException e) {
                Log.e(DEBUG_TAG, "Server error occurred " + e.toString());
            }
        }
        return result;
    }

    public static int signUp(User user, String password) {
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
                        Log.v(DEBUG_TAG, "Sign up success");
                        break;
                    case 1:
                        resultCode = 1;
                        Log.w(DEBUG_TAG, "Phone number has been already taken");
                        break;
                    case 2:
                        resultCode = 2;
                        Log.w(DEBUG_TAG, "User with the same full name already exists");
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

    public static List<Order> getMyOrders(int userId, String state) {
        List<Order> orderList = null;
        ContentValues values = new ContentValues();
        values.put("customerId", userId);
        values.put("state", state);
        String response = HTTPHandler.sendGET(URL_GET_MY_ORDERS, values);

        if (response != null) {
            try {
                orderList = new ArrayList<Order>();
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Order order = new Order();
                    order.setId(jsonObject.getInt("id"));
                    order.setOrigin(jsonObject.getString("origin"));
                    order.setOriginCoordinates(new Location(jsonObject.getDouble("originLatitude"), jsonObject.getDouble("originLongitude")));
                    order.setDestination(jsonObject.getString("destination"));
                    order.setDestinationCoordinates(new Location(jsonObject.getDouble("destinationLatitude"), jsonObject.getDouble("destinationLongitude")));
                    order.setContact(jsonObject.getString("contact"));
                    order.setTaxiType(TaxiType.getEnum(jsonObject.getInt("taxiTypeId")));
                    order.setDriver(new Gson().fromJson(jsonObject.getJSONObject("taxi_driver").toString(), Driver.class));

                    if (state.equals(Order.OrderState.FINISHED.toString())) {
                        order.setTime(new Time(jsonObject.getString("startTime")));
                        order.setOrderState(Order.OrderState.FINISHED);
                    } else {
                        order.setTime(new Time(jsonObject.getString("time")));
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

    public static List<FinishedOrder> getFinishedOrders(int userId) {
        List<FinishedOrder> orderList = null;
        ContentValues values = new ContentValues();
        values.put("customerId", userId);
        values.put("state", "FINISHED");
        String response = HTTPHandler.sendGET(URL_GET_MY_ORDERS, values);

        if (response != null) {
            try {
                orderList = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    FinishedOrder order = new FinishedOrder();
                    order.setId(jsonObject.getInt("id"));
                    order.setOrigin(jsonObject.getString("origin"));
                    order.setOriginCoordinates(new Location(jsonObject.getDouble("originLatitude"), jsonObject.getDouble("originLongitude")));
                    order.setDestination(jsonObject.getString("destination"));
                    order.setDestinationCoordinates(new Location(jsonObject.getDouble("destinationLatitude"), jsonObject.getDouble("destinationLongitude")));
                    order.setContact(jsonObject.getString("contact"));
                    order.setTaxiType(TaxiType.getEnum(jsonObject.getInt("taxiTypeId")));
                    order.setDriver(new Gson().fromJson(jsonObject.getJSONObject("taxi_driver").toString(), Driver.class));
                    order.setStartTime(new Time(jsonObject.getString("startTime")));
                    order.setEndTime(new Time(jsonObject.getString("endTime")));
                    order.setDriver(new Gson().fromJson(jsonObject.getJSONObject("taxi_driver").toString(), Driver.class));
                    order.setRating(jsonObject.getInt("rating"));
                    order.setComment(jsonObject.getString("comment"));
                    order.setFare(jsonObject.getInt("fare"));
                    order.setDistance(jsonObject.getDouble("distance"));
                    orderList.add(order);
                }
            } catch (JSONException|ParseException e) {
                Log.e(DEBUG_TAG, "Invalid JSON", e);
            }
        } else {
            Log.v(DEBUG_TAG, "Response is null");
        }
        return orderList;
    }

    public static List<Offer> getOffers() {
        List<Offer> offerList = new ArrayList<>();
        ContentValues values = new ContentValues();
        String response = HTTPHandler.sendGET(URL_GET_OFFERS, values);

        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                offerList.add(new Gson().fromJson(jsonArray.get(i).toString(), Offer.class));
            }
        } catch (JSONException e) {
            Log.e(DEBUG_TAG, "Error converting json array " + e.toString());
        } catch (NullPointerException e) {
            Log.e(DEBUG_TAG, "Server error occurred " + e.toString());
        }
        return offerList;
    }

    public static boolean rateOrder(int id, int rating, String comment) {
        boolean result = false;
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("rating", rating);
        values.put("comment", comment);
        String response = HTTPHandler.sendPOST(URL_RATE, values);

        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                result = jsonObject.getBoolean("success");
                if(result){
                    Log.v(DEBUG_TAG, "Review success");
                }
                else {
                    Log.w(DEBUG_TAG, "Review failed");
                }
            } catch (JSONException e) {
                Log.e(DEBUG_TAG, e.toString());
            } catch (NullPointerException e) {
                Log.e(DEBUG_TAG, "Server error occurred " + e.toString());
            }
        }
        return result;
    }
}

