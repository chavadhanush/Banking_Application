package com.ltp.banking_application.service;

import com.ltp.banking_application.entity.User;

public interface UserService {
    User getUser(Long id);
    User getUser(String username);
    User saveUser(User user);

}