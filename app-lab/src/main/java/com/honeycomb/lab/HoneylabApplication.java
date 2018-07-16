package com.honeycomb.lab;

import android.app.Application;

import com.honeycomb.lib.common.AppCommon;

public class HoneylabApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppCommon.initialize(this);

//        AppCommonRegistry.start();
    }
}
