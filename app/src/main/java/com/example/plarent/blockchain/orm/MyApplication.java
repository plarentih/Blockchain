package com.example.plarent.blockchain.orm;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

/**
 * Created by plarent on 14/03/2018.
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}
