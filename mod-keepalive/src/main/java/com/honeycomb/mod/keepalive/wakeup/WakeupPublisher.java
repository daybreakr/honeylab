package com.honeycomb.mod.keepalive.wakeup;

public interface WakeupPublisher {

    void addWakeupListener(WakeupListener listener);

    void removeWakeupListener(WakeupListener listener);

    void clearWakeupListeners();

    void publishWakeupEvent(WakeupEvent wakeupEvent);
}
