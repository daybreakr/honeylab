package com.honeycomb.mod.heartbeat.recorder;

import com.honeycomb.mod.heartbeat.HeartbeatEvent;
import com.honeycomb.mod.heartbeat.HeartbeatListener;

import java.util.LinkedList;
import java.util.List;

public class HeartbeatBufferedRecorder implements HeartbeatListener {
    private final HeartbeatListener mRecorder;

    private List<HeartbeatEvent> mBufferedHeartbeats;
    private boolean mFlushed = false;

    public HeartbeatBufferedRecorder(HeartbeatListener recorder) {
        mRecorder = recorder;
    }

    @Override
    public void onHeartbeat(HeartbeatEvent heartbeat) {
        if (!mFlushed) {
            synchronized (this) {
                if (addToBufferLocked(heartbeat)) {
                    return;
                }
            }
        }

        publishHeartbeat(heartbeat);
    }

    public void flush() {
        if (mFlushed) {
            // Already flushed, don't flush again.
            return;
        }

        synchronized (this) {
            flushBufferLocked();
        }
    }

    private boolean addToBufferLocked(HeartbeatEvent heartbeat) {
        if (mFlushed) {
            // Already flushed, don't add to buffer now
            return false;
        }

        if (mBufferedHeartbeats == null) {
            mBufferedHeartbeats = new LinkedList<>();
        }
        mBufferedHeartbeats.add(heartbeat);
        return true;
    }

    private void flushBufferLocked() {
        if (mBufferedHeartbeats != null) {
            for (HeartbeatEvent heartbeat : mBufferedHeartbeats) {
                publishHeartbeat(heartbeat);
            }
            mBufferedHeartbeats = null;
        }
        mFlushed = true;
    }

    private void publishHeartbeat(HeartbeatEvent heartbeat) {
        mRecorder.onHeartbeat(heartbeat);
    }
}
