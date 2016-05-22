package com.innocept.taximastercustomer.presenter;

import android.content.Intent;
import android.location.Location;

import com.innocept.taximastercustomer.ApplicationContext;
import com.innocept.taximastercustomer.ui.activity.NewOrderActivity;

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

    public Location fetchMyLocation() {
        return null;
    }
}
