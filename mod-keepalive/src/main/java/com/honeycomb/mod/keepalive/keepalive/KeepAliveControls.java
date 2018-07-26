package com.honeycomb.mod.keepalive.keepalive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class KeepAliveControls {
    private static final String ACTION_START_KEEP_ALIVE = "com.honeycomb.action.START_KEEP_ALIVE";
    private static final String ACTION_STOP_KEEP_ALIVE = "com.honeycomb.action.STOP_KEEP_ALIVE";

    private final IntentFilter mFilter;
    private final BroadcastReceiver mReceiver;

    private Context mContext;

    KeepAliveControls() {
        mFilter = new IntentFilter();
        mFilter.addAction(ACTION_START_KEEP_ALIVE);
        mFilter.addAction(ACTION_STOP_KEEP_ALIVE);

        mReceiver = new KeepAliveControlsReceiver();
    }

    public synchronized void register(Context context) {
        if (mContext == null) {
            mContext = context;
            mContext.registerReceiver(mReceiver, mFilter);
        }
    }

    public synchronized void unregister() {
        if (mContext != null) {
            mContext.unregisterReceiver(mReceiver);
            mContext = null;
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
