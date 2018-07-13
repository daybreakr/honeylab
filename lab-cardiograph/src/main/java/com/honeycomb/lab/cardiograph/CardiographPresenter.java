package com.honeycomb.lab.cardiograph;

import com.honeycomb.lab.cardiograph.model.HeartbeatMonitor;
import com.honeycomb.lab.cardiograph.model.HeartbeatSession;

import java.util.Collection;

public class CardiographPresenter {
    private final ICardiographView mView;
    private final HeartbeatMonitor mHeartbeatMonitor;

    CardiographPresenter(ICardiographView view, HeartbeatMonitor heartbeatMonitor) {
        mView = view;
        mHeartbeatMonitor = heartbeatMonitor;
    }

    public void start() {
        Collection<HeartbeatSession> sessions = mHeartbeatMonitor.getHeartbeatSessions();
        mView.showHeartbeatSessions(sessions);

        mHeartbeatMonitor.addCallback(mHeartbeatSessionCallback);
    }

    public void stop() {
        mHeartbeatMonitor.removeCallback(mHeartbeatSessionCallback);
    }

    private HeartbeatMonitor.Callback mHeartbeatSessionCallback = new HeartbeatMonitor.Callback() {

        @Override
        protected void onHeartbeat(HeartbeatSession session) {
            mView.showHeartbeat(session);
        }
    };
}
