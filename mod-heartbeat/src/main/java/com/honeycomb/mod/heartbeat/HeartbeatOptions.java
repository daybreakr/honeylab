package com.honeycomb.mod.heartbeat;

import com.honeycomb.mod.heartbeat.recorder.HeartbeatRecorderOptions;

public class HeartbeatOptions {

    public long heartbeatInterval = 2 * 1000; // 10 seconds

    public boolean useBuffer = true;

    public boolean startHeartbeatRecorder = true;

    public HeartbeatRecorderOptions heartbeatRecorderOptions = null;
}
