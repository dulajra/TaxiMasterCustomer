package com.innocept.taximastercustomer.presenter;

import android.content.Intent;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.innocept.taximastercustomer.ApplicationContext;
import com.innocept.taximastercustomer.model.Communicator;
import com.innocept.taximastercustomer.model.foundation.Location;
import com.innocept.taximastercustomer.model.foundation.Taxi;
import com.innocept.taximastercustomer.model.foundation.TaxiType;
import com.innocept.taximastercustomer.ui.activity.NewOrderActivity;

import java.util.List;

/**
 * Created by Dulaj on 17-Apr-16.
 */

/**
 * Presenter if NewOrderActivity
 */
public class NewOrderPresenter {

    private static NewOrderPresenter instance = null;
    private NewOrderActivity view;

    private NewOrderPresenter() {
    }

    public static NewOrderPresenter getInstance(){
        if(instance==null){
            instance = new NewOrderPresenter();
        }
        return instance;
    }

    public void setView(NewOrderActivity view) {
        this.view = view;
    }

    public void getAvailableTaxis(final LatLng origin, final TaxiType taxiType){

        new AsyncTask<Void, Void, Void>(){

            Communicator communicator;
            List<Taxi> taxiList;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                communicator = new Communicator();
            }

            @Override
            protected Void doInBackground(Void... params) {
                taxiList = communicator.getAvailableTaxis(new Location(origin.latitude, origin.longitude), taxiType);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                view.nanoCabFragment.test(taxiList);
            }

        }.execute();
    }
}
