package com.reactivemobile.ukpoliceapp;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
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

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9090;

    @BindView(R.id.fragment_holder)
    View mRootView;

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isGooglePlayServicesAvailable()) {
            finish();
        }

        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);
        showFragment(WelcomeFragment.TAG);
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
        showFragment(MapFragment.TAG);
    }

    public void showMessage(String message) {
        Snackbar.make(mRootView, message, Snackbar.LENGTH_SHORT).show();
    }

    public void fatalMapError() {
        showFragment(WelcomeFragment.TAG);
    }
}
