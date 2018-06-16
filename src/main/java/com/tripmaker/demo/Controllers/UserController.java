package com.tripmaker.demo.Controllers;

import com.tripmaker.demo.Data.TripGroup;
import com.tripmaker.demo.Data.User;
import com.tripmaker.demo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.Set;

@CrossOrigin(origins = "*")
@RequestMapping("api/user")
public class UserController {

    @Autowired
    UserService userService;

    //TODO zapytać się o poprawność zwracania kodu odpowiedzi w takim przypadku
    @GetMapping("{mail}")
    public ResponseEntity<User> findUserByEmail(@PathVariable("mail") String mail){
        User user = userService.findUserByEmail(mail);
        if(user == null) return new ResponseEntity<User>(new User(), HttpStatus.NOT_FOUND);
        else return new ResponseEntity<User>(userService.findUserByEmail(mail), HttpStatus.FOUND);
    }

    @GetMapping("getTripGroup/{mail")
    public ResponseEntity<Set<TripGroup>> getTripGroupsByEmail(@PathVariable("mail") String mail){
        User user = userService.findUserByEmail(mail);
        if(user == null) return new ResponseEntity<Set<TripGroup>>(new HashSet<>(), HttpStatus.NOT_FOUND);
        else return new ResponseEntity<Set<TripGroup>>(user.getTripGroups(), HttpStatus.FOUND);
    }



}
