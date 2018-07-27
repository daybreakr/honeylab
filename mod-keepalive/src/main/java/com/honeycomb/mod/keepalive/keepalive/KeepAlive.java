package com.honeycomb.mod.keepalive.keepalive;

import android.content.Context;

import com.honeycomb.lib.utilities.SwitchShell;
import com.honeycomb.mod.keepalive.keepalive.background.KeepAliveBackgroundService;
import com.honeycomb.mod.keepalive.keepalive.bound.KeepAliveBoundServiceController;
import com.honeycomb.mod.keepalive.keepalive.foreground.KeepAliveForegroundService;
import com.honeycomb.mod.keepalive.keepalive.foreground.OnePixelActivityController;
import com.honeycomb.sdk.common.AppCommon;

public class KeepAlive extends SwitchShell {
    private static KeepAliveOptions sOptions = new KeepAliveOptions();

    private static volatile KeepAlive sInstance;

    private KeepAliveOptions mOptions;
    private KeepAliveControls mControls;

    private OnePixelActivityController mOnePixelActivityController;
    private KeepAliveBoundServiceController mBoundServiceController;

    private KeepAlive(KeepAliveOptions options) {
        KeepAliveAssembler assembler = new KeepAliveAssembler(options);
        mOptions = options;
        mControls = assembler.provideControls();

        mOnePixelActivityController = assembler.provideOnePixelActivityController();
        mBoundServiceController = assembler.provideBoundServiceController();

        if (mControls != null) {
            mControls.register();
        }
    }

    // Set before initialize
    public static void setOptions(KeepAliveOptions options) {
        if (options != null) {
            sOptions = options;
        }
    }

    public static KeepAlive getInstance() {
        if (sInstance == null) {
            synchronized (KeepAlive.class) {
                if (sInstance == null) {
                    sInstance = new KeepAlive(sOptions);
                }
            }
        }
        return sInstance;
    }

    @Override
    protected void onStart() {
        Context context = AppCommon.getInstance().getApplicationContext();

        if (mOnePixelActivityController != null) {
            mOnePixelActivityController.start();
        }

        if (mOptions.enableForegroundService) {
            KeepAliveForegroundService.start(context);
        }

        if (mBoundServiceController != null) {
            mBoundServiceController.start();
        }

        if (mOptions.enableBackgroundService) {
            KeepAliveBackgroundService.start(context);
        }
    }

    @Override
    protected void onStop() {
        Context context = AppCommon.getInstance().getApplicationContext();

        if (mOnePixelActivityController != null) {
            mOnePixelActivityController.stop();
        }

        if (mOptions.enableForegroundService) {
            KeepAliveForegroundService.stop(context);
        }

        if (mBoundServiceController != null) {
            mBoundServiceController.stop();
        }

        if (mOptions.enableBackgroundService) {
            KeepAliveBackgroundService.stop(context);
        }
    }

    @Override
    protected void onDestroy() {
        if (mControls != null) {
            mControls.unregister();
        }
    }
}
