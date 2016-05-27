package com.innocept.taximastercustomer;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

/**
 * Created by dulaj on 5/26/16.
 */

/**
 * Actions regarding all the user preferences of the application.
 */
public class ApplicationPreferences {

    private static ApplicationPreferences instance = null;
    private static SharedPreferences sharedPreferences;

    private ApplicationPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.getContext());
    }

    private static ApplicationPreferences init(){
        if(instance==null){
            instance=new ApplicationPreferences();
        }
        return instance;
    }

    public static void setOneSignalUserId(String id){
        init();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("oneSignalUserId", id);
        editor.commit();
    }

    public static String getOneSignalUserId(){
        init();
        return sharedPreferences.getString("oneSignalUserId", null);
    }


}
