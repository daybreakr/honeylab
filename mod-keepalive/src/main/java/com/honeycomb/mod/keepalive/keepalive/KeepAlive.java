package com.honeycomb.mod.keepalive.keepalive;

import android.content.Context;

import com.honeycomb.lib.common.AppCommon;
import com.honeycomb.lib.utilities.Action;
import com.honeycomb.lib.utilities.Switch;
import com.honeycomb.mod.keepalive.services.KeepAliveService;

public class KeepAlive {
    private static volatile KeepAlive sInstance;

    private final Switch mSwitch;

    private KeepAlive() {
        mSwitch = new Switch().onStart(new Action() {
            @Override
            public void onAction() {
                onStart();
            }
        }).onStop(new Action() {
            @Override
            public void onAction() {
                onStop();
            }
        });
    }

    public static KeepAlive getInstance() {
        if (sInstance == null) {
            synchronized (KeepAlive.class) {
                if (sInstance == null) {
                    sInstance = new KeepAlive();
                }
            }
        }
        return sInstance;
    }

    public void start() {
        mSwitch.start();
    }

    public void stop() {
        mSwitch.stop();
    }

    private void onStart() {
        Context context = AppCommon.getInstance().getApplicationContext();

        KeepAliveService.start(context);
    }

    private void onStop() {
        Context context = AppCommon.getInstance().getApplicationContext();

        KeepAliveService.stop(context);
    }
}
