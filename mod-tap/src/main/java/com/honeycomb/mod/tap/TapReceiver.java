package com.honeycomb.mod.tap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TapReceiver extends BroadcastReceiver {
    private static final String EXTRA_X = "com.honeycomb.extra.x";
    private static final String EXTRA_Y = "com.honeycomb.extra.y";

    @Override
    public void onReceive(Context context, Intent intent) {
        int x = intent.getIntExtra(EXTRA_X, -1);
        int y = intent.getIntExtra(EXTRA_Y, -1);
        if (x < 0 || y < 0) {
            return;
        }

        new TapInjector().injectTap(x, y);
    }
}
