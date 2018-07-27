package com.honeycomb.mod.keepalive.keepalive;

import android.content.Context;

import com.honeycomb.mod.keepalive.keepalive.bound.KeepAliveBoundServiceController;
import com.honeycomb.mod.keepalive.keepalive.foreground.OnePixelActivityController;
import com.honeycomb.sdk.common.AppCommon;

public class KeepAliveAssembler {
    private final KeepAliveOptions mOptions;

    KeepAliveAssembler(KeepAliveOptions options) {
        mOptions = options;
    }

    KeepAliveControls provideControls() {
        if (mOptions.enableControls) {
            return new KeepAliveControls(provideContext());
        }
        return null;
    }

    OnePixelActivityController provideOnePixelActivityController() {
        if (mOptions.enableOnePixelActivity) {
            return new OnePixelActivityController(provideContext());
        }
        return null;
    }

    KeepAliveBoundServiceController provideBoundServiceController() {
        if (mOptions.enableBoundService) {
            return new KeepAliveBoundServiceController(provideContext());
        }
        return null;
    }

    private Context provideContext() {
        return AppCommon.getInstance().getApplicationContext();
    }
}
