package com.ep.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ep.dao.TemplateDao;
import com.ep.entity.TemplateEntity;
import com.ep.service.TemplateService;
@Service
public class TemplateServiceImpl implements TemplateService{
	@Autowired
	private TemplateDao dao;

	@Override
	public List getAll() {
		String hql = "from TemplateEntity where tempStatus=0";
		List list = dao.getList(hql);
		return list;
	}

	@Override
	public String updateSelectedStatus(String ids) {
		String hql = "update TemplateEntity set checkedStatus=1 where tempId="+Integer.parseInt(ids);
		int results = dao.updateSelectedStatus(hql);
		if(results>0) {
			return "success";
		}else {
			return "error";
		}
	}

	@Override
	public int updateOldStatus() {
		String hql = "update TemplateEntity set checkedStatus=0 where checkedStatus=1";
		int results = dao.updateSelectedStatus(hql);
		return results;
	}

	@Override
	public TemplateEntity getSelectedTemp() {
		String hql = "from TemplateEntity where checkedStatus=1";
		List list = dao.getList(hql);
		if(list!=null && list.size()>0) {
			return (TemplateEntity) list.get(0);
		}else {
			return null;
		}
		
	}
}
