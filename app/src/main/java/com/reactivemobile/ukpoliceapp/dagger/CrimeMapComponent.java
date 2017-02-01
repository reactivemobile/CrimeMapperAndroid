package com.reactivemobile.ukpoliceapp.dagger;

import com.reactivemobile.ukpoliceapp.ui.map.MapFragment;
import com.reactivemobile.ukpoliceapp.ui.welcome.WelcomePresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {UtilsModule.class})
public interface CrimeMapComponent {

    void inject(WelcomePresenter welcomePresenter);

    void inject(MapFragment mapFragment);
}
