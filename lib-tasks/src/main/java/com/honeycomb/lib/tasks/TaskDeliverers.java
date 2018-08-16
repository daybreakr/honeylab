package com.honeycomb.lib.tasks;

import android.support.annotation.NonNull;

import java.util.ArrayDeque;
import java.util.Queue;

class TaskDeliverers<TResult> {
    private Queue<TaskDeliverer<TResult>> mDeliverers;
    private boolean mDelivered;

    private final Object mLock = new Object();

    public final void addDeliverer(@NonNull TaskDeliverer<TResult> deliverer) {
        synchronized (mLock) {
            if (mDeliverers == null) {
                mDeliverers = new ArrayDeque<>();
            }

            mDeliverers.add(deliverer);
        }
    }

    public final void deliverComplete(@NonNull Task<TResult> task) {
        synchronized (mLock) {
            if (mDeliverers == null || mDelivered) {
                return;
            }

            mDelivered = true;
        }

        while (true) {
            TaskDeliverer<TResult> deliverer;
            synchronized (mLock) {
                if ((deliverer = mDeliverers.poll()) == null) {
                    mDelivered = false;
                    return;
                }
            }

            deliverer.onComplete(task);
        }
    }
}
