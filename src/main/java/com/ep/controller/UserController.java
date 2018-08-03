package com.ep.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ep.entity.Sysuser;
import com.ep.service.UserInfoService;
import com.ep.util.CMyString;

/**
 * 
 * @author admin
 *
 */
@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
    public UserInfoService userInfoService;
	HttpServletRequest request;
	HttpServletResponse response;
	@ModelAttribute
    public void setReqAndRes(HttpServletRequest request,
    HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        this.request = request;
        this.response = response;
    }
	/**
	 * 登录
	 */
    @RequestMapping(value = "/login")
    public void login() {
    	try {
	    	String name = CMyString.filterForHTMLValue(request.getParameter("name"));
			String pwd = CMyString.filterForHTMLValue(request.getParameter("pwd"));
			String validateCode = CMyString.filterForHTMLValue(request.getParameter("validateCode"));//用户输入的验证码
			String str = "";
			String validateCodeServer = (String) request.getSession().getAttribute("logincode"); //自动生成的code
			JSONObject obj = new JSONObject();
			if (!validateCode.equals(validateCodeServer)) {
				obj.put("results", "codeError");
				str = obj.toString();
			} else {
				if(!CMyString.isEmpty(name) &&!CMyString.isEmpty(pwd)) {
					str = userInfoService.login(name,pwd, request);
				}else{
					obj.put("results", "null");
					str = obj.toString();
				}
			}
			response.getWriter().println(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 安全退出
     */
    @RequestMapping(value = "/loginOut")
	public void loginOut(){
	  //安全退出
        Sysuser user = (Sysuser)request.getSession().getAttribute("user");
        String str = "";
        if(user!=null){
            request.getSession().setAttribute("user", null);
            str = "success";
        }else{
            str ="null";
        }
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("results", str);
        str = jsonObj.toString();
        try {
            this.response.getWriter().print(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
    
    @RequestMapping(value = "/isLogin")
   	public void isLogin(){
   	  //安全退出
           Sysuser user = (Sysuser)request.getSession().getAttribute("user");
           String str = "";
           JSONObject jsonObj = new JSONObject();
           if(user!=null){
               int roleId = user.getRoleId();
               String userName=user.getUserName();
               jsonObj.put("roleId", roleId);
               jsonObj.put("userName", userName);
           }else{
        	   jsonObj.put("results", "null");
           }
           str = jsonObj.toString();
           try {
               this.response.getWriter().print(str);
           } catch (IOException e) {
               e.printStackTrace();
           }
   	}
    /**
     * 添加用户
     */
    @RequestMapping(value = "/addUser")
    public void addUser(){
    	try {
	    	String name = CMyString.filterForHTMLValue(request.getParameter("name"));
			String pwd = CMyString.filterForHTMLValue(request.getParameter("pwd"));
			String roleId = CMyString.filterForHTMLValue(request.getParameter("roleId"));
			String email = CMyString.filterForHTMLValue(request.getParameter("email"));
			String str = "";
			JSONObject obj = new JSONObject();
			if(!CMyString.isEmpty(name) &&!CMyString.isEmpty(pwd)&&!CMyString.isEmpty(roleId)) {
				str = userInfoService.addUser(name,pwd,email,roleId);
			}else{
				obj.put("results", "null");
				str = obj.toString();
			}
			response.getWriter().print(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    @RequestMapping(value = "/updateUser")
    public void updateUser(){
    	try {
	    	String ids = CMyString.filterForHTMLValue(request.getParameter("ids"));
			String edit_name = CMyString.filterForHTMLValue(request.getParameter("edit_name"));
			String edt_roleId = CMyString.filterForHTMLValue(request.getParameter("edt_roleId"));
			String str = "";
			boolean idss = ids.matches("[0-9]+");
			boolean roles = edt_roleId.matches("[0-1]+");
			JSONObject objs = new JSONObject();
			if(idss && roles && StringUtils.isNotBlank(ids)&&StringUtils.isNotBlank(edit_name)&&StringUtils.isNotBlank(edt_roleId)){
				Sysuser user = (Sysuser) request.getSession().getAttribute("user");
	    		if(user.getRoleId()==0) {
	    			str = userInfoService.updateUser(ids,edit_name,edt_roleId);
	    		}else {
	    			objs.put("results", "error"); 
					str = objs.toString();
	    		}
			}else{
				objs.put("results", "ERROR"); 
				str = objs.toString();
			}
			response.getWriter().println(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    /**
     * 修改密码
     */
    @RequestMapping(value = "/updatePwd")
    public void updatePwd(){
    	try {
			String oldPwd = CMyString.filterForHTMLValue(request.getParameter("oldPwd"));
			String newPwd = CMyString.filterForHTMLValue(request.getParameter("newPwd"));
			String ids = "";
			if(request.getSession().getAttribute("user") != null){
				Sysuser user = (Sysuser) request.getSession().getAttribute("user");
				ids =String.valueOf(user.getUserId());
			}
			String str = "";
			if(StringUtils.isNotBlank(ids)&&StringUtils.isNotBlank(oldPwd)&&StringUtils.isNotBlank(newPwd)){
				str = userInfoService.updatePwd(ids,oldPwd,newPwd);
			}else{
				JSONObject objs = new JSONObject();
				objs.put("results", "ERROR"); 
				str = objs.toString();
			}
			response.getWriter().println(str);
		} catch (IOException e) {
		/*	try {
				request.getRequestDispatcher("login.html").forward(request, response);
//				response.sendRedirect("login.html");
			} catch (IOException | ServletException e1) {
				e1.printStackTrace();
			}   */
			e.printStackTrace();
		}
    }
    
    /**
     * 删除用户
     */
    @RequestMapping(value="/deleteUser")
    public void deleteUser(){
    	try {
	    	String ids = CMyString.filterForHTMLValue(request.getParameter("ids"));
	    	String str = "";
	    	JSONObject objs = new JSONObject();
	    	boolean fa = ids.matches("[0-9]+");
	    	if(fa && StringUtils.isNotBlank(ids)){
	    		Sysuser user = (Sysuser) request.getSession().getAttribute("user");
	    		if(user.getRoleId()==0) {
	    			str = userInfoService.deleteUser(ids);
	    		}else {
	    			objs.put("results", "error"); 
					str = objs.toString();
	    		}
	    	}else{
				objs.put("results", "null"); 
				str = objs.toString();
	    	}
			response.getWriter().print(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * 根据id查询用户
     */
    @RequestMapping("/getById")
    public void getById(){
    	try {
	    	String ids = CMyString.filterForHTMLValue(request.getParameter("ids"));
	    	String str = "";
	    	if(StringUtils.isNotBlank(ids)){
	    		str = userInfoService.getById(ids);
	    	}else{
	    		JSONObject objs = new JSONObject();
				objs.put("results", "null"); 
				str = objs.toString();
	    	}
			response.getWriter().print(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /*
     *分页查询用戶
     * author:刘瑶
     */
    @RequestMapping("/getUserAllList")
    public void getUserAllList(){
    	try {
	        String pageNum =CMyString.filterForHTMLValue( request.getParameter("pageNumber"));
	        String pageSize =CMyString.filterForHTMLValue( request.getParameter("pageSize"));
	        String name = CMyString.filterForHTMLValue(request.getParameter("searchName"));
	        boolean result = pageSize.matches("[0-9]+");
	        boolean result1 = pageNum.matches("[0-9]+");
	        String str = "";
	        
	        if(result&&StringUtils.isNotBlank(pageSize)&&StringUtils.isNotBlank(pageNum)&&result1){
	        	str=userInfoService.getUserAllList(name,pageSize,pageNum,request);
	        	response.getWriter().println(str);
	        }else{
	        	JSONObject obj = new JSONObject();
	        	obj.put("results", "error");
	        	str = obj.toString();
	        	response.getWriter().println(str);
	        }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
