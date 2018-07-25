package com.honeycomb.mod.keepalive.keepalive;

import android.content.Context;

import com.honeycomb.mod.keepalive.keepalive.activity.OnePixelActivityController;
import com.honeycomb.sdk.common.AppCommon;

public class KeepAliveAssembler {
    private final KeepAliveOptions mOptions;

    KeepAliveAssembler(KeepAliveOptions options) {
        mOptions = options;
    }

    OnePixelActivityController provideOnePixelActivityController() {
        if (mOptions.enableOnePixelActivity) {
            Context context = AppCommon.getInstance().getApplicationContext();
            return new OnePixelActivityController(context);
        }
        return null;
    }
}
