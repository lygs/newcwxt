package com.ep.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ep.dao.WordExpansionDao;
import com.ep.entity.WordExpansion;
import com.ep.service.WordExpansionService;
import com.ep.util.DateUtil;

import net.sf.json.JSONObject;

/**
* @author  Zhaoxh
* @version 创建时间：2018年7月5日 下午3:07:16
* 
*/
@Service
public class WordExpansionServiceImpl implements WordExpansionService {

	@Autowired
	WordExpansionDao wordExpansionDao;
	
	/**
	 * 分页查询
	 */
	@Override
	public List<WordExpansion> getData(String content, String pageSize, String pageNum) {
		
			String hql = "from WordExpansion where content like '%"+content+"%' order by id DESC ";
			List<WordExpansion> list = wordExpansionDao.getData(hql, pageSize, pageNum);
	return list;
	}

	@Override
	public String updataOrSaveWordExpansion(WordExpansion obj) {
		JSONObject json = new JSONObject();
		
		Date date = new Date();
		obj.setCretime(DateUtil.paseDate(date,"yyyy-MM-dd HH:mm:ss"));
		Integer id = wordExpansionDao.updataOrSaveWordExpansion(obj);
		if(id > 0) {
			json.put("msg", "操作成功");
			json.put("data", id);
			json.put("status","1");
		}else {
			json.put("msg", "保存失败");
			json.put("data",id);
			json.put("status","-1");
		}
	
	
	return json.toString();
	}

	@Override
	public int delObjById(Integer id) {
		WordExpansion obj = new WordExpansion();
		obj.setId(id);
		return  wordExpansionDao.delByObj(obj);
	}
	
	public int getTotal() {
		String hql = "select count(*) from WordExpansion ";
		return wordExpansionDao.getTotal(hql);
	}

}
