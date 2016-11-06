package com.innocept.taximastercustomer.ui.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.innocept.taximastercustomer.R;

/**
 * @author dulaj
 * @version 1.0.0
 */
public class MyPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}