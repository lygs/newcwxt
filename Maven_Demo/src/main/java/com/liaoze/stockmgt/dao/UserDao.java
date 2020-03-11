package com.liaoze.stockmgt.dao;

import com.liaoze.stockmgt.model.User;

public interface UserDao {
    /**
     * @param userId
     * @return User
     */
    public User selectUserById(Integer userId);
    
}