package com.reactivemobile.ukpoliceapp.utils;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.reactivemobile.ukpoliceapp.map.StreetLevelCrimeMapItem;

import java.util.Collection;

/**
 * Created by donalocallaghan on 01/06/2017.
 */

public class Utils {
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
}
