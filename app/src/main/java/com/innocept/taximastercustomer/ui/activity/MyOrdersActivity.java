package com.innocept.taximastercustomer.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;

import com.innocept.taximastercustomer.ApplicationContext;
import com.innocept.taximastercustomer.R;
import com.innocept.taximastercustomer.model.data.DatabaseHandler;
import com.innocept.taximastercustomer.model.foundation.Order;
import com.innocept.taximastercustomer.model.foundation.Taxi;
import com.innocept.taximastercustomer.ui.adapters.MyOrderAdapter;
import com.innocept.taximastercustomer.ui.adapters.TaxiListAdapter;

import java.util.List;

/**
 * Created by dulaj on 5/25/16.
 */
public class MyOrdersActivity extends AppCompatActivity {

    private final String DEBUG_TAG = MyOrdersActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Order> dataSet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        DatabaseHandler db = new DatabaseHandler(ApplicationContext.getContext());
        db.saveOrder((Order)getIntent().getSerializableExtra("order"));

        dataSet = db.getAllOrders(Order.OrderState.PENDING);

        Log.i(DEBUG_TAG, dataSet.size() + " >>>>>>>>>>>>");
        for (Order order:dataSet){
            Log.i(DEBUG_TAG, order.getOrigin());
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_my_orders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyOrderAdapter(this, dataSet);
        recyclerView.setAdapter(adapter);
    }
}
