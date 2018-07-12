package com.honeycomb.mod.keepalive.debug.heartbeat;

public interface HeartbeatPublisher {

    void addHeartbeatListener(HeartbeatListener listener);

    void removeHeartbeatListener(HeartbeatListener listener);

    void clearHeartbeatListeners();

    void publishHeartbeatEvent(HeartbeatEvent heartbeat);
}
