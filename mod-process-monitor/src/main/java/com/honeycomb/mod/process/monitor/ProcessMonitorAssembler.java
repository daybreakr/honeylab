package com.honeycomb.mod.process.monitor;

import android.content.Context;

import com.honeycomb.mod.process.monitor.impl.ProcessDetector;
import com.honeycomb.mod.process.monitor.impl.UsageStatsDetector;
import com.honeycomb.sdk.common.AppCommon;

public class ProcessMonitorAssembler {
    private final ProcessMonitorOptions mOptions;

    ProcessMonitorAssembler(ProcessMonitorOptions options) {
        mOptions = options;
    }

    ProcessMonitorThread provideMonitorThread() {
        return new ProcessMonitorThread(provideContext(), provideDetector(),
                mOptions.monitorInterval);
    }

    private ForegroundAppDetector provideDetector() {
        switch (mOptions.monitorMethod) {
            case PROCESS:
                return new ProcessDetector();
            case USAGE_STATS:
                return new UsageStatsDetector();
            default:
                throw new IllegalArgumentException("Invalid monitor method.");
        }
    }

    private Context provideContext() {
        return AppCommon.getInstance().getApplicationContext();
    }
}
