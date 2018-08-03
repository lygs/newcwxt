package com.ep.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
* @author Zhaoxh
* @version 创建时间：2018年6月26日 上午9:23:29
* 类说明：高频词
*/ 
@Entity
@Table(name = "HighFrequencyWords")
public class HighFrequencyWordsEntity {

	private Integer id; 	//id
	private String words;   //词
	private Integer num;    //访问量
	
	
	@Id
	@Column(name = "H_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "H_WORDS")
	public String getWords() {
		return words;
	}
	public void setWords(String words) {
		this.words = words;
	}
	
	@Column(name = "H_NUM")
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	@Override
	public String toString() {
		return "HighFrequencyWordsEntity [id=" + id + ", words=" + words + ", num=" + num + "]";
	}
	
	
	
}
