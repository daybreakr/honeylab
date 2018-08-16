package com.honeycomb.mod.process.monitor.impl;

import android.content.Context;
import android.text.TextUtils;

import com.honeycomb.mod.process.monitor.ProcessMonitorThread;

public class ForegroundAppDetectorMonitor extends ProcessMonitorThread {
    private final Context mContext;
    private final ForegroundAppDetector mDetector;
    private final long mInterval;

    private String mTopPackage;

    public ForegroundAppDetectorMonitor(Context context, ForegroundAppDetector detector, long interval) {
        mContext = context.getApplicationContext();
        mDetector = detector;
        mInterval = Math.max(10, interval);
    }

    @Override
    public void run() {
        try {
            while (!isStopped()) {
                mainLoop();
                Thread.sleep(mInterval);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mainLoop() {
        String last = mTopPackage;
        mTopPackage = mDetector.getForegroundPackage(mContext);
        boolean changed = !TextUtils.equals(last, mTopPackage);
        if (changed) {
            invokeForegroundProcessChanged(last, mTopPackage);
        }
    }
}
