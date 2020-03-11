package com.liaoze.stockmgt.service;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.liaoze.stockmgt.model.User;
import com.liaoze.stockmgt.service.UserService;
import com.liaoze.stockmgt.test.SpringTestCase;  

public class UserServiceTest extends SpringTestCase {

    @Autowired  
    private UserService userService; 

    @Test  
    public void selectUserByIdTest(){  
        User user = userService.selectUserById(1);  
        System.out.println(user.getUserName() + ":" + user.getUserPassword());
    }  
}