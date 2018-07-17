package com.honeycomb.lab;

import android.app.Application;

import com.honeycomb.mod.heartbeat.Heartbeat;
import com.honeycomb.mod.keepalive.keepalive.KeepAlive;
import com.honeycomb.mod.keepalive.wakeup.Wakeup;

public class HoneylabApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Start heartbeat
        Heartbeat.getInstance().start();

        // Keep wake-up
        Wakeup.getInstance().start();

        // Keep alive
        KeepAlive.getInstance().start();
    }
}
