package com.example.mauriciogodinez.splashtest.impl;

/*
 * Created by mauriciogodinez on 12/12/17.
 */

import com.example.mauriciogodinez.splashtest.constructor.MyIntentListener;

public class MyIntentImpl {
    private MyIntentListener listener;

    public MyIntentImpl(){
        this.listener = null;
    }

    public void start(){
        if(listener != null){
            listener.finishedTime();
        }
    }

    public void setOnStartListener(MyIntentListener listener){
        this.listener = listener;
    }
}