package com.ep.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ep.dao.LogsDao;
import com.ep.entity.LogMessage;
import com.ep.service.LogsService;

@Service("logService")
public class LogsServiceImpl implements LogsService{
	@Autowired
    public LogsDao logsDao;

	@Override
	public void save(LogMessage log) {
		// TODO Auto-generated method stub
		logsDao.saveDao(log);
	}

	@Override
	public List<LogMessage> findAll(String pageSize, String pageNumber) {
		String hql = "from LogMessage order by createdate desc";
		List<LogMessage> list = logsDao.getAllData(hql, pageSize, pageNumber);
		return list;
	}

	@Override
	public int getTotal() {
		String hql = "select count(id) from LogMessage where 1=1";
		int num = logsDao.getAllCount(hql);
		return num;
	}
}
