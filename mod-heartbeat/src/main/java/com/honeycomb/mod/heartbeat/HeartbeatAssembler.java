package com.honeycomb.mod.heartbeat;

import com.honeycomb.mod.heartbeat.recorder.HeartbeatBroadcastSender;
import com.honeycomb.mod.heartbeat.recorder.HeartbeatFileRecorder;
import com.honeycomb.mod.heartbeat.recorder.HeartbeatLogcatPrinter;

class HeartbeatAssembler {
    private final HeartbeatOptions mOptions;

    HeartbeatAssembler(HeartbeatOptions options) {
        mOptions = options;
    }

    Pacemaker providePacemaker() {
        return new Pacemaker(mOptions.heartbeatInterval);
    }

    HeartbeatPublisher provideHeartbeatPublisher() {
        return new HeartbeatPublisherImpl();
    }

    void registerHeartbeatListeners(HeartbeatPublisher publisher) {
        final HeartbeatOptions.RecorderOptions options = mOptions.recorderOptions;

        if (options.enableLog) {
            publisher.addHeartbeatListener(provideLogRecorder());
        }

        if (options.enableBroadcast) {
            publisher.addHeartbeatListener(provideBroadcastRecorder());
        }

        if (options.saveToFile) {
            publisher.addHeartbeatListener(provideFileRecorder());
        }
    }

    private HeartbeatListener provideLogRecorder() {
        return new HeartbeatLogcatPrinter();
    }

    private HeartbeatListener provideBroadcastRecorder() {
        return new HeartbeatBroadcastSender();
    }

    private HeartbeatListener provideFileRecorder() {
        return new HeartbeatFileRecorder();
    }
}
