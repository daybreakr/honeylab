package com.honeycomb.mod.keepalive.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.honeycomb.mod.keepalive.KeepAliveConstants;
import com.honeycomb.mod.keepalive.KeepAliveUtils;

public class KeepAliveReceiver extends BroadcastReceiver {
    private static final String TAG = KeepAliveConstants.TAG + "-receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent != null ? intent.getAction() : null;
        Log.i(TAG, "Received action: " + action);

        KeepAliveUtils.sendKeepAliveBroadcast(context);
    }
}
