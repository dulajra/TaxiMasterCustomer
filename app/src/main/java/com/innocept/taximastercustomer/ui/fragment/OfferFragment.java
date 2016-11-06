package com.innocept.taximastercustomer.ui.fragment;

import android.os.AsyncTask;
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
import com.innocept.taximastercustomer.model.foundation.Offer;
import com.innocept.taximastercustomer.model.network.Communicator;
import com.innocept.taximastercustomer.ui.adapters.OfferAdapter;

import java.util.List;

/**
 * @author dulaj
 * @version 1.0.0
 */
public class OfferFragment extends Fragment {

    private final String DEBUG_TAG = OfferFragment.class.getSimpleName();

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ProgressBar progressBar;

    private List<Offer> dataSet;

    public OfferFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    private void loadData() {
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (swipeRefreshLayout != null){
                    swipeRefreshLayout.setRefreshing(true);
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                dataSet = new Communicator().getOffers();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter = new OfferAdapter(getActivity(), dataSet);
                recyclerView.setAdapter(adapter);
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        }.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_offers, container, false);
        progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_offers);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                adapter.notifyDataSetChanged();
            }
        });

        progressBar.setVisibility(View.VISIBLE);

        return rootView;
    }

}
