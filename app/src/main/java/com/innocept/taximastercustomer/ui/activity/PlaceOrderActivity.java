package com.innocept.taximastercustomer.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.innocept.taximastercustomer.R;

/**
 * Created by dulaj on 5/24/16.
 */
public class PlaceOrderActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inflater_alert_dialog_place_order);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Place an order");
        setSupportActionBar(toolbar);
    }
}
