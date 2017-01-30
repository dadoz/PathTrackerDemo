package com.application.i21lab.pathtrackerdemo.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

class Leg implements Parcelable {

    private List<Step> steps = null;
    public Leg(ArrayList<Step> stepList) {
        this.steps = stepList;
    }

    protected Leg(Parcel in) {
        steps = in.createTypedArrayList(Step.CREATOR);
    }

    public static final Creator<Leg> CREATOR = new Creator<Leg>() {
        @Override
        public Leg createFromParcel(Parcel in) {
            return new Leg(in);
        }

        @Override
        public Leg[] newArray(int size) {
            return new Leg[size];
        }
    };

    List<Step> getSteps() {
        return steps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(steps);
    }
}
