package com.honeycomb.mod.keepalive.debug.heartbeat;

import java.util.LinkedList;
import java.util.List;

class HeartbeatPublisherImpl implements HeartbeatPublisher {
    private final List<HeartbeatListener> mListeners = new LinkedList<>();

    @Override
    public void addHeartbeatListener(HeartbeatListener listener) {
        if (listener != null && !mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    @Override
    public void removeHeartbeatListener(HeartbeatListener listener) {
        if (listener != null) {
            mListeners.remove(listener);
        }
    }

    @Override
    public void clearHeartbeatListeners() {
        mListeners.clear();
    }

    @Override
    public void publishHeartbeatEvent(HeartbeatEvent heartbeat) {
        if (heartbeat == null) {
            return;
        }

        for (HeartbeatListener listener : mListeners) {
            try {
                listener.onHeartbeat(heartbeat);
            } catch (Exception ignored) {
            }
        }
    }
}
