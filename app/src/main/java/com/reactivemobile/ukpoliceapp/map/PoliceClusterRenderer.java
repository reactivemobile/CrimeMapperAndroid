package com.reactivemobile.ukpoliceapp.map;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.reactivemobile.ukpoliceapp.R;
import com.reactivemobile.ukpoliceapp.utils.Utils;

/**
 * Cluster renderer for police crime reports
 */
public class PoliceClusterRenderer extends DefaultClusterRenderer<StreetLevelCrimeMapItem> {

    private final BitmapDescriptor clusterIcon;
    private final BitmapDescriptor clusterIconRed;

    private final Utils mUtils;

    public PoliceClusterRenderer(Context context, GoogleMap map, ClusterManager<StreetLevelCrimeMapItem> clusterManager, Utils utils) {
        super(context, map, clusterManager);
        clusterIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_taxi_ffffff_512);
        clusterIconRed = BitmapDescriptorFactory.fromResource(R.drawable.ic_taxi_ff0000_512);
        mUtils = utils;
    }

    @Override
    protected void onBeforeClusterItemRendered(StreetLevelCrimeMapItem item, MarkerOptions markerOptions) {
        markerOptions.title(item.getTitle());
        super.onBeforeClusterItemRendered(item, markerOptions);
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<StreetLevelCrimeMapItem> cluster, MarkerOptions markerOptions) {
        super.onBeforeClusterRendered(cluster, markerOptions);
        if (mUtils.allItemsAtSameLocation(cluster)) {
            markerOptions.icon(clusterIconRed);
        }
    }

    @Override
    protected void onClusterItemRendered(StreetLevelCrimeMapItem clusterItem, Marker marker) {
        marker.setIcon(clusterIcon);
        super.onClusterItemRendered(clusterItem, marker);
    }
}

