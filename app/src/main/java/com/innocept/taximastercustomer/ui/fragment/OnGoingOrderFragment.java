package com.innocept.taximastercustomer.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.innocept.taximastercustomer.ApplicationContext;
import com.innocept.taximastercustomer.R;
import com.innocept.taximastercustomer.model.data.DatabaseHandler;
import com.innocept.taximastercustomer.model.foundation.Order;
import com.innocept.taximastercustomer.ui.adapters.MyOrderAdapter;

import java.util.List;

/**
 * Created by dulaj on 5/24/16.
 */
public class OnGoingOrderFragment extends Fragment {

    private final String DEBUG_TAG = OnGoingOrderFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Order> dataSet;

    public OnGoingOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseHandler db = new DatabaseHandler(ApplicationContext.getContext());
        Intent intent = (getActivity()).getIntent();

        if(intent.getBooleanExtra("isUpdate", false)){
            Log.i(DEBUG_TAG, "1 Called >>>>>>>>>>>>>>");
            if(intent.getBooleanExtra("response", false)){
                Log.i(DEBUG_TAG, "2 Called >>>>>>>>>>>>>>");
                db.updateOrderState(intent.getIntExtra("id", -1), Order.OrderState.ACCEPTED);
            }
            else{
                Log.i(DEBUG_TAG, "3 Called >>>>>>>>>>>>>>");
                db.updateOrderState(intent.getIntExtra("id", -1), Order.OrderState.REJECTED);
            }
        }
        else if(intent.getBooleanExtra("isNewOrder", false)){
            db.saveOrder((Order)(intent.getSerializableExtra("order")));
        }
        else if(intent.getBooleanExtra("now", false)){
            db.updateOrderState(intent.getIntExtra("id", -1), Order.OrderState.NOW);
        }

        dataSet = db.getAllOrders(new Order.OrderState[]{Order.OrderState.PENDING, Order.OrderState.ACCEPTED, Order.OrderState.REJECTED, Order.OrderState.NOW});
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_order, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_my_orders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyOrderAdapter(getActivity(), dataSet);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

}
