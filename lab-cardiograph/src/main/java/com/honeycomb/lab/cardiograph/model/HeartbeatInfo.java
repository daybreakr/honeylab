package com.honeycomb.lab.cardiograph.model;

import android.text.TextUtils;

import java.util.Date;

public class HeartbeatInfo {
    public final String packageName;
    public final long timestamp;
    public final long dt;
    public final long interval;

    public HeartbeatInfo(String packageName, long timestamp, long dt, long interval) {
        this.packageName = packageName;
        this.timestamp = timestamp;
        this.dt = dt;
        this.interval = interval;
    }

    boolean isValid() {
        return !TextUtils.isEmpty(packageName) && timestamp > 0 && dt >= 0 && interval > 0;
    }

    @Override
    public String toString() {
        return "{package:" + packageName
                + ",time:" + new Date(timestamp).toString()
                + ",dt:" + dt
                + ",interval:" + interval
                + "}";
    }
}
