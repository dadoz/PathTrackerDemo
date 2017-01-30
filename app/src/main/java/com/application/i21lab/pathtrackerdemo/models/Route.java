package com.application.i21lab.pathtrackerdemo.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

class Route implements Parcelable {
    private List<Leg> legs = null;
    public Route(ArrayList legs) {
        this.legs = legs;
    }

    protected Route(Parcel in) {
        legs = in.createTypedArrayList(Leg.CREATOR);
    }

    public static final Creator<Route> CREATOR = new Creator<Route>() {
        @Override
        public Route createFromParcel(Parcel in) {
            return new Route(in);
        }

        @Override
        public Route[] newArray(int size) {
            return new Route[size];
        }
    };

    List<Leg> getLegs() {
        return legs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(legs);
    }
}