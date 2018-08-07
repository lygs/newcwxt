package com.ep.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 模板
 * @author Administrator
 *
 */
@Entity
@Table(name="TEMPLATES")
public class TemplateEntity {
	@Id
	@Column(name="TEMPID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int tempId;
	
	@Column(name="PICURL")
	private String picUrl;//模板路径
	
	@Column(name="URLNAME")
	private String urlName;//网页名称
	
	@Column(name="CHECKEDSTATUS")
	private int checkedStatus;//是否选中为模板     0未选中  1选中
	
	@Column(name="TEMPSTATUS")
	private int tempStatus=0;//模板状态    0未删除  1删除
	
	public int getTempId() {
		return tempId;
	}
	public void setTempId(int tempId) {
		this.tempId = tempId;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getUrlName() {
		return urlName;
	}
	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}
	public int getCheckedStatus() {
		return checkedStatus;
	}
	public void setCheckedStatus(int checkedStatus) {
		this.checkedStatus = checkedStatus;
	}
	public int getTempStatus() {
		return tempStatus;
	}
	public void setTempStatus(int tempStatus) {
		this.tempStatus = tempStatus;
	}
	public TemplateEntity(String picUrl, String urlName, int checkedStatus, int tempStatus) {
		this.picUrl = picUrl;
		this.urlName = urlName;
		this.checkedStatus = checkedStatus;
		this.tempStatus = tempStatus;
	}
	public TemplateEntity() {
		
	}
	
	
}
