package com.innocept.taximastercustomer;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.innocept.taximastercustomer.model.foundation.User;

/**
 * @author dulaj
 * @version 1.0.0
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

    public static void saveUser(User user){
        init();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user", new Gson().toJson(user).toString());
        editor.commit();
    }

    public static User getUser(){
        init();
        return new Gson().fromJson(sharedPreferences.getString("user", null), User.class);
    }

}
