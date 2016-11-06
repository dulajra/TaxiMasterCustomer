package com.innocept.taximastercustomer.presenter;

import android.os.AsyncTask;

import com.innocept.taximastercustomer.model.network.Communicator;
import com.innocept.taximastercustomer.ui.activity.LoginActivity;

/**
 * @author dulaj
 * @version 1.0.0
 */
public class LoginPresenter {

    private static LoginPresenter instance = null;
    private LoginActivity view;

    private LoginPresenter() {
    }

    public static LoginPresenter getInstance(){
        if(instance==null){
            instance = new LoginPresenter();
        }
        return instance;
    }

    public void setView(LoginActivity view) {
        this.view = view;
    }

    public void signIn(final String phone, final String password){
        new AsyncTask<Void, Void, Integer>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Integer doInBackground(Void... params) {
                int resultCode = Communicator.login(phone, password);
                return resultCode;
            }

            @Override
            protected void onPostExecute(Integer resultCode) {
                super.onPostExecute(resultCode);
                switch (resultCode){
                    case 0:
                        view.onSignInSuccess();
                        break;
                    case 1:
                        view.onSignInFailed("Phone number or password is incorrect");
                        break;
                    default:
                        view.onSignInFailed("Something went wrong. Try again!");
                }
            }
        }.execute();
    }
}
