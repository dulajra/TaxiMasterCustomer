package com.innocept.taximastercustomer;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.onesignal.OneSignal;

import org.json.JSONObject;

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
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
                .init();

        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                Log.d("debug", "User:" + userId);
                if (registrationId != null)
                    Log.d("debug", "registrationId:" + registrationId);
            }
        });

        OneSignal.sendTag("userType", "customer");
    }

    public static Context getContext() {
        return context;
    }

    // This fires when a notification is opened by tapping on it or one is received while the app is running.
    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {

        @Override
        public void notificationOpened(String message, JSONObject additionalData, boolean isActive) {
            try {
                if (additionalData != null) {
                    if (additionalData.has("actionSelected")) {
                        Log.i("OneSignalExample", "Full additionalData:\n" + additionalData.toString());
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }

            Intent intent = new Intent(context, null);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
