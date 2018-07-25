package com.ep.entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 * @author admin
 *
 */
@Entity
@Table(name = "CHANNELS")
public class Channels implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer channelid;
	private String chnlname;//名称
	//private String chnldesc;//描述
	private Integer parentid;//父id
	//private Integer chnlorder;//?	
	private String cruser; //创建人
	private Timestamp crtime;
	private Integer status; //状态  0
	private String chnlurl;  //? 0,2
	//private Integer docchnlId; //文檔欄目id
	//private Integer siteId;//站點id
	//private String siteName;//站點名稱
	//private String siteDesc;
	
	
	public Channels() {
		
	}
	@Id
	@Column(name = "CHANNELID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getChannelid() {
		return this.channelid;
	}

	public void setChannelid(Integer channelid) {
		this.channelid = channelid;
	}

	@Column(name = "CHNLNAME", nullable = false)
	public String getChnlname() {
		return this.chnlname;
	}

	public void setChnlname(String chnlname) {
		this.chnlname = chnlname;
	}

	

	@Column(name = "PARENTID", nullable = false)
	public Integer getParentid() {
		return this.parentid;
	}

	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}

	

	@Column(name = "CRUSER")
	public String getCruser() {
		return this.cruser;
	}

	public void setCruser(String cruser) {
		this.cruser = cruser;
	}

	@Column(name = "CRTIME", length = 23)
	public Timestamp getCrtime() {
		return this.crtime;
	}

	public void setCrtime(Timestamp crtime) {
		this.crtime = crtime;
	}

	@Column(name = "STATUS", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
     
	@Column(name = "CHNLURL")
	public String getChnlurl() {
		return chnlurl;
	}
	public void setChnlurl(String chnlurl) {
		this.chnlurl = chnlurl;
	}	
}