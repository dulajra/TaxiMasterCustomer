package com.innocept.taximastercustomer;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.innocept.taximastercustomer.ui.activity.MyOrdersActivity;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.text.SimpleDateFormat;

/**
 * Created by Dulaj on 14-Apr-16.
 */

/**
 * ApplicationContext is used to get the context of the application from any where.
 */
public class ApplicationContext extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new NotificationOpenedHandler())
                .init();

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                ApplicationPreferences.setOneSignalUserId(userId);
            }
        });

        OneSignal.sendTag("userType", "customer");
    }

    public static Context getContext() {
        return context;
    }

    // This fires when a notification is opened by tapping on it or one is received while the app is running.
    private class NotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {

        @Override
        public void notificationOpened(String message, JSONObject additionalData, boolean isActive) {
            try {
                if (additionalData != null) {
                    if(additionalData.getString("notificationType").equals("driverResponse")){
                        Intent intent = new Intent(context, MyOrdersActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);

                        intent.putExtra("isUpdate", true);
                        intent.putExtra("id", additionalData.getInt("id"));
                        intent.putExtra("response", additionalData.getBoolean("response"));
                        startActivity(intent);
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
