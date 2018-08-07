package com.ep.service;

import java.util.List;

import com.ep.entity.TemplateEntity;

public interface TemplateService {

	public String updateSelectedStatus(String ids);

	public List getAll();

	public int updateOldStatus();

	public TemplateEntity getSelectedTemp();
	
}
