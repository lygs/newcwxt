package com.ep.controller;

import java.sql.Array;
import java.util.Arrays;
import java.util.Random;

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
	public static void main(String[] args) {
		double money= 0.55; int num=3;
		int fen = (int) (100*money);
		
		int[] a = new int[num];
		Arrays.fill(a, 1);
		fen -= num;
		int i=0;
		Random r = new Random();
		while (fen>1) {
			int f = r.nextInt(fen);
			a[i++ % num] += f;
			fen -= f ;
		}
		a[0] +=fen;
		for(int m:a) {
			System.out.println(m);
		}
	}

}
