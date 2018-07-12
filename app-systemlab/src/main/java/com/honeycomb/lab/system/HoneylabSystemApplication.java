package com.honeycomb.lab.system;

import android.app.Application;

import com.honeycomb.lib.common.AppCommon;

public class HoneylabSystemApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppCommon.initialize(this);

        AppCommonRegistry.start();
    }
}
