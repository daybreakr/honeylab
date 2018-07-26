package com.honeycomb.mod.keepalive.keepalive;

import android.content.Context;

import com.honeycomb.lib.utilities.SwitchShell;
import com.honeycomb.mod.keepalive.keepalive.activity.OnePixelActivityController;
import com.honeycomb.mod.keepalive.keepalive.service.KeepAliveForegroundService;
import com.honeycomb.mod.keepalive.keepalive.service.KeepAliveService;
import com.honeycomb.sdk.common.AppCommon;

public class KeepAlive extends SwitchShell {
    private static KeepAliveOptions sOptions = new KeepAliveOptions();

    private static volatile KeepAlive sInstance;

    private KeepAliveOptions mOptions;

    private OnePixelActivityController mOnePixelActivityController;

    private KeepAlive(KeepAliveOptions options) {
        mOptions = options;

        KeepAliveAssembler assembler = new KeepAliveAssembler(options);

        mOnePixelActivityController = assembler.provideOnePixelActivityController();
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

        if (mOptions.enableForegroundService) {
            KeepAliveForegroundService.start(context);
        }

        if (mOptions.enableBackgroundService) {
            KeepAliveService.start(context);
        }

        if (mOnePixelActivityController != null) {
            mOnePixelActivityController.start();
        }
    }

    @Override
    protected void onStop() {
        Context context = AppCommon.getInstance().getApplicationContext();

        if (mOptions.enableForegroundService) {
            KeepAliveForegroundService.stop(context);
        }

        if (mOptions.enableBackgroundService) {
            KeepAliveService.stop(context);
        }

        if (mOnePixelActivityController != null) {
            mOnePixelActivityController.stop();
        }
    }
}
