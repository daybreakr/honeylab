package com.honeycomb.mod.heartbeat;

import java.util.LinkedList;
import java.util.List;

class HeartbeatBufferPublisher implements HeartbeatListener, HeartbeatPublisher {
    private final HeartbeatPublisher mPublisher = new HeartbeatPublisherImpl();

    private List<HeartbeatEvent> mPendingHeartbeats;
    private boolean mStarted = false;

    void flush() {
        mStarted = true;

        flushPendingHeartbeats();
    }

    @Override
    public void addHeartbeatListener(HeartbeatListener listener) {
        mPublisher.addHeartbeatListener(listener);
    }

    @Override
    public void removeHeartbeatListener(HeartbeatListener listener) {
        mPublisher.removeHeartbeatListener(listener);
    }

    @Override
    public void clearHeartbeatListeners() {
        mPublisher.clearHeartbeatListeners();
    }

    @Override
    public void publishHeartbeatEvent(HeartbeatEvent heartbeat) {
        mPublisher.publishHeartbeatEvent(heartbeat);
    }

    @Override
    public void onHeartbeat(HeartbeatEvent heartbeat) {
        if (!mStarted) {
            if (mPendingHeartbeats == null) {
                mPendingHeartbeats = new LinkedList<>();
            }
            mPendingHeartbeats.add(heartbeat);
            return;
        }

        publishHeartbeatEvent(heartbeat);
    }

    private void flushPendingHeartbeats() {
        if (mPendingHeartbeats == null) {
            return;
        }

        for (HeartbeatEvent heartbeat : mPendingHeartbeats) {
            publishHeartbeatEvent(heartbeat);
        }

        mPendingHeartbeats = null;
    }
}
