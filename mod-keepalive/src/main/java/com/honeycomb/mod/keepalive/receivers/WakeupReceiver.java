package com.honeycomb.mod.keepalive.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.honeycomb.mod.keepalive.wakup.Wakeup;
import com.honeycomb.mod.keepalive.wakup.WakeupEvent;
import com.honeycomb.mod.keepalive.wakup.WakeupIntents;

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
        WakeupEvent wakeupEvent = new WakeupEvent(receivedTime, tag);
        Wakeup.getInstance().publishWakeupEvent(wakeupEvent);
    }
}
