package com.application.i21lab.pathtrackerdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.application.i21lab.pathtrackerdemo.helpers.ConnectionStatusHelper;
import com.application.i21lab.pathtrackerdemo.helpers.JsonParser;
import com.application.i21lab.pathtrackerdemo.helpers.LocationHelper;
import com.application.i21lab.pathtrackerdemo.helpers.RequestPermissionHelper;
import com.application.i21lab.pathtrackerdemo.httpClient.NetworkTask;
import com.application.i21lab.pathtrackerdemo.models.Direction;
import com.application.i21lab.pathtrackerdemo.utils.MapsUtils;
import com.application.i21lab.pathtrackerdemo.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.lang.ref.WeakReference;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static com.application.i21lab.pathtrackerdemo.helpers.LocationHelper.REQUEST_CHECK_SETTINGS;
import static com.application.i21lab.pathtrackerdemo.helpers.RequestPermissionHelper.COARSE_LOCATION_REQUEST_CODE;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        RequestPermissionHelper.RequestPermissionCallbacks, NetworkTask.OnCompleteCallbacks, LocationHelper.DisplayLocationCallbacks {

    private GoogleMap map;
    private GoogleApiClient client;
    private Location lastLocation;
    private String TAG = "TAG";

    private static final LatLng LUGANO = new LatLng(46.03629, 8.954198);
    private String DIRECTION_BUNDLE_KEY = "DIRECTION_BUNDLE_KEY";
    private static final String CURRENT_LOCATION_BUNDLE_KEY = "CURRENT_LOCATION_BUNDLE_KEY";
    private Direction direction;
    private LatLng currentLocation;
    private Bundle stateBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle(R.string.map_title);
        stateBundle = savedInstanceState;

        initView();
    }

    /**
     *
     */
    private void initView() {
        if (!ConnectionStatusHelper.isNetworkAvailable(getApplicationContext())) {
            onConnectionNotEnabled();
            return;
        }
        initMap();
        buildGoogleApiClient();
    }

    /**
     *
     */
    private void initMap() {
        //inject map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * build google Api Client
     */
    private void buildGoogleApiClient() {
        if (client == null) {
            client = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //todo weak ref on list
        RequestPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults,
                new WeakReference<RequestPermissionHelper.RequestPermissionCallbacks>(this));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.addMarker(new MarkerOptions()
                .position(LUGANO)
                .title(getString(R.string.lugano_position)));
    }

    @Override
    public void onStart() {
        super.onStart();
        if (client != null) {
            client.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (client != null) {
            client.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (RequestPermissionHelper.requestPermission(new WeakReference<Activity>(this),
                ACCESS_COARSE_LOCATION , COARSE_LOCATION_REQUEST_CODE)) {
            return;
        }

        LocationHelper.displayLocationSettingsRequest(client, new WeakReference<Activity>(this),
                new WeakReference<LocationHelper.DisplayLocationCallbacks>(this));
//        initLocationOnMap();
    }


    @Override
    public void onConnectionSuspended(int i) {
        setProgressbar(false);
        Snackbar snackbar = Utils.getSnackBar(findViewById(R.id.layoutMainId),
                getString(R.string.connection_error), true, Snackbar.LENGTH_SHORT);
        if (snackbar != null)
            snackbar.show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        setProgressbar(false);
        Snackbar snackbar = Utils.getSnackBar(findViewById(R.id.layoutMainId),
                getString(R.string.connection_error), true, Snackbar.LENGTH_SHORT);
        if (snackbar != null)
            snackbar.show();
    }


    /**
     *
     */
    private void initLocationOnMap() {
        if (stateBundle != null) {
            setCurrentLocationOnMap();
            handlePlotDirection();
            return;
        }

        if (client != null)
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(client);

        if (lastLocation != null) {
            //set up current location
            currentLocation = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
            setCurrentLocationOnMap();

            //retrieve direction
            new NetworkTask(new WeakReference<NetworkTask.OnCompleteCallbacks>(this), currentLocation)
                    .execute(MapsUtils.buildUrl(currentLocation, LUGANO));
        }
    }

    /**
     * set current location
     */
    private void setCurrentLocationOnMap() {
        if (currentLocation == null) {
            return;
        }
        map.setMyLocationEnabled(true);
        //set position on my current location
        map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        map.setMinZoomPreference(8);
    }

    /**
     * plot direction on map
     */
    private void handlePlotDirection() {
        setProgressbar(false);
        if (direction != null) {
            //set polyline to handle direction
            map.addPolyline(new PolylineOptions()
                    .add(currentLocation)
                    .addAll(MapsUtils.getLatLngList(direction))
                    .width(10)
                    .color(ContextCompat.getColor(getBaseContext(), R.color.colorPrimary))
                    .geodesic(true));
        }
    }

    @Override
    public void onPermissionGrantedSuccessCb() {
        LocationHelper.displayLocationSettingsRequest(client, new WeakReference<Activity>(this),
                new WeakReference<LocationHelper.DisplayLocationCallbacks>(this));
//        initLocationOnMap();
    }

    @Override
    public void onPermissionGrantedFailureCb() {
        setProgressbar(false);
        Snackbar snackbar = Utils.getSnackBar(findViewById(R.id.layoutMainId), getString(R.string.error_on_grant), true, Snackbar.LENGTH_SHORT);
        if (snackbar != null)
            snackbar.show();

    }

    @Override
    public void onSuccessCb(String jsonString, LatLng currLoc) {
        Object decodedDirection = JsonParser.parse(jsonString, "direction");
        if (decodedDirection != null) {
            direction = (Direction) decodedDirection;
            handlePlotDirection();
        }
    }

    public void setProgressbar(boolean isSet) {
        View view = findViewById(R.id.mapProgressbarId);
        if (view != null)
            view.setVisibility(isSet ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            initLocationOnMap();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(CURRENT_LOCATION_BUNDLE_KEY, currentLocation);
        outState.putParcelable(DIRECTION_BUNDLE_KEY, direction);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        direction = (Direction) savedInstanceState.get(DIRECTION_BUNDLE_KEY);
        currentLocation = (LatLng) savedInstanceState.get(CURRENT_LOCATION_BUNDLE_KEY);
    }

    @Override
    public void onDisplayLocationSuccessCallback() {
        initLocationOnMap();
    }

    @Override
    public void onDisplayLocationErrorCallback() {
        onPermissionGrantedFailureCb();
    }

    /**
     * on connection not enabled handler
     */
    private void onConnectionNotEnabled() {
        setProgressbar(false);
        Snackbar snackbar = Utils.getSnackBar(findViewById(R.id.layoutMainId),
                getString(R.string.no_internet_connection), true, Snackbar.LENGTH_INDEFINITE);
        if (snackbar != null)
            snackbar.show();
    }
}
