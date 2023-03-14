package com.example.bms12;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoQuery;
import com.google.firebase.database.DatabaseReference;

public class GeofenceObjects {
    String id;
    GeoFire geoFire;

    GeoQuery geoQuery;
   public boolean geoQueryFlag,notifyFlag,firstTime;
    DatabaseReference tempRef,geoFenceRef;

    public DatabaseReference getTempRef() {
        return tempRef;
    }

    public void setTempRef(DatabaseReference tempRef) {
        this.tempRef = tempRef;
    }

    public DatabaseReference getGeoFenceRef() {
        return geoFenceRef;
    }

    public void setGeoFenceRef(DatabaseReference geoFenceRef) {
        this.geoFenceRef = geoFenceRef;
    }

    public GeofenceObjects(String id) {
        this.id = id;
        geoQueryFlag=false;
        notifyFlag=false;
        firstTime=true;
    }

    public GeofenceObjects(String id, GeoFire geoFire, GeoQuery geoQuery, DatabaseReference tempRef, DatabaseReference geoFenceRef,
                           boolean geoQueryFlag,boolean notifyFlag) {
        this.id = id;
        this.geoFire = geoFire;
        this.geoQuery = geoQuery;
        this.tempRef = tempRef;
        this.geoFenceRef = geoFenceRef;
        this.geoQueryFlag=geoQueryFlag;
        this.notifyFlag=notifyFlag;
    }

    @Override
    public String toString() {
        return "GeofenceObjects{" +
                "id='" + id + '\'' +
                ", geoFire=" + geoFire +
                ", geoQuery=" + geoQuery +
                '}';
    }

    public GeofenceObjects(String id, GeoFire geoFire, GeoQuery geoQuery) {
        this.id = id;
        this.geoFire = geoFire;
        this.geoQuery = geoQuery;
        geoQueryFlag=false;
        notifyFlag=false;
        firstTime=true;
    }
    public GeofenceObjects()
    {
        firstTime=true;
    }

    public boolean isGeoQueryFlag() {
        return geoQueryFlag;
    }

    public void setGeoQueryFlag(boolean geoQueryFlag) {
        this.geoQueryFlag = geoQueryFlag;
    }

    public boolean isNotifyFlag() {
        return notifyFlag;
    }

    public void setNotifyFlag(boolean notifyFlag) {
        this.notifyFlag = notifyFlag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GeoFire getGeoFire() {
        return geoFire;
    }

    public void setGeoFire(GeoFire geoFire) {
        this.geoFire = geoFire;
    }

    public GeoQuery getGeoQuery() {
        return geoQuery;
    }

    public void setGeoQuery(GeoQuery geoQuery) {
        this.geoQuery = geoQuery;
    }
}
