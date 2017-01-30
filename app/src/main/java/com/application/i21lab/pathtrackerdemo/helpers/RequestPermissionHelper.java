package com.application.i21lab.pathtrackerdemo.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.lang.ref.WeakReference;

public class RequestPermissionHelper {

    public static final int COARSE_LOCATION_REQUEST_CODE = 999;
    public static final int FINGERPRINT_REQUEST_CODE = 888;
    private static final String TAG = "REQ_PERMISSION";

    /**
     * request permission to grant COARSE location
     * @param activity
     * @param permission
     */
    public static boolean requestPermission(WeakReference<Activity> activity, String permission, int reqCode) {
        boolean requestedPermission = false;
        if (!isPermissionGranted(activity.get(), permission)) {
            requestedPermission = true;
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity.get(), permission)) {
                ActivityCompat.requestPermissions(activity.get(), new String[] {permission}, reqCode);
            }
        }
        Log.i(TAG, "request permission - " + requestedPermission);
        return requestedPermission;
    }

    /**
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     * @param lst
     */
    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults,
                                           WeakReference<RequestPermissionCallbacks> lst) {
        boolean checkedPermission = false;
        if (requestCode == COARSE_LOCATION_REQUEST_CODE) {
            checkedPermission = checkPermission(permissions, grantResults, Manifest.permission.ACCESS_COARSE_LOCATION);
        } else if (requestCode == FINGERPRINT_REQUEST_CODE &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.e("TAG","hey req");
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

    /**
     *
     * @param permissions
     * @param grantResults
     * @param permission
     * @return
     */
    private static boolean checkPermission(String[] permissions, int[] grantResults, String permission) {
        return permissions.length == 1 &&
                permissions[0].equals(permission) &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * handing if coarse and fine location are granted
     * @return
     */
    public static boolean isPermissionGranted(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }


    public interface RequestPermissionCallbacks{
        void onPermissionGrantedSuccessCb();
        void onPermissionGrantedFailureCb();
    }
}
