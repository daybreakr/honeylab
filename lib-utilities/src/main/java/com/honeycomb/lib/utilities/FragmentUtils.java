package com.honeycomb.lib.utilities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;

public class FragmentUtils {

    public static void addFragment(Activity activity, Fragment fragment, String tag) {
        FragmentManager fm = activity.getFragmentManager();
        if (tag == null || fm.findFragmentByTag(tag) == null) {
            fm.beginTransaction().add(fragment, tag).commit();
            fm.executePendingTransactions();
        }
    }
}
