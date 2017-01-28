package com.application.i21lab.pathtrackerdemo.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.lang.ref.WeakReference;

public class RequestPermissionHelper {

    public static final int COARSE_LOCATION_REQUEST_CODE = 999;
    public static final int FINGERPRINT_REQUEST_CODE = 888;

    /**
     * request permission to grant COARSE location
     * @param activity
     * @param permission
     */
    public static boolean requestPermission(WeakReference<Activity> activity, String permission, int reqCode) {
        if (ContextCompat.checkSelfPermission(activity.get(), permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity.get(), permission)) {
                ActivityCompat.requestPermissions(activity.get(),
                        new String[]{ permission }, reqCode);
            }
            return false;
        }
        return true;
    }

    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults,
                                           WeakReference<RequestPermissionCallbacks> lst) {
        boolean checkedPermission = false;
        if (requestCode == COARSE_LOCATION_REQUEST_CODE) {
            checkedPermission = checkPermission(permissions, grantResults, Manifest.permission.ACCESS_COARSE_LOCATION);
        } else if (requestCode == FINGERPRINT_REQUEST_CODE &&
                Build.VERSION.SDK_INT >= 23) {
            checkedPermission = checkPermission(permissions, grantResults, Manifest.permission.USE_FINGERPRINT);
        }

        if (lst.get() != null) {
            if (checkedPermission) {
                lst.get().onPermissionGrantedSuccessCb();
                return;
            }
            lst.get().onPermissionGrantedFailureCb();
        }
    }

    private static boolean checkPermission(String[] permissions, int[] grantResults, String permission) {
        return permissions.length == 1 &&
                permissions[0].equals(permission) &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED;
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
