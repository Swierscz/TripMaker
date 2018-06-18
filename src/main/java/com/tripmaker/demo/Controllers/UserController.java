package com.tripmaker.demo.Controllers;

import com.tripmaker.demo.Data.TripGroup;
import com.tripmaker.demo.Data.User;
import com.tripmaker.demo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    UserService userService;

    //TODO zapytać się o poprawność zwracania kodu odpowiedzi w takim przypadku
    @GetMapping("{mail}")
    public ResponseEntity<User> findUserByEmail(@PathVariable("mail") String mail){
        User user = userService.findUserByEmail(mail);
        if(user == null) return new ResponseEntity<User>(new User(), HttpStatus.I_AM_A_TEAPOT);
        else return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @GetMapping("getTripGroup/{mail}")
    public ResponseEntity<Set<TripGroup>> getTripGroupsByEmail(@PathVariable("mail") String mail){
        User user = userService.findUserByEmail(mail);
        if(user == null) return new ResponseEntity<Set<TripGroup>>(new HashSet<>(), HttpStatus.I_AM_A_TEAPOT);
        else return new ResponseEntity<Set<TripGroup>>(user.getTripGroups(), HttpStatus.OK);
    }



}
