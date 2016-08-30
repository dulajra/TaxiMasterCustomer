package com.innocept.taximastercustomer.ui;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.Language;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Created by Dulaj on 27-Aug-16.
 */
public class MapUtils {

    private static final String SERVER_KEY = "AIzaSyBV39bSkS5_GjTJcrN4Jd_lhEbz1DnOCNU";

    private final static String DEBUG_TAG = MapUtils.class.getSimpleName();

    public static void setMarker(GoogleMap map, Marker marker, LatLng latLng, String title) {
        if (marker != null) {
            marker.remove();
        }
        marker = map.addMarker(new MarkerOptions().position(latLng).title(title));
    }

    public static void setMarker(GoogleMap map, Marker marker, LatLng latLng, String title, String snippet) {
        if (marker != null) {
            marker.remove();
        }
        marker = map.addMarker(new MarkerOptions().position(latLng).title(title).snippet(snippet));
    }

    public static void setMarker(GoogleMap map, Marker marker, LatLng latLng, String title, int drawable) {
        if (marker != null) {
            marker.remove();
        }
        marker = map.addMarker(new MarkerOptions().position(latLng).title(title).icon(BitmapDescriptorFactory.fromResource(drawable)));
    }

    public static void setMarker(GoogleMap map, Marker marker, LatLng latLng, String title, String snippet, int drawable) {
        if (marker != null) {
            marker.remove();
        }
        marker = map.addMarker(new MarkerOptions().position(latLng).title(title).snippet(snippet).icon(BitmapDescriptorFactory.fromResource(drawable)));
    }

    public static void setMarker(GoogleMap map, Marker marker, LatLng latLng, int drawable) {
        if (marker != null) {
            marker.remove();
        }
        marker = map.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(drawable)));
    }

    public static void setMarker(GoogleMap map, Marker marker, LatLng latLng) {
        if (marker != null) {
            marker.remove();
        }
        marker = map.addMarker(new MarkerOptions().position(latLng));
    }

    public static void moveAndAnimateCamera(GoogleMap map, LatLng latLng, int zoomLevel) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
    }

    public static void plotRoute(Context context, GoogleMap googleMap, Polyline polyline, LatLng startLatLng, LatLng endLatLng, int routeColor) {
        GoogleDirection.withServerKey(SERVER_KEY)
                .from(startLatLng)
                .to(endLatLng)
                .transportMode(TransportMode.DRIVING)
                .language(Language.ENGLISH)
                .unit(Unit.METRIC)
                .execute(new CustomDirectionCallBack(context, googleMap, polyline, routeColor));
    }

    private static class CustomDirectionCallBack implements DirectionCallback {

        Context context;
        GoogleMap googleMap;
        Polyline polyline;
        int routeColor;

        public CustomDirectionCallBack(Context context, GoogleMap googleMap, Polyline polyline, int routeColor) {
            this.context = context;
            this.googleMap = googleMap;
            this.routeColor = routeColor;
            this.polyline = polyline;

            if(polyline!=null){
                polyline.remove();
            }
        }

        @Override
        public void onDirectionSuccess(Direction direction, String rawBody) {
            if (direction.getStatus().equals(RequestResult.OK)) {
                Route route = direction.getRouteList().get(0);
                Leg leg = route.getLegList().get(0);
                ArrayList<LatLng> pointList = leg.getDirectionPoint();
                PolylineOptions polylineOptions = DirectionConverter.createPolyline(context, pointList, 5, routeColor);
                polyline = googleMap.addPolyline(polylineOptions);
            } else if (direction.getStatus().equals(RequestResult.NOT_FOUND)) {
                Toast.makeText(context, "No route cannot be found.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Error occurred. Contact developers if error continues.", Toast.LENGTH_LONG).show();
                Log.e(DEBUG_TAG, "Error in while getting directions: " + direction.getStatus());
            }
        }

        @Override
        public void onDirectionFailure(Throwable t) {
            Log.e(DEBUG_TAG, "Error in while getting directions: " + t.toString());
        }
    }
}
