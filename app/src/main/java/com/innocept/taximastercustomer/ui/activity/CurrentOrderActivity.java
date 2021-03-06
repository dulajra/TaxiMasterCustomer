package com.innocept.taximastercustomer.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.innocept.taximastercustomer.R;
import com.innocept.taximastercustomer.model.foundation.DriverUpdate;
import com.innocept.taximastercustomer.model.foundation.Order;
import com.innocept.taximastercustomer.presenter.CurrentOrderPresenter;
import com.innocept.taximastercustomer.ui.MapUtils;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author dulaj
 * @version 1.0.0
 */
public class CurrentOrderActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final String DEBUG_TAG = CurrentOrderActivity.class.getSimpleName();

    public CurrentOrderPresenter currentOrderPresenter;

    private Toolbar toolbar;
    private GoogleMap mMap;
    private UiSettings mapUiSettings;

    private Order order;

    private LatLng customerLatLng;
    private LatLng driverLatLng;
    private Marker driverMarker;
    private Marker customerMarker;
    private float DEFAULT_ZOOM_LEVEL = 10f;

    SupportMapFragment mapFragment;
    View mapView;
    LinearLayout linearStatusPanel;
    private TextView textViewDistance;
    private TextView textViewTime;
    private TextView textViewFromTo;
    private TextView textViewExpectedTime;
    FloatingActionButton fab;
    com.github.clans.fab.FloatingActionButton fabCall;

    private boolean isExpanded = false;
    private Polyline polyline = null;

    private DriverUpdate driverLocation;
    private Timer timer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Current Hire");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (currentOrderPresenter == null) {
            currentOrderPresenter = CurrentOrderPresenter.getInstance();
        }
        currentOrderPresenter.setView(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        textViewDistance = (TextView)findViewById(R.id.text_distance);
        textViewTime = (TextView)findViewById(R.id.text_estimated_time);
        textViewFromTo = (TextView)findViewById(R.id.text_from_to);
        textViewExpectedTime = (TextView)findViewById(R.id.text_expected_time);

        linearStatusPanel = (LinearLayout) findViewById(R.id.linear_status_panel);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fabCall = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_call);

        fabCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCustomer();
            }
        });

        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra("order");
        customerLatLng = new LatLng(order.getOriginCoordinates().getLatitude(), order.getOriginCoordinates().getLongitude());
        textViewFromTo.setText(order.getOrigin() + " to " + order.getDestination());
        textViewExpectedTime.setText("Expected time: " + new SimpleDateFormat("yyyy-MM-dd hh:mm").format(order.getTime()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_current_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refresh:
                updateDetails();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateDetails() {
        currentOrderPresenter.updateDriverDetails(order.getDriver().getId(), customerLatLng);
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

        setCustomerMarker();
        moveAndAnimateCamera(customerLatLng, DEFAULT_ZOOM_LEVEL);

        updateDetails();

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                currentOrderPresenter.getDriverLocation(order.getDriver().getId());
            }
        }, 10000, 10000);
    }

    private void moveAndAnimateCamera(LatLng latLng, float zoom) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    private void callCustomer() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + order.getDriver().getPhone()));
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

    public void whileGettingUpdates() {
        Toast.makeText(CurrentOrderActivity.this, "Updating details...", Toast.LENGTH_SHORT).show();
    }

    public void updateDriverLocation(LatLng latLng){
        this.driverLatLng = latLng;
        setDriverMarker();
    }

    public void onUpdateSuccess(DriverUpdate driverUpdate) {
        driverLatLng = new LatLng(driverUpdate.getLatitude(), driverUpdate.getLongitude());
        setDriverMarker();
        moveAndAnimateCamera(driverLatLng, DEFAULT_ZOOM_LEVEL);
        setStatusPanelValues(driverUpdate.getDistance(), driverUpdate.getDuration());
        Toast.makeText(CurrentOrderActivity.this, "Details updated...", Toast.LENGTH_SHORT).show();

        MapUtils.plotRoute(CurrentOrderActivity.this, this.mMap, polyline, driverLatLng, this.customerLatLng, getResources().getColor(R.color.routeColor));
    }

    public void setDriverMarker(){
        if(driverMarker!=null){
            driverMarker.remove();
        }
        driverMarker = mMap.addMarker(new MarkerOptions().position(driverLatLng).title("Your taxi").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_taxi)));
    }

    private void setCustomerMarker(){
        if(customerMarker!=null){
            customerMarker.remove();
        }
        customerMarker = mMap.addMarker(new MarkerOptions().position(customerLatLng).title("You").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_street_view)));
    }

    private void setStatusPanelValues(String distance, String time){
        textViewDistance.setText("Distance remaining: " + distance);
        textViewTime.setText("Time remaining: " + time);
    }
}
