package com.honeycomb.lib.utilities;

import java.util.concurrent.atomic.AtomicBoolean;

public class Switch {
    private final AtomicBoolean mStarted = new AtomicBoolean(false);
    private final AtomicBoolean mDestroyed = new AtomicBoolean(false);

    private Action mOnStart;
    private Action mOnStop;
    private Action mOnDestroy;

    public Switch onStart(Action onStart) {
        mOnStart = onStart;
        return this;
    }

    public Switch onStop(Action onStop) {
        mOnStop = onStop;
        return this;
    }

    public Switch onDestroy(Action onDestroy) {
        mOnDestroy = onDestroy;
        return this;
    }

    public boolean start() {
        if (isDestroyed()) {
            return false;
        }
        if (mStarted.compareAndSet(false, true)) {
            mOnStart.onAction();
            return true;
        }
        return false;
    }

    public boolean stop() {
        if (isDestroyed()) {
            return false;
        }
        if (mStarted.compareAndSet(true, false)) {
            mOnStop.onAction();
            return true;
        }
        return false;
    }

    public boolean destroy() {
        if (mDestroyed.compareAndSet(false, true)) {
            mOnDestroy.onAction();

            mOnStart = null;
            mOnStop = null;
            mOnDestroy = null;

            return true;
        }
        return false;
    }

    public boolean isStarted() {
        return mStarted.get();
    }

    public boolean isDestroyed() {
        return mDestroyed.get();
    }
}
