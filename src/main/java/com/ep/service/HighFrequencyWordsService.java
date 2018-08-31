package com.ep.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ep.entity.HighFrequencyWordsEntity;

/**
* @author Zhaoxh
* @version 创建时间：2018年6月26日 上午10:25:29
* @about 关键字
*/
public interface HighFrequencyWordsService {

	public int updataOrSaveHFW(String words);
	
	public  List<HighFrequencyWordsEntity> getTopEleven() ;

	public String getGpcAllList(String name, String pageSize, String pageNum,String startDate,String endDate);
	
}
