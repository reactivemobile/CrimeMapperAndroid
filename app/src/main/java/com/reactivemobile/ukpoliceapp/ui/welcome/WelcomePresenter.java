package com.reactivemobile.ukpoliceapp.ui.welcome;

import android.app.Activity;

import com.reactivemobile.ukpoliceapp.UKPoliceAppApplication;
import com.reactivemobile.ukpoliceapp.utils.Utils;

import javax.inject.Inject;

/**
 * Presenter implementing WelcomePresenterContract functionality
 */
public class WelcomePresenter implements WelcomeContract.WelcomePresenterContract {

    private static final String DISCLAIMER_KEY = "DISCLAIMER_KEY";
    
    @Inject
    Utils mUtils;

    WelcomePresenter(Activity activity) {
        ((UKPoliceAppApplication) activity.getApplication()).getDaggerComponent().inject(this);
    }

    @Override
    public void saveDisclaimerState(boolean disclaimerAccepted) {
        mUtils.saveBoolean(DISCLAIMER_KEY, disclaimerAccepted);
    }

    @Override
    public boolean isDisclaimerAccepted() {
        return mUtils.loadBoolean(DISCLAIMER_KEY, false);
    }
}
