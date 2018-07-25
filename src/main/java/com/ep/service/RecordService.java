package com.ep.service;


import javax.servlet.http.HttpServletRequest;

import com.ep.entity.RecordEntity;

import net.sf.json.JSONObject;

public interface RecordService {
    public int saveRecord(RecordEntity rEntity);
    public String getAllList(int pageSize,int pageNumber);
    public int getRecordEntityTotal();
	public JSONObject getRecordTotal(HttpServletRequest request);
	public String delRecord(int parseInt);
	public int updateObj(int ids);
}
