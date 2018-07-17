package com.honeycomb.mod.keepalive.wakeup.impl;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.honeycomb.lib.common.AppCommon;
import com.honeycomb.mod.keepalive.KeepAliveConstants;
import com.honeycomb.mod.keepalive.wakeup.WakeupIntents;

public class WakeupAlarm {
    private static final String WAKEUP_TAG = "Alarm";

    public static class Options {
        public long interval = 60 * 1000; // 1 minute (at least 1 minute)
        public int requestCode = KeepAliveConstants.WAKEUP_ALARM_REQUEST_CODE;
    }

    private final Options mOptions;

    private PendingIntent mPendingIntent;

    public WakeupAlarm() {
        this(new Options());
    }

    public WakeupAlarm(Options options) {
        mOptions = options;
    }

    public boolean set() {
        Context context = AppCommon.getInstance().getApplicationContext();
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am == null) {
            return false;
        }

        final int type = AlarmManager.ELAPSED_REALTIME_WAKEUP;
        final long interval = mOptions.interval;
        final long startTime = SystemClock.elapsedRealtime() + interval;
        final PendingIntent pendingIntent = getPendingIntent();
        am.cancel(pendingIntent);
        am.setRepeating(type, startTime, interval, pendingIntent);
        return true;
    }

    public boolean cancel() {
        Context context = AppCommon.getInstance().getApplicationContext();
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (am == null) {
            return false;
        }

        final PendingIntent pendingIntent = getPendingIntent();
        am.cancel(pendingIntent);
        return true;
    }

    private synchronized PendingIntent getPendingIntent() {
        if (mPendingIntent == null) {
            final Context context = AppCommon.getInstance().getApplicationContext();
            final int req = mOptions.requestCode;
            final Intent intent = WakeupIntents.create(context, WAKEUP_TAG);
            final int flags = PendingIntent.FLAG_UPDATE_CURRENT;
            mPendingIntent = PendingIntent.getBroadcast(context, req, intent, flags);
        }
        return mPendingIntent;
    }
}
