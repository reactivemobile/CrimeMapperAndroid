package com.reactivemobile.ukpoliceapp.ui.map;

import com.google.android.gms.maps.model.LatLng;
import com.reactivemobile.ukpoliceapp.objects.StreetLevelAvailabilityDates;
import com.reactivemobile.ukpoliceapp.objects.StreetLevelCrime;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Contract defining the MVP interfaces for the map feature
 */
public class MapContract {
    public interface MapViewContract {
        void streetLevelAvailabilityDatesRetrievedOk(StreetLevelAvailabilityDates streetLevelAvailabilityDates);

        void streetLevelAvailabilityDatesRetrievedError(Throwable cause);

        void streetLevelCrimesRetrievedError(Throwable cause);

        void streetLevelCrimesRetrievedOk(ArrayList<StreetLevelCrime> streetLevelCrimeArrayList);

        void crimeCategoriesRetrievedOk();

        void crimeCategoriesRetrievedError(Throwable cause);
    }

    public interface MapPresenterContract {
        void viewIsReady();

        void getCrimesForLocationAndDate(double latitude, double longitude, String date);

        void getCrimeAvailability();

        StreetLevelAvailabilityDates getStreetLevelAvailabilityDates();

        HashMap<String, String> getCrimeCategoryMap();

        void setView(MapViewContract viewContract);
    }
}
