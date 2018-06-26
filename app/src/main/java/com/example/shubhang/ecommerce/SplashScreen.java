package com.example.shubhang.ecommerce;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


public class SplashScreen extends Activity {



    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), FilterActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);




    }




}