package com.honeycomb.mod.keepalive.debug.heartbeat.recorder.impl;

import android.content.Context;
import android.content.Intent;

import com.honeycomb.mod.keepalive.debug.Sdk;
import com.honeycomb.mod.keepalive.debug.heartbeat.HeartbeatEvent;
import com.honeycomb.mod.keepalive.debug.heartbeat.HeartbeatListener;

public class HeartbeatBroadcastSender implements HeartbeatListener {
    private static final String PERMISSION_RECEIVE_HEARTBEAT = "com.honeycomb.permission.RECEIVE_HEARTBEAT";

    private static final String ACTION_HEARTBEAT = "com.honeycomb.action.HEARTBEAT";
    private static final String EXTRA_PACKAGE_NAME = "com.honeycomb.extra.package_name";
    private static final String EXTRA_TIMESTAMP = "com.honeycomb.extra.timestamp";
    private static final String EXTRA_DT = "com.honeycomb.extra.dt";

    @Override
    public void onHeartbeat(HeartbeatEvent heartbeat) {
        Context context = Sdk.getInstance().getApplicationContext();

        Intent intent = new Intent(ACTION_HEARTBEAT);
        intent.putExtra(EXTRA_PACKAGE_NAME, context.getPackageName());
        intent.putExtra(EXTRA_TIMESTAMP, heartbeat.timestamp);
        intent.putExtra(EXTRA_DT, heartbeat.dt);

        context.sendBroadcast(intent, PERMISSION_RECEIVE_HEARTBEAT);
    }
}
