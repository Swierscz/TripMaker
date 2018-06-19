package com.tripmaker.demo.Controllers;

import com.tripmaker.demo.Data.User;
import com.tripmaker.demo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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
            userService.saveUser(user);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

}
