package com.honeycomb.mod.keepalive.keepalive.bound;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class KeepAliveBoundServiceController {
    private final Context mContext;

    private ServiceConnection mConnection;

    public KeepAliveBoundServiceController(Context context) {
        mContext = context.getApplicationContext();
    }

    public synchronized void start() {
        if (mConnection == null) {
            mConnection = new KeepAliveBoundServiceConnection();
            bindService(mConnection);
        }
    }

    public synchronized void stop() {
        if (mConnection != null) {
            unbindService(mConnection);
            mConnection = null;
        }
    }

    private void bindService(ServiceConnection connection) {
        Intent intent = new Intent(mContext, KeepAliveBoundService.class);
        mContext.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private void unbindService(ServiceConnection connection) {
        mContext.unbindService(connection);
    }

    private static class KeepAliveBoundServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // Just for starting the service in background, no need to hold the interface here.
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // Do nothing
        }
    }
}
