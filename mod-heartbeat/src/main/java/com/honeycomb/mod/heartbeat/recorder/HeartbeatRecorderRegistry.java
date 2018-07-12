package com.honeycomb.mod.heartbeat.recorder;

import com.honeycomb.mod.heartbeat.HeartbeatPublisher;
import com.honeycomb.mod.heartbeat.recorder.impl.HeartbeatBroadcastSender;
import com.honeycomb.mod.heartbeat.recorder.impl.HeartbeatFileRecorder;
import com.honeycomb.mod.heartbeat.recorder.impl.HeartbeatLogcatPrinter;

public class HeartbeatRecorderRegistry {

    static void registerLogRecorder(HeartbeatPublisher publisher) {
        publisher.addHeartbeatListener(new HeartbeatLogcatPrinter());
    }

    static void registerBroadcastRecorder(HeartbeatPublisher publisher) {
        publisher.addHeartbeatListener(new HeartbeatBroadcastSender());
    }

    static void registerFileRecorder(HeartbeatPublisher publisher) {
        publisher.addHeartbeatListener(new HeartbeatFileRecorder());
    }
}
