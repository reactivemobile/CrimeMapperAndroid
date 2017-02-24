package com.reactivemobile.ukpoliceapp.ui.map;

import com.google.android.gms.maps.model.LatLng;
import com.reactivemobile.ukpoliceapp.BuildConfig;
import com.reactivemobile.ukpoliceapp.objects.StreetLevelAvailabilityDates;
import com.reactivemobile.ukpoliceapp.rest.RestInterface;
import com.reactivemobile.ukpoliceapp.utils.Utils;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Presenter implementing MapPresenterContract functionality
 */
class MapPresenter implements MapContract.MapPresenterContract {
    private final MapContract.MapViewContract mView;

    private final RestInterface mRestInterface;

    @Inject
    Utils mUtils;

    private StreetLevelAvailabilityDates mStreetLevelAvailabilityDates;

    private HashMap<String, String> mCategoryMap;

    MapPresenter(MapContract.MapViewContract view) {
        this.mView = view;
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(BuildConfig.BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).
                addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
                build();

        mRestInterface = retrofit.create(RestInterface.class);
    }

    @Override
    public void viewIsReady() {
        getCrimeAvailability();
    }

    @Override
    public void getCrimesForLocationAndDate(LatLng location, String date) {
        mRestInterface.getStreetLevelCrimes(String.valueOf(location.latitude), String.valueOf(location.longitude), date).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                doOnNext(mView::streetLevelCrimesRetrievedOk).
                doOnError(throwable -> mView.streetLevelCrimesRetrievedError(throwable.getCause())).
                subscribe();
    }

    @Override
    public void getCrimeAvailability() {
        mRestInterface.getStreetLevelAvailability().
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                doOnNext(this::streetLevelAvailabilityDatesRetrievedOk).
                doOnError(throwable -> mView.streetLevelAvailabilityDatesRetrievedError(throwable.getCause())).
                subscribe();
    }

    private void streetLevelAvailabilityDatesRetrievedOk(StreetLevelAvailabilityDates streetLevelAvailabilityDates) {
        mStreetLevelAvailabilityDates = streetLevelAvailabilityDates;
        mView.streetLevelAvailabilityDatesRetrievedOk(streetLevelAvailabilityDates);
        mCategoryMap = new HashMap<>();

        mRestInterface.getCrimeCategories(mStreetLevelAvailabilityDates.get(0).getDate()).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                flatMapIterable((crimeCategories) -> crimeCategories).
                doOnNext(crimeCategory -> mCategoryMap.put(crimeCategory.getUrl(), crimeCategory.getName())).
                doOnComplete(mView::crimeCategoriesRetrievedOk).
                doOnError(throwable -> mView.streetLevelCrimesRetrievedError(throwable.getCause())).
                subscribe();
    }

    @Override
    public StreetLevelAvailabilityDates getStreetLevelAvailabilityDates() {
        return mStreetLevelAvailabilityDates;
    }

    @Override
    public HashMap<String, String> getCrimeCategoryMap() {
        return mCategoryMap;
    }
}
