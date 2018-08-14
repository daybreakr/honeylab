package com.honeycomb.mod.process.monitor.impl;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.honeycomb.mod.process.monitor.ForegroundAppDetector;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProcessDetector implements ForegroundAppDetector {
    private static final String TAG = "ProcessDetector";

    private static final boolean USE_CACHED_LAUNCHERS = true;

    private Set<String> mCachedLaunchers;

    @Override
    public String getForegroundPackage(Context context) {
        List<AndroidAppProcess> processes = getForegroundApps(context, getLaunchers(context));
        if (processes.isEmpty()) {
            return null;
        }

        Collections.sort(processes, FOREGROUND_COMPARATOR);
        return processes.get(0).getPackageName();
    }

    private Set<String> getLaunchers(Context context) {
        if (USE_CACHED_LAUNCHERS) {
            if (mCachedLaunchers == null) {
                mCachedLaunchers = queryLaunchers(context);
            }
            return mCachedLaunchers;
        }
        return queryLaunchers(context);
    }

    private Set<String> queryLaunchers(Context context) {
        Set<String> launchers = new HashSet<>();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> infoList = pm.queryIntentActivities(intent, 0);
        if (infoList != null) {
            for (ResolveInfo info : infoList) {
                launchers.add(info.activityInfo.packageName);
            }
        }

        return launchers;
    }

    private List<AndroidAppProcess> getForegroundApps(Context context,
                                                      Set<String> inclusive) {
        List<AndroidAppProcess> processes = new ArrayList<>();
        File[] files = new File("/proc").listFiles();
        PackageManager pm = context.getPackageManager();
        for (File file : files) {
            if (file.isDirectory()) {
                int pid;
                try {
                    pid = Integer.parseInt(file.getName());
                } catch (NumberFormatException e) {
                    continue;
                }
                try {
                    AndroidAppProcess process = new AndroidAppProcess(pid);
                    String packageName = process.getPackageName();
                    if (!process.foreground
                            // ignore processes that are not running in the default app process.
                            || process.name.contains(":")) {
                        continue;
                    }
                    // Ignore processes that the user cannot launch .
                    if (pm.getLaunchIntentForPackage(packageName) == null
                            && (inclusive == null || !inclusive.contains(packageName))) {
                        continue;
                    }
                    processes.add(process);
                } catch (AndroidAppProcess.NotAndroidAppProcessException ignored) {
                } catch (IOException e) {
                    Log.e(TAG, "Error reading from /proc/" + pid, e);
                    // System apps will not be readable on Android 5.0+ if SELinux is enforcing.
                    // You will need root access or an elevated SELinux context to read all files under /proc.
                }
            }
        }
        return processes;
    }

    private final Comparator<AndroidAppProcess> FOREGROUND_COMPARATOR =
            new Comparator<AndroidAppProcess>() {
                @Override
                public int compare(AndroidAppProcess processA, AndroidAppProcess processB) {
                    int valueA;
                    try {
                        valueA = getScore(processA);
                    } catch (IOException e) {
                        return 1;
                    }
                    int valueB;
                    try {
                        valueB = getScore(processB);
                    } catch (IOException e) {
                        return -1;
                    }

                    return valueA - valueB;
                }

                private int getScore(AndroidAppProcess process) throws IOException {
                    return process.oom_adj();
                }
            };
}
