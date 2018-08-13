package com.honeycomb.mod.process.monitor;

import android.content.Context;

import com.honeycomb.lib.utilities.SwitchShell;
import com.honeycomb.sdk.common.AppCommon;

public class ProcessMonitor extends SwitchShell {
    private static final long CHECK_INTERVAL = 100;

    private ProcessMonitorThread mMonitorThread;

    private static volatile ProcessMonitor sInstance;

    private ProcessMonitor() {
    }

    public static ProcessMonitor getInstance() {
        if (sInstance == null) {
            synchronized (ProcessMonitor.class) {
                if (sInstance == null) {
                    sInstance = new ProcessMonitor();
                }
            }
        }
        return sInstance;
    }

    @Override
    protected void onStart() {
        Context context = AppCommon.getInstance().getApplicationContext();

        mMonitorThread = new ProcessMonitorThread(context, CHECK_INTERVAL);
        mMonitorThread.startMonitor();
    }

    @Override
    protected void onStop() {
        mMonitorThread.stopMonitor();
        mMonitorThread = null;
    }
}
