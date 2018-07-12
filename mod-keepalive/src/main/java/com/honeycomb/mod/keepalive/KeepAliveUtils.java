package com.honeycomb.mod.keepalive;

import android.content.Context;
import android.content.Intent;

public class KeepAliveUtils {

    public static void sendKeepAliveBroadcast(Context context) {
        Intent intent = new Intent(KeepAliveIntents.ACTION_KEEP_ALIVE);
        intent.setPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        context.sendBroadcast(intent);
    }
}
