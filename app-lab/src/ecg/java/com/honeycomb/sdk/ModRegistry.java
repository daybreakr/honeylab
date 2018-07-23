package com.honeycomb.sdk;

import android.content.Context;

import com.honeycomb.lab.cardiograph.model.HeartbeatMonitor;
import com.honeycomb.sdk.common.AppCommonRegistry;

public class ModRegistry extends AppCommonRegistry {

    @Override
    public void onStart(Context context) {
        // Monitor heartbeat broadcast
        HeartbeatMonitor.getInstance().start();
    }
}
