package com.honeycomb.lab;

import android.app.Application;

import com.honeycomb.lab.cardiograph.model.HeartbeatMonitor;

public class HoneylabApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Monitor heartbeat broadcast
        HeartbeatMonitor.getInstance().start();
    }
}
