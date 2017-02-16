package com.scofieldservices.services;

import com.scofieldservices.entities.Meeting;
import com.scofieldservices.entities.User;
import com.scofieldservices.entities.Venue;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by scofieldservices on 1/2/17.
 */
public interface UserRepository extends CrudRepository<User, Integer> {
    User findFirstByUserName(String userName);
//    List<User> findAllByOrderByUser(User user);
//    List<User> findAllByOrderByVenue(Venue venue);
//    List<User> findAllByOrderByMeeting(Meeting meeting);


}
