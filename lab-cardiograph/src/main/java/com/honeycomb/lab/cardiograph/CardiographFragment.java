package com.honeycomb.lab.cardiograph;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.honeycomb.lab.cardiograph.model.HeartbeatMonitor;
import com.honeycomb.lab.cardiograph.model.HeartbeatSession;

import java.util.Collection;

public class CardiographFragment extends Fragment implements ICardiographView {
    private HeartbeatSessionAdapter mAdapter;

    CardiographPresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cardiograph, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = view.getContext();

        mAdapter = new HeartbeatSessionAdapter(context);

        RecyclerView cardiograph = view.findViewById(R.id.heartbeat_sessions);
        cardiograph.setLayoutManager(new LinearLayoutManager(context));
        cardiograph.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new CardiographPresenter(this, HeartbeatMonitor.getInstance());
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.stop();
    }

    @Override
    public void showHeartbeatSessions(Collection<HeartbeatSession> sessions) {
        mAdapter.setHeartbeatSessions(sessions);
    }

    @Override
    public void showHeartbeat(HeartbeatSession session) {
        mAdapter.updateHeartbeatSession(session, true);
    }
}
