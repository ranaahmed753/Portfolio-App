package com.codingbaba.portfolioapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import static java.lang.Thread.sleep;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class SplashScreenActivity extends AppCompatActivity {
    private ConstraintLayout mConstraintLayout;
    private TextView mInitializingText;
    private AdView mAdView;
    private AdRequest adRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        mConstraintLayout = findViewById(R.id.constraintLayout);
        mInitializingText = findViewById(R.id.initializingTextID);
//        MobileAds.initialize(this);
////        mAdView = findViewById(R.id.adView);
////        adRequest = new AdRequest.Builder().build();
////        mAdView.loadAd(adRequest);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    sleep(6000);
                    startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mConstraintLayout.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left));
    }
}