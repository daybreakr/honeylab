package com.honeycomb.mod.keepalive.debug;

import android.content.Context;

public class Sdk {
    private static volatile Sdk sInstance;
    private static final Object sLock = new Object();

    private final Context mApplicationContext;

    private Sdk(Context context) {
        mApplicationContext = context.getApplicationContext();
    }

    public static Sdk getInstance() {
        synchronized (sLock) {
            if (sInstance == null) {
                throw new IllegalStateException("Business SDK is not initialized in the process. "
                        + "Make sure to call Sdk.initialize(Context) first.");
            }
            return sInstance;
        }
    }

    public static void initialize(Context context) {
        synchronized (sLock) {
            if (sInstance != null) {
                return;
            }
            sInstance = new Sdk(context);
        }

        sInstance.initialize();
    }

    public Context getApplicationContext() {
        return mApplicationContext;
    }

    private void initialize() {
        SdkStartupRegistry.start();
    }
}
