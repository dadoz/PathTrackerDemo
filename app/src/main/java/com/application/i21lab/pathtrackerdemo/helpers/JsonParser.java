package com.application.i21lab.pathtrackerdemo.helpers;

import android.content.res.AssetManager;
import android.support.annotation.NonNull;

import com.application.i21lab.pathtrackerdemo.models.Direction;
import com.application.i21lab.pathtrackerdemo.models.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class JsonParser {

    private static String TAG = "Parser";

    /**
     *
      * @param assets
     * @param filename
     * @return
     */
    public static String getJsonFromAssets(AssetManager assets, @NonNull  String filename) {
        try {
            StringBuilder buf = new StringBuilder();
            InputStream json = assets.open(filename);
            BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;

            while ((str = in.readLine()) != null) {
                buf.append(str);
            }
            in.close();
            return buf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param string
     * @param type
     * @return
     */
    public static Object parse(@NonNull String string, String type) {
        switch (type) {
            case "direction":
                return parseDirection(string);
            default:
                try {
                    return new JSONObject(string);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

    /**
     *
     * @param string
     * @return
     */
    private static Direction parseDirection(@NonNull String string) {
        Direction direction = new Direction();
        try {
            ArrayList<Step> stepList = new ArrayList<Step>();
            JSONArray stepsArray = ((JSONObject) ((JSONObject)(new JSONObject(string)).getJSONArray("routes").get(0))
                    .getJSONArray("legs").get(0)).getJSONArray("steps");
            for (int i = 0; i < stepsArray.length(); i++) {
                JSONObject step = (JSONObject) stepsArray.get(i);
                stepList.add(new Step(new Step.Polyline(step.getJSONObject("polyline").getString("points"))));
            }
            direction.setSteps(stepList);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return direction;
    }
}
