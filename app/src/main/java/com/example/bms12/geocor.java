package com.example.bms12;


public class geocor {
    double lattitude;
    double longitude;
    String id;
    String entered;
    String exited;

    public String getEntered() {
        return entered;
    }

    @Override
    public String toString() {
        return "geocor{" +
                "lattitude=" + lattitude +
                ", longitude=" + longitude +

                ", id='" + id + '\'' +
                '}';
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



    public geocor(geocor t)
    {
        this.lattitude = t.lattitude;
        this.longitude = t.longitude;
        this.id = t.id;
    }
    public geocor( String id,double lattitude, double longitude) {
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public geocor()
    {

    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public geocor(double lattitude, double longitude) {
        this.lattitude = lattitude;
        this.longitude = longitude;
    }



}
