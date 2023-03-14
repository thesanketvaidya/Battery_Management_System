package com.example.bms12;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.security.Timestamp;
import java.sql.Time;

public class MyMarker  {
    @Override
    public String toString() {
        return "MyMarker{" +
                "marker=" + marker +
                ", Id='" + Id + '\'' +
                ", time=" + time +
                ", previousPosition=" + previousPosition +
                '}';
    }

    Marker marker;
    String Id;
    Long time;
    LatLng previousPosition;
    LatLng currPosition;

    public LatLng getPreviousPosition() {
        return previousPosition;
    }

    public void setPreviousPosition(LatLng previousPosition) {
        this.previousPosition = previousPosition;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Marker getMarker() {
        return marker;
    }

    public MyMarker(Marker marker, String id,LatLng previousPosition,Long time) {
        this.marker = marker;
        Id = id;
        this.previousPosition=previousPosition;
        this.time=time;
        this.currPosition=new LatLng(marker.getPosition().latitude,marker.getPosition().longitude);

    }

    public LatLng getCurrPosition() {
        return currPosition;
    }

    public void setCurrPosition(LatLng currPosition) {
        this.currPosition = currPosition;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
