package com.innocept.taximastercustomer.ui;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.innocept.taximastercustomer.R;

/**
 * Created by Dulaj on 27-Aug-16.
 */
public class MapUtils {

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

    public static void moveAndAnimateCamera(GoogleMap map, LatLng latLng, int zoomLevel){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
    }
}
