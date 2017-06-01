package com.reactivemobile.ukpoliceapp.ui.map;

import com.google.android.gms.maps.model.LatLng;
import com.reactivemobile.ukpoliceapp.BuildConfig;
import com.reactivemobile.ukpoliceapp.objects.CrimeCategories;
import com.reactivemobile.ukpoliceapp.objects.CrimeCategory;
import com.reactivemobile.ukpoliceapp.objects.StreetLevelAvailabilityDates;
import com.reactivemobile.ukpoliceapp.objects.StreetLevelCrime;
import com.reactivemobile.ukpoliceapp.rest.RestInterface;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Presenter implementing MapPresenterContract functionality
 */
public class MapPresenter implements MapContract.MapPresenterContract {
    private MapContract.MapViewContract mView;

    private final RestInterface mRestInterface;

    private StreetLevelAvailabilityDates mStreetLevelAvailabilityDates;

    private HashMap<String, String> mCategoryMap;

    public MapPresenter(MapContract.MapViewContract view, RestInterface restInterface) {
        this.mView = view;
        this.mRestInterface = restInterface;
    }

    @Override
    public void viewIsReady() {
        getCrimeAvailability();
    }

    @Override
    public void getCrimesForLocationAndDate(double latitude, double longitude, String date) {
        Function<? super Throwable, ? extends ArrayList<StreetLevelCrime>> emptyData = (Function<Throwable, ArrayList<StreetLevelCrime>>) throwable -> null;

        mRestInterface.getStreetLevelCrimes(String.valueOf(latitude), String.valueOf(longitude), date).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                doOnNext(mView::streetLevelCrimesRetrievedOk).
                onErrorReturn(emptyData).
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

    @Override
    public void setView(MapContract.MapViewContract viewContract) {
        this.mView = viewContract;
    }
}
