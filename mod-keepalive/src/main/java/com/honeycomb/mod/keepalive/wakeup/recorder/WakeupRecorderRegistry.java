package com.honeycomb.mod.keepalive.wakeup.recorder;

import com.honeycomb.mod.keepalive.wakeup.WakeupPublisher;
import com.honeycomb.mod.keepalive.wakeup.recorder.impl.WakeupFileRecorder;
import com.honeycomb.mod.keepalive.wakeup.recorder.impl.WakeupLogcatPrinter;

public class WakeupRecorderRegistry {

    static void registerLogRecorder(WakeupPublisher publisher) {
        publisher.addWakeupListener(new WakeupLogcatPrinter());
    }

    static void registerFileRecorder(WakeupPublisher publisher) {
        publisher.addWakeupListener(new WakeupFileRecorder());
    }
}
