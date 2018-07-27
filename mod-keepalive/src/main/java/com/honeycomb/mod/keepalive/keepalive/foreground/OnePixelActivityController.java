package com.honeycomb.mod.keepalive.keepalive.foreground;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class OnePixelActivityController {
    private final Context mContext;

    private static final IntentFilter FILTER = new IntentFilter(Intent.ACTION_SCREEN_OFF);

    public OnePixelActivityController(Context context) {
        mContext = context.getApplicationContext();
    }

    public void start() {
        mContext.registerReceiver(mReceiver, FILTER);
    }

    public void stop() {
        mContext.unregisterReceiver(mReceiver);
        OnePixelActivity.stop(mContext);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            OnePixelActivity.start(context);
        }
    };
}
