package com.honeycomb.mod.keepalive.debug.heartbeat.recorder;

import com.honeycomb.mod.keepalive.debug.heartbeat.Heartbeat;

public class HeartbeatRecorder {
    private static volatile HeartbeatRecorder sInstance;

    private HeartbeatRecorder() {
    }

    public static HeartbeatRecorder getInstance() {
        if (sInstance == null) {
            synchronized (HeartbeatRecorder.class) {
                if (sInstance == null) {
                    sInstance = new HeartbeatRecorder();
                }
            }
        }
        return sInstance;
    }

    public void start() {
        Heartbeat heartbeat = Heartbeat.getInstance();
        HeartbeatRecorderRegistry.register(heartbeat);

        heartbeat.flush();
    }
}
