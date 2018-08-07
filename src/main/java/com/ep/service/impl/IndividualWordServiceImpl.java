package com.ep.service.impl;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ep.dao.IndividualWordDao;
import com.ep.entity.IndividualWord;
import com.ep.service.IndividualWordService;
import com.ep.util.CMyString;
import com.ep.util.DateUtil;

import net.sf.json.JSONObject;

/**
* @author  Zhaoxh
* @version 创建时间：2018年7月4日 下午4:21:16
* 
*/
@Service
public class IndividualWordServiceImpl implements IndividualWordService{

	@Autowired
	IndividualWordDao individualWordDao;
	
	@Override
	public List<IndividualWord> getDataByTitle(String title, String pageSize, String pageNum, String startTime, String endTime) {
			
			StringBuffer str = new StringBuffer();
			String hql = "from IndividualWord where qaQuestion like '%"+title+"%' ";
			String endhql = " order by cretime DESC ";
			str.append(hql);
			
			if(!CMyString.isEmpty(startTime)) {
				startTime = CMyString.filterForHTMLValue(startTime);
				str.append(" and cretime >= '"+startTime+"' ");
			}
			
			if(!CMyString.isEmpty(endTime)) {
				String end = DateUtil.getSpecifiedDayBefore(CMyString.filterForHTMLValue(endTime), "yyyy-MM-dd", 1, "+");
				str.append(" and cretime < '"+end+ "' ");
			}
			str.append(endhql);
			
			List<IndividualWord> list = individualWordDao.getDataByTitle(str.toString(), pageSize, pageNum);
				
		return list;
	}

	@Override
	public String updataOrSaveIndividualWord(IndividualWord obj) {
		JSONObject json = new JSONObject();
		
			Date date = new Date();
			obj.setCretime(DateUtil.paseDate(date,"yyyy-MM-dd HH:mm:ss"));
			Integer id = individualWordDao.updataOrSaveIndividualWord(obj);
			if(id > 0) {
				json.put("msg", "操作成功");
				json.put("data", id);
				json.put("status","1");
			}else {
				json.put("msg", "操作失败");
				json.put("data",id);
				json.put("status","0");
			}
		
		
		return json.toString();
	}

	
	@Override
	public int delObjById(Integer id) {
		IndividualWord obj =  new IndividualWord();
		obj.setId(id);
		return individualWordDao.delByObj(obj);
	}

	@Override
	public int getTotal(String title, String startTime, String endTime) {
		StringBuffer str = new StringBuffer();
		String hql = "select count(*) from IndividualWord where I_TITLE like '%"+title+"%' ";
		str.append(hql);
		
		if(!CMyString.isEmpty(startTime)) {
			startTime = CMyString.filterForHTMLValue(startTime);
			str.append(" and I_CRETIME >= '"+startTime+"' ");
		}
		
		if(!CMyString.isEmpty(endTime)) {
			String end = DateUtil.getSpecifiedDayBefore(CMyString.filterForHTMLValue(endTime), "yyyy-MM-dd", 1, "+");
			str.append(" and I_CRETIME < '"+end+ "' ");
		}
		
		return individualWordDao.getTotal(str.toString());
	}
	
	

}
