package com.liaoze.stockmgt.service;

import com.liaoze.stockmgt.model.User;

public interface UserService {
    User selectUserById(Integer userId);  
}