package com.honeycomb.mod.keepalive.wakeup.recorder;

import android.content.Context;

import com.honeycomb.sdk.common.AppCommon;
import com.honeycomb.lib.utilities.Action;
import com.honeycomb.lib.utilities.Switch;
import com.honeycomb.mod.keepalive.wakeup.Wakeup;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.List;

public class WakeupRecorder {
    private static volatile WakeupRecorder sInstance;

    private Switch mSwitch;

    private WakeupRecorder() {
        mSwitch = new Switch().onStart(new Action() {
            @Override
            public void onAction() {
                onStart();
            }
        });
    }

    public static WakeupRecorder getInstance() {
        if (sInstance == null) {
            synchronized (WakeupRecorder.class) {
                if (sInstance == null) {
                    sInstance = new WakeupRecorder();
                }
            }
        }
        return sInstance;
    }

    public void start() {
        mSwitch.start();
    }

    public void onStart() {
        final Wakeup wakeup = Wakeup.getInstance();

        WakeupRecorderRegistry.registerLogRecorder(wakeup);

        Context context = AppCommon.getInstance().getApplicationContext();
        AndPermission.with(context).runtime().permission(
                Permission.READ_EXTERNAL_STORAGE,
                Permission.WRITE_EXTERNAL_STORAGE)
                .onGranted(new com.yanzhenjie.permission.Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        WakeupRecorderRegistry.registerFileRecorder(wakeup);
                    }
                }).start();
    }
}
