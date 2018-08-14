package com.honeycomb.mod.process.monitor.impl;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import com.honeycomb.mod.process.monitor.ForegroundAppDetector;
import com.rvalerio.fgchecker.AppChecker;

import java.util.concurrent.atomic.AtomicBoolean;

public class UsageStatsDetector implements ForegroundAppDetector {
    private final AppChecker mAppChecker;
    private final AtomicBoolean mRequested = new AtomicBoolean(false);

    public UsageStatsDetector() {
        mAppChecker = new AppChecker();
    }

    @Override
    public String getForegroundPackage(Context context) {
        if (!hasUsageStatsPermission(context)) {
            if (mRequested.compareAndSet(false, true)) {
                requestUsageStatsPermission(context);
            }
            return null;
        }

        return mAppChecker.getForegroundApp(context);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean hasUsageStatsPermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return true;
        }

        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        if (appOps == null) {
            return true;
        }
        int mode = appOps.checkOpNoThrow("android:get_usage_stats",
                android.os.Process.myUid(), context.getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void requestUsageStatsPermission(Context context) {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
