package com.tripmaker.demo.Services;


import com.tripmaker.demo.Data.User;

public interface UserService {
     User findUserByEmail(String email);
     User findUserByUserName(String username);
     void saveUser(User user);
     User getCurrentUser();
}
