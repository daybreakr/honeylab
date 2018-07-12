package com.honeycomb.lab.common;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.honeycomb.lab.common.util.Constants;
import com.honeycomb.lab.common.util.SupportFragmentUtils;
import com.honeycomb.nav.INavPresenter;
import com.honeycomb.nav.INavigation;
import com.honeycomb.nav.NavigationFactory;
import com.honeycomb.nav.impl.presenter.ActivityNavPresenter;
import com.honeycomb.nav.impl.view.MenuNavView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView mNavigationView;
    private DrawerLayout mDrawer;

    private boolean mHasDrawer;

    private MenuNavView mNavView;
    private INavigation mNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fragment mainFragment = SupportFragmentUtils.createFragmentFromMetaData(this,
                Constants.META_DATA_MAIN_FRAGMENT);

        mHasDrawer = mainFragment != null;
        if (mHasDrawer) {
            setContentView(R.layout.activity_main_with_drawer);
        } else {
            setContentView(R.layout.activity_main_no_drawer);
        }
        setupView();

        // Navigation
        mNavView = new MenuNavView(mNavigationView.getMenu());
        INavPresenter navPresenter = new ActivityNavPresenter(this);
        mNavigation = NavigationFactory.createDefaultNavigation(mNavView, navPresenter, this);
        mNavigation.inflateNavItems();

        if (mainFragment != null) {
            SupportFragmentUtils.inflateFragment(this, R.id.fragment_container, mainFragment);
        }
    }

    private void setupView() {
        mNavigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        // Toolbar
        setSupportActionBar(toolbar);

        // Navigation view
        mNavigationView.setNavigationItemSelectedListener(this);

        // Drawer
        if (mHasDrawer) {
            mDrawer = findViewById(R.id.drawer);

            // Action bar toggle
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, 0, 0);
            mDrawer.addDrawerListener(toggle);
            toggle.syncState();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNavigation.detach();
    }

    @Override
    public void onBackPressed() {
        if (closeDrawer()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        closeDrawer();
        return mNavView.selectMenuItem(item);
    }

    private boolean closeDrawer() {
        if (mDrawer != null && mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }
}
