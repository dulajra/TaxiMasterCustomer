package com.innocept.taximastercustomer.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.innocept.taximastercustomer.ApplicationContext;
import com.innocept.taximastercustomer.R;
import com.innocept.taximastercustomer.model.data.DatabaseHandler;
import com.innocept.taximastercustomer.model.foundation.Order;

/**
 * Created by dulaj on 5/25/16.
 */
public class MyOrdersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        DatabaseHandler db = new DatabaseHandler(ApplicationContext.getContext());
        db.saveOrder((Order)getIntent().getSerializableExtra("order"));
    }
}
