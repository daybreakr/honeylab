package com.honeycomb.mod.keepalive.wakeup;

import com.honeycomb.mod.keepalive.wakeup.alarm.WakeupAlarm;
import com.honeycomb.mod.keepalive.wakeup.recorder.WakeupFileRecorder;
import com.honeycomb.mod.keepalive.wakeup.recorder.WakeupLogcatPrinter;

public class WakeupAssembler {
    private final WakeupOptions mOptions;

    WakeupAssembler(WakeupOptions options) {
        mOptions = options;
    }

    WakeupPublisher provideWakeupPublisher() {
        return new WakeupPublisherImpl();
    }

    WakeupAlarm provideWakeupAlarm() {
        if (mOptions.enableWakeupAlarm) {
            return new WakeupAlarm(mOptions.wakeupAlarmOptions);
        }
        return null;
    }

    void registerWakeupListeners(WakeupPublisher publisher) {
        final WakeupOptions.RecorderOptions options = mOptions.recorderOptions;

        if (options.enableLog) {
            publisher.addWakeupListener(provideLogRecorder());
        }

        if (options.saveToFile) {
            publisher.addWakeupListener(provideFileRecorder());
        }
    }

    private WakeupListener provideLogRecorder() {
        return new WakeupLogcatPrinter();
    }

    private WakeupListener provideFileRecorder() {
        return new WakeupFileRecorder();
    }
}
