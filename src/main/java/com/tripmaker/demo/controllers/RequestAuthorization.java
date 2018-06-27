package com.tripmaker.demo.controllers;

import com.tripmaker.demo.data.Role;
import com.tripmaker.demo.data.TripGroup;
import com.tripmaker.demo.data.User;
import com.tripmaker.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RequestAuthorization {

    @Autowired
    UserService userService;

    public boolean isCurrentUserOwner(TripGroup tripGroup){
        User user = userService.getCurrentUser();
        return tripGroup.getOwner().equals(user) ? true : false;
    }

    public boolean isCurrentUserAdmin(){
        User user = userService.getCurrentUser();
        return user.getRole().equals(Role.ADMIN) ? true : false;
    }

    public boolean isCurrentUserMember(TripGroup tripGroup) {
        User currentUser = userService.getCurrentUser();
        for(User user : tripGroup.getUsers()){
            if(user.getEmail().equals(currentUser.getEmail())) return true;
        }
        return false;
    }

}
