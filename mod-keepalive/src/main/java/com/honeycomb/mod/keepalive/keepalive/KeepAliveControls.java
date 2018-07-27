package com.honeycomb.mod.keepalive.keepalive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class KeepAliveControls {
    private static final String ACTION_START_KEEP_ALIVE = "com.honeycomb.action.START_KEEP_ALIVE";
    private static final String ACTION_STOP_KEEP_ALIVE = "com.honeycomb.action.STOP_KEEP_ALIVE";

    private final Context mContext;

    private final IntentFilter mFilter;
    private BroadcastReceiver mReceiver;

    KeepAliveControls(Context context) {
        mContext = context.getApplicationContext();

        mFilter = new IntentFilter();
        mFilter.addAction(ACTION_START_KEEP_ALIVE);
        mFilter.addAction(ACTION_STOP_KEEP_ALIVE);
    }

    public synchronized void register() {
        if (mReceiver == null) {
            mReceiver = new KeepAliveControlsReceiver();
            mContext.registerReceiver(mReceiver, mFilter);
        }
    }

    public synchronized void unregister() {
        if (mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

    private static class KeepAliveControlsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent != null ? intent.getAction() : null;
            if (ACTION_START_KEEP_ALIVE.equals(action)) {
                KeepAlive.getInstance().start();
            } else if (ACTION_STOP_KEEP_ALIVE.equals(action)) {
                KeepAlive.getInstance().stop();
            }
        }
    }
}
