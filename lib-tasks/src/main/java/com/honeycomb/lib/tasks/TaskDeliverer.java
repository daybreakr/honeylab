package com.honeycomb.lib.tasks;

import android.support.annotation.NonNull;

interface TaskDeliverer<TResult> {

    void onComplete(@NonNull Task<TResult> task);

    void cancel();
}
