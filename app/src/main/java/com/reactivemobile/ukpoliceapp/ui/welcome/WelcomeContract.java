package com.reactivemobile.ukpoliceapp.ui.welcome;

/**
 * Contract defining methods for Welcome screen view and presenter
 */
class WelcomeContract {
    interface WelcomeViewContract {
    }

    interface WelcomePresenterContract {
        void saveDisclaimerState(boolean disclaimerAccepted);

        boolean isDisclaimerAccepted();
    }
}
