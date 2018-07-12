package com.honeycomb.mod.heartbeat.recorder;

import com.honeycomb.mod.heartbeat.HeartbeatPublisher;
import com.honeycomb.mod.heartbeat.recorder.impl.HeartbeatBroadcastSender;
import com.honeycomb.mod.heartbeat.recorder.impl.HeartbeatLogcatPrinter;

public class HeartbeatRecorderRegistry {

    static void register(HeartbeatPublisher publisher) {
        publisher.addHeartbeatListener(new HeartbeatLogcatPrinter());
        publisher.addHeartbeatListener(new HeartbeatBroadcastSender());
    }
}
