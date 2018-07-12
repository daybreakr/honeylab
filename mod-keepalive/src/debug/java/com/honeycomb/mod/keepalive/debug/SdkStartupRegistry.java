package com.honeycomb.mod.keepalive.debug;

import com.honeycomb.mod.keepalive.debug.heartbeat.Heartbeat;
import com.honeycomb.mod.keepalive.debug.heartbeat.recorder.HeartbeatRecorder;

public class SdkStartupRegistry {

    public static void start() {
        Heartbeat.getInstance().start();
        HeartbeatRecorder.getInstance().start();
    }
}
