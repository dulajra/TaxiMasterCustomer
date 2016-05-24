package com.innocept.taximastercustomer.presenter;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.innocept.taximastercustomer.model.Communicator;
import com.innocept.taximastercustomer.model.foundation.Location;
import com.innocept.taximastercustomer.model.foundation.Taxi;
import com.innocept.taximastercustomer.model.foundation.TaxiType;
import com.innocept.taximastercustomer.ui.activity.NewOrderActivity;
import com.innocept.taximastercustomer.ui.fragment.TaxiFragment;

import java.util.List;

/**
 * Created by Dulaj on 17-Apr-16.
 */

/**
 * Presenter if NewOrderActivity
 */
public class NewOrderPresenter {

    private final String DEBUG_TAG = NewOrderPresenter.class.getSimpleName();

    private static NewOrderPresenter instance = null;
    private NewOrderActivity view;

    private TaxiFragment nanoCabFragment;
    private TaxiFragment carFragment;
    private TaxiFragment vanFragment;

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
        this.nanoCabFragment = view.nanoCabFragment;
        this.carFragment = view.carFragment;
        this.vanFragment = view.vanFragment;
    }

    public void getAvailableTaxis(final LatLng origin, final TaxiType taxiType){

        new AsyncTask<Void, Void, Void>(){

            Communicator communicator;
            List<Taxi> taxiList;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                communicator = new Communicator();

                view.lockUI();

                if(taxiType == TaxiType.NANO){
                    nanoCabFragment.lockUI();
                }
                else if(taxiType == TaxiType.CAR){
                    carFragment.lockUI();
                }
                else if(taxiType == TaxiType.VAN){
                    vanFragment.lockUI();
                }
            }

            @Override
            protected Void doInBackground(Void... params) {
                taxiList = communicator.getAvailableTaxis(new Location(origin.latitude, origin.longitude), taxiType);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                view.releaseUI();

                if(taxiType == TaxiType.NANO){
                    nanoCabFragment.setData(taxiList);
                    nanoCabFragment.releaseUI();
                }
                else if(taxiType == TaxiType.CAR){
                    carFragment.setData(taxiList);
                    carFragment.releaseUI();
                }
                else if(taxiType == TaxiType.VAN){
                    vanFragment.setData(taxiList);
                    vanFragment.releaseUI();
                }
            }

        }.execute();
    }
}
