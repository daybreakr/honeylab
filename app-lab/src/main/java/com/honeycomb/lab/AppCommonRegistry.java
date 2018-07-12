package com.honeycomb.lab;

import com.honeycomb.mod.heartbeat.Heartbeat;
import com.honeycomb.mod.heartbeat.recorder.HeartbeatRecorder;

public class AppCommonRegistry {

    public static void start() {
        Heartbeat.getInstance().start();
        HeartbeatRecorder.getInstance().start();
    }
}
