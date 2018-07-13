package com.honeycomb.lab.cardiograph;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.honeycomb.lab.cardiograph.model.HeartbeatInfo;
import com.honeycomb.lab.cardiograph.model.HeartbeatSession;
import com.honeycomb.lab.cardiograph.view.HeartbeatAnimator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HeartbeatSessionAdapter extends
        RecyclerView.Adapter<HeartbeatSessionAdapter.ViewHolder> {
    private final LayoutInflater mInflater;

    private final List<HeartbeatSessionState> mSessions = new ArrayList<>();

    public HeartbeatSessionAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public void setHeartbeatSessions(Collection<HeartbeatSession> sessions) {
        mSessions.clear();
        if (sessions != null) {
            for (HeartbeatSession session : sessions) {
                mSessions.add(HeartbeatSessionState.fromSession(session));
            }
        }
        notifyDataSetChanged();
    }

    public void updateHeartbeatSession(HeartbeatSession session, boolean beat) {
        int index = indexOf(session.getPackageName());
        HeartbeatSessionState state;
        if (index < 0) {
            state = HeartbeatSessionState.fromSession(session);
            state.beat = beat;
            mSessions.add(state);
            notifyItemInserted(mSessions.size() - 1);
        } else {
            state = mSessions.get(index);
            state.set(session);
            state.beat = beat;
            notifyItemChanged(index);
        }
    }

    private int indexOf(String packageName) {
        if (packageName != null) {
            for (int i = 0, count = mSessions.size(); i < count; i++) {
                if (packageName.equals(mSessions.get(i).packageName)) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return mSessions.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.heartbeat_session_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final HeartbeatSessionState state = mSessions.get(position);
        holder.label.setText(state.packageName);
        if (state.beat) {
            state.beat = false;
            holder.heartbeat.beat();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView label;
        HeartbeatAnimator heartbeat;

        ViewHolder(View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.label);
            ImageView heartbeatIcon = itemView.findViewById(R.id.heartbeat_icon);
            heartbeat = new HeartbeatAnimator(heartbeatIcon);

            heartbeat.idle();
        }
    }

    private static class HeartbeatSessionState {
        String packageName;
        long heartbeatInterval;
        long lastHeartbeatTime;
        long lastHeartbeatDt;

        boolean beat;

        static HeartbeatSessionState fromSession(HeartbeatSession session) {
            HeartbeatSessionState state = new HeartbeatSessionState();
            state.set(session);
            return state;
        }

        void set(HeartbeatSession session) {
            packageName = session.getPackageName();
            heartbeatInterval = session.getHeartbeatInterval();
            HeartbeatInfo lastHeartbeat = session.getLastReceivedHeartbeat();
            if (lastHeartbeat != null) {
                lastHeartbeatTime = lastHeartbeat.timestamp;
                lastHeartbeatDt = lastHeartbeat.dt;
            } else {
                lastHeartbeatTime = 0;
                lastHeartbeatDt = 0;
            }
        }
    }
}
