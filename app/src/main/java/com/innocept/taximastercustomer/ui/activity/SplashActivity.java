package com.innocept.taximastercustomer.ui.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innocept.taximastercustomer.presenter.NewOrderPresenter;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dulaj on 16-Apr-16.
 */
public class SplashActivity extends AppCompatActivity {

    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_splash);

//        Test splash
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        TextView textView = new TextView(this);
        textView.setLayoutParams(layoutParams);
        textView.setTextSize(36);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setGravity(Gravity.CENTER);
        textView.setText("Taxi Master");
        setContentView(textView);

        timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, NewOrderActivity.class);
                startActivity(intent);
                finish();
            }
        };

//        Splash screen is closed after 2000 milliseconds
        timer.schedule(timerTask, 10);
    }
}

