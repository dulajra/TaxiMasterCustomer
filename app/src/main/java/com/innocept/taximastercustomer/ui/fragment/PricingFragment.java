package com.innocept.taximastercustomer.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.innocept.taximastercustomer.R;

/**
 * @author dulaj
 * @version 1.0.0
 */
public class PricingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pricing, container, false);
        return rootView;
    }
}
