package com.honeycomb.lib.utilities.support;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class SupportFragmentUtils {

    public static Fragment createFragmentFromMetaData(Context context, String name) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            Bundle metaData = appInfo.metaData;
            if (metaData != null && metaData.containsKey(name)) {
                String fragmentName = metaData.getString(name);
                return Fragment.instantiate(context, fragmentName);
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static void inflateFragment(FragmentActivity activity, int containerViewId,
                                       Fragment fragment) {
        activity.getSupportFragmentManager().beginTransaction()
                .replace(containerViewId, fragment)
                .commit();
    }

    public static void addFragment(FragmentActivity activity, Fragment fragment, String tag) {
        FragmentManager fm = activity.getSupportFragmentManager();
        if (tag == null || fm.findFragmentByTag(tag) == null) {
            fm.beginTransaction().add(fragment, tag).commit();
            fm.executePendingTransactions();
        }
    }
}
