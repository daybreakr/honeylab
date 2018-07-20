package com.honeycomb.lab.lifecycle;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LifecycleSupportFragment extends Fragment {
    private static final String TAG = Constants.TAG;

    private LifecycleLogger mLogger = LifecycleLogger.buildUpon().tag(TAG)
            .prefix("  ")
            .suffix(" - support-fragment")
            .build();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mLogger.method();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLogger.method(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mLogger.method();
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mLogger.method();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLogger.method(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mLogger.method();
    }

    @Override
    public void onResume() {
        super.onResume();
        mLogger.method();
    }

    @Override
    public void onPause() {
        super.onPause();
        mLogger.method();
    }

    @Override
    public void onStop() {
        super.onStop();
        mLogger.method();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLogger.method();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLogger.method();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLogger.method();
    }
}
