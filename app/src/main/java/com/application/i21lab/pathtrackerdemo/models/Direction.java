package com.application.i21lab.pathtrackerdemo.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class Direction implements Parcelable {
    private List<Route> routes = null;

    public Direction() {
        this.routes = new ArrayList<Route>();
    }

    protected Direction(Parcel in) {
        routes = in.createTypedArrayList(Route.CREATOR);
    }

    public static final Creator<Direction> CREATOR = new Creator<Direction>() {
        @Override
        public Direction createFromParcel(Parcel in) {
            return new Direction(in);
        }

        @Override
        public Direction[] newArray(int size) {
            return new Direction[size];
        }
    };

    public List<Route> getRoutes() {
        return routes;
    }

    public void setSteps(ArrayList<Step> stepList) {
        ArrayList<Leg> legList = new ArrayList<Leg>();
        legList.add(new Leg(stepList));
        routes.add(new Route(legList));
    }

    public List<Step> getSteps() {
        return getRoutes().get(0).getLegs().get(0).getSteps();
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(routes);
    }

}
