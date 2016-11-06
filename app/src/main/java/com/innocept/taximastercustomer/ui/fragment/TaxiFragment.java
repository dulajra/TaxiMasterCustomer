package com.innocept.taximastercustomer.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.innocept.taximastercustomer.R;
import com.innocept.taximastercustomer.model.foundation.Taxi;
import com.innocept.taximastercustomer.ui.activity.NewOrderActivity;
import com.innocept.taximastercustomer.ui.adapters.TaxiListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dulaj
 * @version 1.0.0
 */
public class TaxiFragment extends Fragment {

    private final String DEBUG_TAG = TaxiFragment.class.getSimpleName();

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ProgressBar progressBar;

    private List<Taxi> dataSet;

    public TaxiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataSet();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_taxi, container, false);
        progressBar = (ProgressBar)rootView.findViewById(R.id.progressbar_taxi_list);
        swipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_taxi_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TaxiListAdapter(getActivity(), dataSet);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((NewOrderActivity)getActivity()).submit();
            }
        });

        return rootView;
    }

    private void initDataSet(){
        this.dataSet = new ArrayList<Taxi>();
    }

    public void setData(List<Taxi> dataSet){
        this.dataSet = dataSet;
        displayData();
    }

    public void displayData(){
        adapter = new TaxiListAdapter(getActivity(), this.dataSet);
        recyclerView.setAdapter(adapter);
    }

    public void lockUI(){
        clearViews();
        swipeRefreshLayout.setRefreshing(true);
//        progressBar.setVisibility(View.VISIBLE);
    }

    public void clearViews(){
        initDataSet();
        displayData();
    }

    public void releaseUI(){
        swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
    }

}
