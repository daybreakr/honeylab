package com.honeycomb.mod.keepalive.wakeup;

import android.content.Context;
import android.content.Intent;

public class WakeupIntents {
    public static final String ACTION_KEEP_ALIVE = "com.honeycomb.action.WAKEUP";

    public static final String EXTRA_TAG = "com.honeycomb.extra.tag";

    public static Intent create(Context context, String tag) {
        Intent intent = new Intent(ACTION_KEEP_ALIVE);
        intent.setPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        if (tag != null) {
            intent.putExtra(EXTRA_TAG, tag);
        }
        return intent;
    }
}
