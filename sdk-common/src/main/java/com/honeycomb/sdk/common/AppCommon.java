package com.honeycomb.sdk.common;

import android.annotation.SuppressLint;
import android.content.Context;

public class AppCommon {
    private static final String MOD_REGISTRY_CLASS = "com.honeycomb.sdk.ModRegistry";

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
        try {
            Class<?> clazz = Class.forName(MOD_REGISTRY_CLASS);
            if (AppCommonRegistry.class.isAssignableFrom(clazz)) {
                AppCommonRegistry registry = (AppCommonRegistry) clazz.newInstance();
                registry.onConfigure(mApplicationContext);
                registry.onStart(mApplicationContext);
            }
        } catch (ClassNotFoundException ignored) {
            // ignored if no registry was found.
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
