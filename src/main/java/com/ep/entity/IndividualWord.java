package com.ep.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
* @author  Zhaoxh
* @version 创建时间：2018年7月4日 下午3:26:39
* 单词实体类 
*/
@Entity
@Table(name = "IndividualWord")
public class IndividualWord {

	private Integer id;
	private String qaQuestion;
	private String qaAnswer;
	private String url;
	private Integer level;//优先级
	private String founder;//创建人
	private String cretime;
	private Integer qaFormat;//qaAnswer内容格式  0 文本 1 音频视频图片
	
	@Id
	@Column(name = "I_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	@Column(name = "I_QAFORMAT")
	public Integer getQaFormat() {
		return qaFormat;
	}
	public void setQaFormat(Integer qaFormat) {
		this.qaFormat = qaFormat;
	}
	
	@Column(name = "I_TITLE")
	public String getQaQuestion() {
		return qaQuestion;
	}
	public void setQaQuestion(String qaQuestion) {
		this.qaQuestion = qaQuestion;
	}
	
	@Column(name = "I_CONTENT")
	public String getQaAnswer() {
		return qaAnswer;
	}
	public void setQaAnswer(String qaAnswer) {
		this.qaAnswer = qaAnswer;
	}
	
	@Column(name = "I_URL")
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Column(name = "I_LEVEL")
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	
	@Column(name = "I_CRETIME")
	public String getCretime() {
		return cretime;
	}
	public void setCretime(String cretime) {
		this.cretime = cretime;
	}
	
	@Column(name = "I_FOUNDER")
	public String getFounder() {
		return founder;
	}
	public void setFounder(String founder) {
		this.founder = founder;
	}
	
	
	
	
	
	
}
