package com.honeycomb.lab;

import com.honeycomb.lab.cardiograph.model.HeartbeatMonitor;
import com.honeycomb.mod.heartbeat.Heartbeat;
import com.honeycomb.mod.heartbeat.recorder.HeartbeatRecorder;

public class AppCommonRegistry {

    public static void start() {
        Heartbeat.getInstance().start();
        HeartbeatRecorder.getInstance().start();

        HeartbeatMonitor.getInstance().start();
    }
}
