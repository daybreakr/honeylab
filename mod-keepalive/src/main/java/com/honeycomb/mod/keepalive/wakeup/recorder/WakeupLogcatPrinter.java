package com.honeycomb.mod.keepalive.wakeup.recorder;

import android.util.Log;

import com.honeycomb.mod.keepalive.wakeup.WakeupEvent;
import com.honeycomb.mod.keepalive.wakeup.WakeupListener;

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
