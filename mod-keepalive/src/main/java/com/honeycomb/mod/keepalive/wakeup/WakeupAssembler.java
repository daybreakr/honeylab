package com.honeycomb.mod.keepalive.wakeup;

import android.os.Build;

import com.honeycomb.mod.keepalive.wakeup.alarm.WakeupAlarm;
import com.honeycomb.mod.keepalive.wakeup.job.WakeupJob;
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

    WakeupJob provideWakeupJob() {
        if (mOptions.enableWakeupJob && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new WakeupJob(mOptions.wakeupJobOptions);
        }
        return null;
    }

    void registerWakeupListeners(WakeupPublisher publisher) {
        WakeupOptions.RecorderOptions options = mOptions.recorderOptions;
        if (options == null) {
            options = new WakeupOptions.RecorderOptions();
        }

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
