package com.ep.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Praise")
public class PraiseEntity {

	private Integer id;
	private String pStatus; 
	private String pDate; 
	private Integer pQaId;
	private String ptype;
	
	
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "P_STATUS")
	public String getpStatus() {
		return pStatus;
	}
	public void setpStatus(String pStatus) {
		this.pStatus = pStatus;
	}
	
	@Column(name = "P_DATE")
	public String getpDate() {
		return pDate;
	}
	public void setpDate(String pDate) {
		this.pDate = pDate;
	}
	
	@Column(name = "P_QAID")
	public Integer getpQaId() {
		return pQaId;
	}
	public void setpQaId(Integer pQaId) {
		this.pQaId = pQaId;
	}
	
	@Column(name = "P_type")
	public String getPtype() {
		return ptype;
	}
	public void setPtype(String ptype) {
		this.ptype = ptype;
	}

	
	@Override
	public String toString() {
		return "PraiseEntity [id=" + id + ", pStatus=" + pStatus + ", pDate=" + pDate + ", pQaId=" + pQaId
				+ "]";
	}
	
	
	
	
	
	
}
