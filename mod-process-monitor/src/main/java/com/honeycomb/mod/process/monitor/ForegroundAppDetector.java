package com.honeycomb.mod.process.monitor;

import android.content.Context;

public interface ForegroundAppDetector {

    String getForegroundPackage(Context context);
}
