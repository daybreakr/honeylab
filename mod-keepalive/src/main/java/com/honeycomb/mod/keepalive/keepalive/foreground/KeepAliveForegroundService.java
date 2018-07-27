package com.honeycomb.mod.keepalive.keepalive.foreground;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class KeepAliveForegroundService extends Service {

    public static void start(Context context) {
        Intent intent = getServiceIntent(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    public static void stop(Context context) {
        Intent intent = getServiceIntent(context);
        context.stopService(intent);
    }

    private static Intent getServiceIntent(Context context) {
        return new Intent(context, KeepAliveForegroundService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        makeServiceForeground();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void makeServiceForeground() {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm == null) {
            return;
        }

        Notification notification = createForegroundNotification(nm);
        if (notification == null) {
            return;
        }

        startForeground(1, notification);
    }

    @SuppressWarnings("deprecation")
    private Notification createForegroundNotification(NotificationManager nm) {
        Notification.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = createKeepAliveNotificationChannel(nm);
            if (channelId != null) {
                builder = new Notification.Builder(this, channelId);
            }
        } else {
            builder = new Notification.Builder(this);
        }
        if (builder == null) {
            return null;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return builder.build();
        } else {
            return builder.getNotification();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private String createKeepAliveNotificationChannel(NotificationManager nm) {
        final String channelId = "keep-alive-foreground";
        final String channelName = "Keep Alive Foreground";
        int importance = NotificationManager.IMPORTANCE_NONE;
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        channel.enableLights(false);
        channel.enableVibration(false);
        channel.setShowBadge(false);
        channel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
        nm.createNotificationChannel(channel);

        return channelId;
    }
}
