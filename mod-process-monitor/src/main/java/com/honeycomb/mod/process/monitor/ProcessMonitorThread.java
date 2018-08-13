package com.honeycomb.mod.process.monitor;

import android.content.Context;
import android.util.Log;

class ProcessMonitorThread extends Thread {
    private static final String TAG = "ProcessMonitor";

    private final Context mContext;
    private final long mInterval;

    private boolean mStopped;

    private String mTopProcess;

    ProcessMonitorThread(Context context, long interval) {
        mContext = context.getApplicationContext();
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
        String last = mTopProcess;
        mTopProcess = ProcessUtils.getTopProcess(mContext);

        printTopProcess(last);
    }

    private void printTopProcess(String last) {
        if (last == null) {
            Log.i(TAG, "Top process: " + mTopProcess);
        } else if (!last.equals(mTopProcess)) {
            Log.i(TAG, "Top process changed from " + last + " to " + mTopProcess);
        }
    }
}
