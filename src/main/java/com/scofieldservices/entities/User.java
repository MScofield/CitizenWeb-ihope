package com.scofieldservices.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by scofieldservices on 1/2/17.
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    int userId;

    @Column(nullable = false, unique = true)
    public String userName;

    @Column
    public String family;

    @Column
    public String firstName;

    @Column(nullable = false)
    public String password;

    @Column
    public String organization;

    @Column
    public String familyMembers;

    @Column
    public String phone;

    @Column
    public String email;

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
    public String twitter;

    @Column
    public String facebook;

    @Column
    public String url;

    @Column
    public String photo;

    @Column
    public boolean isMaster;

//    @ManyToOne
//    @JoinColumn(name = "attendees")
//    public Meeting meeting;
//
//    @ManyToOne
//    @JoinColumn(name = "visitors")
//    public Venue venue;
//
//    @ManyToOne
//    @JoinColumn(name = "friended-by")
//    public User user;
//
//    @OneToMany(mappedBy = "user")
//    Set<Venue> userVenues = new HashSet<Venue>();
//
//    @OneToMany(mappedBy = "users")
//    Set<Meeting> userMeetings = new HashSet<Meeting>();


    public User() {
    }

    public User(String userName, String family, String firstName, String password, String organization,
                String familyMembers, String phone, String email, String address, String suite,
                double latitude, double longitude, String gplaceId, String twitter, String facebook, String url, String photo) {
        this.userName = userName;
        this.family = family;
        this.firstName = firstName;
        this.password = password;
        this.organization = organization;
        this.familyMembers = familyMembers;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.suite = suite;
        this.latitude = latitude;
        this.longitude = longitude;
        this.gplaceId = gplaceId;
        this.twitter = twitter;
        this.facebook = facebook;
        this.url = url;
        this.photo = photo;
    }

    public User(String userName, String family, String firstName, String password, String organization, String familyMembers, String phone, String email, String address, String suite, double latitude, double longitude, String gplaceId, String twitter, String facebook, String url, String photo, boolean isMaster) {
        this.userName = userName;
        this.family = family;
        this.firstName = firstName;
        this.password = password;
        this.organization = organization;
        this.familyMembers = familyMembers;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.suite = suite;
        this.latitude = latitude;
        this.longitude = longitude;
        this.gplaceId = gplaceId;
        this.twitter = twitter;
        this.facebook = facebook;
        this.url = url;
        this.photo = photo;
        this.isMaster = isMaster;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getFamilyMembers() {
        return familyMembers;
    }

    public void setFamilyMembers(String familyMembers) {
        this.familyMembers = familyMembers;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public void setMaster(boolean master) {
        isMaster = master;
    }
}
