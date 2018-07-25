package com.ep.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
* @author  Zhaoxh
* @version 创建时间：2018年7月5日 下午2:15:31
* 扩展词汇，存放用户输入的同义词
*/
@Entity
@Table(name = "WordExpansion")
public class WordExpansion {

	private Integer id;
	private String content;
	private String founder;//创建人
	private String cretime;
	
	@Id
	@Column(name = "W_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "W_CONTENT")
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Column(name = "W_FOUNDER")
	public String getFounder() {
		return founder;
	}
	public void setFounder(String founder) {
		this.founder = founder;
	}
	
	@Column(name = "W_CRETIME")
	public String getCretime() {
		return cretime;
	}
	public void setCretime(String cretime) {
		this.cretime = cretime;
	}
	
	
}
