package com.innocept.taximastercustomer.ui.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.innocept.taximastercustomer.R;
import com.innocept.taximastercustomer.model.foundation.Order;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by dulaj on 5/28/16.
 */
public class CurrentOrderActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final String DEBUG_TAG = CurrentOrderActivity.class.getSimpleName();

    private Toolbar toolbar;
    private GoogleMap mMap;
    private UiSettings mapUiSettings;

    private Order order;

    private LatLng startLatLng;
    private LatLng driverLatLng;
    private Marker startMarker;
    private Marker driverMarker;
    private float DEFAULT_ZOOM_LEVEL = 11f;

    SupportMapFragment mapFragment;
    View mapView;
    LinearLayout linearStatusPanel;
    FloatingActionButton fab;
    com.github.clans.fab.FloatingActionButton fabCall;
    private boolean isExpanded = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Going for hire");
        setSupportActionBar(toolbar);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        linearStatusPanel = (LinearLayout) findViewById(R.id.linear_status_panel);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fabCall = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_call);

        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra("order");

        startLatLng = new LatLng(order.getOriginCoordinates().getLatitude(), order.getOriginCoordinates().getLongitude());


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        mMap.setMyLocationEnabled(true);
        mapUiSettings = mMap.getUiSettings();
        mapUiSettings.setZoomControlsEnabled(false);
        mapUiSettings.setRotateGesturesEnabled(true);

//        setStartMarker();
    }

    private void setStartMarker() {
        startMarker = mMap.addMarker(new MarkerOptions().position(startLatLng).title(order.getOrigin()).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_street_view)).snippet("Passenger"));
    }

    private void moveAndAnimateCamera(LatLng latLng, float zoom) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, zoom));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case 0:
                callCustomer();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    private void callCustomer() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + order.getContact()));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 100);
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            callCustomer();
        }
    }

    public void showOrHideStatusBar(View view) {
        if(isExpanded){
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_action_expand));
            linearStatusPanel.setVisibility(View.VISIBLE);
            isExpanded = false;
            fabCall.setVisibility(View.VISIBLE);
        }
        else{
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_action_collapse));
            linearStatusPanel.setVisibility(View.GONE);
            isExpanded = true;
            fabCall.setVisibility(View.GONE);
        }
    }
}
