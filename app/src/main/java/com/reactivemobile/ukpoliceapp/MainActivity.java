package com.reactivemobile.ukpoliceapp;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.reactivemobile.ukpoliceapp.ui.map.MapFragment;
import com.reactivemobile.ukpoliceapp.ui.welcome.WelcomeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

public class MainActivity extends FragmentActivity {
    public static final String DISCLAIMER_KEY = "KEY";

    @BindView(R.id.fragment_holder)
    View mRootView;

    private Unbinder mUnbinder;

    private boolean disclaimerAccepted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isGooglePlayServicesAvailable()) {
            finish();
        }

        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);
        if (savedInstanceState != null && savedInstanceState.getBoolean(DISCLAIMER_KEY, false)) {
            showMap();
        } else {
            disclaimerAccepted = true;
            showFragment(WelcomeFragment.TAG);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(DISCLAIMER_KEY, disclaimerAccepted);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    private boolean isGooglePlayServicesAvailable() {
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS;
    }

    private void showFragment(String fragmentTag) {
        Log.e("XXX", "Showing " + fragmentTag);
        Thread.dumpStack();
        Timber.d("Showing fragment %s", fragmentTag);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment previous = getSupportFragmentManager().findFragmentByTag(fragmentTag);
        if (previous != null) {
            return;
        }

        Fragment newInstance = Fragment.instantiate(getApplicationContext(), fragmentTag,
                null);

        ft.replace(R.id.fragment_holder, newInstance, fragmentTag);
        ft.commit();
    }

    public void showMap() {
        disclaimerAccepted = true;
        showFragment(MapFragment.TAG);
    }

    public void showMessage(String message) {
        Snackbar.make(mRootView, message, Snackbar.LENGTH_SHORT).show();
    }

    public void fatalMapError() {
        showFragment(WelcomeFragment.TAG);
    }
}
