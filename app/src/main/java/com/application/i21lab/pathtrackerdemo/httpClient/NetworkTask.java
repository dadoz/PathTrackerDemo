package com.application.i21lab.pathtrackerdemo.httpClient;

import android.os.AsyncTask;
import android.util.Log;


import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.R.attr.key;

public class NetworkTask extends AsyncTask<String, Void, String> {
    private final WeakReference<OnCompleteCallbacks> listener;
    private final LatLng currentLocation;
    private final LatLng destinationLocation;

    public NetworkTask(WeakReference<OnCompleteCallbacks> lst, LatLng from, LatLng to) {
        listener = lst;
        this.currentLocation = from;
        this.destinationLocation = to;
    }
    @Override
    protected String doInBackground(String... params) {
        StringBuffer chaine = new StringBuffer("");
        try{
            String urlString = params[0] +
                    "?origin=" + currentLocation.latitude + "," + currentLocation.longitude +
                    "&destination=" + destinationLocation.latitude + "," + destinationLocation.longitude +
                    "&key=" + "AIzaSyA_Z8BF_fESeP7vfK4scnnbK9NbWdVmqME";
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestProperty("User-Agent", "");
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            InputStream inputStream = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = rd.readLine()) != null) {
                chaine.append(line);
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chaine.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        //Do something with result
        if (listener.get() != null)
            listener.get().onSuccessCb(result, currentLocation);
    }

    public interface OnCompleteCallbacks {
        void onSuccessCb(String result, LatLng currentLocation);
    }
}