package com.innocept.taximastercustomer;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.util.Log;


import com.innocept.taximastercustomer.ui.activity.HomeActivity;
import com.onesignal.OneSignal;

import org.json.JSONObject;

/**
 * @author dulaj
 * @version 1.0.0
 */
public class ApplicationContext extends Application {

    private final String DEBUG_TAG = ApplicationContext.class.getSimpleName();

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

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    public static Context getContext() {
        return context;
    }

    // This fires when a notification is opened by tapping on it or notification is received while the app is running.
    private class NotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {

        @Override
        public void notificationOpened(String message, JSONObject additionalData, boolean isActive) {
            try {
                if (additionalData != null) {
                    Log.v(DEBUG_TAG, "Additional data: " + additionalData.toString());
                    if (additionalData.getString("notificationType").equals("offer")) {
                        Intent intent = new Intent(context, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("navigationItem", 3);
                        startActivity(intent);
                    }
                    if (additionalData.getString("notificationType").equals("driverResponse")) {
                        Intent intent = new Intent(context, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);

                        intent.putExtra("isUpdate", true);
                        intent.putExtra("id", additionalData.getInt("id"));
                        intent.putExtra("response", additionalData.getBoolean("response"));
                        startActivity(intent);
                    } else if (additionalData.getString("notificationType").equals("now")) {
                        Intent intent = new Intent(context, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);

                        intent.putExtra("now", true);
                        intent.putExtra("id", additionalData.getInt("id"));
                        startActivity(intent);
                    } else if (additionalData.getString("notificationType").equals("finishHire")) {
                        Intent intent = new Intent(context, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);

                        intent.putExtra("finish", true);
                        intent.putExtra("id", additionalData.getInt("id"));
                        startActivity(intent);
                    }
                } else {
                    Log.w(DEBUG_TAG, "Additional data is null");
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
