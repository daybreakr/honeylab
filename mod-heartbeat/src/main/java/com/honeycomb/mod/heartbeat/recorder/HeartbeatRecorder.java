package com.honeycomb.mod.heartbeat.recorder;

import com.honeycomb.mod.heartbeat.Heartbeat;

public class HeartbeatRecorder {
    private static HeartbeatRecorderOptions sOptions = new HeartbeatRecorderOptions();

    private static volatile HeartbeatRecorder sInstance;

    private final HeartbeatRecorderOptions mOptions;

    private HeartbeatRecorder(HeartbeatRecorderOptions options) {
        mOptions = options;
    }

    // Set before initialize
    public static void setOptions(HeartbeatRecorderOptions options) {
        if (options != null) {
            sOptions = options;
        }
    }

    public static HeartbeatRecorder getInstance() {
        if (sInstance == null) {
            synchronized (HeartbeatRecorder.class) {
                if (sInstance == null) {
                    sInstance = new HeartbeatRecorder(sOptions);
                }
            }
        }
        return sInstance;
    }

    public void start() {
        Heartbeat heartbeat = Heartbeat.getInstance();

        if (mOptions.printLog) {
            HeartbeatRecorderRegistry.registerLogRecorder(heartbeat);
        }
        if (mOptions.sendHeartbeatBroadcast) {
            HeartbeatRecorderRegistry.registerBroadcastRecorder(heartbeat);
        }
        if (mOptions.saveToFile) {
            HeartbeatRecorderRegistry.registerFileRecorder(heartbeat);
        }

        heartbeat.flush();
    }
}
