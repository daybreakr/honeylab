package com.honeycomb.sdk.common;

import android.annotation.SuppressLint;
import android.content.Context;

public class AppCommon {
    @SuppressLint("StaticFieldLeak")
    // AppCommon represents the application, it's OK to hold the applications Context here.
    private static volatile AppCommon sInstance;

    private static final Object sLock = new Object();

    private final Context mApplicationContext;

    private AppCommon(Context context) {
        mApplicationContext = context.getApplicationContext();
    }

    public static AppCommon getInstance() {
        synchronized (sLock) {
            if (sInstance == null) {
                throw new IllegalStateException("AppCommon is not initialized in the process. "
                        + "Make sure to call AppCommon.initialize(Context) first.");
            }
            return sInstance;
        }
    }

    public static void initialize(Context context) {
        synchronized (sLock) {
            if (sInstance != null) {
                return;
            }
            sInstance = new AppCommon(context);
        }

        sInstance.initialize();
    }

    public Context getApplicationContext() {
        return mApplicationContext;
    }

    private void initialize() {
        // TODO: Use annotation processor to generate the AppCommonRegistry class and start it.
    }
}
