package com.honeycomb.mod.heartbeat.recorder;

import android.content.Context;

import com.honeycomb.lib.common.AppCommon;
import com.honeycomb.mod.heartbeat.Heartbeat;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.List;

public class HeartbeatRecorder {
    private static HeartbeatRecorderOptions sOptions = new HeartbeatRecorderOptions();

    private static volatile HeartbeatRecorder sInstance;

    private final HeartbeatRecorderOptions mOptions;

    private HeartbeatRecorder(HeartbeatRecorderOptions options) {
        mOptions = options;
    }

    // Set before initialize
    public static void setOptions(HeartbeatRecorderOptions options) {
        if (options != null) {
            sOptions = options;
        }
    }

    public static HeartbeatRecorder getInstance() {
        if (sInstance == null) {
            synchronized (HeartbeatRecorder.class) {
                if (sInstance == null) {
                    sInstance = new HeartbeatRecorder(sOptions);
                }
            }
        }
        return sInstance;
    }

    public void start() {
        final Heartbeat heartbeat = Heartbeat.getInstance();

        if (mOptions.printLog) {
            HeartbeatRecorderRegistry.registerLogRecorder(heartbeat);
        }
        if (mOptions.sendHeartbeatBroadcast) {
            HeartbeatRecorderRegistry.registerBroadcastRecorder(heartbeat);
        }
        if (mOptions.saveToFile) {
            Context context = AppCommon.getInstance().getApplicationContext();
            AndPermission.with(context).runtime().permission(
                    Permission.READ_EXTERNAL_STORAGE,
                    Permission.WRITE_EXTERNAL_STORAGE)
                    .onGranted(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> data) {
                            HeartbeatRecorderRegistry.registerFileRecorder(heartbeat);

                            heartbeat.flush();
                        }
                    }).start();
        } else {
            heartbeat.flush();
        }
    }
}
