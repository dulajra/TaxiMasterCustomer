package com.innocept.taximastercustomer.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.innocept.taximastercustomer.R;
import com.innocept.taximastercustomer.model.foundation.Taxi;
import com.innocept.taximastercustomer.presenter.NewOrderPresenter;
import com.innocept.taximastercustomer.ui.adapters.TaxiListAdapter;

import java.util.List;

/**
 * Created by Dulaj on 17-Apr-16.
 */
public class NanoCabFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public NanoCabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_nano_cab, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_nano_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        return rootView;
    }

    public void test(List<Taxi> dataSet){
        Log.i("NanoCabFragment", "This is a test >>>>>>>>>>>>");
        adapter = new TaxiListAdapter(getActivity(), dataSet);
        recyclerView.setAdapter(adapter);
    }

}