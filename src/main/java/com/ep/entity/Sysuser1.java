package com.ep.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


//@Entity
//@Table(name = "SYSUSER")
public class Sysuser1 implements Serializable {
	private static final long serialVersionUID = 1L;

	private int userId;
	private String userName;// 用户名
	private String password;// 密码
	private String email;// 邮箱
	private String createTime;//创建时间
	private int roleId;//角色    0管理員    1普通用戶
	
	@Column(name="ROLEID")
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	
	@Id
	@Column(name = "USERID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	@Column(name="USERNAME")
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Column(name="PASSWORD")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Column(name="EMAIL")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Column(name="CREATETIME")
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}// 创建时间
	public Sysuser1(int userId, String userName, String password, String email,
			String createTime) {
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.createTime = createTime;
	}
	public Sysuser1() {
	}

	
}
