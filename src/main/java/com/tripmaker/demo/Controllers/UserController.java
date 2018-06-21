package com.tripmaker.demo.Controllers;

import com.tripmaker.demo.Config.AuthenticationFacade;
import com.tripmaker.demo.Data.TripGroup;
import com.tripmaker.demo.Data.User;
import com.tripmaker.demo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    AuthenticationFacade authenticationFacade;

    @Autowired
    UserService userService;

    @GetMapping("getUserByMail/{mail}")
    public ResponseEntity<User> findUserByEmail(@PathVariable("mail") String mail){
        User user = userService.findUserByEmail(mail);
        if(user == null) return new ResponseEntity<User>((User) null, HttpStatus.NOT_FOUND);
        else return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @GetMapping("getUserByUsername/{username}")
    public ResponseEntity<User> findUserByUserName(@PathVariable("username") String username){
        User user = userService.findUserByUserName(username);
        if(user == null) return new ResponseEntity<User>((User) null, HttpStatus.NOT_FOUND);
        else return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @GetMapping("getTripGroup/{mail}")
    public ResponseEntity<Set<TripGroup>> getTripGroupsByEmail(@PathVariable("mail") String mail){
        User user = userService.findUserByEmail(mail);
        if(user == null) return new ResponseEntity<Set<TripGroup>>((Set<TripGroup>) null, HttpStatus.NOT_FOUND);
        else return new ResponseEntity<Set<TripGroup>>(user.getTripGroups(), HttpStatus.OK);
    }

    @GetMapping("getCurrentUserName")
    public ResponseEntity<User> getCurrentUserName(){
        return new ResponseEntity<User>(userService.getCurrentUser(), HttpStatus.OK);
    }
}
