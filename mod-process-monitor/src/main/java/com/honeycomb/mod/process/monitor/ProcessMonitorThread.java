package com.honeycomb.mod.process.monitor;

import android.content.Context;
import android.util.Log;

class ProcessMonitorThread extends Thread {
    private static final String TAG = "ProcessMonitor";

    private final Context mContext;
    private final ForegroundAppDetector mDetector;
    private final long mInterval;

    private boolean mStopped;

    private String mTopPackage;

    ProcessMonitorThread(Context context, ForegroundAppDetector detector, long interval) {
        mContext = context.getApplicationContext();
        mDetector = detector;
        mInterval = Math.max(10, interval);
    }

    void startMonitor() {
        mStopped = false;
        start();
    }

    void stopMonitor() {
        mStopped = true;
    }

    @Override
    public void run() {
        try {
            while (!mStopped) {
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

        printTopPackage(last);
    }

    private void printTopPackage(String last) {
        if (last == null && mTopPackage != null) {
            Log.i(TAG, "Top package: " + mTopPackage);
        } else if (last != null && !last.equals(mTopPackage)) {
            Log.i(TAG, "Top package changed from " + last + " to " + mTopPackage);
        }
    }
}
