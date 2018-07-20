package com.honeycomb.lab.lifecycle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.honeycomb.lib.utilities.FragmentUtils;
import com.honeycomb.lib.utilities.support.SupportFragmentUtils;

public class LifecycleActivity extends AppCompatActivity {
    private static final String TAG = Constants.TAG;

    private static final boolean FRAMEWORK_FRAGMENT = true;
    private static final boolean SUPPORT_FRAGMENT = true;
    private static final boolean LIFECYCLE_AWARE_COMPONENT = true;

    private LifecycleLogger mLogger = LifecycleLogger.buildUpon().tag(TAG).level(Log.INFO).build();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mLogger.methodStart(savedInstanceState);
        super.onCreate(savedInstanceState);
        mLogger.log("  after super.onCreate");

        setContentView(R.layout.activity_lifecycle);

        injectFrameworkFragmentIfNeeded();
        injectSupportFragmentIfNeeded();
        injectLifecycleAwareComponentIfNeeded();

        mLogger.methodEnd();
    }

    @Override
    protected void onRestart() {
        mLogger.methodStart();
        super.onRestart();
        mLogger.methodEnd();
    }

    @Override
    protected void onStart() {
        mLogger.methodStart();
        super.onStart();
        mLogger.methodEnd();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mLogger.methodStart(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
        mLogger.methodEnd();
    }

    @Override
    protected void onResume() {
        mLogger.methodStart();
        super.onResume();
        mLogger.methodEnd();
    }

    @Override
    protected void onPause() {
        mLogger.methodStart();
        super.onPause();
        mLogger.methodEnd();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mLogger.methodStart(outState);
        super.onSaveInstanceState(outState);
        mLogger.methodEnd();
    }

    @Override
    protected void onStop() {
        mLogger.methodStart();
        super.onStop();
        mLogger.methodEnd();
    }

    @Override
    protected void onDestroy() {
        mLogger.methodStart();
        super.onDestroy();
        mLogger.methodEnd();
    }

    private void injectFrameworkFragmentIfNeeded() {
        if (!FRAMEWORK_FRAGMENT) {
            return;
        }
        final String tag = getPackageName() + ".lifecycle_fragment_tag";
        FragmentUtils.addFragment(this, new LifecycleFragment(), tag);
    }

    private void injectSupportFragmentIfNeeded() {
        if (!SUPPORT_FRAGMENT) {
            return;
        }
        final String tag = getPackageName() + ".lifecycle_support_fragment_tag";
        SupportFragmentUtils.addFragment(this, new LifecycleSupportFragment(), tag);
    }

    private void injectLifecycleAwareComponentIfNeeded() {
        if (!LIFECYCLE_AWARE_COMPONENT) {
            return;
        }
        getLifecycle().addObserver(new LifecycleAwareComponent());
    }
}
