package com.ep.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ep.entity.LogMessage;
import com.ep.entity.Sysuser;
import com.ep.service.LogsService;
import com.ep.service.UserInfoService;
import com.ep.util.CMyString;
import com.ep.util.DateUtil;
import com.ep.util.IpUtils;

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
	
	@Autowired
    public LogsService logService;
	
	HttpServletRequest request;
	HttpServletResponse response;
	private Sysuser user;// 用户
	
	@ModelAttribute
    public void setReqAndRes(HttpServletRequest request,
    HttpServletResponse response) throws UnsupportedEncodingException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        this.request = request;
        this.response = response;
    }
	
	public Sysuser getUser() {
		return user;
	}

	public void setUser(Sysuser user) {
		this.user = user;
	}
    @RequestMapping(value = "/login")
	public void login() throws Exception {
    	/*try {
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
	}*/
    	response.setContentType("text/html;charset=utf-8");//设置编码
    	String name = CMyString.filterForHTMLValue(request.getParameter("name"));
		String pwd = CMyString.filterForHTMLValue(request.getParameter("pwd"));
		String validateCode = CMyString.filterForHTMLValue(request.getParameter("validateCode"));//用户输入的验证码
		String str = "";
		String validateCodeServer = (String) request.getSession().getAttribute("logincode"); //自动生成的code
		ServletContext application = request.getSession().getServletContext(); //全局变量
	    List<HttpSession> sessions = new ArrayList<HttpSession>();
	    String login_=request.getParameter("login_"); //login_ = 1 强行登陆
	    String ipRemoteHost = IpUtils.getIpAddrByRequest(request); //ip
	    JSONObject obj = new JSONObject();
	    List userlist1 = (List) application.getAttribute("userList");// 用户最后一次密码错误信息
	    if(userlist1!=null && userlist1.size()>0) {
	    	String hql = "from Sysuser  where username='"+ name + "'";
			Sysuser u = (Sysuser) userInfoService.findObject(hql);
			Date errordate = new Date();
			Long errortime = errordate.getTime();// 本次密码输错的时间
			Integer log = u.getLogin_num();
			if (log == null) {
				log = 0;
			}
	    	 for (int i = 0; i < userlist1.size(); i++) {
	 			Map map = (Map) userlist1.get(i);
	 			 if((map.get("username").equals(u.getUserName()) && map.get("ip").equals(ipRemoteHost)
	 					&& errortime- (Long) map.get("time") > 1800000L) && log >=10) { //30分钟后自动解锁
	 				userlist1.remove(i);
	 				u.setLogin_num(null);
	 				userInfoService.update(u);
	 			}
	 		}
	    }
		if (login_ != null && login_.equals("1")) {// 强行登录
			sessions = (List) application.getAttribute("sessionList");
			if (sessions != null) {
				for (int si = 0; si < sessions.size(); si++) {
					HttpSession s = (HttpSession) sessions.get(si);
					Sysuser _Usertemp = (Sysuser) s.getAttribute("user");
					Sysuser usernow = userInfoService.login(name, pwd);
					String ipRemoteHosteTemp = (String) s.getAttribute("ipRemoteHost");
					if (_Usertemp != null && _Usertemp.getUserName().equals(name) && !ipRemoteHosteTemp.equals(ipRemoteHost)) {
						if (usernow != null) {
							sessions.remove(si);
							s.invalidate();
							HttpSession session = request.getSession();
							request.getSession().setAttribute("user", usernow);
							request.getSession().setAttribute("username",name);
							session.setAttribute("ipRemoteHost", ipRemoteHost);
							sessions.add(session);
							application.setAttribute("sessionList", sessions);
							// 登陆成功后清空错误登录次数
							usernow.setLogin_num(null);
							userInfoService.update(usernow);
							obj.put("results", "success");
						} else {
							// 用户名或密码错误
							String hql = "from Sysuser  where username='"+ name + "'";
							Sysuser u = (Sysuser) userInfoService.findObject(hql);
							if (u != null) {// 密码错误
								List userlist = (List) application.getAttribute("userList");// 用户最后一次密码错误信息
								Date errordate = new Date();
								Long errortime = errordate.getTime();// 本次密码输错的时间
								Integer log = u.getLogin_num();
								if (log == null) {
									log = 0;
								}
								// 同一账号，同一ip在1分钟内连续错误登录10次 账号被锁定
								if (userlist != null) {
									for (int i = 0; i < userlist.size(); i++) {
										Map map = (Map) userlist.get(i);
										if (map.get("username").equals(u.getUserName()) && map.get("ip").equals(ipRemoteHost)
												&& errortime- (Long) map.get("time") < 600000L) {
											if (log >= 10) {
												str = "max_login";
											} else {
												userlist.remove(i);
												u.setLogin_num(log + 1);
												userInfoService.update(u);
												Map userMap = new HashMap();// 存放用户错误登录信息，记录最新一次错误时间和错误ip地址
												userMap.put("time", errortime);
												userMap.put("ip", ipRemoteHost);
												userMap.put("username",u.getUserName());
												userlist.add(userMap);
												application.setAttribute("userList", userlist);
												obj.put("results", "ERROR");
											}
										}else {// 在不同ip密码错误，或是10次错误登录时间相差10分钟以上，此时只保存最新一次登录错误信息
											userlist.remove(i);
											Map userMap = new HashMap();// 存放用户错误登录信息，记录最新一次错误时间和错误ip地址
											userMap.put("time", errortime);
											userMap.put("ip", ipRemoteHost);
											userMap.put("username",u.getUserName());
											userlist.add(userMap);
											application.setAttribute("userList", userlist);
											obj.put("results", "ERROR");
										}

									}
								} else {
									if (log >= 10) {
										str = "max_login";
										return;
									}
									u.setLogin_num(log + 1);
									userInfoService.update(u);
									Map userMap = new HashMap();// 存放用户错误登录信息，记录最新一次错误时间和错误ip地址
									userMap.put("time", errortime);
									userMap.put("ip", ipRemoteHost);
									userMap.put("username", u.getUserName());
									List userlist_temp = new ArrayList();// 存放用户map的list
									userlist_temp.add(userMap);
									application.setAttribute("userList",userlist_temp);
									obj.put("results", "ERROR");
								}
							} else {// 用户名错误
								obj.put("results", "ERROR");
							}
						}

					}
				}
			}
		} else {
			if (validateCode != null && validateCode.equals(validateCodeServer)) {
				user = userInfoService.login(name, pwd);
				if (user != null) {
					Integer num = user.getLogin_num();
					if (user.getLogin_num() != null && (user.getLogin_num().intValue() >= 10)) {
						str = "max_login";
						this.request.setAttribute("user", null);
					} else {
						if (application.getAttribute("sessionList") != null) {
							sessions = (List) application.getAttribute("sessionList");
							if (sessions.size() > 0) {
								for (int si = 0; si < sessions.size(); si++) {
									HttpSession ses = null;
									try {
										ses = (HttpSession) sessions.get(si);
										Sysuser _Usertemp = (Sysuser) ses.getAttribute("user");
										String ipRemoteHosteTemp = (String) ses.getAttribute("ipRemoteHost");
										// 提示用户是否强制登录
										if (_Usertemp != null && _Usertemp.getUserName().equals(name) && !ipRemoteHosteTemp.equals(ipRemoteHost)) {
											obj.put("results", "loginin");
											// 该账号已经在相同ip登录
										} else if (_Usertemp != null && _Usertemp.getUserName().equals(name) && ipRemoteHosteTemp.equals(ipRemoteHost)) {
											sessions = (List) application.getAttribute("sessionList");
											Sysuser usernow = userInfoService.login(name, pwd);
											if (sessions != null) {
												for (int si1 = 0; si1 < sessions.size(); si1++) {
													HttpSession s = (HttpSession) sessions.get(si1);
													if (_Usertemp != null && _Usertemp.getUserName().equals(name)) {
														if (usernow != null) {
															sessions.remove(si1);
															s.invalidate();
															HttpSession session = request.getSession();
															session.setAttribute("user",usernow);
															session.setAttribute("username",name);
															session.setAttribute("ipRemoteHost",ipRemoteHost);
															sessions.add(session);
															application.setAttribute("sessionList",sessions);
															// 登陆成功后清空错误登录次数
															usernow.setLogin_num(null);
															userInfoService.update(usernow);
															obj.put("results", "SUCCESS");
															break;
														} else {
															obj.put("results", "ERROR");
														}

													}
												}
											}
										} else {
											obj.put("results", "success");
											request.getSession().setAttribute("user", user);
											request.getSession().setAttribute("username", name);
											// 登录后把用户session放入applycation
											HttpSession session = request.getSession();
											session.setAttribute("ipRemoteHost",ipRemoteHost);
											sessions.add(session);
											application.setAttribute("sessionList", sessions);
											// 登陆成功后清空错误登录次数
//											String hql = "from Sysuser  where username='"+ name + "'";
//											Sysuser u = (Sysuser) userInfoService.findObject(hql);
											user.setLogin_num(null);
											userInfoService.update(user);
											break;
										}
									} catch (Exception e) {
										sessions.remove(si);
									}
								}

							} else {
								obj.put("results", "success");
								request.getSession().setAttribute("user", user);
								request.getSession().setAttribute("username",name);
								// 登录后把用户session放入applycation
								HttpSession session = request.getSession();
								session.setAttribute("ipRemoteHost",ipRemoteHost);
								sessions.add(session);
								application.setAttribute("sessionList",sessions);
								// 登陆成功后清空错误登录次数
								user.setLogin_num(null);
								userInfoService.update(user);
							}
						} else {
							this.request.getSession().setAttribute("user", user);
							request.getSession().setAttribute("username",name);
							// 登录后把用户session放入applycation
							HttpSession session = request.getSession();
							session.setAttribute("ipRemoteHost", ipRemoteHost);
							sessions.add(session);
							application.setAttribute("sessionList", sessions);
							user.setLogin_num(null);
							userInfoService.update(user);
							obj.put("results", "success");
						}
					}
				} else {
					String hql = "from Sysuser where username='" + name+ "'";
					Sysuser u = (Sysuser) userInfoService.findObject(hql);
					// 密码错误
					if (u != null) {
						Date errordate = new Date();
						Long errortime = Long.valueOf(errordate.getTime());
						Integer log = u.getLogin_num();
						if (log == null) {
							log = Integer.valueOf(0);
						}
						List userlist = new ArrayList();
						userlist = (List) application.getAttribute("userList");
						// 判断用户是否登录过
						if (userlist != null) {
							for (int i = 0; i < userlist.size(); i++) {
								Map map = (Map) userlist.get(i);
								if ((map.get("username").equals(u.getUserName()))&& (map.get("ip").equals(ipRemoteHost))
										&& (errortime.longValue()- ((Long) map.get("time")).longValue() < 600000L)) {
									if (log.intValue() >= 10) {
										obj.put("results", "max_login");
									} else {
										userlist.remove(i);
										u.setLogin_num(Integer.valueOf(log.intValue() + 1));
										userInfoService.update(u);
										Map userMap = new HashMap();
										userMap.put("time", errortime);
										userMap.put("ip", ipRemoteHost);
										userMap.put("username", u.getUserName());
										userlist.add(userMap);
										application.setAttribute("userList",userlist);
										obj.put("results", "error");
									}
								} else {
									userlist.remove(i);
									Map userMap = new HashMap();
									userMap.put("time", errortime);
									userMap.put("ip", ipRemoteHost);
									userMap.put("username", u.getUserName());
									userlist.add(userMap);
									application.setAttribute("userList",userlist);
									obj.put("results", "error");
								}
							}
						} else {
							if (log.intValue() >= 10) {
								obj.put("results", "max_login");
								return;
							}
							u.setLogin_num(Integer.valueOf(log.intValue() + 1));
							this.userInfoService.update(u);
							Map userMap = new HashMap();
							userMap.put("time", errortime);
							userMap.put("ip", ipRemoteHost);
							userMap.put("username", u.getUserName());
							List userlist_temp = new ArrayList();
							userlist_temp.add(userMap);
							application.setAttribute("userList", userlist_temp);
							obj.put("results", "error");
						}
					} else {
						obj.put("results", "error");
					}
				}
			} else {
				obj.put("results", "codeerror");
			}
		}
		str = obj.toString();
		this.response.getWriter().println(str);
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
        	ServletContext application = request.getSession().getServletContext(); //全局变量
    	    List<HttpSession> sessions = (List<HttpSession>) application.getAttribute("sessionList");
    	    if(sessions!=null && sessions.size()>0) {
    	    	Sysuser u = (Sysuser) request.getSession().getAttribute("user");
    	    	for(int i=0;i<sessions.size();i++) {
    	    		HttpSession ses = sessions.get(i);
    	    		if(ses.getAttribute("user").equals(u)) {
    	    			sessions.remove(i);
    	    		}
    	    	}
    	    }
            request.getSession().setAttribute("user", null);
            LogMessage logs = new LogMessage();
			logs.setClassMethod("loginOut");
			logs.setClassName("UserInfo");
			logs.setContent(user.getUserName()+"退出");
			logs.setCreatedate(DateUtil.paseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			logService.save(logs);
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
				Sysuser user = (Sysuser) request.getSession().getAttribute("user");
	    		if(user.getRoleId()==0) {
	    			str = userInfoService.addUser(name,pwd,email,roleId);
	    		}else {
	    			//无权限
	    			
	    		}
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
				Sysuser user = (Sysuser) request.getSession().getAttribute("user");
	    		if(user.getRoleId()==0) {
	    			str = userInfoService.updatePwd(ids,oldPwd,newPwd);
	    		}else {
	    			//无权限
	    		}
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
	        	
	        	Sysuser user = (Sysuser) request.getSession().getAttribute("user");
	    		if(user.getRoleId()==0) {
	    			str=userInfoService.getUserAllList(name,pageSize,pageNum,request);
	    		}else {
	    			//无权限
	    		}
	        	
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
