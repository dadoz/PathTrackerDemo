package com.application.i21lab.pathtrackerdemo.utils;

import android.support.annotation.NonNull;

import com.application.i21lab.pathtrackerdemo.models.Direction;
import com.application.i21lab.pathtrackerdemo.models.Step;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MapsUtils {
    public static ArrayList<LatLng> getLatLngList(@NonNull Direction direction) {
        ArrayList<LatLng> latLngList = new ArrayList<>();
        for (Step item : ((Direction) direction).getSteps()) {
            latLngList.addAll(MapsUtils.decode(item.getPolyline().getPoints()));
        }
        return latLngList;
    }
    /**
     * Decodes an encoded path string into a sequence of LatLngs.
     * @param encodedPath
     * @return
     */
    public static List<LatLng> decode(final String encodedPath) {
        int len = encodedPath.length();

        // For speed we preallocate to an upper bound on the final length, then
        // truncate the array before returning.
        final List<LatLng> path = new ArrayList<LatLng>();
        int index = 0;
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            path.add(new LatLng(lat * 1e-5, lng * 1e-5));
        }

        return path;
    }

    public static String buildUrl(LatLng currentLocation, LatLng lugano) {
        return "https://maps.googleapis.com/maps/api/directions/json" +
                "?origin=" + currentLocation.latitude + "," + currentLocation.longitude +
                "&destination=" + lugano.latitude + "," + lugano.longitude +
                "&key=" + "AIzaSyA_Z8BF_fESeP7vfK4scnnbK9NbWdVmqME";
    }
}
