package com.innocept.taximastercustomer.ui.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.innocept.taximastercustomer.ApplicationPreferences;
import com.innocept.taximastercustomer.R;
import com.innocept.taximastercustomer.model.foundation.DriverUpdate;
import com.innocept.taximastercustomer.model.foundation.Order;
import com.innocept.taximastercustomer.model.network.Communicator;
import com.innocept.taximastercustomer.ui.MapUtils;
import com.innocept.taximastercustomer.ui.activity.CurrentOrderActivity;
import com.innocept.taximastercustomer.ui.adapters.MyOrderAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by dulaj on 5/24/16.
 */
public class OnGoingOrderFragment extends Fragment implements OnMapReadyCallback {

    private final String DEBUG_TAG = OnGoingOrderFragment.class.getSimpleName();

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private CardView cardViewNow;
    private TextView textViewLocation;
    private TextView textViewDistance;
    private TextView textViewDuration;

    private SupportMapFragment mapFragment;
    private Marker driverMarker;
    private GoogleMap map;

    private List<Order> dataSet;
    private Order orderNow;
    private DriverUpdate driverUpdate;
    private boolean mapReadyNotCalled = false;

    public OnGoingOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    private void loadData() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (swipeRefreshLayout != null){
                    swipeRefreshLayout.setRefreshing(true);
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                dataSet = Communicator.getMyOrders(ApplicationPreferences.getUser().getId(), "ANY");

                Collections.sort(dataSet, new Comparator<Order>() {
                    @Override
                    public int compare(Order lhs, Order rhs) {
                        return rhs.getTime().compareTo(lhs.getTime());
                    }
                });

                List<Order> itemsToMove = new ArrayList<Order>();
                for(Order order:dataSet){
                    if(order.getOrderState()== Order.OrderState.NOW){
                        itemsToMove.add(order);
                    }
                }
                for (Order order:itemsToMove){
                    dataSet.remove(order);
                    dataSet.add(0, order);
                }

                if(dataSet.get(0).getOrderState()== Order.OrderState.NOW){
                    orderNow = dataSet.remove(0);
                }
                driverUpdate = Communicator.getDriverUpdate(orderNow.getDriver().getId(), new LatLng(orderNow.getDestinationCoordinates().getLatitude(), orderNow.getDestinationCoordinates().getLongitude()));
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter = new MyOrderAdapter(getActivity(), dataSet);
                recyclerView.setAdapter(adapter);
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }

                if(mapReadyNotCalled){
                    updateMap();
                }

                textViewLocation.setText(driverUpdate.getLocation());
                textViewDistance.setText(driverUpdate.getDistance() + " away");
                textViewDuration.setText(driverUpdate.getDuration() + " remaining");
            }
        }.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(DEBUG_TAG, "Called 1 >>>>>>>>>>");
        View rootView = inflater.inflate(R.layout.fragment_ongoing_orders, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_my_orders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        cardViewNow = (CardView)rootView.findViewById(R.id.card_view_now);
        textViewLocation = (TextView)rootView.findViewById(R.id.text_location);
        textViewDistance = (TextView)rootView.findViewById(R.id.text_distance);
        textViewDuration = (TextView)rootView.findViewById(R.id.text_time);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_now_small_view);
        mapFragment.getMapAsync(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                adapter.notifyDataSetChanged();
            }
        });

        cardViewNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CurrentOrderActivity.class);
                intent.putExtra("order", orderNow);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        updateMap();
    }

    private void updateMap() {
        if(driverUpdate!=null){
            LatLng latLng = new LatLng(driverUpdate.getLatitude(), driverUpdate.getLongitude());
            MapUtils.setMarker(this.map, driverMarker, latLng, R.drawable.ic_marker_taxi);
            MapUtils.moveAndAnimateCamera(this.map, latLng, 13);
        }
        else{
            mapReadyNotCalled = true;
        }
    }
}
