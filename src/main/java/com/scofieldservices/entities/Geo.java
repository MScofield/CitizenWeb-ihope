package com.scofieldservices.entities;

/**
 * Created by scofieldservices on 2/8/17.
 */
public class Geo {

    String address;
    double latitude;
    double longitude;
    String gplaceId;

    public Geo() {
    }

    public Geo(String address, double latitude, double longitude, String gplaceId) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.gplaceId = gplaceId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getGplaceId() {
        return gplaceId;
    }

    public void setGplaceId(String gplaceId) {
        this.gplaceId = gplaceId;
    }
}
