package com.liaoze.stockmgt.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.liaoze.stockmgt.dao.UserDao;
import com.liaoze.stockmgt.model.User;
import com.liaoze.stockmgt.service.UserService;

@Service  
public class UserServiceImpl implements UserService {

    @Autowired  
    private UserDao userDao;  

    public User selectUserById(Integer userId) {  
        return userDao.selectUserById(userId);  
    }  
}