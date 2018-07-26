package com.honeycomb.sdk;

import android.content.Context;
import android.os.Build;

import com.honeycomb.mod.heartbeat.Heartbeat;
import com.honeycomb.mod.keepalive.keepalive.KeepAlive;
import com.honeycomb.mod.keepalive.keepalive.KeepAliveOptions;
import com.honeycomb.mod.keepalive.wakeup.Wakeup;
import com.honeycomb.sdk.common.AppCommonRegistry;

public class ModRegistry extends AppCommonRegistry {

    @Override
    public void onConfigure(Context context) {
        KeepAliveOptions keepAliveOptions = new KeepAliveOptions();
        keepAliveOptions.enableForegroundService = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            keepAliveOptions.enableBackgroundService = false;
        }
        KeepAlive.setOptions(keepAliveOptions);
    }

    @Override
    public void onStart(Context context) {
        // Start heartbeat
        Heartbeat.getInstance().start();

        // Keep wake-up
        Wakeup.getInstance().start();

        // Keep alive
        KeepAlive.getInstance().start();
    }
}
