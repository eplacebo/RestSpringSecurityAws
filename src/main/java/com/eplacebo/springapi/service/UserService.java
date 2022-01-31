package com.eplacebo.springapi.service;

import com.eplacebo.springapi.model.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface UserService  {

    User getById(Long id);

    void save(User user);

    void delete(Long id);

    List<User> getAll();;

    User findByUsername(String username);

    String getUsernameByAuth();
}
