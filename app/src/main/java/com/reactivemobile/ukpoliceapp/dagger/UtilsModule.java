package com.reactivemobile.ukpoliceapp.dagger;

import android.content.Context;

import com.reactivemobile.ukpoliceapp.utils.Utils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UtilsModule {

    private final Utils mUtils;

    public UtilsModule(Context context) {
        mUtils = new Utils();
    }

    @Provides
    @Singleton
    Utils providesUtils() {
        return mUtils;
    }
}
