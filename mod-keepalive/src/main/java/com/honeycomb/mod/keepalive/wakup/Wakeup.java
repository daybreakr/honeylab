package com.honeycomb.mod.keepalive.wakup;

import com.honeycomb.lib.utilities.Action;
import com.honeycomb.lib.utilities.Switch;
import com.honeycomb.mod.keepalive.wakup.recorder.WakeupRecorder;

public class Wakeup implements WakeupPublisher {
    private static volatile Wakeup sInstance;

    private final WakeupPublisher mPublisher;
    private WakeupRecorder mRecorder;
    private final Switch mSwitch;

    private WakeupAlarm mWakeupAlarm;

    private Wakeup() {
        mPublisher = new WakeupPublisherImpl();
        mRecorder = WakeupRecorder.getInstance();
        mSwitch = new Switch().onStart(new Action() {
            @Override
            public void onAction() {
                onStart();
            }
        }).onStop(new Action() {
            @Override
            public void onAction() {
                onStop();
            }
        }).onDestroy(new Action() {
            @Override
            public void onAction() {
                onDestroy();
            }
        });

        mWakeupAlarm = new WakeupAlarm();
    }

    public static Wakeup getInstance() {
        if (sInstance == null) {
            synchronized (Wakeup.class) {
                if (sInstance == null) {
                    sInstance = new Wakeup();
                }
            }
        }
        return sInstance;
    }

    public void start() {
        mSwitch.start();
    }

    public void stop() {
        mSwitch.stop();
    }

    public void destroy() {
        mSwitch.destroy();
    }

    private void onStart() {
        if (mRecorder != null) {
            mRecorder.start();
        }

        if (mWakeupAlarm != null) {
            mWakeupAlarm.set();
        }
    }

    private void onStop() {
        if (mWakeupAlarm != null) {
            mWakeupAlarm.cancel();
        }
    }

    private void onDestroy() {
        clearWakeupListeners();
        mWakeupAlarm = null;
    }

    @Override
    public void addWakeupListener(WakeupListener listener) {
        mPublisher.addWakeupListener(listener);
    }

    @Override
    public void removeWakeupListener(WakeupListener listener) {
        mPublisher.removeWakeupListener(listener);
    }

    @Override
    public void clearWakeupListeners() {
        mPublisher.clearWakeupListeners();
    }

    @Override
    public void publishWakeupEvent(WakeupEvent wakeupEvent) {
        mPublisher.publishWakeupEvent(wakeupEvent);
    }
}
