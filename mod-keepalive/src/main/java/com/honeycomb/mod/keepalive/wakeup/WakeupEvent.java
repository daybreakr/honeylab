package com.honeycomb.mod.keepalive.wakeup;

public class WakeupEvent {
    public final long receivedTime;
    public final String tag;

    public WakeupEvent(long receivedTime, String tag) {
        this.receivedTime = receivedTime;
        this.tag = tag;
    }
}
