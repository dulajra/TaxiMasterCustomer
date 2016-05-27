package com.innocept.taximastercustomer.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.innocept.taximastercustomer.ApplicationContext;
import com.innocept.taximastercustomer.R;
import com.innocept.taximastercustomer.model.foundation.Driver;
import com.innocept.taximastercustomer.model.foundation.Order;
import com.innocept.taximastercustomer.model.foundation.TaxiType;
import com.innocept.taximastercustomer.presenter.NewOrderPresenter;
import com.innocept.taximastercustomer.ui.fragment.TaxiFragment;

/**
 * Created by Dulaj on 17-Apr-16.
 */

/**
 * New orders are placed by this activity.
 */
public class NewOrderActivity extends LocationActivity {

    private final String DEBUG_TAG = NewOrderActivity.class.getSimpleName();

    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE_FROM = 1;
    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE_TO = 2;

    public NewOrderPresenter newOrderPresenter;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    RelativeLayout relativeLayoutFrom;
    RelativeLayout relativeLayoutTo;
    public TextView textViewFrom;
    public TextView textViewTo;
    ImageButton imageButtonMyLocationFrom;
    ImageButton imageButtonMyLocationTo;
    ImageButton imageButtonMapFrom;
    ImageButton imageButtonMapTo;

    LatLng latLngFrom;
    LatLng latLngTo;

    public TaxiFragment nanoCabFragment;
    public TaxiFragment carFragment;
    public TaxiFragment vanFragment;

    boolean isTouchable = true;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        if (newOrderPresenter == null) {
            newOrderPresenter = NewOrderPresenter.getInstance();
        }
        newOrderPresenter.setView(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        relativeLayoutFrom = (RelativeLayout) findViewById(R.id.relative_from);
        relativeLayoutTo = (RelativeLayout) findViewById(R.id.relative_to);
        textViewFrom = (TextView) findViewById(R.id.text_from);
        textViewTo = (TextView) findViewById(R.id.text_to);
        imageButtonMyLocationFrom = (ImageButton)findViewById(R.id.image_btn_my_location_from);
        imageButtonMyLocationTo = (ImageButton)findViewById(R.id.image_btn_my_location_to);
        imageButtonMapFrom = (ImageButton)findViewById(R.id.image_btn_map_from);
        imageButtonMapTo = (ImageButton)findViewById(R.id.image_btn_map_to);

        relativeLayoutFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPlace(PLACE_AUTOCOMPLETE_REQUEST_CODE_FROM);
            }
        });

        relativeLayoutTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPlace(PLACE_AUTOCOMPLETE_REQUEST_CODE_TO);
            }
        });

        textViewFrom.addTextChangedListener(fromToTextWatcher);
        textViewTo.addTextChangedListener(fromToTextWatcher);

        imageButtonMyLocationFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLastLocation!=null && mLastLocation.getAccuracy()<50){
                    latLngFrom = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    textViewFrom.setText("Your location");
                }
                else{
                    Toast.makeText(NewOrderActivity.this, "Waiting for location...", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(mLastLocation!=null && mLastLocation.getAccuracy()<50){
                                latLngFrom = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        textViewFrom.setText("Your location");
                                        Toast.makeText(NewOrderActivity.this, "Location detected", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else{
                             runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                     Toast.makeText(NewOrderActivity.this, "Location is not available", Toast.LENGTH_SHORT).show();
                                 }
                             });
                            }
                        }
                    }, 10000);
                }
            }
        });

        imageButtonMyLocationTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLastLocation!=null && mLastLocation.getAccuracy()<50){
                    latLngTo = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                    textViewTo.setText("Your location");
                }
                else{
                    Toast.makeText(NewOrderActivity.this, "Waiting for location...", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(mLastLocation!=null && mLastLocation.getAccuracy()<50){
                                latLngTo = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        textViewTo.setText("Your location");
                                        Toast.makeText(NewOrderActivity.this, "Location detected", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(NewOrderActivity.this, "Location is not available", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }, 10000);
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                submit();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void searchPlace(int REQUEST_CODE){
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setBoundsBias(new LatLngBounds(new LatLng(5.909953, 79.678507), new LatLng(9.886534, 81.889820)))
                    .build(NewOrderActivity.this);
            startActivityForResult(intent, REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            Log.e(DEBUG_TAG, e.toString());
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(DEBUG_TAG, e.toString());
        }
    }

    TextWatcher fromToTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            submit();
        }
    };

    private void submit() {
        String from = textViewFrom.getText().toString();
        String to = textViewTo.getText().toString();

        if(from!=null && to!=null && from.length()>0 && to.length()>0){
            if(from.equals(to)){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Invalid entry!");
                alertDialogBuilder.setMessage("Starting point and destination cannot be same");
                alertDialogBuilder.setPositiveButton("Ok", null);
                alertDialogBuilder.create().show();
                return;
            }
            TaxiType taxiType = null;
            if(tabLayout.getSelectedTabPosition()==0)
                taxiType = TaxiType.NANO;
            else if(tabLayout.getSelectedTabPosition()==1)
                taxiType = TaxiType.CAR;
            else if(tabLayout.getSelectedTabPosition()==2)
                taxiType = TaxiType.VAN;

            newOrderPresenter.getAvailableTaxis(latLngFrom, taxiType);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_FROM) {
            if (resultCode == RESULT_OK) {
                Place placeFrom = PlaceAutocomplete.getPlace(this, data);
                if(placeFrom!=null){
                    if(placeFrom.getName()!=null && placeFrom.getLatLng()!=null){
                        latLngFrom = placeFrom.getLatLng();
                        textViewFrom.setText(placeFrom.getName());
                    }
                    else{
                        textViewFrom.setText("");
                    }
                }
                else{
                    textViewFrom.setText("");
                }
                Log.i(DEBUG_TAG, "Place: " + placeFrom.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(DEBUG_TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
        else if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_TO) {
            if (resultCode == RESULT_OK) {
                Place placeTo = PlaceAutocomplete.getPlace(this, data);
                if(placeTo!=null){
                    if(placeTo.getName()!=null && placeTo.getLatLng()!=null){
                        textViewTo.setText(placeTo.getName());
                        latLngTo = placeTo.getLatLng();
                    }
                    else{
                        textViewTo.setText("");
                    }
                }
                else{
                    textViewTo.setText("");
                }
                Log.i(DEBUG_TAG, "Place: " + placeTo.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(DEBUG_TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        nanoCabFragment = new TaxiFragment();
        carFragment = new TaxiFragment();
        vanFragment = new TaxiFragment();
        adapter.addFragment(nanoCabFragment, "NANO");
        adapter.addFragment(carFragment, "CAR");
        adapter.addFragment(vanFragment, "VAN");
        viewPager.setAdapter(adapter);
    }

    public void openMyOrdersActivity(View view) {
        startActivity(new Intent(NewOrderActivity.this, MyOrdersActivity.class));
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);
        mLastLocation = location;
        if(mLastLocation!=null){
            String message = "Lat: " + mLastLocation.getLatitude() + ", Lng: " + mLastLocation.getLongitude() + ", Accu: " + mLastLocation.getAccuracy();
            Log.i(DEBUG_TAG, message);
        }
    }

    public void lockUI(){
        this.isTouchable = false;
    }

    public void releaseUI(){
        this.isTouchable = true;
    }

    public void placeOrder(Date time, String note, Driver driver, String contact){
        Order order = new Order();
        order.setOrigin(textViewFrom.getText().toString());
        order.setDestination(textViewTo.getText().toString());
        order.setOriginCoordinates(new com.innocept.taximastercustomer.model.foundation.Location(latLngFrom.latitude, latLngFrom.longitude));
        order.setDestinationCoordinates(new com.innocept.taximastercustomer.model.foundation.Location(latLngTo.latitude, latLngTo.longitude));
        order.setTime(time);
        order.setNote(note);
        order.setContact(contact);
        order.setDriver(driver);
        order.setOrderState(Order.OrderState.PENDING);
        newOrderPresenter.placeOrder(order);
    }

    @Override

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(isTouchable){
            return super.dispatchTouchEvent(ev);
        }
        return false;
    }

    public void showProgressDialog(String message){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void closeProgressDialog(){
        progressDialog.dismiss();
    }

    public void onPlaceOrderSuccess(Order order){
        Intent intent = new Intent(NewOrderActivity.this, MyOrdersActivity.class);
        intent.putExtra("isNewOrder", true);
        intent.putExtra("order", order);
        startActivity(intent);
    }
}
