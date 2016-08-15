package com.innocept.taximastercustomer.presenter;

import android.os.AsyncTask;

import com.innocept.taximastercustomer.model.foundation.User;
import com.innocept.taximastercustomer.model.network.Communicator;
import com.innocept.taximastercustomer.ui.activity.LoginActivity;
import com.innocept.taximastercustomer.ui.activity.SignUpActivity;

/**
 * Created by Dulaj on 16-Apr-16.
 */
public class SignUpPresenter {

    private static SignUpPresenter instance = null;
    private SignUpActivity view;

    private SignUpPresenter() {
    }

    public static SignUpPresenter getInstance(){
        if(instance==null){
            instance = new SignUpPresenter();
        }
        return instance;
    }

    public void setView(SignUpActivity view) {
        this.view = view;
    }

    public void signUp(User user){
        new AsyncTask<Void, Void, Integer>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Integer doInBackground(Void... params) {
                Communicator communicator = new Communicator();
                int resultCode = communicator.signUp(user);
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
