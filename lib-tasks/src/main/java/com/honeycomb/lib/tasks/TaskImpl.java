package com.honeycomb.lib.tasks;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.Executor;

class TaskImpl<TResult> extends Task<TResult> {
    private TaskDeliverers<TResult> mDeliverers = new TaskDeliverers<>();
    private boolean mComplete;
    private TResult mResult;
    private Exception mException;

    private final Object mLock = new Object();

    @Override
    public boolean isComplete() {
        synchronized (mLock) {
            return mComplete;
        }
    }

    @Override
    public boolean isSuccessful() {
        synchronized (mLock) {
            return mComplete && mException == null;
        }
    }

    @Override
    public TResult getResult() {
        synchronized (mLock) {
            requireComplete();
            if (mException != null) {
                throw new RuntimeException(mException);
            } else {
                return mResult;
            }
        }
    }

    @Override
    public <X extends Throwable> TResult getResult(@NonNull Class<X> exception) throws X {
        synchronized (mLock) {
            requireComplete();
            if (exception.isInstance(mException)) {
                throw exception.cast(mException);
            } else if (mException != null) {
                throw new RuntimeException(mException);
            } else {
                return mResult;
            }
        }
    }

    @Nullable
    @Override
    public Exception getException() {
        synchronized (mLock) {
            return mException;
        }
    }

    @NonNull
    @Override
    public Task<TResult> addOnSuccessListener(@NonNull OnSuccessListener<? super TResult> listener) {
        return addOnSuccessListener(TaskExecutors.DEFAULT, listener);
    }

    @NonNull
    @Override
    public Task<TResult> addOnSuccessListener(@NonNull Executor executor,
                                              @NonNull OnSuccessListener<? super TResult> listener) {
        mDeliverers.addDeliverer(new OnSuccessDeliverer<>(executor, listener));
        tryDeliverComplete();
        return this;
    }

    @NonNull
    @Override
    public Task<TResult> addOnFailureListener(@NonNull OnFailureListener listener) {
        return addOnFailureListener(TaskExecutors.DEFAULT, listener);
    }

    @NonNull
    @Override
    public Task<TResult> addOnFailureListener(@NonNull Executor executor,
                                              @NonNull OnFailureListener listener) {
        mDeliverers.addDeliverer(new OnFailureDeliverer<TResult>(executor, listener));
        tryDeliverComplete();
        return this;
    }

    @NonNull
    @Override
    public Task<TResult> addOnCompleteListener(@NonNull OnCompleteListener<TResult> listener) {
        return addOnCompleteListener(TaskExecutors.DEFAULT, listener);
    }

    @NonNull
    @Override
    public Task<TResult> addOnCompleteListener(@NonNull Executor executor,
                                               @NonNull OnCompleteListener<TResult> listener) {
        mDeliverers.addDeliverer(new OnCompleteDeliverer<>(executor, listener));
        tryDeliverComplete();
        return this;
    }

    public void setResult(TResult result) {
        synchronized (mLock) {
            requireNotComplete();
            mComplete = true;
            mResult = result;
        }

        mDeliverers.deliverComplete(this);
    }

    public boolean trySetResult(TResult result) {
        synchronized (mLock) {
            if (mComplete) {
                return false;
            }

            mComplete = true;
            mResult = result;
        }

        mDeliverers.deliverComplete(this);
        return true;
    }

    public void setException(@NonNull Exception exception) {
        synchronized (mLock) {
            requireNotComplete();
            mComplete = true;
            mException = exception;
        }

        mDeliverers.deliverComplete(this);
    }

    public boolean trySetException(@NonNull Exception exception) {
        synchronized (mLock) {
            if (mComplete) {
                return false;
            }

            mComplete = true;
            mException = exception;
        }

        mDeliverers.deliverComplete(this);
        return true;
    }

    private void requireComplete() {
        if (!isComplete()) {
            throw new IllegalStateException("Task is not yet complete");
        }
    }

    private void requireNotComplete() {
        if (isComplete()) {
            throw new IllegalStateException("Task is already complete");
        }
    }

    private void tryDeliverComplete() {
        synchronized (mLock) {
            if (!mComplete) {
                return;
            }
        }

        mDeliverers.deliverComplete(this);
    }
}
