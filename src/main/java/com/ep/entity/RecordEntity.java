package com.ep.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "Record")
public class RecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;// 编号
	private String rQuestion;// 问
	private String rAnswer;// 答
	private String rChnnelid;//栏目id
	private String rKnowledgeid; //知识点id
	private String rQuestionId;   //问题id
	private String rCreatetime;// 创建时间
	private String rCriterion; //设为标准问题1
	private String userIp;//网友IP
	private String keyword;//问题输入关键字
	
	

	private String rOther;// 备用

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "R_USERIP")
	public String getUserIp() {
		return userIp;
	}
	
	@Column(name = "R_KEYWORD")
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	@Column(name = "R_QUESTION")
	public String getrQuestion() {
		return rQuestion;
	}
    
	public void setrQuestion(String rQuestion) {
		this.rQuestion = rQuestion == null ? null : rQuestion.trim();
	}

	@Column(name = "R_CRITERION")
	public String getrCriterion() {
		return rCriterion;
	}

	public void setrCriterion(String rCriterion) {
		this.rCriterion = rCriterion;
	}

	@Column(name = "R_ANSWER")
	public String getrAnswer() {
		return rAnswer;
	}

	public void setrAnswer(String rAnswer) {
		this.rAnswer = rAnswer == null ? null : rAnswer.trim();
	}

	@Column(name = "R_CREATETIME")
	public String getrCreatetime() {
		return rCreatetime;
	}

	public void setrCreatetime(String rCreatetime) {
		this.rCreatetime = rCreatetime;
	}
	
	@Column(name = "R_CHNNELID")
	public String getrChnnelid() {
		return rChnnelid;
	}

	public void setrChnnelid(String rChnnelid) {
		this.rChnnelid = rChnnelid;
	}

	@Column(name = "R_KNOWLEDGEID")
	public String getrKnowledgeid() {
		return rKnowledgeid;
	}

	public void setrKnowledgeid(String rKnowledgeid) {
		this.rKnowledgeid = rKnowledgeid;
	}
	
	@Column(name = "R_QUESTIONID")
	public String getrQuestionId() {
		return rQuestionId;
	}

	public void setrQuestionId(String rQuestionId) {
		this.rQuestionId = rQuestionId;
	}

	@Column(name = "R_OTHER")
	public String getrOther() {
		return rOther;
	}

	public void setrOther(String rOther) {
		this.rOther = rOther == null ? null : rOther.trim();
	}
}