package com.honeycomb.lab.common;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String META_DATA_MAIN_FRAGMENT = "com.honeycomb.lab.MAIN_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment mainFragment = getMainFragment();
        if (mainFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, mainFragment)
                    .commit();
        }
    }

    private Fragment getMainFragment() {
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(),
                    PackageManager.GET_META_DATA);
            Bundle metaData = appInfo.metaData;
            if (metaData != null && metaData.containsKey(META_DATA_MAIN_FRAGMENT)) {
                String name = metaData.getString(META_DATA_MAIN_FRAGMENT);
                return Fragment.instantiate(this, name);
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
