package com.tripmaker.demo.controllers;

import com.tripmaker.demo.data.User;
import com.tripmaker.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class LoginController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ResponseEntity<User> createNewUser(@Valid User user, BindingResult bindingResult) {

        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<User>(user, HttpStatus.NOT_ACCEPTABLE);
        } else {
            userService.createUser(user);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

}
