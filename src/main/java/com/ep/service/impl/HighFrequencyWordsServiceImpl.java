package com.ep.service.impl;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ep.dao.HighFrequencyWordsDao;
import com.ep.entity.HighFrequencyWordsEntity;
import com.ep.entity.Sysuser;
import com.ep.service.HighFrequencyWordsService;

import net.sf.json.JSONObject;

/**
* @author Zhaoxh
* @version 创建时间：2018年6月26日 上午10:27:07
* @about 
*/
@Service("highFrequencyWordsService")
public class HighFrequencyWordsServiceImpl implements HighFrequencyWordsService {

	@Autowired
	HighFrequencyWordsDao hfwsDao;
	
	@Override
	/**
	 * 关键字存在 数字+1 不存在就保存
	 */
	public int updataOrSaveHFW(String words) {
		HighFrequencyWordsEntity obj = hfwsDao.getHFWByWord(words);
		 if(obj != null) {
			int num = obj.getNum() +1;
			obj.setNum(num);
			 int resutl = hfwsDao.updataOrSaveHFW(obj);
			 return resutl;
		 }else {
			 HighFrequencyWordsEntity obj1 = new HighFrequencyWordsEntity();
			 obj1.setNum(1);
			 obj1.setWords(words);
			 
			 int resutl = hfwsDao.updataOrSaveHFW(obj1);
			 return resutl;
		 }
		
	}

	/**
	 * 查询前10
	 */
	@Override
	public List<HighFrequencyWordsEntity> getTopEleven() {
		List<HighFrequencyWordsEntity> list = hfwsDao.getHFWByTop();
		return list;
	}

	@Override
	public String getGpcAllList(String name, String pageSize, String pageNum) {
		  StringBuffer hql = new StringBuffer("from HighFrequencyWordsEntity where 1=1");
	        StringBuffer hql_ = new StringBuffer("select count(*) from HighFrequencyWordsEntity where 1=1");
	        if(StringUtils.isNotBlank(name)){
	        	hql.append(" and words like '%"+name+"%'");
	        	hql_.append(" and words like '%"+name+"%'");
	        }
	        hql.append(" order by num desc");
	        List<Sysuser> list=hfwsDao.getGpcAllList(hql.toString(),pageSize,pageNum);
	        int counts = hfwsDao.getCount(hql_.toString());
	        JSONObject obj=new JSONObject();
	        obj.put("list", list);
	        obj.put("total",counts);
	        return obj.toString();
	}

}
