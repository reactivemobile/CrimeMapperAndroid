package com.reactivemobile.ukpoliceapp.ui.welcome;

/**
 * Presenter implementing WelcomePresenterContract functionality
 */
public class WelcomePresenter implements WelcomeContract.WelcomePresenterContract {


    private final WelcomeContract.WelcomeViewContract view;


    public WelcomePresenter(WelcomeContract.WelcomeViewContract view) {
        this.view = view;
    }

    @Override
    public void setDisclaimerAccepted(boolean isAccepted) {
        // TODO Analytics for disclaimer
        view.disclaimerAccepted(isAccepted);
    }
}
