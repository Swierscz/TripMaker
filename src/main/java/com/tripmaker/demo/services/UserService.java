package com.tripmaker.demo.services;


import com.tripmaker.demo.data.User;

public interface UserService {
     User findUserByEmail(String email);
     User findUserByUserName(String username);
     void createUser(User user);
     void saveUser(User user);
     User getCurrentUser();
}
