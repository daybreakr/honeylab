package com.honeycomb.mod.process.monitor;

public class ProcessMonitorOptions {

    public enum MonitorMethod {
        PROCESS,
        USAGE_STATS,
    }

    public MonitorMethod monitorMethod = MonitorMethod.USAGE_STATS;

    public long monitorInterval = 100;
}
