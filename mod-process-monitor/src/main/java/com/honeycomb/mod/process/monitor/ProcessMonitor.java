package com.honeycomb.mod.process.monitor;

import com.honeycomb.lib.utilities.SwitchShell;

public class ProcessMonitor extends SwitchShell {
    private static ProcessMonitorOptions sOptions = new ProcessMonitorOptions();

    private static volatile ProcessMonitor sInstance;

    private final ProcessMonitorAssembler mAssembler;
    private ProcessMonitorThread mMonitorThread;

    private ProcessMonitor(ProcessMonitorOptions options) {
        mAssembler = new ProcessMonitorAssembler(options);
    }

    public static void setOptions(ProcessMonitorOptions options) {
        if (options != null) {
            sOptions = options;
        }
    }

    public static ProcessMonitor getInstance() {
        if (sInstance == null) {
            synchronized (ProcessMonitor.class) {
                if (sInstance == null) {
                    sInstance = new ProcessMonitor(sOptions);
                }
            }
        }
        return sInstance;
    }

    @Override
    protected void onStart() {
        mMonitorThread = mAssembler.provideMonitorThread();
        mMonitorThread.startMonitor();
    }

    @Override
    protected void onStop() {
        mMonitorThread.stopMonitor();
        mMonitorThread = null;
    }
}
