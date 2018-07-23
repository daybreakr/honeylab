package com.honeycomb.mod.keepalive.wakeup.job;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;

import com.honeycomb.mod.keepalive.wakeup.Wakeup;
import com.honeycomb.mod.keepalive.wakeup.WakeupIntents;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class WakeupJobService extends JobService {
    private static final String WAKEUP_TAG = "JobScheduler";

    @Override
    public boolean onStartJob(JobParameters params) {
        // Send wake-up broadcast.
        WakeupIntents.send(getApplicationContext(), WAKEUP_TAG);

        // Schedule next job.
        Wakeup.getInstance().onWakeupJob();

        // Job finished.
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
