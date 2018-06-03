package com.tripmaker.demo.Services;


import com.tripmaker.demo.Data.User;

public interface UserService {
     User findUserByEmail(String email);
     void saveUser(User user);
}
