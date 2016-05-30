package com.innocept.taximastercustomer.presenter;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.innocept.taximastercustomer.model.foundation.Location;
import com.innocept.taximastercustomer.model.foundation.Order;
import com.innocept.taximastercustomer.model.foundation.Taxi;
import com.innocept.taximastercustomer.model.foundation.TaxiType;
import com.innocept.taximastercustomer.model.network.Communicator;
import com.innocept.taximastercustomer.ui.activity.CurrentOrderActivity;
import com.innocept.taximastercustomer.ui.activity.NewOrderActivity;
import com.innocept.taximastercustomer.ui.fragment.TaxiFragment;

import java.util.List;

/**
 * Created by Dulaj on 17-Apr-16.
 */

/**
 * Presenter if NewOrderActivity
 */
public class CurrentOrderPresenter {

    private final String DEBUG_TAG = CurrentOrderPresenter.class.getSimpleName();

    private static CurrentOrderPresenter instance = null;
    private CurrentOrderActivity view;

    private CurrentOrderPresenter() {
    }

    public static CurrentOrderPresenter getInstance(){
        if(instance==null){
            instance = new CurrentOrderPresenter();
        }
        return instance;
    }

    public void setView(CurrentOrderActivity view) {
        this.view = view;
    }

    public void updateDriverDetails(final int driverId, final LatLng latLng){

        new AsyncTask<Void, Void, Void>(){

            Communicator communicator;
            String[] result;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                view.whileGettingUpdates();
                communicator = new Communicator();
            }

            @Override
            protected Void doInBackground(Void... params) {
                result = communicator.getDriverUpdates(driverId, latLng);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(result!=null){
                    view.onUpdateSuccess(result);
                }
            }

        }.execute();
    }
}
