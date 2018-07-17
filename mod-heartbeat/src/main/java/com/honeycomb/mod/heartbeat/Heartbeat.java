package com.honeycomb.mod.heartbeat;

public class Heartbeat implements HeartbeatPublisher, Pacemaker.PacingCallback {
    private static HeartbeatOptions sOptions = new HeartbeatOptions();

    private static volatile Heartbeat sInstance;

    private Pacemaker mPacemaker;
    private final HeartbeatPublisher mPublisher;

    private long mLastHeartbeat;

    private Heartbeat(HeartbeatOptions options) {
        setPacemaker(new Pacemaker(options.heartbeatInterval));

        if (options.useBuffer) {
            mPublisher = new HeartbeatBufferPublisher();
        } else {
            mPublisher = new HeartbeatPublisherImpl();
        }
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
        return getPacemaker().getInterval();
    }

    public Pacemaker getPacemaker() {
        return mPacemaker;
    }

    public Heartbeat setPacemaker(Pacemaker pacemaker) {
        if (pacemaker != null) {
            // Destroy old pacemaker first if exists.
            if (mPacemaker != null) {
                mPacemaker.destroy();
            }

            mPacemaker = pacemaker;
            mPacemaker.setPacingCallback(this);
        }
        return this;
    }

    public void start() {
        mPacemaker.start();
    }

    public void stop() {
        mPacemaker.stop();
    }

    public void destroy() {
        clearHeartbeatListeners();
        mPacemaker.destroy();
    }

    public void flush() {
        if (mPublisher instanceof HeartbeatBufferPublisher) {
            ((HeartbeatBufferPublisher) mPublisher).flush();
        }
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
