package com.application.i21lab.pathtrackerdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
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

import com.application.i21lab.pathtrackerdemo.helpers.JsonParser;
import com.application.i21lab.pathtrackerdemo.helpers.RequestPermissionHelper;
import com.application.i21lab.pathtrackerdemo.models.Direction;
import com.application.i21lab.pathtrackerdemo.models.Step;
import com.application.i21lab.pathtrackerdemo.utils.MapsUtils;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

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

    private static final LatLng LUGANO = new LatLng(46.03629, 8.954198);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("map");

        initMap();
        buildGoogleApiClient();
        initView();
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

        Log.e(TAG, "Hye correct");
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
        map.setMyLocationEnabled(true);
        if (lastLocation == null ||
                map == null) {
            return;
        }

        Log.e(TAG, "hye prepare location");
        LatLng currentLocation = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());

        //set position on my current location
        map.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        map.setMinZoomPreference(6);

        String jsonString = JsonParser.getJsonFromAssets(getAssets(), "json/direction_track.json");
        Object direction = JsonParser.parse(jsonString, "direction");
        if (direction == null) {
            //handle error
            return;
        }

        //set polyline to handle direction
        map.addPolyline(new PolylineOptions()
                .add(currentLocation)
                .addAll(MapsUtils.getLatLngList((Direction) direction))
                .width(5)
                .color(Color.BLUE)
                .geodesic(true));

        //TEST
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
