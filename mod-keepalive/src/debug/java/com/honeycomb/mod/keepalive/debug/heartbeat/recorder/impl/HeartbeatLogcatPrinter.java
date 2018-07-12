package com.honeycomb.mod.keepalive.debug.heartbeat.recorder.impl;

import android.util.Log;

import com.honeycomb.mod.keepalive.debug.heartbeat.HeartbeatEvent;
import com.honeycomb.mod.keepalive.debug.heartbeat.HeartbeatListener;

import java.util.Calendar;

public class HeartbeatLogcatPrinter implements HeartbeatListener {
    private static final String DEFAULT_TAG = "HeartbeatRecorder";

    private final String mTag;

    public HeartbeatLogcatPrinter() {
        this(DEFAULT_TAG);
    }

    public HeartbeatLogcatPrinter(String tag) {
        mTag = tag;
    }

    @Override
    public void onHeartbeat(HeartbeatEvent heartbeat) {
        Log.i(mTag, " - Heartbeat: " + formatHeartbeat(heartbeat));
    }

    private String formatHeartbeat(HeartbeatEvent heartbeat) {
        if (heartbeat == null) {
            return "no details";
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(heartbeat.timestamp);
        return calendar.getTime().toString() + " - " + heartbeat.dt;
    }
}
