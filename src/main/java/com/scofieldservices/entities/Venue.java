package com.scofieldservices.entities;

import org.springframework.data.geo.Point;

import javax.persistence.*;

import java.net.URL;
import java.util.Set;

/**
 * Created by scofieldservices on 1/2/17.
 */

@Entity
@Table(name = "venues")
public class Venue {
    @Id
    @GeneratedValue
    Integer venueId;

    @Column(nullable = false, unique = true)
    public String venueName;

    @Column
    public String buildingName;

    @Column(nullable = false)
    public String address;

    @Column
    public String suite;

    @Column
    public double latitude;

    @Column
    public double longitude;

    @Column
    public String gplaceId;

    @Column
    public String url;

    @Column
    public String twitter;

    @Column
    public String facebook;

    @Column
    public String photo;

    @Column
    public String openHours;

    @Column
    public Integer ownerId;

//    @ManyToOne
//    @JoinColumn(name = "fave-venues")
//    public User user;
//
//    @ManyToOne
//    @JoinColumn(name = "meeting-places")
//    public Meeting meeting;

    public Venue() {
    }

    public Venue(String venueName, String buildingName, String address, String suite, double latitude, double longitude, String gplaceId, String url, String twitter, String facebook, String photo, String openHours, int ownerId) {
        this.venueName = venueName;
        this.buildingName = buildingName;
        this.address = address;
        this.suite = suite;
        this.latitude = latitude;
        this.longitude = longitude;
        this.gplaceId = gplaceId;
        this.url = url;
        this.twitter = twitter;
        this.facebook = facebook;
        this.photo = photo;
        this.openHours = openHours;
        this.ownerId = ownerId;
    }

    public Venue(String venueName, String buildingName, String address, String suite, double latitude, double longitude, String gplaceId, String url, String twitter, String facebook, String photo, String openHours, Integer ownerId) {
        this.venueName = venueName;
        this.buildingName = buildingName;
        this.address = address;
        this.suite = suite;
        this.latitude = latitude;
        this.longitude = longitude;
        this.gplaceId = gplaceId;
        this.url = url;
        this.twitter = twitter;
        this.facebook = facebook;
        this.photo = photo;
        this.openHours = openHours;
        this.ownerId = ownerId;
    }

    public int getVenueId() {
        return venueId;
    }

    public void setVenueId(int venueId) {
        this.venueId = venueId;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSuite() {
        return suite;
    }

    public void setSuite(String suite) {
        this.suite = suite;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }
}