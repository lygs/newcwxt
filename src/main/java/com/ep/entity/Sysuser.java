package com.ep.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Sysuser entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYSUSER")
public class Sysuser implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int userId;
	private String userName;// 用户名
	private String password;// 密码
	private String email;// 邮箱
	private String createTime;//创建时间
	private int roleId;//角色    0管理員    1普通用戶
	private Integer login_num;//登录失败次数

	/** default constructor */
	public Sysuser() {
		
	}
	@Column(name = "ROLEID")
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
	}
	
	@Column(name = "LOGIN_NUM")
	public Integer getLogin_num() {
		return login_num;
	}

	public void setLogin_num(Integer login_num) {
		this.login_num = login_num;
	}
}