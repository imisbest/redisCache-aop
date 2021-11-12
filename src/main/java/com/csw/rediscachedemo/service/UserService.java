package com.csw.rediscachedemo.service;

import com.csw.rediscachedemo.entity.User;

import java.util.List;


public interface UserService {
    void insertUser(User user);

    User selectUserById(String userId);

    List<User> selectAllUser(String userId);

    User updateUser(User user);

    void deleteUser(String userId);

    List<User> selectTable(String userId);
}


