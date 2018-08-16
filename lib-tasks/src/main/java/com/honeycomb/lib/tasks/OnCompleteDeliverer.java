package com.honeycomb.lib.tasks;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

class OnCompleteDeliverer<TResult> implements TaskDeliverer<TResult> {
    private final Executor mExecutor;
    private OnCompleteListener<TResult> mListener;

    private final Object mLock = new Object();

    OnCompleteDeliverer(@NonNull Executor executor,
                        @NonNull OnCompleteListener<TResult> listener) {
        mExecutor = executor;
        mListener = listener;
    }

    @Override
    public void onComplete(final @NonNull Task<TResult> task) {
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
                        mListener.onComplete(task);
                    }
                }
            }
        });
    }

    @Override
    public void cancel() {
        synchronized (mLock) {
            mListener = null;
        }
    }
}
