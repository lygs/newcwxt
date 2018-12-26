package com.ep.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ep.entity.Sysuser;



public interface UserInfoService {
    public List<Sysuser> findAll();
    public String login(String name,String pwd,HttpServletRequest request);
	public String addUser(String name, String pwd, String email,String roleId);
	public String updatePwd(String ids, String oldPwd, String newPwd);
	public String deleteUser(String ids);
	public String getById(String ids);
	public String getUserAllList(String name, String pageSize, String pageNum,
			HttpServletRequest request);
	public String updateUser(String ids, String edt_roleId, String edt_roleId2);
	public Sysuser login(String name, String pwd);
	public Object findObject(String hql) throws Exception;
	public void update(Object u) throws Exception;
	
	
	
	
	
}
