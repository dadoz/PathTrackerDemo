package com.application.i21lab.pathtrackerdemo.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Step implements Parcelable {

    private Polyline polyline;

    public Step(Polyline polyline) {
        this.polyline = polyline;
    }

    protected Step(Parcel in) {
        polyline = in.readParcelable(Polyline.class.getClassLoader());
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public Polyline getPolyline() {
        return polyline;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(polyline, flags);
    }
}