package com.honeycomb.mod.keepalive.wakeup.recorder;

import com.honeycomb.mod.keepalive.wakeup.WakeupEvent;
import com.honeycomb.mod.keepalive.wakeup.WakeupListener;

import java.util.LinkedList;
import java.util.List;

public class WakeupBufferedRecorder implements WakeupListener {
    private final WakeupListener mRecorder;

    private List<WakeupEvent> mBufferedEvents;
    private boolean mFlushed = false;

    public WakeupBufferedRecorder(WakeupListener recorder) {
        mRecorder = recorder;
    }

    @Override
    public void onWakeup(WakeupEvent event) {
        if (!mFlushed) {
            synchronized (this) {
                if (addToBufferLocked(event)) {
                    return;
                }
            }
        }

        publishEvent(event);
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

    private boolean addToBufferLocked(WakeupEvent event) {
        if (mFlushed) {
            // Already flushed, don't add to buffer now
            return false;
        }

        if (mBufferedEvents == null) {
            mBufferedEvents = new LinkedList<>();
        }
        mBufferedEvents.add(event);
        return true;
    }

    private void flushBufferLocked() {
        if (mBufferedEvents != null) {
            for (WakeupEvent event : mBufferedEvents) {
                publishEvent(event);
            }
            mBufferedEvents = null;
        }
        mFlushed = true;
    }

    private void publishEvent(WakeupEvent event) {
        mRecorder.onWakeup(event);
    }
}
