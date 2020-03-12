package com.liaoze.stockmgt.controller;

import javax.annotation.Resource;

import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.liaoze.stockmgt.model.User;
import com.liaoze.stockmgt.service.UserService;

@Controller  
@RequestMapping("/user")  
public class UserController { 
	//��ǰ�����log4j
	private static Logger logger = Logger.getLogger(UserController.class); 
    @Resource  
    private UserService userService; 
    @RequestMapping("/login")    
    public ModelAndView getIndex(){      
        ModelAndView mav = new ModelAndView("login"); 
        User user = userService.selectUserById(2);
        System.out.println(user.getUserName());
        logger.info("���˷���ϵͳ����������"+user.getUserName()+"��¼ϵͳ������������������������");
        mav.addObject("user",user); 
        return mav; 
    }    
}  