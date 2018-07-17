package com.honeycomb.mod.keepalive.wakeup;

import java.util.LinkedList;
import java.util.List;

public class WakeupPublisherImpl implements WakeupPublisher {
    private final List<WakeupListener> mListeners = new LinkedList<>();

    @Override
    public void addWakeupListener(WakeupListener listener) {
        if (listener != null && !mListeners.contains(listener)) {
            mListeners.add(listener);
        }
    }

    @Override
    public void removeWakeupListener(WakeupListener listener) {
        if (listener != null) {
            mListeners.remove(listener);
        }
    }

    @Override
    public void clearWakeupListeners() {
        mListeners.clear();
    }

    @Override
    public void publishWakeupEvent(WakeupEvent wakeupEvent) {
        if (wakeupEvent == null) {
            return;
        }

        for (WakeupListener listener : mListeners) {
            try {
                listener.onWakeup(wakeupEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
