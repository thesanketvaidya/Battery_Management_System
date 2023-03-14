package com.example.bms12;

import com.firebase.geofire.GeoLocation;

public class BMSData {

    double Battery_Curr;
    double Battery_Volt;
    double Battery_Temp;
    double Lattitude,prevLattitude;
    double Longitude;

    public double getPrevLattitude() {
        return prevLattitude;
    }

    public void setPrevLattitude(double prevLattitude) {
        this.prevLattitude = prevLattitude;
    }

    public double getPrevLongitude() {
        return prevLongitude;
    }

    public void setPrevLongitude(double prevLongitude) {
        this.prevLongitude = prevLongitude;
    }

    double prevLongitude;
    String status;
    String Id;
    String entered;
    String exited;
    boolean All_entered,All_exited,Self_entered,Self_exited,flagAll,flagSelf;

    public boolean isAll_entered() {
        return All_entered;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isFlagSelf() {
        return flagSelf;
    }

    public void setFlagSelf(boolean flagSelf) {
        this.flagSelf = flagSelf;
    }

    public boolean isFlagAll() {
        return flagAll;
    }

    public void setFlagAll(boolean flagAll) {
        this.flagAll = flagAll;
    }

    public void setAll_entered(boolean all_entered) {
        All_entered = all_entered;
    }

    public boolean isAll_exited() {
        return All_exited;
    }

    public void setAll_exited(boolean all_exited) {
        All_exited = all_exited;
    }

    public boolean isSelf_entered() {
        return Self_entered;
    }

    public void setSelf_entered(boolean self_entered) {
        Self_entered = self_entered;
    }

    public boolean isSelf_exited() {
        return Self_exited;
    }

    public void setSelf_exited(boolean self_exited) {
        Self_exited = self_exited;
    }

    public String getId() {
        return Id;
    }


    public String toStringBoolean() {
        return "BMSData{" +
                "Id='" + Id + '\'' +
                ", All_entered=" + All_entered +
                ", All_exited=" + All_exited +
                ", Self_entered=" + Self_entered +
                ", Self_exited=" + Self_exited +
                ", flagAll=" + flagAll +
                ", flagSelf=" + flagSelf +
                '}';
    }

    public BMSData(BMSData temp)
{
     this.Id=temp.Id;
     this.Battery_Curr=temp.Battery_Curr;
     this.Battery_Volt=temp.Battery_Volt;
     this.Battery_Temp=temp.Battery_Temp;
     this.Lattitude=temp.Lattitude;
     this.Longitude=temp.Longitude;
     this.status=temp.status;
    All_entered=false;
    All_exited=false;
    Self_entered=false;
    Self_exited=false;

}
    @Override
    public String toString() {
        return "BMSData{" +
                "Battery_Curr=" + Battery_Curr +
                ", Battery_Volt=" + Battery_Volt +
                ", Battery_Temp=" + Battery_Temp +
                ", Lattitude=" + Lattitude +
                ", Longitude=" + Longitude +
                ", Id='" + Id + '\'' +
                ", entered='" + entered + '\'' +
                ", exited='" + exited + '\'' +
                '}';
    }




    public void setId(String id) {
        Id = id;
    }

    public BMSData()
    {
        All_entered=true;
        All_exited=true;
        Self_entered=true;
        Self_exited=true;
        flagAll=false;
        flagSelf=false;
    }

    public BMSData(double battery_Curr, double battery_Volt, double battery_Temp, double lattitude, double longitude) {
        Battery_Curr = battery_Curr;
        Battery_Volt = battery_Volt;
        Battery_Temp = battery_Temp;
        Lattitude = lattitude;
        Longitude = longitude;
        All_entered=false;
        All_exited=false;
        Self_entered=false;
        Self_exited=false;
    }

    public String getEntered() {
        return entered;
    }

    public void setEntered(String entered) {
        this.entered = entered;
    }

    public String getExited() {
        return exited;
    }

    public void setExited(String exited) {
        this.exited = exited;
    }

    public double getBattery_Curr() {
        return Battery_Curr;
    }

    public void setBattery_Curr(double battery_Curr) {
        Battery_Curr = battery_Curr;
    }

    public double getBattery_Volt() {
        return Battery_Volt;
    }

    public void setBattery_Volt(double battery_Volt) {
        Battery_Volt = battery_Volt;
    }

    public double getBattery_Temp() {
        return Battery_Temp;
    }

    public void setBattery_Temp(double battery_Temp) {
        Battery_Temp = battery_Temp;
    }

    public double getLattitude() {
        return Lattitude;
    }

    public void setLattitude(double lattitude) {
        Lattitude = lattitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}

