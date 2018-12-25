package com.ep.service.impl;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ep.dao.LogsDao;
import com.ep.dao.UserInfoDAO;
import com.ep.entity.LogMessage;
import com.ep.entity.Sysuser;
import com.ep.service.UserInfoService;
import com.ep.util.DateUtil;
import com.ep.util.MD5;

@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService{

	@Autowired
    public UserInfoDAO userInfoDAO;
	@Autowired
    public LogsDao logsDao;
	
	@Override
	public List<Sysuser> findAll() {
		return userInfoDAO.getUserInfoDatas();
	}
	@Override
	public String login(String name, String pwd, HttpServletRequest request) {
	/*	try {
			//第一次使用创建默认管理员
			String hqlCount = "select count(*)  from Sysuser where userName = 'admin'";
			int count = userInfoDAO.getCount(hqlCount);//总数
			if (count < 1) {
				DateUtil util = new DateUtil();
				String creates = util.paseDate(new Date(), "yyyy-MM-dd HH:mm:ss");
				UserInfo sysuser = new UserInfo();
				sysuser.setUserName("admin");
				sysuser.setUserPwd(MD5.crypt("123456"));
				String sql = "insert into SYSUSER values ('"+name+"','"+MD5.crypt("123456")+"','','"+creates+"')";
				userInfoDAO.addUser(sql);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		String hql = "from Sysuser where userName =?0 and password=?1";
		Object[] param ={name,MD5.crypt(pwd)};
		Sysuser user = (Sysuser) userInfoDAO.getObj(hql, param);
		JSONObject obj = new JSONObject();
		if(user!=null){
			request.getSession().setAttribute("user", user);
			obj.put("results", "success");
			obj.put("roleId", user.getRoleId());
			LogMessage logs = new LogMessage();
			logs.setClassMethod("userLogin");
			logs.setClassName("UserInfo");
			logs.setContent(name+":登录成功");
			logs.setCreatedate(DateUtil.paseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			logsDao.saveDao(logs);
		}else{
			obj.put("results", "error");
			LogMessage logs = new LogMessage();
			logs.setClassMethod("userLogin");
			logs.setClassName("UserInfo");
			logs.setContent(name+":登录失败");
			logs.setCreatedate(DateUtil.paseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			logsDao.saveDao(logs);
		}
		return obj.toString();
	}
	@Override
	public String addUser(String name, String pwd, String email,String roleId) {
		String hql = "from Sysuser where userName=?0";
		Object[] params ={name};
		Sysuser user =(Sysuser) userInfoDAO.getObj(hql, params);
		JSONObject obj = new JSONObject();
		if(user!=null){
			obj.put("results", "has");
		}else{
			DateUtil util = new DateUtil();
			String creates = util.paseDate(new Date(), "yyyy-MM-dd HH:mm:ss");
			Sysuser suser = new Sysuser();
			suser.setUserName(name);
			suser.setPassword(MD5.crypt(pwd));
			suser.setCreateTime(creates);
			suser.setEmail(email);
			suser.setRoleId(Integer.parseInt(roleId));
//			String sql = "insert into SYSUSER values ('"+name+"','"+MD5.crypt(pwd)+"','"+email+"','"+creates+"',"+Integer.parseInt(roleId)+")";
			int nums = userInfoDAO.addUser(suser);
			if(nums>0){
				obj.put("results", "success");
				LogMessage logs = new LogMessage();
				logs.setClassMethod("addUser");
				logs.setClassName("UserInfo");
				logs.setContent("添加用户："+name);
				logs.setCreatedate(DateUtil.paseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
				logsDao.saveDao(logs);
			}else{
				obj.put("results", "error");
				LogMessage logs = new LogMessage();
				logs.setClassMethod("userLogin");
				logs.setClassName("UserInfo");
				logs.setContent("添加用户失败");
				logs.setCreatedate(DateUtil.paseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
				logsDao.saveDao(logs);
			}
		}
		return obj.toString();
	}
	@Override
	public String updatePwd(String ids, String oldPwd, String newPwd) {
		
		String hql1 ="from Sysuser where userId="+ids;
		Sysuser user = (Sysuser) userInfoDAO.getObjectById(Sysuser.class, Integer.valueOf(ids));//userInfoDAO.getObj(hql1,null);
		JSONObject obj = new JSONObject();
		if(user!=null){
			if(!MD5.crypt(oldPwd).equals(user.getPassword())){
				obj.put("results", "oldPwdError"); //原密码输入错误
			}else{
				user.setPassword(MD5.crypt(newPwd));
				int nums = userInfoDAO.updataOrSaveUser(user);
				if(nums>0){
					LogMessage logs = new LogMessage();
					logs.setClassMethod("updatePwd");
					logs.setClassName("UserInfo");
					logs.setContent("用户"+user.getUserName()+"修改密码成功");
					logs.setCreatedate(DateUtil.paseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
					logsDao.saveDao(logs);
					obj.put("results", "succ");
				}else{
					obj.put("results", "error");
				}
			}
		}else{
			obj.put("results", "null");
		}
		String str = obj.toString();
		return str;
	}
	
	@Override
	public String updateUser(String ids, String name, String roleId) {
		Sysuser users = userInfoDAO.getObjectById(Sysuser.class, Integer.valueOf(ids));//userInfoDAO.getObj(hql1,null);
		JSONObject obj = new JSONObject();
		if(users!=null){
			users.setRoleId(Integer.parseInt(roleId));
			users.setUserName(name);
			int nums = userInfoDAO.updataOrSaveUser(users);
			if(nums>0){
				obj.put("results", "succ");
			}else{
				obj.put("results", "error");
			}
		}else{
			obj.put("results", "null");
		}
		String str = obj.toString();
		return str;
	}
	
	@Override
	public String deleteUser(String ids) {
		JSONObject obj = new JSONObject();
		int nums = userInfoDAO.deleteUserById(Integer.parseInt(ids));
		if(nums>0){
			LogMessage logs = new LogMessage();
			logs.setClassMethod("deleteUser");
			logs.setClassName("UserInfo");
			logs.setContent("删除用户成功");
			logs.setCreatedate(DateUtil.paseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			logsDao.saveDao(logs);
			obj.put("results", "success");
		}else{
			obj.put("results", "error");
		}
		return obj.toString();
	}
	@Override
	public String getById(String ids) {
		JSONObject obj = new JSONObject();
		Sysuser user = (Sysuser) userInfoDAO.getObjectById(Sysuser.class,Integer.parseInt(ids));
		if(user!=null){
			user.setPassword("");
			obj.put("results", "success");
			obj.put("user", user);
			LogMessage logs = new LogMessage();
			logs.setClassMethod("getById");
			logs.setClassName("UserInfo");
			logs.setContent("查询用户"+user.getUserName()+"成功");
			logs.setCreatedate(DateUtil.paseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			logsDao.saveDao(logs);
		}else{
			obj.put("results", "null");
		}
		return obj.toString();
	}
	
	 /**
	    * 查询所有的知识点
	    */
	    public String getUserAllList(String name,String pageSize,String pageNum,HttpServletRequest requests) {
	        StringBuffer hql = new StringBuffer("select USERID userId,USERNAME userName,EMAIL email,CREATETIME createTime,ROLEID roleId  from SYSUSER where 1=1");
	        StringBuffer hql_ = new StringBuffer("select count(*) from Sysuser where 1=1");
	        if(StringUtils.isNotBlank(name)){
	        	hql.append(" and USERNAME like '%"+name+"%'");
	        	hql_.append(" and userName like '%"+name+"%'");
	        }
	        hql.append(" order by CREATETIME desc");
	        List<Sysuser> list=userInfoDAO.getUserAllList(hql.toString(),pageSize,pageNum);
	        int counts = userInfoDAO.getCount(hql_.toString());
	        JSONObject obj=new JSONObject();
	        Sysuser user =(Sysuser) requests.getSession().getAttribute("user");
	        if(user!=null){
	        	obj.put("roleId", user.getRoleId());
	        }
	        obj.put("list", list);
	        obj.put("total",counts);
	        LogMessage logs = new LogMessage();
			logs.setClassMethod("getUserAllList");
			logs.setClassName("UserInfo");
			logs.setContent("查询所有用户成功");
			logs.setCreatedate(DateUtil.paseDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			logsDao.saveDao(logs);
	        return obj.toString();
	    }
}
