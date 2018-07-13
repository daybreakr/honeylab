package com.honeycomb.lab.cardiograph;

import com.honeycomb.lab.cardiograph.model.HeartbeatSession;

import java.util.Collection;

public interface ICardiographView {

    void showHeartbeatSessions(Collection<HeartbeatSession> sessions);

    void showHeartbeat(HeartbeatSession session);
}
