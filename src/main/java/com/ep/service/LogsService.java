package com.ep.service;

import java.util.List;

import com.ep.entity.LogMessage;

public interface LogsService {
	public void save(LogMessage log);
	public List<LogMessage> findAll(String pageSize,String pageNumber);
	public int getTotal();
}
