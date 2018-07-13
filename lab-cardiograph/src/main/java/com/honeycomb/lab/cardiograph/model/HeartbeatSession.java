package com.honeycomb.lab.cardiograph.model;

import android.text.TextUtils;

import java.util.Date;

public class HeartbeatSession {
    private final String mPackageName;
    private long mHeartbeatInterval;
    private HeartbeatInfo mLastReceivedHeartbeat;

    public HeartbeatSession(String packageName, long heartbeatInterval) {
        mPackageName = packageName;
        setHeartbeatInterval(heartbeatInterval);

        if (TextUtils.isEmpty(mPackageName) || getHeartbeatInterval() <= 0) {
            throw new IllegalArgumentException("Invalid heartbeat session " + toString());
        }
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setHeartbeatInterval(long heartbeatInterval) {
        mHeartbeatInterval = heartbeatInterval;
    }

    public long getHeartbeatInterval() {
        return mHeartbeatInterval;
    }

    public HeartbeatInfo getLastReceivedHeartbeat() {
        return mLastReceivedHeartbeat;
    }

    public long getLastHeartbeatTimestamp() {
        return mLastReceivedHeartbeat != null ? mLastReceivedHeartbeat.timestamp : -1;
    }

    public void setLastReceivedHeartbeat(HeartbeatInfo heartbeat) {
        if (heartbeat == null || heartbeat.isValid()) {
            mLastReceivedHeartbeat = heartbeat;
        }
    }

    @Override
    public String toString() {
        StringBuilder info = new StringBuilder("{");
        info.append("package:").append(mPackageName);
        info.append(",interval").append(mHeartbeatInterval);
        info.append(",lastHeartbeat:");
        if (mLastReceivedHeartbeat != null) {
            info.append(new Date(mLastReceivedHeartbeat.timestamp));
            info.append(',');
            info.append(mLastReceivedHeartbeat.dt);
        } else {
            info.append("null");
        }
        return info.toString();
    }
}
