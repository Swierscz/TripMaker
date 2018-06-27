package com.tripmaker.demo.controllers;

import com.tripmaker.demo.config.AuthenticationFacade;
import com.tripmaker.demo.data.TripGroup;
import com.tripmaker.demo.data.TripGroupLimiter;
import com.tripmaker.demo.data.User;
import com.tripmaker.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("api")
public class UserController {

    @Autowired
    AuthenticationFacade authenticationFacade;

    @Autowired
    UserService userService;

    @GetMapping("r_user/user/getCurrentUserGroups")
    public ResponseEntity<Set<TripGroup>> getCurrentUserGroups() {
        User currentUser = userService.getCurrentUser();
        Set<TripGroup> setOfTripGroups = currentUser.getTripGroups();

        return setOfTripGroups == null
                ? new ResponseEntity<Set<TripGroup>>((Set<TripGroup>) null, HttpStatus.NOT_FOUND)
                : new ResponseEntity<Set<TripGroup>>(setOfTripGroups, HttpStatus.OK);
    }


    @GetMapping("r_user/user/getCurrentUserOwnedGroups")
    public ResponseEntity<Set<TripGroup>> getCurrentUserOwnedGroups() {
        User currentUser = userService.getCurrentUser();
        Set<TripGroup> allUserGroups = currentUser.getTripGroups();
        Set<TripGroup> ownedGroups = new HashSet<TripGroup>();
        if (allUserGroups == null || allUserGroups.isEmpty())
            return new ResponseEntity<Set<TripGroup>>((Set<TripGroup>) null, HttpStatus.NOT_FOUND);

        for (TripGroup tripGroup : allUserGroups) {
            if (tripGroup.getOwner() == currentUser) ownedGroups.add(tripGroup);
        }

        return ownedGroups.isEmpty()
                ? new ResponseEntity<Set<TripGroup>>((Set<TripGroup>) null, HttpStatus.NOT_FOUND)
                : new ResponseEntity<Set<TripGroup>>(ownedGroups, HttpStatus.OK);
    }

    @GetMapping("r_admin/user/getUserTripGroupsByMail/{mail}")
    public ResponseEntity<Set<TripGroup>> getUserTripGroupsByMail(@PathVariable("mail") String mail) {
        User user = userService.findUserByEmail(mail);
        Set<TripGroup> tripGroupSet = user.getTripGroups();

        return tripGroupSet == null
                ? new ResponseEntity<Set<TripGroup>>((Set<TripGroup>) null, HttpStatus.NOT_FOUND)
                : new ResponseEntity<Set<TripGroup>>(user.getTripGroups(), HttpStatus.OK);
    }


    @GetMapping("r_admin/user/getUserByMail/{mail}")
    public ResponseEntity<User> findUserByEmail(@PathVariable("mail") String mail) {
        return new ResponseEntity<User>(userService.findUserByEmail(mail), HttpStatus.OK);
    }


    @GetMapping("r_admin/user/getUserByUsername/{username}")
    public ResponseEntity<User> findUserByUserName(@PathVariable("username") String username) {
        return new ResponseEntity<User>(userService.findUserByUserName(username), HttpStatus.OK);
    }

    @GetMapping("r_user/user/getCurrentUser")
    public ResponseEntity<User> getCurrentUser() {
        return new ResponseEntity<User>(userService.getCurrentUser(), HttpStatus.OK);
    }

    //TODO Przerobić na refleksję
    @PostMapping("r_user/user/updateCurrentUser")
    public ResponseEntity<User> updateCurrentUser(@RequestBody User newUser){
        User user = userService.getCurrentUser();
        if(newUser.getEmail() != null) user.setEmail(newUser.getEmail());
        if(newUser.getUserName() !=null) user.setUserName(newUser.getUserName());
        if(newUser.getName() != null) user.setName(newUser.getName());
        if(newUser.getLastName() != null) user.setLastName(newUser.getLastName());
        if(newUser.getLocale() != null) user.setLocale(newUser.getLocale());
        userService.saveUser(user);
        return new ResponseEntity<User>(user,HttpStatus.OK);
    }

    /*
     *       |
     *       |   Funkcje nie zdefiniowane w dokumentacji. Użycie na własną odpowiedzialność :)
     *       V
     * */


}
