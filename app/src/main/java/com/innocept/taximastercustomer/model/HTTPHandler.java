package com.innocept.taximastercustomer.model;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.innocept.taximastercustomer.ApplicationContext;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Dulaj on 14-Apr-16.
 */
public class HTTPHandler {

    public static JSONObject sendGET(String urlString, ContentValues values){
        JSONObject jsonObject = null;
        if (HTTPHandler.isOnline()) {
            try {
                URL url = new URL(urlString + "?" + getQuery(values));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                String json = sb.toString();
                Log.i("sendGET", json);
                jsonObject = new JSONObject(json);
            } catch (Exception e) {
                Log.e("sendGET", "Error in makeHttpRequest " + e.toString());
            }
        } else {
            // If not connected to internet
            Log.w("sendGET", "No internet connection");
        }
        return jsonObject;
    }

    public static JSONObject sendPOST(String urlString, ContentValues values){
        JSONObject jsonObject = null;
        if (HTTPHandler.isOnline()) {
            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "iso-8859-1"));
                writer.write(getQuery(values));
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                String json = sb.toString();
                Log.i("sendPOST", json);
                jsonObject = new JSONObject(json);
            } catch (Exception e) {
                Log.e("sendPOST", "Error in makeHttpRequest " + e.toString());
            }
        } else {
            // If not connected to internet
            Log.w("sendPOST", "No internet connection");
        }
        return jsonObject;
    }

    private static String getQuery(ContentValues values) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, Object> item : values.valueSet()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }

            result.append(URLEncoder.encode(item.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(item.getValue().toString(), "UTF-8"));
        }
        return result.toString();
    }

    public static boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) ApplicationContext.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
