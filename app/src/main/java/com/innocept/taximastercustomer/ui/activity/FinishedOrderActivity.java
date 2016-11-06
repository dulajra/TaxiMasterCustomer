package com.innocept.taximastercustomer.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.media.Rating;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.innocept.taximastercustomer.model.foundation.FinishedOrder;
import com.innocept.taximastercustomer.model.foundation.Order;
import com.innocept.taximastercustomer.presenter.CurrentOrderPresenter;
import com.innocept.taximastercustomer.presenter.FinishedOrderPresenter;
import com.innocept.taximastercustomer.ui.MapUtils;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.text.WordUtils;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author dulaj
 * @version 1.0.0
 */
public class FinishedOrderActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final String DEBUG_TAG = FinishedOrderActivity.class.getSimpleName();

    public FinishedOrderPresenter finishedOrderPresenter;

    private Toolbar toolbar;
    private GoogleMap mMap;
    private UiSettings mapUiSettings;

    private FinishedOrder order;

    private LatLng customerLatLng;
    private LatLng driverLatLng;
    private Marker driverMarker;
    private Marker customerMarker;
    private Polyline polyline = null;
    private float DEFAULT_ZOOM_LEVEL = 8f;

    private ImageView imageView;
    private TextView textViewDriverName;
    private TextView textViewTaxiType;
    private TextView textViewDistance;
    private TextView textViewTime;
    private TextView textViewFare;
    private TextView textViewFromTo;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_order);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (finishedOrderPresenter == null) {
            finishedOrderPresenter = FinishedOrderPresenter.getInstance();
        }
        finishedOrderPresenter.setView(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        imageView = (ImageView)findViewById(R.id.imageViewDriverProfilePicture);
        textViewDriverName = (TextView)findViewById(R.id.textDriverName);
        textViewTaxiType = (TextView)findViewById(R.id.textTaxiType);
        textViewFromTo = (TextView)findViewById(R.id.text_from_to);
        textViewTime = (TextView)findViewById(R.id.text_time);
        textViewDistance = (TextView)findViewById(R.id.text_distance);
        textViewFare = (TextView)findViewById(R.id.text_fare);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);

        Intent intent = getIntent();
        order = (FinishedOrder) intent.getSerializableExtra("order");
        customerLatLng = new LatLng(order.getOriginCoordinates().getLatitude(), order.getOriginCoordinates().getLongitude());
        driverLatLng = new LatLng(order.getDestinationCoordinates().getLatitude(), order.getDestinationCoordinates().getLongitude());

        textViewDriverName.setText(order.getDriver().getFullName());
        textViewTaxiType.setText(WordUtils.capitalizeFully(order.getTaxiType().toString()));
        textViewFromTo.setText(order.getOrigin() + " to " + order.getDestination());
        textViewTime.setText("\u2022 " + order.getStartTime().toString());
        textViewDistance.setText("\u2022 " + order.getDistance() + " Km");
        textViewFare.setText("\u2022 Rs. " + order.getFare());
        ratingBar.setRating(order.getRating());
        Picasso.with(FinishedOrderActivity.this)
                .load(order.getDriver().getImage())
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(imageView);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, final float rating, boolean fromUser) {
                LayoutInflater inflater = LayoutInflater.from(FinishedOrderActivity.this);
                View view = inflater.inflate(R.layout.inflater_alert_dialog_order_review, null);
                final EditText editTextComment = (EditText)view.findViewById(R.id.edit_comment);
                final TextView textViewRating = (TextView)view.findViewById(R.id.textRatingLevel);
                final RatingBar ratingBarDialog = (RatingBar)view.findViewById(R.id.ratingBarCommentDialog);

                ratingBarDialog.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        switch ((int)rating){
                            case 1:
                                textViewRating.setText(getResources().getString(R.string.rating_1));
                                break;
                            case 2:
                                textViewRating.setText(getResources().getString(R.string.rating_2));
                                break;
                            case 3:
                                textViewRating.setText(getResources().getString(R.string.rating_3));
                                break;
                            case 4:
                                textViewRating.setText(getResources().getString(R.string.rating_4));
                                break;
                            case 5:
                                textViewRating.setText(getResources().getString(R.string.rating_5));
                                break;
                        }
                    }
                });
                ratingBarDialog.setRating(rating);
                if(order.getComment()!=null && order.getComment().length()>0 && !order.getComment().equalsIgnoreCase("null")){
                    editTextComment.setText(order.getComment());
                }

                new AlertDialog.Builder(FinishedOrderActivity.this)
                        .setView(view)
                        .setCancelable(true)
                        .setPositiveButton("SUBMIT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finishedOrderPresenter.rateOrder(order.getId(), (int)rating, editTextComment.getText().toString());
                            }
                        })
                        .show();
            }
        });
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
        mapUiSettings.setMyLocationButtonEnabled(false);
        mapUiSettings.setZoomControlsEnabled(false);
        mapUiSettings.setRotateGesturesEnabled(true);
        mapUiSettings.setMapToolbarEnabled(false);

        setCustomerMarker();
        setDriverMarker();
        moveAndAnimateCamera(customerLatLng, DEFAULT_ZOOM_LEVEL);

        MapUtils.plotRoute(this, mMap, polyline, order.getOriginCoordinates().getLatLng(), order.getDestinationCoordinates().getLatLng(), getResources().getColor(R.color.routeColor));
    }

    private void moveAndAnimateCamera(LatLng latLng, float zoom) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
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

    public void afterRating(Boolean result) {
        if(result){
            Toast.makeText(FinishedOrderActivity.this, getResources().getString(R.string.on_rating_success), Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(FinishedOrderActivity.this, getResources().getString(R.string.on_rating_failed), Toast.LENGTH_SHORT).show();
        }
    }
}
