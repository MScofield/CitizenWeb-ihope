package com.scofieldservices.services;

import com.scofieldservices.entities.Meeting;
import com.scofieldservices.entities.User;
import com.scofieldservices.entities.Venue;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.jws.soap.SOAPBinding;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

/**
 * Created by scofieldservices on 1/2/17.
 */
public interface MeetingRepository extends CrudRepository<Meeting, Integer>{
    List<Meeting> findAllByOrderByStartTimeAsc();
    List<Meeting> findAllByOrderByOrganizerId(Integer organizerId);
    List<Meeting> findAllByOrderByVenueId(Integer venueId);
//    List<Meeting> findAllByOrderByUser(User user);
//    List<Meeting> findAllByOrderByVenueIdOrderByOrganizerId(int venueId, int organizerId);
    Meeting findFirstByName(String name);
//    Meeting addUser(User user);


    @Query("SELECT t FROM Meeting t WHERE t.description LIKE ?1%")
    List<Meeting> findByDescription(String description);
}
