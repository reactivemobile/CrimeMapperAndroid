package com.reactivemobile.ukpoliceapp.rest;

import com.reactivemobile.ukpoliceapp.objects.CrimeCategories;
import com.reactivemobile.ukpoliceapp.objects.StreetLevelAvailabilityDates;
import com.reactivemobile.ukpoliceapp.objects.StreetLevelCrime;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * REST interface for UK Police Data API
 */
public interface RestInterface {

    @GET("crimes-street/all-crime")
    Observable<ArrayList<StreetLevelCrime>> getStreetLevelCrimes(
            @Query("lat") String lat,
            @Query("lng") String lng,
            @Query("date") String date);

    @GET("crime-categories")
    Observable<CrimeCategories> getCrimeCategories(@Query("date") String date);


    @GET("crimes-street-dates")
    Observable<StreetLevelAvailabilityDates> getStreetLevelAvailability();
}
