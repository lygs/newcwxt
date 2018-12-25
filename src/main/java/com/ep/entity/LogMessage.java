package com.ep.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Logs")
public class LogMessage {

	private Integer id;
	private String className;
	private String classMethod;
	private String content; //错误事件
	private String createdate;
	
	
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "L_CLASSNAME")
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	@Column(name = "L_CLASSMETHOD")
	public String getClassMethod() {
		return classMethod;
	}
	public void setClassMethod(String classMethod) {
		this.classMethod = classMethod;
	}
	@Column(name = "L_CONTENT")
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Column(name = "L_CREATEDATE")
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
}
