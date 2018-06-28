package com.tripmaker.demo.controllers;

import com.tripmaker.demo.data.TripGroup;
import com.tripmaker.demo.data.User;
import com.tripmaker.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
public class UserController {

    @Autowired
    RequestAuthorization rAuth;

    @Autowired
    UserService userService;

    @GetMapping("user/getCurrentUserGroups")
    public ResponseEntity<Set<TripGroup>> getCurrentUserGroups() {
        User currentUser = userService.getCurrentUser();
        Set<TripGroup> setOfTripGroups = currentUser.getTripGroups();

        return setOfTripGroups == null
                ? new ResponseEntity(HttpStatus.NOT_FOUND)
                : new ResponseEntity<Set<TripGroup>>(setOfTripGroups, HttpStatus.OK);
    }

    @GetMapping("user/getCurrentUserOwnedGroups")
    public ResponseEntity<Set<TripGroup>> getCurrentUserOwnedGroups() {
        User currentUser = userService.getCurrentUser();
        Set<TripGroup> allUserGroups = currentUser.getTripGroups();

        if (allUserGroups == null || allUserGroups.isEmpty())
            return new ResponseEntity<Set<TripGroup>>((Set<TripGroup>) null, HttpStatus.NOT_FOUND);

        Set<TripGroup> ownedGroups = allUserGroups.stream()
                .filter(tripGroup -> tripGroup.getOwner() == currentUser )
                .collect(Collectors.toSet());

        return ownedGroups.isEmpty()
                ? new ResponseEntity(HttpStatus.NOT_FOUND)
                : new ResponseEntity<Set<TripGroup>>(ownedGroups, HttpStatus.OK);
    }

    @GetMapping("user/getUserTripGroupsByMail/{mail}")
    public ResponseEntity<Set<TripGroup>> getUserTripGroupsByMail(@PathVariable("mail") String mail) {
        if(!rAuth.isCurrentUserAdmin()) return new ResponseEntity<Set<TripGroup>>((Set<TripGroup>) null, HttpStatus.UNAUTHORIZED);
        User user = userService.findUserByEmail(mail);
        Set<TripGroup> tripGroupSet = user.getTripGroups();

        return tripGroupSet == null
                ? new ResponseEntity(HttpStatus.NOT_FOUND)
                : new ResponseEntity<Set<TripGroup>>(user.getTripGroups(), HttpStatus.OK);
    }


    @GetMapping("user/getUserByMail/{mail}")
    public ResponseEntity<User> findUserByEmail(@PathVariable("mail") String mail) {
        return !rAuth.isCurrentUserAdmin()
                ? new ResponseEntity(HttpStatus.UNAUTHORIZED)
                : new ResponseEntity<User>(userService.findUserByEmail(mail), HttpStatus.OK);
    }


    @GetMapping("user/getUserByUsername/{username}")
    public ResponseEntity<User> findUserByUserName(@PathVariable("username") String username) {
        return !rAuth.isCurrentUserAdmin()
                ? new ResponseEntity(HttpStatus.UNAUTHORIZED)
                : new ResponseEntity<User>(userService.findUserByUserName(username), HttpStatus.OK);
    }

    @GetMapping("user/getCurrentUser")
    public ResponseEntity<User> getCurrentUser() {
        return new ResponseEntity<User>(userService.getCurrentUser(), HttpStatus.OK);
    }

    @PostMapping("user/updateCurrentUser")
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

}
