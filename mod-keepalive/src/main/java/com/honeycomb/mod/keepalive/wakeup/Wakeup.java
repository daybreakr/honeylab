package com.honeycomb.mod.keepalive.wakeup;

import com.honeycomb.lib.utilities.SwitchShell;
import com.honeycomb.mod.keepalive.wakeup.alarm.WakeupAlarm;

public class Wakeup extends SwitchShell implements WakeupPublisher {
    private static WakeupOptions sOptions = new WakeupOptions();

    private static volatile Wakeup sInstance;

    private WakeupAlarm mWakeupAlarm;
    private final WakeupPublisher mPublisher;

    private Wakeup(WakeupOptions options) {
        WakeupAssembler assembler = new WakeupAssembler(options);

        mWakeupAlarm = assembler.provideWakeupAlarm();
        mPublisher = assembler.provideWakeupPublisher();

        assembler.registerWakeupListeners(this);
    }

    // Set before initialize
    public static void setOptions(WakeupOptions options) {
        if (options != null) {
            sOptions = options;
        }
    }

    public static Wakeup getInstance() {
        if (sInstance == null) {
            synchronized (Wakeup.class) {
                if (sInstance == null) {
                    sInstance = new Wakeup(sOptions);
                }
            }
        }
        return sInstance;
    }

    @Override
    protected void onStart() {
        if (mWakeupAlarm != null) {
            mWakeupAlarm.set();
        }
    }

    @Override
    protected void onStop() {
        if (mWakeupAlarm != null) {
            mWakeupAlarm.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        mWakeupAlarm = null;

        clearWakeupListeners();
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

    public void onWakeup(long wakeupTime, String tag) {
        WakeupEvent event = new WakeupEvent(wakeupTime, tag);

        publishWakeupEvent(event);
    }
}
