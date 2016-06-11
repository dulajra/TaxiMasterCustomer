package com.innocept.taximastercustomer.ui.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innocept.taximastercustomer.R;
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

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ImageView imageView = new ImageView(SplashActivity.this);
        imageView.setBackgroundResource(R.drawable.ic_splash);
        imageView.setAdjustViewBounds(true);
        setContentView(imageView);

        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, NewOrderActivity.class);
                startActivity(intent);
                finish();
            }
        };

//        Splash screen is closed after 1500 milliseconds
        timer.schedule(timerTask, 1500);
    }
}

