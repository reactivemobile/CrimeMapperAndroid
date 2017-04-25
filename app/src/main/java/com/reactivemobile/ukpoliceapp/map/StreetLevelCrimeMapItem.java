package com.reactivemobile.ukpoliceapp.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.reactivemobile.ukpoliceapp.objects.StreetLevelCrime;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;
import org.parceler.Transient;

@Parcel
public class StreetLevelCrimeMapItem implements ClusterItem {

    final double lat;

    final double lng;

    @Transient
    private LatLng mPosition;

    final String title;

    final StreetLevelCrime crime;

    @ParcelConstructor
    public StreetLevelCrimeMapItem(double lat, double lng, String title, StreetLevelCrime crime) {
        this.lat = lat;
        this.lng = lng;
        this.title = title;
        this.crime = crime;
    }

    @Override
    public LatLng getPosition() {
        if (mPosition == null) {
            mPosition = new LatLng(lat, lng);
        }
        return mPosition;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    @Override
    public String toString() {
        return title;
    }

    public StreetLevelCrime getCrime() {
        return crime;
    }
}
