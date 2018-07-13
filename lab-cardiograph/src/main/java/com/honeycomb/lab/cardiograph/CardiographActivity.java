package com.honeycomb.lab.cardiograph;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.honeycomb.lab.cardiograph.model.HeartbeatMonitor;
import com.honeycomb.lab.cardiograph.model.HeartbeatSession;

import java.util.Collection;

public class CardiographActivity extends AppCompatActivity implements ICardiographView {
    private RecyclerView mHeartbeatSessionsView;
    private HeartbeatSessionAdapter mAdapter;

    private CardiographPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardiograph);
        setupView();

        mPresenter = new CardiographPresenter(this, HeartbeatMonitor.getInstance());
    }

    private void setupView() {
        mHeartbeatSessionsView = findViewById(R.id.heartbeat_sessions);
        mHeartbeatSessionsView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new HeartbeatSessionAdapter(this);
        mHeartbeatSessionsView.setAdapter(mAdapter);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.start();
    }

    @Override
    protected void onStop() {
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
