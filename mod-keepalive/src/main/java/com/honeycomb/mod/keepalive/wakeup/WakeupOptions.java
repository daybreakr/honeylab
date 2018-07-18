package com.honeycomb.mod.keepalive.wakeup;

import com.honeycomb.mod.keepalive.wakeup.alarm.WakeupAlarm;

public class WakeupOptions {

    public boolean enableWakeupAlarm = true;

    public WakeupAlarm.Options wakeupAlarmOptions = new WakeupAlarm.Options();

    public RecorderOptions recorderOptions = new RecorderOptions();

    public static class RecorderOptions {

        public boolean enableLog = true;

        public boolean saveToFile = true;
    }
}
