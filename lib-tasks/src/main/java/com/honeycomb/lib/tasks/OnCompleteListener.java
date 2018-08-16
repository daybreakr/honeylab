package com.honeycomb.lib.tasks;

public interface OnCompleteListener<TResult> {

    void onComplete(Task<TResult> task);
}
