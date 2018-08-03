package com.ep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.ep.service.UserInfoService;

/**
 * Created by yyx
 * On 04/15/2018.11:59 PM
 */
@Controller
public class TestController {

	@Autowired
    public UserInfoService userInfoService;
	
   /* @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(UserInfo userInfo) {
    	List<Sysuser> list = userInfoService.findAll();
    	//System.out.println("-------------"+list.get(0).getUserName());
        return "test";
    }*/

}
