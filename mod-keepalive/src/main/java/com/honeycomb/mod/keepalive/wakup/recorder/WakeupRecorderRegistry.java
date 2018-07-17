package com.honeycomb.mod.keepalive.wakup.recorder;

import com.honeycomb.mod.keepalive.wakup.WakeupPublisher;
import com.honeycomb.mod.keepalive.wakup.recorder.impl.WakeupFileRecorder;
import com.honeycomb.mod.keepalive.wakup.recorder.impl.WakeupLogcatPrinter;

public class WakeupRecorderRegistry {

    static void registerLogRecorder(WakeupPublisher publisher) {
        publisher.addWakeupListener(new WakeupLogcatPrinter());
    }

    static void registerFileRecorder(WakeupPublisher publisher) {
        publisher.addWakeupListener(new WakeupFileRecorder());
    }
}
