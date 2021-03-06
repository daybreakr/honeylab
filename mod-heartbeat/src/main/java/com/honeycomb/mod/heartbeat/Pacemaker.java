package com.honeycomb.mod.heartbeat;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

class Pacemaker {

    public interface PacingCallback {

        void onPacing();
    }

    private final ScheduledExecutorService mScheduler;

    private long mInterval;
    private PacingCallback mCallback;

    private final AtomicBoolean mStarted = new AtomicBoolean(false);

    Pacemaker(long interval) {
        setInterval(interval);

        mScheduler = Executors.newSingleThreadScheduledExecutor();
    }

    long getInterval() {
        return mInterval;
    }

    void setInterval(long millis) {
        mInterval = millis;
    }

    void setPacingCallback(PacingCallback callback) {
        mCallback = callback;
    }

    void start() {
        if (mStarted.compareAndSet(false, true)) {
            pace();
        }
    }

    void stop() {
        mStarted.set(false);
    }

    void destroy() {
        stop();
        mScheduler.shutdown();
        mCallback = null;
    }

    boolean isStarted() {
        return mStarted.get() && mInterval > 0;
    }

    private void pace() {
        if (!isStarted()) {
            return;
        }

        // Since pacing might block for a while, schedule next first.
        mScheduler.schedule(new Runnable() {
            @Override
            public void run() {
                pace();
            }
        }, mInterval, TimeUnit.MILLISECONDS);

        onPacing();
    }

    private void onPacing() {
        if (mCallback != null) {
            try {
                mCallback.onPacing();
            } catch (Exception ignored) {
            }
        }
    }
}
