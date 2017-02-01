package com.reactivemobile.ukpoliceapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.reactivemobile.ukpoliceapp.map.StreetLevelCrimeMapItem;

import java.util.Collection;

public class Utils {

    private static final String PREFERENCES_NAME = "POLICE_APP";
    private final Context mContext;

    public Utils(Context mContext) {
        this.mContext = mContext;
    }

    public boolean allItemsAtSameLocation(Cluster<StreetLevelCrimeMapItem> streetLevelCrimeMapItemCluster) {
        Collection<StreetLevelCrimeMapItem> items = streetLevelCrimeMapItemCluster.getItems();

        int index = 0;
        LatLng previousLatLng = null;
        for (StreetLevelCrimeMapItem item : items) {
            LatLng latlng = item.getPosition();
            if (index > 0) {
                if (!latlng.equals(previousLatLng)) {
                    return false;
                }
            }
            previousLatLng = latlng;
            index++;
        }

        return true;
    }

    public void saveBoolean(String key, boolean value) {
        SharedPreferences sharedPref = mContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean loadBoolean(String key, boolean defValue) {
        SharedPreferences sharedPref = mContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, defValue);
    }
}
