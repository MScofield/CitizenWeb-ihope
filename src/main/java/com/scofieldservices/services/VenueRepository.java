package com.scofieldservices.services;

import com.scofieldservices.entities.Meeting;
import com.scofieldservices.entities.User;
import com.scofieldservices.entities.Venue;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by scofieldservices on 2/4/17.
 */
public interface VenueRepository extends CrudRepository<Venue, Integer> {
    Venue findFirstByVenueName(String VenueName);
//    List<Venue> findAllByOrderByUser(User user);
//    List<Venue> findAllByOderByOrganizerId(Integer organizerId);
//    List<Venue> findAllByOrderByMeeting(Meeting meeting);
    Venue findFirstByAddress(String address);
//    Venue findFirstByMeeting(Meeting meeting);


}
