package com.honeycomb.mod.keepalive.keepalive.background;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class KeepAliveBackgroundService extends Service {

    public static void start(Context context) {
        startForegroundActivity(context);
    }

    public static void stop(Context context) {
        stopKeepAliveService(context);
    }

    private static void startForegroundActivity(Context context) {
        try {
            Intent intent = new Intent(context, ForegroundActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startKeepAliveService(Context context) {
        try {
            Intent intent = getServiceIntent(context);
            context.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void stopKeepAliveService(Context context) {
        try {
            Intent intent = getServiceIntent(context);
            context.stopService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Intent getServiceIntent(Context context) {
        return new Intent(context, KeepAliveBackgroundService.class);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Do nothing, just improve the importance of this process.
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static class ForegroundActivity extends Activity {

        @Override
        protected void onResume() {
            super.onResume();
            startKeepAliveService(this);
            finish();
        }
    }
}
