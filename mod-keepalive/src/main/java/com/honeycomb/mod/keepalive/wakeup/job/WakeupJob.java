package com.honeycomb.mod.keepalive.wakeup.job;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

import com.honeycomb.mod.keepalive.KeepAliveConstants;
import com.honeycomb.sdk.common.AppCommon;

import java.util.LinkedHashSet;
import java.util.Set;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class WakeupJob {

    public static class Options {
        public int jobId = KeepAliveConstants.WAKEUP_JOB_ID;
        public long minLatency = 15 * 1000; // 2 seconds
        public long deadline = 30 * 1000; // 1 minute
        public int networkType = JobInfo.NETWORK_TYPE_ANY;
        public boolean charging = false;
        public boolean deviceIdle = false;
        public boolean persisted = true;
    }

    private Set<Integer> mScheduledJobIds;

    private final Options mOptions;

    public WakeupJob(Options options) {
        if (options == null) {
            options = new Options();
        }
        mOptions = options;
    }

    public void scheduleNext() {
        schedule();
    }

    public boolean schedule() {
        Context context = AppCommon.getInstance().getApplicationContext();
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(
                Context.JOB_SCHEDULER_SERVICE);
        if (jobScheduler == null) {
            return false;
        }

        final int jobId = mOptions.jobId;
        final ComponentName componentName = new ComponentName(context, WakeupJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(jobId, componentName)
                .setMinimumLatency(mOptions.minLatency)
                .setOverrideDeadline(mOptions.deadline)
                .setPersisted(mOptions.persisted)
                .setRequiredNetworkType(mOptions.networkType)
                .setRequiresCharging(mOptions.charging)
                .setRequiresDeviceIdle(mOptions.deviceIdle)
                .build();

        boolean scheduled = jobScheduler.schedule(jobInfo) == JobScheduler.RESULT_SUCCESS;
        if (scheduled) {
            if (mScheduledJobIds == null) {
                mScheduledJobIds = new LinkedHashSet<>();
            }
            mScheduledJobIds.add(jobId);
        }

        return scheduled;
    }

    public boolean cancel() {
        if (mScheduledJobIds != null && mScheduledJobIds.isEmpty()) {
            Context context = AppCommon.getInstance().getApplicationContext();
            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(
                    Context.JOB_SCHEDULER_SERVICE);
            if (jobScheduler == null) {
                return false;
            }

            for (int jobId : mScheduledJobIds) {
                jobScheduler.cancel(jobId);
            }
        }
        return true;
    }
}
