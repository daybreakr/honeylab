package com.honeycomb.lab;

import android.app.Application;

import com.honeycomb.mod.heartbeat.Heartbeat;
import com.honeycomb.mod.keepalive.wakup.Wakeup;

public class HoneylabApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Start heartbeat
        Heartbeat.getInstance().start();

        // Keep wake-up
        Wakeup.getInstance().start();
    }
}
