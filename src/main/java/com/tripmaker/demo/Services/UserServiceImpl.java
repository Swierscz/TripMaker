package com.tripmaker.demo.Services;

import com.tripmaker.demo.Config.AuthenticationFacade;
import com.tripmaker.demo.Data.Role;
import com.tripmaker.demo.Data.TripGroup;
import com.tripmaker.demo.Data.User;
import com.tripmaker.demo.Data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    AuthenticationFacade authenticationFacade;

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findUserByUserName(String username) {
        return userRepository.findByUserName(username);
    }

    @Override
    public void createUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getCurrentUser() {
        String mail = authenticationFacade.getAuthentication().getName();
        return findUserByEmail(mail);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findUserByEmail(username);
        return buildUserForAuthentication(user);
    }

    private List<SimpleGrantedAuthority> getAuthority(String role){
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    private UserDetails buildUserForAuthentication(User user) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthority(user.getRole()));
    }

}
