package com.application.i21lab.pathtrackerdemo.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Polyline implements Parcelable {
    private String points;
    public Polyline(String string) {
        points = string;
    }

    protected Polyline(Parcel in) {
        points = in.readString();
    }

    public static final Creator<Polyline> CREATOR = new Creator<Polyline>() {
        @Override
        public Polyline createFromParcel(Parcel in) {
            return new Polyline(in);
        }

        @Override
        public Polyline[] newArray(int size) {
            return new Polyline[size];
        }
    };

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(points);
    }
}