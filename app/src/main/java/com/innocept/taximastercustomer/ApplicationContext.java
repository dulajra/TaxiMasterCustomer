package com.innocept.taximastercustomer;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
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

        OneSignal.sendTag("userType", "customer");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
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
                    Log.i(DEBUG_TAG, additionalData.toString());
//                    if(additionalData.getString("notificationType").equals("driverResponse")){
//                        Intent intent = new Intent(context, MyOrdersActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                        intent.putExtra("isUpdate", true);
//                        intent.putExtra("id", additionalData.getInt("id"));
//                        intent.putExtra("response", additionalData.getBoolean("response"));
//                        startActivity(intent);
//                    }
//                    else  if(additionalData.getString("notificationType").equals("now")){
//                        Intent intent = new Intent(context, MyOrdersActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                        intent.putExtra("now", true);
//                        intent.putExtra("id", additionalData.getInt("id"));
//                        startActivity(intent);
//                    }
//                    else  if(additionalData.getString("notificationType").equals("finishHire")){
//                        Intent intent = new Intent(context, MyOrdersActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                        intent.putExtra("finish", true);
//                        intent.putExtra("id", additionalData.getInt("id"));
//                        startActivity(intent);
//                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
