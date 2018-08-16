package com.honeycomb.lib.tasks;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

class OnSuccessDeliverer<TResult> implements TaskDeliverer<TResult> {
    private final Executor mExecutor;
    private OnSuccessListener<? super TResult> mListener;

    private final Object mLock = new Object();

    OnSuccessDeliverer(@NonNull Executor executor,
                       @NonNull OnSuccessListener<? super TResult> listener) {
        mExecutor = executor;
        mListener = listener;
    }

    @Override
    public void onComplete(final @NonNull Task<TResult> task) {
        if (task.isSuccessful()) {
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
                            mListener.onSuccess(task.getResult());
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
