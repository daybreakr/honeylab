package com.honeycomb.lab.cardiograph;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.honeycomb.lib.utilities.support.SupportFragmentUtils;

public class CardiographActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardiograph);

        CardiographFragment fragment = new CardiographFragment();
        SupportFragmentUtils.inflateFragment(this, R.id.fragment_container, fragment);

        // Set display hone as up
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
