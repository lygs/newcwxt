package com.ep.service;

import java.util.List;

import com.ep.entity.IndividualWord;

/**
* @author  Zhaoxh
* @version 创建时间：2018年7月4日 下午4:16:22
* 
*/
public interface IndividualWordService {

	List<IndividualWord> getDataByTitle( String title, String pageSize, String pageNum);
	String updataOrSaveIndividualWord(IndividualWord obj);
	int delObjById(Integer id);
	int getTotal(String title);
}
