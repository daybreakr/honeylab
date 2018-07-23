package com.honeycomb.mod.keepalive.wakeup;

import com.honeycomb.mod.keepalive.wakeup.alarm.WakeupAlarm;
import com.honeycomb.mod.keepalive.wakeup.job.WakeupJob;

public class WakeupOptions {

    public boolean enableWakeupAlarm = true;

    public WakeupAlarm.Options wakeupAlarmOptions;

    public boolean enableWakeupJob = true;

    public WakeupJob.Options wakeupJobOptions;

    public RecorderOptions recorderOptions;

    public static class RecorderOptions {

        public boolean enableLog = true;

        public boolean saveToFile = true;
    }
}
