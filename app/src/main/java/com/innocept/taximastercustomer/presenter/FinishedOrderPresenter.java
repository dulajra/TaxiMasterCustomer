package com.innocept.taximastercustomer.presenter;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.innocept.taximastercustomer.model.foundation.DriverUpdate;
import com.innocept.taximastercustomer.model.network.Communicator;
import com.innocept.taximastercustomer.ui.activity.FinishedOrderActivity;

/**
 * Created by Dulaj on 17-Apr-16.
 */

/**
 * Presenter if NewOrderActivity
 */
public class FinishedOrderPresenter {

    private final String DEBUG_TAG = FinishedOrderPresenter.class.getSimpleName();

    private static FinishedOrderPresenter instance = null;
    private FinishedOrderActivity view;

    private FinishedOrderPresenter() {
    }

    public static FinishedOrderPresenter getInstance(){
        if(instance==null){
            instance = new FinishedOrderPresenter();
        }
        return instance;
    }

    public void setView(FinishedOrderActivity view) {
        this.view = view;
    }

    public void updateDriverDetails(final int driverId, final LatLng latLng){

        new AsyncTask<Void, Void, Void>(){

            Communicator communicator;
            DriverUpdate driverUpdate;

            @Override
            protected Void doInBackground(Void... params) {
                driverUpdate = Communicator.getDriverUpdate(driverId, latLng);
                return null;
            }

        }.execute();
    }

    public void rateOrder(final int id, final int rating, final String comment) {
        new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected Boolean doInBackground(Void... params) {
                return Communicator.rateOrder(id, rating, comment);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                view.afterRating(result);
            }
        }.execute();
    }
}
