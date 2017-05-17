package com.reactivemobile.ukpoliceapp.ui.map;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.ClusterRenderer;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.nhaarman.supertooltips.ToolTip;
import com.nhaarman.supertooltips.ToolTipRelativeLayout;
import com.reactivemobile.ukpoliceapp.MainActivity;
import com.reactivemobile.ukpoliceapp.R;
import com.reactivemobile.ukpoliceapp.UKPoliceAppApplication;
import com.reactivemobile.ukpoliceapp.map.PoliceClusterRenderer;
import com.reactivemobile.ukpoliceapp.map.StreetLevelCrimeMapItem;
import com.reactivemobile.ukpoliceapp.objects.StreetLevelAvailabilityDates;
import com.reactivemobile.ukpoliceapp.objects.StreetLevelCrime;
import com.reactivemobile.ukpoliceapp.utils.Utils;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import timber.log.Timber;

/**
 * Fragment for displaying a map with crime report locations and controls to change date and showing / hiding the heat map
 */
@RuntimePermissions
public class MapFragment extends Fragment implements MapContract.MapViewContract, ClusterManager.OnClusterClickListener<StreetLevelCrimeMapItem>, ClusterManager.OnClusterItemClickListener<StreetLevelCrimeMapItem>, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = MapFragment.class.getName();
    public static final int DEFAULT_CAMERA_ZOOM = 15;

    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;

    @BindView(R.id.date_spinner)
    Spinner mDateSpinner;

    @BindView(R.id.fab_refresh)
    FloatingActionButton mRefreshButton;

    @BindView(R.id.fab_heatmap)
    FloatingActionButton mHeatmapButton;

    @BindView(R.id.progress_bar)
    View mProgressBar;

    private ClusterManager<StreetLevelCrimeMapItem> mClusterManager;

    private View mRootView;

    private boolean initialSetup;
    private HeatmapTileProvider mHeatmapTileProvider;
    private TileOverlayOptions tileOverlayOptions;
    private TileOverlay mHeatmapOverlay;
    private ArrayList<StreetLevelCrimeMapItem> offsetItems = new ArrayList<>();

    private Unbinder mUnbinder;

    private MapContract.MapPresenterContract mPresenter;

    @Inject
    Utils mUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_crime_map, container, false);
        mUnbinder = ButterKnife.bind(this, mRootView);
        if (mPresenter == null) {
            mPresenter = new MapPresenter(this);
        } else {
            mPresenter.setView(this);
        }
        setupMap();
        setupGoogleApiClient();
        ((UKPoliceAppApplication) getActivity().getApplication()).getDaggerComponent().inject(this);
        return mRootView;
    }

    private void setupGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.viewIsReady();
        showTip();
    }

    private void showTip() {
        final ToolTipRelativeLayout toolTipRelativeLayout = ButterKnife.findById(mRootView, R.id.activity_main_tooltipRelativeLayout);

        ToolTip toolTip = new ToolTip()
                .withText(getString(R.string.toggle_heat_map))
                .withShadow()

                .withAnimationType(ToolTip.AnimationType.NONE);

        toolTipRelativeLayout.showToolTipForView(toolTip, mHeatmapButton);

        ToolTip toolTip1 = new ToolTip()
                .withText(getString(R.string.refresh_crime_data))
                .withShadow()

                .withAnimationType(ToolTip.AnimationType.NONE);

        toolTipRelativeLayout.showToolTipForView(toolTip1, mRefreshButton);


        toolTipRelativeLayout.setOnTouchListener((v, event) -> {
            toolTipRelativeLayout.setVisibility(View.GONE);
            return false;
        });
    }

    private void setupMap() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    private void setMapLocation(Location location) {
        if (location != null) {
            Timber.d("Updating map with location %s", location.toString());
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_CAMERA_ZOOM);
            mGoogleMap.moveCamera(update);
        }
    }

    private void getCrimes() {
        showProgressBar();
        if (mPresenter.getStreetLevelAvailabilityDates() == null) {
            mPresenter.getCrimeAvailability();
        } else {
            ((MainActivity) getActivity()).showMessage(getString(R.string.getting_crimes_for_location));
            int selectedItemIndex = Math.max(mDateSpinner.getSelectedItemPosition(), 0);
            String date = mPresenter.getStreetLevelAvailabilityDates().get(selectedItemIndex).getDate();
            mPresenter.getCrimesForLocationAndDate(mGoogleMap.getCameraPosition().target, date);
        }
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onClusterClick(Cluster<StreetLevelCrimeMapItem> streetLevelCrimeMapItemCluster) {
        if (mUtils.allItemsAtSameLocation(streetLevelCrimeMapItemCluster)) {
            StreetLevelCrimeMapItem[] items = new StreetLevelCrimeMapItem[streetLevelCrimeMapItemCluster.getItems().size()];
            streetLevelCrimeMapItemCluster.getItems().toArray(items);

            ArrayList<StreetLevelCrimeMapItem> streetLevelCrimeMapItemArrayList = new ArrayList<>(Arrays.asList(items));

            showDialogFragment(streetLevelCrimeMapItemArrayList);
        }
        return false;
    }

    @Override
    public boolean onClusterItemClick(StreetLevelCrimeMapItem streetLevelCrimeMapItem) {
        ArrayList<StreetLevelCrimeMapItem> streetLevelCrimeMapItemArrayList = new ArrayList<>();
        streetLevelCrimeMapItemArrayList.add(streetLevelCrimeMapItem);
        showDialogFragment(streetLevelCrimeMapItemArrayList);
        return true;
    }

    /**
     * Show the crime list fragment in a dialog popup
     *
     * @param streetLevelCrimeMapItemArrayList Crimes to be shown
     */
    void showDialogFragment(List<StreetLevelCrimeMapItem> streetLevelCrimeMapItemArrayList) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ListDialogFragment.PARAM_CRIME_LIST, Parcels.wrap(streetLevelCrimeMapItemArrayList));
        bundle.putParcelable(ListDialogFragment.PARAM_CATEGORY_MAP, Parcels.wrap(mPresenter.getCrimeCategoryMap()));


        DialogFragment newInstance = (DialogFragment) DialogFragment.instantiate(getActivity(), ListDialogFragment.TAG, bundle);
        FragmentManager fm = getChildFragmentManager();
        newInstance.show(fm, ListDialogFragment.TAG);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setMapToolbarEnabled(false);

        initialSetup = false;

        mDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (initialSetup) {
                    getCrimes();
                }

                initialSetup = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mGoogleApiClient.connect();
    }

    @OnClick(R.id.fab_refresh)
    void refreshClicked() {
        getCrimes();
    }

    @OnClick(R.id.fab_heatmap)
    void heatmapClicked() {
        if (mHeatmapOverlay != null) {
            mHeatmapOverlay.remove();
            mClusterManager.addItems(offsetItems);
            mClusterManager.cluster();
            mHeatmapOverlay = null;
        } else {
            mHeatmapOverlay = mGoogleMap.addTileOverlay(tileOverlayOptions);
            mClusterManager.clearItems();
            mClusterManager.cluster();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MapFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @SuppressWarnings("MissingPermission")
    @NeedsPermission({android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION})
    void enableLocationButton() {
        mGoogleMap.setMyLocationEnabled(true);
        setMapLocation(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        MapFragmentPermissionsDispatcher.enableLocationButtonWithCheck(this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Timber.e("onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Timber.e("onConnectionFailed %s", connectionResult.getErrorMessage());
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void streetLevelAvailabilityDatesRetrievedOk(StreetLevelAvailabilityDates streetLevelAvailabilityDates) {
        mDateSpinner.setAdapter(new DateAdapter(getActivity(), R.layout.spinner_item));
        Timber.d("Response is %s", streetLevelAvailabilityDates);
        ((MainActivity) getActivity()).showMessage(getString(R.string.getting_categories));
    }

    @Override
    public void streetLevelAvailabilityDatesRetrievedError(Throwable cause) {
        ((MainActivity) getActivity()).showMessage(getString(R.string.error_getting_dates));
        ((MainActivity) getActivity()).fatalMapError();
    }

    @Override
    public void streetLevelCrimesRetrievedError(Throwable cause) {
        ((MainActivity) getActivity()).showMessage(getString(R.string.error_getting_crimes));
        hideProgressBar();
    }

    @Override
    public void streetLevelCrimesRetrievedOk(ArrayList<StreetLevelCrime> streetLevelCrimeArrayList) {
        if (streetLevelCrimeArrayList.size() == 0) {
            hideProgressBar();
            ((MainActivity) getActivity()).showMessage(getString(R.string.no_data_for_location));
        } else {

            ArrayList<LatLng> latLngs = new ArrayList<>();

            offsetItems = new ArrayList<>();

            for (StreetLevelCrime item : streetLevelCrimeArrayList) {
                double lat = Double.parseDouble(item.getLocation().getLatitude());
                double lng = Double.parseDouble(item.getLocation().getLongitude());

                StreetLevelCrimeMapItem offsetItem = new StreetLevelCrimeMapItem(lat, lng, item.getLocation().getStreet().getName(), item);
                offsetItems.add(offsetItem);


                LatLng latLng = new LatLng(lat, lng);
                latLngs.add(latLng);
            }

            if (mHeatmapTileProvider == null) {
                mHeatmapTileProvider = new HeatmapTileProvider.Builder()
                        .data(latLngs)
                        .build();

                tileOverlayOptions = new TileOverlayOptions().tileProvider(mHeatmapTileProvider);

            } else {
                mHeatmapTileProvider.setData(latLngs);
            }

            if (mClusterManager == null) {
                mClusterManager = new ClusterManager<>(getActivity(), mGoogleMap);
                ClusterRenderer<StreetLevelCrimeMapItem> clusterRenderer = new PoliceClusterRenderer(getActivity(), mGoogleMap, mClusterManager, mUtils);

                mClusterManager.setRenderer(clusterRenderer);
                clusterRenderer.setOnClusterClickListener(this);
                clusterRenderer.setOnClusterItemClickListener(this);
                mGoogleMap.setOnCameraIdleListener(mClusterManager);
                mGoogleMap.setOnMarkerClickListener(mClusterManager);
                mClusterManager.addItems(offsetItems);
            } else {
                mClusterManager.clearItems();
                if (mHeatmapOverlay == null) {
                    mClusterManager.addItems(offsetItems);
                }
            }

            if (mHeatmapOverlay == null) {
                mClusterManager.cluster();
            }

            hideProgressBar();
        }
    }

    @Override
    public void crimeCategoriesRetrievedOk() {
        getCrimes();
    }

    @Override
    public void crimeCategoriesRetrievedError(Throwable cause) {
        ((MainActivity) getActivity()).showMessage(getString(R.string.error_getting_categories));
        ((MainActivity) getActivity()).fatalMapError();
    }

    private class DateAdapter extends ArrayAdapter<String> {

        static final String DATE_FORMAT_IN = "yyyy-MM";
        static final String DATE_FORMAT_OUT = "MMMM yyyy";
        private final SimpleDateFormat dateFormatIn = new SimpleDateFormat(DATE_FORMAT_IN, Locale.UK);
        private final SimpleDateFormat dateFormatOut = new SimpleDateFormat(DATE_FORMAT_OUT, Locale.UK);

        DateAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public int getCount() {
            return mPresenter.getStreetLevelAvailabilityDates() != null ? mPresenter.getStreetLevelAvailabilityDates().size() : 0;
        }

        @Override
        public String getItem(int position) {
            String dateString = mPresenter.getStreetLevelAvailabilityDates().get(position).getDate();
            try {
                Date date = dateFormatIn.parse(dateString);
                return dateFormatOut.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return dateString;
        }
    }
}
