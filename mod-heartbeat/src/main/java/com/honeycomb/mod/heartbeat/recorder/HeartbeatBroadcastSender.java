package com.honeycomb.mod.heartbeat.recorder;

import android.content.Context;
import android.content.Intent;

import com.honeycomb.lib.common.AppCommon;
import com.honeycomb.mod.heartbeat.Heartbeat;
import com.honeycomb.mod.heartbeat.HeartbeatEvent;
import com.honeycomb.mod.heartbeat.HeartbeatListener;

public class HeartbeatBroadcastSender implements HeartbeatListener {
    private static final String PERMISSION_RECEIVE_HEARTBEAT = "com.honeycomb.permission.RECEIVE_HEARTBEAT";

    private static final String ACTION_HEARTBEAT = "com.honeycomb.action.HEARTBEAT";
    private static final String EXTRA_PACKAGE_NAME = "com.honeycomb.extra.package_name";
    private static final String EXTRA_TIMESTAMP = "com.honeycomb.extra.timestamp";
    private static final String EXTRA_DT = "com.honeycomb.extra.dt";
    private static final String EXTRA_HEARTBEAT_INTERVAL = "com.honeycomb.extra.heartbeat_interval";

    @Override
    public void onHeartbeat(HeartbeatEvent heartbeat) {
        Context context = AppCommon.getInstance().getApplicationContext();

        Intent intent = new Intent(ACTION_HEARTBEAT);
        intent.putExtra(EXTRA_PACKAGE_NAME, context.getPackageName());
        intent.putExtra(EXTRA_TIMESTAMP, heartbeat.timestamp);
        intent.putExtra(EXTRA_DT, heartbeat.dt);
        intent.putExtra(EXTRA_HEARTBEAT_INTERVAL, Heartbeat.getInstance().getHeartbeatInterval());

        context.sendBroadcast(intent, PERMISSION_RECEIVE_HEARTBEAT);
    }
}
