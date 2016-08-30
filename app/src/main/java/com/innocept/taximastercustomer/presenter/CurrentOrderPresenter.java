package com.innocept.taximastercustomer.presenter;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.innocept.taximastercustomer.model.foundation.DriverUpdate;
import com.innocept.taximastercustomer.model.network.Communicator;
import com.innocept.taximastercustomer.ui.activity.CurrentOrderActivity;

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
            DriverUpdate driverUpdate;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                view.whileGettingUpdates();
            }

            @Override
            protected Void doInBackground(Void... params) {
                driverUpdate = Communicator.getDriverUpdate(driverId, latLng);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(driverUpdate !=null){
                    view.onUpdateSuccess(driverUpdate);
                }
            }

        }.execute();
    }

    public void getDriverLocation(final int driverId){

        new AsyncTask<Void, Void, Void>(){

            Communicator communicator;
            DriverUpdate driverUpdate;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                driverUpdate = Communicator.getDriverLocation(driverId);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(driverUpdate !=null){
                    view.updateDriverLocation(new LatLng(driverUpdate.getLatitude(), driverUpdate.getLongitude()));
                }
            }

        }.execute();
    }
}
