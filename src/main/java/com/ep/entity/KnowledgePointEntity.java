package com.ep.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Knowledge")
public class KnowledgePointEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private String knowName;// 知识点名称
	private int epcid;// 栏目id
	private String remark;// 备用
	private int isHot;// 0表示是热点 1表示普通
	private String createTime;// 创建时间
	private String creator; //创建人
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "K_CREATETIME")
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "K_ISHOT")
	public int getIsHot() {
		return isHot;
	}

	public void setIsHot(int isHot) {
		this.isHot = isHot;
	}

	@Column(name = "K_NAME")
	public String getKnowName() {
		return knowName;
	}

	public void setKnowName(String knowName) {
		this.knowName = knowName;
	}

	@Column(name = "K_EPCID")
	public int getEpcid() {
		return epcid;
	}

	public void setEpcid(int epcid) {
		this.epcid = epcid;
	}

	@Column(name = "K_REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name = "K_CREATOR")
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	

	public KnowledgePointEntity(int id, String knowName, int epcid, String remark) {
		super();
		this.id = id;
		this.knowName = knowName;
		this.epcid = epcid;
		this.remark = remark;
	}
	
	public KnowledgePointEntity(String knowName, int isHot, String createTime) {
//		super();
		this.knowName = knowName;
		this.isHot = isHot;
		this.createTime = createTime;
	}

	public KnowledgePointEntity() {
		super();
	}

}
