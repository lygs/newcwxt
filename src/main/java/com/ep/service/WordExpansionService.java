package com.ep.service;

import java.util.List;

import com.ep.entity.WordExpansion;

/**
* @author  Zhaoxh
* @version 创建时间：2018年7月5日 下午3:06:25
* 
*/
public interface WordExpansionService {

	List<WordExpansion>  getData( String content, String pageSize, String pageNum);
	String updataOrSaveWordExpansion(WordExpansion obj);
	int delObjById(Integer id);
	int getTotal();
}
