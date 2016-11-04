package com.innocept.taximastercustomer.ui.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.innocept.taximastercustomer.R;

/**
 * Created by Dulaj on 24-Mar-16.
 */
public class MyPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}