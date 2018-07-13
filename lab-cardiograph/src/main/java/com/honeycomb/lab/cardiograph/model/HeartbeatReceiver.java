package com.honeycomb.lab.cardiograph.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.lang.ref.WeakReference;

public class HeartbeatReceiver extends BroadcastReceiver {

    public interface Callback {

        void onReceiveHeartbeat(HeartbeatInfo heartbeat);
    }

    private static final String ACTION_HEARTBEAT = "com.honeycomb.action.HEARTBEAT";
    private static final String EXTRA_PACKAGE_NAME = "com.honeycomb.extra.package_name";
    private static final String EXTRA_TIMESTAMP = "com.honeycomb.extra.timestamp";
    private static final String EXTRA_DT = "com.honeycomb.extra.dt";
    private static final String EXTRA_HEARTBEAT_INTERVAL = "com.honeycomb.extra.heartbeat_interval";

    private static final IntentFilter sFilter = new IntentFilter(ACTION_HEARTBEAT);

    private WeakReference<Callback> mCallbackRef;

    private Context mContext;

    public void register(Context context, Callback callback) {
        if (context == null || callback == null) {
            return;
        }
        synchronized (this) {
            if (mContext == null) {
                mContext = context;
                mCallbackRef = new WeakReference<>(callback);

                mContext.registerReceiver(this, sFilter);
            }
        }
    }

    public void unregister() {
        synchronized (this) {
            if (mContext != null) {
                mContext.unregisterReceiver(this);

                mContext = null;
                mCallbackRef = null;
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String packageName = intent.getStringExtra(EXTRA_PACKAGE_NAME);
        long timestamp = intent.getLongExtra(EXTRA_TIMESTAMP, -1);
        long dt = intent.getLongExtra(EXTRA_DT, -1);
        long interval = intent.getLongExtra(EXTRA_HEARTBEAT_INTERVAL, -1);
        HeartbeatInfo heartbeat = new HeartbeatInfo(packageName, timestamp, dt, interval);
        if (!heartbeat.isValid()) {
            return;
        }

        publishHeartbeat(heartbeat);
    }

    private void publishHeartbeat(HeartbeatInfo heartbeat) {
        Callback callback = mCallbackRef != null ? mCallbackRef.get() : null;
        if (callback != null) {
            try {
                callback.onReceiveHeartbeat(heartbeat);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
