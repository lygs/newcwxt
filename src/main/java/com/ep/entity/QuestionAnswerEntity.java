package com.ep.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "QuestionAnswer")
public class QuestionAnswerEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;// 序号

	private String qaQuestion;// 问

	private String qaAnswer;// 答

	private String qaCreatetime;// 创建时间

	private Integer qaType;// 类型 0：非常见 1：常见   默認為0

	private String qaCreator;// 创建人

	private Integer qaKnowledge;// 知识点id   默認為-1

	private String qaOther;// 备用    站點
	
	private String qaKeywords;//关键字
	
	private String qaResource;//信息源    
	private String qaUrl;//url地址
	private Integer qaChnlid;//欄目    默認為-1
	private String qaResourceType;//信息来源类型 0手工录入  1批量导入
	private String qaFileName;//批量导入的文件名
	
	@Column(name = "QA_RESOURCETYPE")
	public String getQaResourceType() {
		return qaResourceType;
	}

	public void setQaResourceType(String qaResourceType) {
		this.qaResourceType = qaResourceType;
	}

	@Column(name = "QA_FILENAME")
	public String getQaFileName() {
		return qaFileName;
	}

	public void setQaFileName(String qaFileName) {
		this.qaFileName = qaFileName;
	}

	@Column(name = "QA_RESOURCE")
	public String getQaResource() {
		return qaResource;
	}

	public void setQaResource(String qaResource) {
		this.qaResource = qaResource;
	}
	@Column(name = "QA_URL")
	public String getQaUrl() {
		return qaUrl;
	}

	public void setQaUrl(String qaUrl) {
		this.qaUrl = qaUrl;
	}
	@Column(name = "QA_CHNLID")
	public Integer getQaChnlid() {
		return qaChnlid;
	}

	public void setQaChnlid(Integer qaChnlid) {
		this.qaChnlid = qaChnlid;
	}

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "QA_QUESTION",length=1000)
	public String getQaQuestion() {
		return qaQuestion;
	}

	public void setQaQuestion(String qaQuestion) {
		this.qaQuestion = qaQuestion == null ? null : qaQuestion.trim();
	}

	@Column(name = "QA_ANSWER",length=6000)
	public String getQaAnswer() {
		return qaAnswer;
	}

	public void setQaAnswer(String qaAnswer) {
		this.qaAnswer = qaAnswer == null ? null : qaAnswer.trim();
	}
    
	@Column(name = "QA_CREATETIME")
	public String getQaCreatetime() {
		return qaCreatetime;
	}

	public void setQaCreatetime(String qaCreatetime) {
		this.qaCreatetime = qaCreatetime;
	}



	@Column(name = "QA_TYPE")
	public Integer getQaType() {
		return qaType;
	}

	public void setQaType(Integer qaType) {
		this.qaType = qaType;
	}

	@Column(name = "QA_CREATOR")
	public String getQaCreator() {
		return qaCreator;
	}

	public void setQaCreator(String qaCreator) {
		this.qaCreator = qaCreator == null ? null : qaCreator.trim();
	}

	@Column(name = "QA_KNOWLEDGE")
	public Integer getQaKnowledge() {
		return qaKnowledge;
	}

	public void setQaKnowledge(Integer qaKnowledge) {
		this.qaKnowledge = qaKnowledge;
	}

	@Column(name = "QA_OTHER")
	public String getQaOther() {
		return qaOther;
	}

	public void setQaOther(String qaOther) {
		this.qaOther = qaOther == null ? null : qaOther.trim();
	}
	
	@Column(name = "QA_KEYWORDS")
	public String getQaKeywords() {
		return qaKeywords;
	}

	public void setQaKeywords(String qaKeywords) {
		this.qaKeywords = qaKeywords;
	}
	
	
}