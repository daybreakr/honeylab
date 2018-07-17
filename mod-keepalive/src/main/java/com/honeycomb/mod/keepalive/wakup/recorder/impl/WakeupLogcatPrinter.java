package com.honeycomb.mod.keepalive.wakup.recorder.impl;

import android.util.Log;

import com.honeycomb.mod.keepalive.wakup.WakeupEvent;
import com.honeycomb.mod.keepalive.wakup.WakeupListener;

import java.util.Calendar;

public class WakeupLogcatPrinter implements WakeupListener {
    private static final String DEFAULT_TAG = "Wakeup";

    private final String mTag;

    public WakeupLogcatPrinter() {
        this(DEFAULT_TAG);
    }

    public WakeupLogcatPrinter(String tag) {
        mTag = tag;
    }

    @Override
    public void onWakeup(WakeupEvent wakeupEvent) {
        Log.i(mTag, " - Wakeup: " + formatWakeup(wakeupEvent));
    }

    private String formatWakeup(WakeupEvent wakeupEvent) {
        if (wakeupEvent == null) {
            return "no details";
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(wakeupEvent.receivedTime);
        return calendar.getTime().toString() + " - " + wakeupEvent.tag;
    }
}
