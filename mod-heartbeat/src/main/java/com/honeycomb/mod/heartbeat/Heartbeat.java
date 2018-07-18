package com.honeycomb.mod.heartbeat;

import com.honeycomb.lib.utilities.SwitchShell;

public class Heartbeat extends SwitchShell implements HeartbeatPublisher, Pacemaker.PacingCallback {
    private static HeartbeatOptions sOptions = new HeartbeatOptions();

    private static volatile Heartbeat sInstance;

    private Pacemaker mPacemaker;
    private HeartbeatPublisher mPublisher;

    private long mLastHeartbeat;

    private Heartbeat(HeartbeatOptions options) {
        HeartbeatAssembler assembler = new HeartbeatAssembler(options);

        installPacemaker(assembler.providePacemaker());
        mPublisher = assembler.provideHeartbeatPublisher();

        assembler.registerHeartbeatListeners(this);
    }

    // Set before initialize
    public static void setOptions(HeartbeatOptions options) {
        if (options != null) {
            sOptions = options;
        }
    }

    public static Heartbeat getInstance() {
        if (sInstance == null) {
            synchronized (Heartbeat.class) {
                if (sInstance == null) {
                    sInstance = new Heartbeat(sOptions);
                }
            }
        }
        return sInstance;
    }

    public long getHeartbeatInterval() {
        return mPacemaker.getInterval();
    }

    private void installPacemaker(Pacemaker pacemaker) {
        if (pacemaker != null) {
            // Destroy old pacemaker first if exists.
            if (mPacemaker != null) {
                mPacemaker.destroy();
            }

            mPacemaker = pacemaker;
            mPacemaker.setPacingCallback(this);
        }
    }

    @Override
    protected void onStart() {
        mPacemaker.start();
    }

    @Override
    protected void onStop() {
        mPacemaker.stop();
    }

    @Override
    protected void onDestroy() {
        mPacemaker.destroy();

        clearHeartbeatListeners();
    }

    @Override
    public void addHeartbeatListener(HeartbeatListener listener) {
        mPublisher.addHeartbeatListener(listener);
    }

    @Override
    public void removeHeartbeatListener(HeartbeatListener listener) {
        mPublisher.removeHeartbeatListener(listener);
    }

    @Override
    public void clearHeartbeatListeners() {
        mPublisher.clearHeartbeatListeners();
    }

    @Override
    public void publishHeartbeatEvent(HeartbeatEvent heartbeat) {
        mPublisher.publishHeartbeatEvent(heartbeat);
    }

    @Override
    public void onPacing() {
        final long now = System.currentTimeMillis();
        // The delta time of first heartbeat is 0.
        final long dt = mLastHeartbeat > 0 ? now - mLastHeartbeat : 0;
        HeartbeatEvent heartbeat = new HeartbeatEvent(now, dt);

        publishHeartbeatEvent(heartbeat);

        mLastHeartbeat = now;
    }
}
