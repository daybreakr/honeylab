package com.honeycomb.lab.lifecycle;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

public class LifecycleAwareComponent implements LifecycleObserver {
    private static final String TAG = Constants.TAG;

    private LifecycleLogger mLogger = LifecycleLogger.buildUpon().tag(TAG)
            .prefix("  ")
            .suffix(" - component")
            .build();

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    public void onLifecycleEvent(LifecycleOwner owner, Lifecycle.Event event) {
        mLogger.log("LifecycleEvent", event, owner.getLifecycle().getCurrentState());
    }
}
