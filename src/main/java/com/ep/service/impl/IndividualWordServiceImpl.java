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
	public List<IndividualWord> getDataByTitle(String title, String pageSize, String pageNum) {
			JSONObject json = new JSONObject();
			String hql = "from IndividualWord where qaQuestion like '%"+title+"%' order by cretime DESC ";
			List<IndividualWord> list = individualWordDao.getDataByTitle(hql, pageSize, pageNum);
				
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
	public int getTotal(String title) {
		String hql = "select count(*) from IndividualWord where I_TITLE like '%"+title+"%'";
		return individualWordDao.getTotal(hql);
	}
	
	

}
