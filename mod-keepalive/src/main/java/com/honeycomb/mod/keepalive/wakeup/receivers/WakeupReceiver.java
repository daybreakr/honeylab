package com.honeycomb.mod.keepalive.wakeup.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.honeycomb.mod.keepalive.wakeup.Wakeup;
import com.honeycomb.mod.keepalive.wakeup.WakeupIntents;

public class WakeupReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent != null ? intent.getAction() : null;
        if (action == null) {
            return;
        }

        final long receivedTime = System.currentTimeMillis();
        String tag = intent.getStringExtra(WakeupIntents.EXTRA_TAG);
        if (tag == null) {
            tag = action;
        }

        Wakeup.getInstance().onWakeup(receivedTime, tag);
    }
}
