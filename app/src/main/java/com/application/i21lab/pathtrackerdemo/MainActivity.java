package com.application.i21lab.pathtrackerdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.application.i21lab.pathtrackerdemo.helpers.RequestPermissionHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        RequestPermissionHelper.RequestPermissionCallbacks {

    private static final int MY_LOCATION_REQUEST_CODE = 9999;
    private GoogleMap map;
    private GoogleApiClient client;
    private Location lastLocation;
    private TextView longitudeView;
    private TextView latitudeView;
    private String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("map");

        initView();
        initMap();
        buildGoogleApiClient();
    }

    /**
     *
     */
    private void initView() {
        longitudeView = (TextView) findViewById(R.id.longitudeViewId);
        latitudeView = (TextView) findViewById(R.id.latitudeViewId);
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
        RequestPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
//        requestPermission();
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
        if (!RequestPermissionHelper.requestPermission(this)) {
            return;
        }
        //permission already granted
        setLocationOnMap();
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "hey error" + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "hey error" + connectionResult.getErrorMessage());
    }


    /**
     *
     */
    private void setLocationOnMap() {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(client);
        if (lastLocation != null &&
                map != null) {
            LatLng latLng = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            map.setMyLocationEnabled(true);
        }
        if (lastLocation != null) {
            latitudeView.setText(String.valueOf(lastLocation.getLatitude()));
            longitudeView.setText(String.valueOf(lastLocation.getLongitude()));
        }
    }

    @Override
    public void onPermissionGrantedSuccessCb() {
        setLocationOnMap();
    }

    @Override
    public void onPermissionGrantedFailureCb() {
        Snackbar.make(getWindow().getDecorView(), "Error on grant permission",
                Snackbar.LENGTH_SHORT).show();
    }
}
