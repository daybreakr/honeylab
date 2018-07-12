package com.honeycomb.mod.keepalive.debug.heartbeat.recorder;

import com.honeycomb.mod.keepalive.debug.heartbeat.HeartbeatPublisher;
import com.honeycomb.mod.keepalive.debug.heartbeat.recorder.impl.HeartbeatBroadcastSender;
import com.honeycomb.mod.keepalive.debug.heartbeat.recorder.impl.HeartbeatLogcatPrinter;

public class HeartbeatRecorderRegistry {

    static void register(HeartbeatPublisher publisher) {
        publisher.addHeartbeatListener(new HeartbeatLogcatPrinter());
        publisher.addHeartbeatListener(new HeartbeatBroadcastSender());
    }
}
