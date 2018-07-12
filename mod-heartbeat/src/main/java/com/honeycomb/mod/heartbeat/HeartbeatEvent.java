package com.honeycomb.mod.heartbeat;

public class HeartbeatEvent {
    public final long timestamp;
    public final long dt;

    HeartbeatEvent(long timestamp, long dt) {
        this.timestamp = timestamp;
        this.dt = dt;
    }
}
