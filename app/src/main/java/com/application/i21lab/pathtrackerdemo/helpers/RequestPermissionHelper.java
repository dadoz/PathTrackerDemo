package com.application.i21lab.pathtrackerdemo.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
public class RequestPermissionHelper {

    private static final int LOCATION_REQUEST_CODE = 999;

    /**
     * request permission to grant COARSE location
     */
    public static boolean requestPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        LOCATION_REQUEST_CODE);
            }
            return false;
        }

        return true;
    }

    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults,
                                           RequestPermissionCallbacks list) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_COARSE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                list.onPermissionGrantedSuccessCb();
                return;
            }

            list.onPermissionGrantedFailureCb();
        }
    }

    /**
     * handing if coarse and fine location are granted
     * @return
     */
    public boolean isLocationPermissionGranted(Context context) {
        return ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }


    public interface RequestPermissionCallbacks{
        void onPermissionGrantedSuccessCb();
        void onPermissionGrantedFailureCb();
    }
}
