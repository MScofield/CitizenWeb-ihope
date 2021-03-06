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
            user.userName = "Master";
            user.family = "Master";
            user.password = PasswordStorage.createHash("1850");
            user.email = "matthew@scofieldunlimited.com";
            user.isMaster = true;
            users.save(user);

        }
    }// end if init postconstruct method

//    ------------------------------------------------------------------------------
//    User & Session handling......

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String loginUser(HttpSession session, String userName, String password) throws Exception {
        User user = users.findFirstByUserName(userName);
        Integer userId = user.getUserId();
        if (userId == null) {
            session.setAttribute("error", "No such user exists, please create account");
            return "redirect:/login";
        }else if (!PasswordStorage.verifyPassword(password, user.password)){
            session.setAttribute("error", "Incorrect Password");
            return "redirect:/login";
        } else
            session.setAttribute("userId", userId);
            session.removeAttribute("address");
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

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpSession session, Model model){
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId != null) {
            User user = users.findOne(userId);
            model.addAttribute("user", user);
            model.addAttribute("address", user.getAddress());}
        return "logout";
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping(path = "/clear", method = RequestMethod.GET)
    public String clearPage(HttpSession session, Model model){
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId != null) {
            User user = users.findOne(userId);
            model.addAttribute("user", user);
            model.addAttribute("address", user.getAddress());}
        return "clear";
    }

    @RequestMapping(path = "/clear", method = RequestMethod.POST)
    public String clear(HttpSession session){
        session.removeAttribute("address");
        return "redirect:/";
    }

//    ------------------------------------------------------------------------------
//    Home slash route

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home(Model model, HttpSession session, String address) throws Exception {
        Integer userId = (Integer) session.getAttribute("userId");
        String errorMsg = (String) session.getAttribute("error");
        List<Meeting> meetingEntities;
        if (errorMsg != null) {
            model.addAttribute("error" , errorMsg);
            session.removeAttribute("error");}
        if (userId != null) {
            User user = users.findOne(userId);
            model.addAttribute("user", user);
            model.addAttribute("address", user.getAddress());
        } else if (address != null) { try {
            Geo geo = addressHandler(address);
            session.setAttribute("address", geo.getAddress());
            model.addAttribute ("address", geo.getAddress());}catch (Exception e) {}
        } else { try {
            String sessionAddress = (String) session.getAttribute("address");
            model.addAttribute ("address", sessionAddress);}catch (Exception e) {} }
            meetingEntities = (List<Meeting>) meetings.findAllByOrderByStartTimeAsc();
            model.addAttribute("meetings", meetingEntities);
        return "home";
    }//end of home slash method

//    ------------------------------------------------------------------------------
//    everything to do with diplaying, creating, managing user accounts....

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public String viewUser (Model model, HttpSession session, Integer userId) {
        Integer sessionUserId = (Integer) session.getAttribute("userId");
        if (sessionUserId != null) {
        User user = users.findOne(sessionUserId);
        model.addAttribute("user", user);}
        System.out.println("userId coming in to /user route" + userId);
        if (userId != null) {
            User userToView = users.findOne(userId);
//        List<Meeting> meetingEntities = meetings.findAllByOrderByUser(userToView);
//        List<Venue> venueEntities = venues.findAllByOrderByUser(userToView);
            model.addAttribute("userToView", userToView);
//        model.addAttribute("meetings", meetingEntities);
//        model.addAttribute("venues", venueEntities);
            assert sessionUserId != null;
            if (sessionUserId.equals(userId)) {
                boolean isOwner = true;
                model.addAttribute("isOwner", isOwner);
            }
        }
        return "user";
    }//end of "viewUser" route

    @RequestMapping(path = "/create-account", method = RequestMethod.GET)
    public String createAccountPage (Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId != null) {
            User user = users.findOne(userId);
            model.addAttribute("user", user);}
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
        session.setAttribute("userId", userId);
        return "redirect:/user?userId="+userId;
    }//end of "createUser" route

//    ------------------------------------------------------------------------------
//    Everything for displaying, creating and managing meetings....

    @RequestMapping(path = "/venue", method = RequestMethod.GET)
    public String viewVenue (Model model, HttpSession session, Integer venueId) {
        Venue venue = venues.findOne(venueId);
        model.addAttribute("venue", venue);
        Integer ownerId = venue.getOwnerId();
        User organizer = users.findOne(ownerId);
        model.addAttribute("organizer", organizer);
        List<Meeting> meetingEntities = meetings.findAllByVenueId(venueId);
        model.addAttribute("meetings", meetingEntities);
        if (session.getAttribute("userId") != null) {
            Integer userId = (Integer) session.getAttribute("userId");
            User user = users.findOne(userId);
                    model.addAttribute("user", user);
            if (user.getUserId() == ownerId) {
                Boolean isOwner = true;
                model.addAttribute("isOwner", isOwner);
            }
        }
        return "venue";
    }//end of "viewVenue" route

    @RequestMapping(path = "/create-venue", method = RequestMethod.GET)
    public String createVenuePage (Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId != null) {
            User user = users.findOne(userId);
            model.addAttribute("user", user);}
        String errorMsg = (String) session.getAttribute("error");
        if (errorMsg != null){
            model.addAttribute("error", errorMsg);
            session.removeAttribute("error");
        }
        return "create-venue";
    }

    @RequestMapping(path = "/create-venue", method = RequestMethod.POST)
    public String createVenue
            (HttpSession session, String venueName, String buildingName, String address,
             String suite, String hours, String website, String twitter, String facebook, String photo, Integer ownerId) throws Exception {
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
        if(venue != null) {
        Integer venueId = venue.getVenueId();
        return "redirect:/venue?venueId="+venueId;}
        else { return "redirect:/"; }
    }//end of "create venue" method

    @RequestMapping(path = "/venuelist", method = RequestMethod.GET)
    public String viewVenuesPage (Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        String errorMsg = (String) session.getAttribute("error");
        String address = (String) session.getAttribute("address");
        if (errorMsg != null) {
            model.addAttribute("error" , errorMsg);
            session.removeAttribute("error");}
        if (userId != null) {
            User user = users.findOne(userId);
            model.addAttribute("user", user);
            model.addAttribute("address", user.getAddress());
        } else if (address != null) { try {
            Geo geo = addressHandler(address);
            session.setAttribute("address", geo.getAddress());
            model.addAttribute ("address", geo.getAddress());}catch (Exception e) {}
        } else { try {
            String sessionAddress = (String) session.getAttribute("address");
            model.addAttribute ("address", sessionAddress);}catch (Exception e) {} }
        if(session.getAttribute("address") != null) {
        System.out.println("address in session at venues route"+" "+address);
        model.addAttribute("address", address);}
        List<Venue> venueEntities = (List<Venue>) venues.findAll();
        model.addAttribute("venues", venueEntities);
        return "venuelist";
    }

//    ------------------------------------------------------------------------------
//Everything to do with handling and displaying meetings....


    @RequestMapping(path = "/meeting", method = RequestMethod.GET)
    public String viewMeeting (Model model, HttpSession session, Integer meetingId) {
        Meeting meeting = meetings.findOne(meetingId);
        model.addAttribute("meeting", meeting);
        LocalDate date = meeting.getStartTime().toLocalDate();
        model.addAttribute("date", date);
        LocalTime time = meeting.getStartTime().toLocalTime().truncatedTo(ChronoUnit.MINUTES);
        model.addAttribute("time", time);
        if (meeting.getVenueId() != null) {
            Venue venue = venues.findOne(meeting.getVenueId());
            model.addAttribute("venue", venue);}
        if (session.getAttribute("userId") != null) {
            Integer userId = (Integer) session.getAttribute("userId");
            User user = users.findOne(userId);
            model.addAttribute("user", user);
            try{
            User organizer = users.findOne(meeting.getOrganizerId());
            model.addAttribute("organizer", organizer);}catch (Exception e){}
            if (user.getUserId() == meeting.getOrganizerId()) {
                Boolean isOrganizer = true;
                model.addAttribute("isOrganizer", isOrganizer);
            }
        }
        return "meeting";
    }//end of "viewVenue" route

    @RequestMapping(path = "/create-meeting", method = RequestMethod.GET)
    public String createMeetingPage (Model model, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId != null) {
            User user = (User) users.findOne(userId);
            model.addAttribute("user", user);}
        String errorMsg = (String) session.getAttribute("error");
        if (errorMsg != null) {
            model.addAttribute("error", errorMsg);
            session.removeAttribute("error");
        }
        List<Venue> venueEntities = (List<Venue>) venues.findAll();
        model.addAttribute("venues", venueEntities);
        return "create-meeting";
    }

    @RequestMapping(path = "/create-meeting", method = RequestMethod.POST)
    public String createMeeting(HttpSession session,
            String name, String start, String end, String address, String suite,
            String description, String url, String photo) throws Exception {
        if(name != null && start != null && address != null) {
                Integer userId = (Integer) session.getAttribute("userId");
                User organizer = users.findOne(userId);
                Integer organizerId = organizer.getUserId();
                Geo geo = addressHandler(address);
                String standardizedAddress = geo.getAddress();
                LocalDateTime startTime = LocalDateTime.parse(start);
                LocalDateTime endTime = startTime.plusHours(2);
                if (venues.findFirstByAddress(standardizedAddress) != null){
                    Venue venue = venues.findFirstByAddress(standardizedAddress);
                    Integer foundVenueId = venue.getVenueId();
                    Meeting meeting = new Meeting(name, startTime, endTime, standardizedAddress, suite,
                            description, url, photo, organizerId, foundVenueId);
                    meetings.save(meeting);
                } else {
                    Meeting meeting = new Meeting(name, startTime, endTime, standardizedAddress, suite,
                            description, url, photo, organizerId);
                    meetings.save(meeting);}
        } else {
            session.setAttribute("error", "Required fields cannot be left blank");
            return "redirect:/create-meeting";
        }
        Meeting meeting = meetings.findFirstByName(name);
        Integer meetingId = meeting.getMeetingId();
        return "redirect:/meeting?meetingId="+meetingId;
    }//end of "create meeting" method

//    ------------------------------------------------------------------------------

//    Nothing but Deletes ------------------------------------------------------

    @RequestMapping(path = "/delete-meeting", method = RequestMethod.POST)
    public String deleteMeeting(Integer meetingId)
    {
        meetings.delete(meetingId);
        return "redirect:/";
    }//end of route "deleteThought"

    @RequestMapping(path = "/delete-user", method = RequestMethod.POST)
    public String deleteUser(Integer userId)
    {
        users.delete(userId);
        return "redirect:/";
    }//end of route "deleteThought"

    @RequestMapping(path = "/delete-venue", method = RequestMethod.POST)
    public String delete(Integer venueId)
    {
        venues.delete(venueId);
        return "redirect:/";
    }//end of route "deleteThought"

//    ------------------------------------------------------------------------------
//    Mapping to view front-ent test page.....

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public String test(){
        return "/test";
    }

 //    ------------------------------------------------------------------------------


    public Geo addressHandler (String address) throws Exception {
        Geo geo = new Geo();
        GeoApiContext context = new GeoApiContext().setApiKey(googleApiKey);
        GeocodingResult[] results =  GeocodingApi.geocode(context, address).await();
        geo.setAddress(results[0].formattedAddress);
        geo.setLatitude(results[0].geometry.location.lat);
        geo.setLongitude(results[0].geometry.location.lng);
        geo.setGplaceId(results[0].placeId);
        return geo;
    }//end of "addresshandler" route

//Wishlist routes from here on down
//    @RequestMapping(path = "/fave-meeting", method = RequestMethod.POST)
//    public String faveMeeting(Integer meetingId, Integer userId) throws Exception {
//        Meeting meeting = meetings.findOne(meetingId);
//        User user = users.findOne(userId);
//        meeting.addUser(user);
//        return "redirect:/";
//    }

//    @RequestMapping(path = "/edit-meeting", method = RequestMethod.POST)
//    public String editMeeting(int meetingId, String descriptionText) throws Exception {
//        Meeting meeting = meetings.findOne(meetingId);
//        if (descriptionText != null){
//        thought.description = descriptionText;
//        thoughts.save(thought);
//        }else throw new Exception("description field cannot be blank");
//        return "redirect:/";
//    }// end of route "edit"


}// end of controller class


