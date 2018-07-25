package com.honeycomb.mod.keepalive.keepalive.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

public class OnePixelActivity extends Activity {
    private static final String TAG = "OnePixelActivity";
    private static final boolean DEBUG = true;

    private static final String ACTION_FINISH_ONE_PIXEL_ACTIVITY = "com.honeycomb.action.internal.FINISH_ONE_PIXEL_ACTIVITY";

    private static final IntentFilter FILTER = new IntentFilter();

    static {
        FILTER.addAction(Intent.ACTION_USER_PRESENT);
        FILTER.addAction(ACTION_FINISH_ONE_PIXEL_ACTIVITY);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, OnePixelActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void stop(Context context) {
        Intent intent = new Intent(ACTION_FINISH_ONE_PIXEL_ACTIVITY);
        intent.setPackage(context.getPackageName());
        context.sendBroadcast(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DEBUG) {
            Log.i(TAG, "onCreate");
        }

        setOnePixelView();

        registerReceiver(mReceiver, FILTER);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (DEBUG) {
            Log.i(TAG, "onDestroy");
        }

        unregisterReceiver(mReceiver);
    }

    private void setOnePixelView() {
        Window window = getWindow();
        window.setGravity(Gravity.END | Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.width = 1;
        params.height = 1;
        window.setAttributes(params);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent != null ? intent.getAction() : null;
            Log.i(TAG, "Received: " + action);

            finish();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (DEBUG) {
            Log.i(TAG, "onStart");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DEBUG) {
            Log.i(TAG, "onResume");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (DEBUG) {
            Log.i(TAG, "onPause");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (DEBUG) {
            Log.i(TAG, "onStop");
        }
    }
}
