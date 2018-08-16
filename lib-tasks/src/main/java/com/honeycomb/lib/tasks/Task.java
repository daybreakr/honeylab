package com.honeycomb.lib.tasks;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.Executor;

public abstract class Task<TResult> {

    public abstract boolean isComplete();

    public abstract boolean isSuccessful();

    public abstract TResult getResult();

    public abstract <X extends Throwable> TResult getResult(@NonNull Class<X> exception) throws X;

    @Nullable
    public abstract Exception getException();

    @NonNull
    public abstract Task<TResult> addOnSuccessListener(@NonNull OnSuccessListener<? super TResult> listener);

    @NonNull
    public abstract Task<TResult> addOnSuccessListener(@NonNull Executor executor,
                                                       @NonNull OnSuccessListener<? super TResult> listener);

    @NonNull
    public abstract Task<TResult> addOnFailureListener(@NonNull OnFailureListener listener);

    @NonNull
    public abstract Task<TResult> addOnFailureListener(@NonNull Executor executor,
                                                       @NonNull OnFailureListener listener);

    @NonNull
    public Task<TResult> addOnCompleteListener(@NonNull OnCompleteListener<TResult> listener) {
        throw new UnsupportedOperationException("addOnCompleteListener is not implemented.");
    }

    @NonNull
    public Task<TResult> addOnCompleteListener(@NonNull Executor executor,
                                               @NonNull OnCompleteListener<TResult> listener) {
        throw new UnsupportedOperationException("addOnCompleteListener is not implemented.");
    }
}
