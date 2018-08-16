package com.honeycomb.lib.tasks;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

class OnFailureDeliverer<TResult> implements TaskDeliverer<TResult> {
    private final Executor mExecutor;
    private OnFailureListener mListener;

    private final Object mLock = new Object();

    OnFailureDeliverer(@NonNull Executor executor, @NonNull OnFailureListener listener) {
        mExecutor = executor;
        mListener = listener;
    }

    @Override
    public void onComplete(final @NonNull Task<TResult> task) {
        if (!task.isSuccessful()) {
            synchronized (mLock) {
                if (mListener == null) {
                    return;
                }
            }

            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    synchronized (mLock) {
                        if (mListener != null) {
                            mListener.onFailure(task.getException());
                        }
                    }
                }
            });
        }
    }

    @Override
    public void cancel() {
        synchronized (mLock) {
            mListener = null;
        }
    }
}
