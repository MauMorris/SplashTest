package com.example.mauriciogodinez.splashtest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SplashActivity extends AppCompatActivity {

    private static final int UI_ANIMATION_DELAY = 2000;
    private final Handler mShowHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mShowHandler.removeCallbacks(mIntentRunnable);
        mShowHandler.postDelayed(mIntentRunnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mIntentRunnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            Intent login = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(login);
            finish();
        }
    };
}
