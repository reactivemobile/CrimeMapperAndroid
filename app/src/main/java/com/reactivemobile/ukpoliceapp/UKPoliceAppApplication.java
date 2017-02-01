package com.reactivemobile.ukpoliceapp;

import android.app.Application;

import com.reactivemobile.ukpoliceapp.dagger.CrimeMapComponent;
import com.reactivemobile.ukpoliceapp.dagger.DaggerCrimeMapComponent;
import com.reactivemobile.ukpoliceapp.dagger.UtilsModule;

public class UKPoliceAppApplication extends Application {
    private CrimeMapComponent mDaggerComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mDaggerComponent = DaggerCrimeMapComponent.builder().utilsModule(new UtilsModule(this)).build();
    }

    public CrimeMapComponent getDaggerComponent() {
        return mDaggerComponent;
    }
}
