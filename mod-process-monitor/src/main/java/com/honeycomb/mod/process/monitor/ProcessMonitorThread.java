package com.honeycomb.mod.process.monitor;

public abstract class ProcessMonitorThread extends Thread {

    public interface OnForegroundProcessChangedListener {

        void onForegroundProcessChanged(String oldProcessName, String newProcessName);
    }

    private OnForegroundProcessChangedListener mListener;

    private boolean mStopped;

    public void setOnProcessChangeListener(OnForegroundProcessChangedListener listener) {
        mListener = listener;
    }

    public void startMonitor() {
        mStopped = false;
        start();
    }

    public void stopMonitor() {
        mStopped = true;
    }

    public boolean isStopped() {
        return mStopped;
    }

    protected void invokeForegroundProcessChanged(String oldProcessName, String newProcessName) {
        if (mListener != null) {
            mListener.onForegroundProcessChanged(oldProcessName, newProcessName);
        }
    }
}
