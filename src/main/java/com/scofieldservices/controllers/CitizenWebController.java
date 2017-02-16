package com.scofieldservices.controllers;

import com.google.maps.model.AddressComponent;
import com.scofieldservices.entities.Geo;
import com.scofieldservices.entities.Meeting;
import com.scofieldservices.entities.User;
import com.scofieldservices.entities.Venue;
import com.scofieldservices.services.MeetingRepository;
import com.scofieldservices.services.UserRepository;
import com.scofieldservices.services.VenueRepository;
import com.scofieldservices.utilities.PasswordStorage;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

/**
 * Created by scofieldservices on 1/2/17.
 */
@Controller
public class CitizenWebController {
    @Autowired
    MeetingRepository meetings;

    @Autowired
    UserRepository users;

    @Autowired
    VenueRepository venues;

    @Value("${google.api.key}")
    String googleApiKey;

    @PostConstruct
    public void init() throws PasswordStorage.CannotPerformOperationException {
        if(users.count()==0) {
            User user = new User();
            user.userName = "StillSprings";
            user.family = "Scofield";
            user.firstName = "Matthew";
            user.password = PasswordStorage.createHash("nonStop");
            user.familyMembers = "Jen, Angelia";
            user.phone = "651-705-6511";
            user.email = "matthewgscofield@outlook.com";
            user.address = "3804 Highcrest Rd NE, Minneapolis, MN 55421";
            user.suite = "204";
            user.latitude = 45.037546;
            user.longitude = -93.208796;
            user.twitter = "@scofieldcodes";
            user.facebook = "MGScofield";
            user.url = "http://www.scofieldunlimited.com/";
            user.photo = "/picture.jpg";
            user.isMaster = false;
            users.save(user);
            user = new User();
            user.userName = "Master";
            user.family = "Master";
            user.password = PasswordStorage.createHash("1850");
            user.email = "matthew@scofieldunlimited.com";
            user.address = "11691 Polk St NE, Blaine, MN 55434";
            user.isMaster = true;
            users.save(user);

        }
    }// end if init postconstruct method

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String loginUser(HttpSession session, String userName, String password) throws Exception {
        User user = users.findFirstByUserName(userName);
        if (user == null) {
            session.setAttribute("error", "No such user exists, please create account");
            return "redirect:/login";
        }else if (!PasswordStorage.verifyPassword(password, user.password)){
            session.setAttribute("error", "Incorrect Password");
            return "redirect:/login";
        } else
            session.setAttribute("user", user);
        return "redirect:/";
    }// end of login method

    @RequestMapping(path = "login", method = RequestMethod.GET)
    public String loginPage(Model model, HttpSession session) throws Exception {
        String errorMsg = (String) session.getAttribute("error");
        if (errorMsg != null) {
            model.addAttribute("error" , errorMsg);
            session.removeAttribute("error");}
        return "login";
    }

//    @RequestMapping(path = "/address", method = RequestMethod.POST)
//    public String addressOnly(HttpSession session, String address) throws Exception {
//        if (address == null) {
//            throw new Exception("address field cannot be blank");
//        } else {
//            Geo geo = addressHandler(address);
//            session.setAttribute("geo", geo);
//            System.out.println("address output from /address route"+" "+geo.getAddress());
//        }
//        return "redirect:/";
//    }// end of login method

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logoutPage(){
        return "/logout";
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home(Model model, HttpSession session, Integer venueId, Integer organizerId, String address) throws Exception {
        User user = (User) session.getAttribute("user");
        Geo geoInput = (Geo) session.getAttribute("geo");
        String errorMsg = (String) session.getAttribute("error");
        if (errorMsg != null) {
            model.addAttribute("error" , errorMsg);
            session.removeAttribute("error");}
//        model.addAttribute("address", geoInput.getAddress());
        List<Meeting> meetingEntities;
        List<Venue> venueEntities;
        List<User> userEntities;
        List<User> organizers;
        if (user != null) {
            model.addAttribute("user", user);
            System.out.println("userId from home"+" "+user.getUserId());
            model.addAttribute("address", user.getAddress());}
//        if (venueId != null && organizerId !=null){
//            Venue venue = venues.findOne(venueId);
//            User organizer = users.findOne(organizerId);
//            meetingEntities = meetings.findAllByOrderByVenueIdOrderByOrganizerId(venueId, organizerId);
//            model.addAttribute("meetings", meetingEntities);
//        }
        if (address != null) {
            String standardizedAddress = addressHandler(address).getAddress();
            model.addAttribute("address", standardizedAddress);}
        if (venueId != null) {
            Venue venue = venues.findOne(venueId);
            meetingEntities = meetings.findAllByOrderByVenueId(venueId);
            model.addAttribute("meetings", meetingEntities);}
        if (organizerId != null) {
            User organizer = users.findOne(organizerId);
            meetingEntities = meetings.findAllByOrderByOrganizerId(organizerId);
            model.addAttribute("meetings", meetingEntities);}
        else{
            meetingEntities = (List<Meeting>) meetings.findAllByOrderByStartTimeAsc();
            model.addAttribute("meetings", meetingEntities);
        }
//        venueEntities = venues.findAllByOrderByUser(user);
//        userEntities = users.findAllByOrderByUser(user);
//        model.addAttribute("venues", venueEntities);
//        model.addAttribute("friends", userEntities);
        return "home";
    }//end of home slash method

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public String viewUser (Model model, HttpSession session, Integer userId) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        System.out.println("userId coming in to /user route" + userId);
        if (userId != null){
        User userToView = users.findOne(userId);
//        List<Meeting> meetingEntities = meetings.findAllByOrderByUser(userToView);
//        List<Venue> venueEntities = venues.findAllByOrderByUser(userToView);
        model.addAttribute("userToView", userToView);
//        model.addAttribute("meetings", meetingEntities);
//        model.addAttribute("venues", venueEntities);
        if (user.getUserId() == userId) {
            boolean isOwner = true;
            model.addAttribute("isOwner", isOwner);}
        }
        return "/user";
    }//end of "viewUser" route

    @RequestMapping(path = "/create-account", method = RequestMethod.GET)
    public String createAccountPage (Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        String errorMsg = (String) session.getAttribute("error");
        if (errorMsg != null) {
            model.addAttribute("error" , errorMsg);
            session.removeAttribute("error");}
        return "create-account";
    }

    @RequestMapping(path = "/create-account", method = RequestMethod.POST)
    public String createAccount
            (HttpSession session,
             String userName, String password, String family, String firstName,
             String organization, String familyMembers, String phone, String email,
             String address, String suite,
             String twitter, String facebook, String website, String photo) throws Exception {
        if (userName != null && password != null && address != null){
            Geo geo = addressHandler(address);
            double latitude = geo.getLatitude();
            double longitude = geo.getLongitude();
            String standardizedAddress = geo.getAddress();
            String gplaceId = geo.getGplaceId();
            User user = new User(userName, family, firstName, PasswordStorage.createHash(password), organization, familyMembers, phone,
                    email, standardizedAddress, suite, latitude, longitude, gplaceId, twitter, facebook, website, photo);
            users.save(user);
        }else {
            session.setAttribute("error", "Required fields cannot be left blank");
            return "redirect:/create-account";
        }
        User user = users.findFirstByUserName(userName);
        Integer userId = user.getUserId();
        session.setAttribute("user", user);
        return "redirect:/user?userId="+userId;
    }//end of "createUser" route

    @RequestMapping(path = "/venue", method = RequestMethod.GET)
    public String viewVenue (Model model, HttpSession session, Integer venueId) {
        Venue venue = venues.findOne(venueId);
        model.addAttribute("venue", venue);
        Integer ownerId = venue.getOwnerId();
        User organizer = users.findOne(ownerId);
        model.addAttribute("organizer", organizer);
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        if (user.getUserId() == ownerId) {
            Boolean isOwner = true;
            model.addAttribute("isOwner", isOwner);
        }
//        List<Meeting> meetingEntities = meetings.findAllByOrderByVenueId(venueId);
//        List<User> userEntities = users.findAllByOrderByVenue(venue);
//        model.addAttribute("meetings", meetingEntities);
//        model.addAttribute("users", userEntities);
        return "venue";
    }//end of "viewVenue" route

    @RequestMapping(path = "/create-venue", method = RequestMethod.GET)
    public String createVenuePage (Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        String errorMsg = (String) session.getAttribute("error");
        if (errorMsg != null){
            model.addAttribute("error", errorMsg);
            session.removeAttribute("error");
        }
        return "create-venue";
    }

    @RequestMapping(path = "/create-venue", method = RequestMethod.POST)
    public String createVenue
            (HttpSession session, String venueName, String buildingName, String address, String suite, String hours,
             String website, String twitter, String facebook, String photo, Integer ownerId) throws Exception {
        if (venueName != null && address != null) {
            try {
                Geo geo = addressHandler(address);
                double latitude = geo.getLatitude();
                double longitude = geo.getLongitude();
                String standardizedAddress = geo.getAddress();
                String gplaceId = geo.getGplaceId();
                Venue v = new Venue(venueName, buildingName, standardizedAddress, suite,
                        latitude, longitude, gplaceId, website, twitter, facebook, photo,
                        hours, ownerId);
                venues.save(v);
            } catch (Exception e) {}
        } else {
            session.setAttribute("error", "required fields cannot be left blank");
            return "redirect:/create-venue";
        }
        Venue venue = venues.findFirstByVenueName(venueName);
        Integer venueId = venue.getVenueId();
        return "redirect:/venue?venueId="+venueId;
    }//end of "create venue" method

    @RequestMapping(path = "/meeting", method = RequestMethod.GET)
    public String viewMeeting (Model model, HttpSession session, Integer meetingId) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        Meeting meeting = meetings.findOne(meetingId);
        model.addAttribute("meeting", meeting);
        LocalDate date = meeting.getStartTime().toLocalDate();
        model.addAttribute("date", date);
        LocalTime time = meeting.getStartTime().toLocalTime().truncatedTo(ChronoUnit.MINUTES);
        model.addAttribute("time", time);
        Integer minutes =
                ((meeting.getEndTime().toLocalTime()).compareTo(meeting.getStartTime().toLocalTime()));
        model.addAttribute(minutes);
        if (meeting.getVenueId() != null) {
            Venue venue = venues.findOne(meeting.getVenueId());
            model.addAttribute("venue", venue);}
//        List<User> userEntities = users.findAllByOrderByMeeting(meeting);
            User organizer = users.findOne(meeting.getOrganizerId());
            model.addAttribute("organizer", organizer);
        if (user.getUserId() == meeting.getOrganizerId()) {
            Boolean isOrganizer = true;
            model.addAttribute("isOrganizer", isOrganizer);
            }
//        model.addAttribute("users", userEntities);
        return "meeting";
    }//end of "viewVenue" route

    @RequestMapping(path = "/create-meeting", method = RequestMethod.GET)
    public String createMeetingPage (Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);
        String errorMsg = (String) session.getAttribute("error");
        if (errorMsg != null) {
            model.addAttribute("error", errorMsg);
            session.removeAttribute("error");
        }
        return "create-meeting";
    }

    @RequestMapping(path = "/create-meeting", method = RequestMethod.POST)
    public String createMeeting(HttpSession session,
            String name, String start, String end, String address, String suite,
            String description, String url, String photo, Integer venueId) throws Exception {
        if(name != null && start != null && address != null) {
            try {
                User organizer = (User) session.getAttribute("user");
                Integer organizerId = organizer.getUserId();
                Geo geo = addressHandler(address);
                String standardizedAddress = geo.getAddress();
                LocalDateTime startTime = LocalDateTime.parse(start);
                LocalDateTime endTime = LocalDateTime.parse(end);
//        double latitude = geo.getLatitude();
//        double longitude = geo.getLongitude();
//        String gplaceId = geo.getGplaceId();
//        Venue venue = venues.findOne(venueId);
                if (venueId == null) {
                    try {
                        Venue venue = venues.findFirstByAddress(standardizedAddress);
                        Integer foundVenueId = venue.getVenueId();
                        Meeting meeting = new Meeting(name, startTime, endTime, standardizedAddress, suite,
                                description, url, photo, organizerId, foundVenueId);
                        meetings.save(meeting);
                    } catch (Exception e) {
                    }
                } else if (venueId != null) {
                    try {
                        Integer foundVenueId = venues.findFirstByAddress(standardizedAddress).getVenueId();
                        Meeting meeting = new Meeting(name, startTime, endTime, standardizedAddress, suite,
                                description, url, photo, organizerId, foundVenueId);
                        meetings.save(meeting);
                    } catch (Exception e) {
                    }
                } else {
                    Meeting meeting = new Meeting(name, startTime, endTime, standardizedAddress, suite, description, url, photo, organizerId);
                    meetings.save(meeting);
                }
            }catch (Exception e) {}
        } else {
            session.setAttribute("error", "Required fields cannot be left blank");
            return "redirect:/create-meeting";
        }
        Meeting meeting = meetings.findFirstByName(name);
        Integer meetingId = meeting.getMeetingId();
        return "redirect:/meeting?meetingId="+meetingId;
    }//end of "create meeting" method

//    @RequestMapping(path = "/fave-meeting", method = RequestMethod.POST)
//    public String faveMeeting(Integer meetingId, Integer userId) throws Exception {
//        Meeting meeting = meetings.findOne(meetingId);
//        User user = users.findOne(userId);
//        meeting.addUser(user);
//        return "redirect:/";
//    }

    @RequestMapping(path = "/delete-meeting", method = RequestMethod.POST)
    public String deleteMeeting(Integer meetingId)
    {
        meetings.delete(meetingId);
        return "redirect:/";
    }//end of route "deleteThought"

    @RequestMapping(path = "/delete-user", method = RequestMethod.POST)
    public String deleteUser(Integer userId)
    {
        meetings.delete(userId);
        return "redirect:/";
    }//end of route "deleteThought"

    @RequestMapping(path = "/delete-venue", method = RequestMethod.POST)
    public String delete(Integer venueId)
    {
        meetings.delete(venueId);
        return "redirect:/";
    }//end of route "deleteThought"

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public String test(){
        return "/test";
    }

//    @RequestMapping(path = "/edit-meeting", method = RequestMethod.POST)
//    public String editMeeting(int meetingId, String descriptionText) throws Exception {
//        Meeting meeting = meetings.findOne(meetingId);
//        if (descriptionText != null){
//        thought.description = descriptionText;
//        thoughts.save(thought);
//        }else throw new Exception("description field cannot be blank");
//        return "redirect:/";
//    }// end of route "edit"

    public Geo addressHandler (String address) throws Exception {
        Geo geo = new Geo();
        GeoApiContext context = new GeoApiContext().setApiKey(googleApiKey);
        GeocodingResult[] results =  GeocodingApi.geocode(context, address).await();
        System.out.println(results[0].formattedAddress);
        geo.setAddress(results[0].formattedAddress);
        System.out.println(results[0].geometry.location.lat);
        geo.setLatitude(results[0].geometry.location.lat);
        System.out.println(results[0].geometry.location.lng);
        geo.setLongitude(results[0].geometry.location.lng);
        System.out.println(results[0].placeId);
        geo.setGplaceId(results[0].placeId);
        return geo;
    }//end of "addresshandler" route

//    public void photoHandler

}// end of controller class


