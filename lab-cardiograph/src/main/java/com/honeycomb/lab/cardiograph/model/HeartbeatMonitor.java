package com.honeycomb.lab.cardiograph.model;

import android.content.Context;

import com.honeycomb.lib.common.AppCommon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeartbeatMonitor implements HeartbeatReceiver.Callback {

    public abstract static class Callback {

        protected void onHeartbeatSessionCreated(HeartbeatSession session) {
        }

        protected void onHeartbeat(HeartbeatSession session) {
        }
    }

    private static volatile HeartbeatMonitor sInstance;

    private HeartbeatReceiver mReceiver;
    private final Map<String, HeartbeatSession> mSessions = new HashMap<>();

    private List<Callback> mCallbacks = new ArrayList<>();

    private HeartbeatMonitor() {
        mReceiver = new HeartbeatReceiver();
    }

    public static HeartbeatMonitor getInstance() {
        if (sInstance == null) {
            synchronized (HeartbeatMonitor.class) {
                if (sInstance == null) {
                    sInstance = new HeartbeatMonitor();
                }
            }
        }
        return sInstance;
    }

    public void start() {
        start(AppCommon.getInstance().getApplicationContext());
    }

    public void start(Context context) {
        mReceiver.register(context, this);
    }

    public void stop() {
        mReceiver.unregister();
    }

    public Collection<HeartbeatSession> getHeartbeatSessions() {
        return Collections.unmodifiableCollection(mSessions.values());
    }

    public void addCallback(Callback callback) {
        if (callback != null && !mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
        }
    }

    public void removeCallback(Callback callback) {
        if (callback != null) {
            mCallbacks.remove(callback);
        }
    }

    @Override
    public void onReceiveHeartbeat(HeartbeatInfo heartbeat) {
        refreshHeartbeatSession(heartbeat);
    }

    private synchronized void refreshHeartbeatSession(HeartbeatInfo heartbeat) {
        HeartbeatSession session = mSessions.get(heartbeat.packageName);
        if (session == null) {
            session = new HeartbeatSession(heartbeat.packageName, heartbeat.interval);
            mSessions.put(session.getPackageName(), session);

            invokeSessionCreated(session);
        }
        if (session.getLastHeartbeatTimestamp() < heartbeat.timestamp) {
            session.setLastReceivedHeartbeat(heartbeat);

            invokeOnHeartbeat(session);
        }
    }

    private void invokeSessionCreated(HeartbeatSession session) {
        for (Callback callback : mCallbacks) {
            callback.onHeartbeatSessionCreated(session);
        }
    }

    private void invokeOnHeartbeat(HeartbeatSession session) {
        for (Callback callback : mCallbacks) {
            callback.onHeartbeat(session);
        }
    }
}
