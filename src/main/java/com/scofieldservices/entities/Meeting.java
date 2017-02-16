package com.scofieldservices.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by scofieldservices on 2/3/17.
 */

@Entity
@Table(name = "meetings")
public class Meeting {
    @Id
    @GeneratedValue
    int meetingId;

    @Column(nullable = false, unique = true)
    public String name;


    @Column(nullable = false)
    public LocalDateTime startTime;

    @Column
    public LocalDateTime endTime;

    @Column(nullable = false)
    public String address;

    @Column
    public String Suite;

    @Column
    public String description;

    @Column
    public String url;

    @Column
    public String photo;

    @Column
    public Integer organizerId;

    @Column
    public Integer venueId;

//    @JoinColumn(name = "fave-meetings")
//    @ManyToOne(fetch = FetchType.LAZY)
//    public User users;
//
//    @OneToMany(mappedBy = "meeting")
//    Set<User> meetingUsers = new HashSet<User>();

    public Meeting() {
    }

    public Meeting(String name, LocalDateTime startTime, LocalDateTime endTime, String address, String suite, String description, String url, String photo, int organizerId) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.address = address;
        Suite = suite;
        this.description = description;
        this.url = url;
        this.photo = photo;
        this.organizerId = organizerId;
    }

    public Meeting(String name, LocalDateTime startTime, LocalDateTime endTime, String address, String suite, String description, String url, String photo, int organizerId, Integer venueId) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.address = address;
        Suite = suite;
        this.description = description;
        this.url = url;
        this.photo = photo;
        this.organizerId = organizerId;
        this.venueId = venueId;
    }

    public Meeting(String name, LocalDateTime startTime, LocalDateTime endTime, String address, String suite, String description, String url, String photo, Integer organizerId, Integer venueId) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.address = address;
        Suite = suite;
        this.description = description;
        this.url = url;
        this.photo = photo;
        this.organizerId = organizerId;
        this.venueId = venueId;
    }

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSuite() {
        return Suite;
    }

    public void setSuite(String suite) {
        Suite = suite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(Integer organizerId) {
        this.organizerId = organizerId;
    }

    public Integer getVenueId() {
        return venueId;
    }

    public void setVenueId(Integer venueId) {
        this.venueId = venueId;
    }
}