package com.application.i21lab.pathtrackerdemo.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RetrieveDirectionService extends IntentService {
    public static final String BROADCAST_ACTION = "BROADCAST_ACTION_CUSTOM";
    public static final String RESULT_DIRECTION_DATA = "EXTENDED_DATA_STATUS";
    private static final String TAG = "REtriveService";

    public RetrieveDirectionService() {
        super(RetrieveDirectionService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e(TAG, "START SERVICE");
        String data = doJob(intent.getDataString());

        Intent localIntent = new Intent(BROADCAST_ACTION).putExtra(RESULT_DIRECTION_DATA, data);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

    /**
     *
     * @param stringUrl
     * @return
     */
    private String doJob(String stringUrl) {
        StringBuilder chain = new StringBuilder("");
        try{
            URL url = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            InputStream inputStream = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = rd.readLine()) != null) {
                chain.append(line);
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chain.toString().equals("") ? null : chain.toString();

    }
}
