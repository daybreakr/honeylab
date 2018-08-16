package com.honeycomb.mod.process.monitor.impl;

import android.content.Context;

public interface ForegroundAppDetector {

    String getForegroundPackage(Context context);
}
