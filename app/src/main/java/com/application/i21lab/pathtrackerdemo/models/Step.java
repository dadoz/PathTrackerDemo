package com.application.i21lab.pathtrackerdemo.models;

import com.google.android.gms.maps.model.Polyline;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by davide on 27/01/2017.
 */

public class Step {

    private Direction.Distance_ distance;
    private Direction.Duration_ duration;
    private Direction.EndLocation_ endLocation;
    private String htmlInstructions;
    private Polyline polyline;
    private Direction.StartLocation_ startLocation;
    private String travelMode;
    private String maneuver;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Step(Polyline polyline) {
        this.polyline = polyline;
    }

    public Direction.Distance_ getDistance() {
        return distance;
    }

    public void setDistance(Direction.Distance_ distance) {
        this.distance = distance;
    }

    public Direction.Duration_ getDuration() {
        return duration;
    }

    public void setDuration(Direction.Duration_ duration) {
        this.duration = duration;
    }

    public Direction.EndLocation_ getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Direction.EndLocation_ endLocation) {
        this.endLocation = endLocation;
    }

    public String getHtmlInstructions() {
        return htmlInstructions;
    }

    public void setHtmlInstructions(String htmlInstructions) {
        this.htmlInstructions = htmlInstructions;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    public Direction.StartLocation_ getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Direction.StartLocation_ startLocation) {
        this.startLocation = startLocation;
    }

    public String getTravelMode() {
        return travelMode;
    }

    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    public String getManeuver() {
        return maneuver;
    }

    public void setManeuver(String maneuver) {
        this.maneuver = maneuver;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public static class Polyline {

        public Polyline(String string) {
            points = string;
        }
        private String points;
        private Map<String, Object> additionalProperties = new HashMap<String, Object>();

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }

        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }

        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }

    }
}