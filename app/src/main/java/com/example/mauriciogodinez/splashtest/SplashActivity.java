package com.example.mauriciogodinez.splashtest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.mauriciogodinez.splashtest.constructor.MyIntentListener;
import com.example.mauriciogodinez.splashtest.impl.MyIntentImpl;
import com.example.mauriciogodinez.splashtest.ui.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    private static final String LOG_TAG = SplashActivity.class.getSimpleName();

    private static final int UI_ANIMATION_DELAY = 2000;

    private Handler mShowHandler;
    private MyIntentImpl mCallback;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mContext = SplashActivity.this;
        mShowHandler = new Handler();
        mCallback = new MyIntentImpl();

        mCallback.setOnStartListener(startActivity);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mShowHandler.removeCallbacks(mIntentRunnable);
        mShowHandler.postDelayed(mIntentRunnable, UI_ANIMATION_DELAY);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mShowHandler.removeCallbacks(mIntentRunnable);
        Log.i(LOG_TAG, " pagaTodo: actividad en background " + LOG_TAG);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mShowHandler.removeCallbacks(mIntentRunnable);
        Log.i(LOG_TAG, " pagaTodo: presiona back: elimina " + LOG_TAG);
    }

    private final Runnable mIntentRunnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            mCallback.start();
        }
    };

    private final MyIntentListener startActivity = new MyIntentListener() {
        @Override
        public void finishedTime() {
            Intent login = new Intent(mContext, LoginActivity.class);
            startActivity(login);
            finish();
        }
    };
}